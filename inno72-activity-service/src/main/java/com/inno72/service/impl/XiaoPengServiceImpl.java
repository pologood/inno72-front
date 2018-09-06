package com.inno72.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.common.BizException;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.QrCodeUtil;
import com.inno72.common.json.JsonUtil;
import com.inno72.mongo.MongoUtil;
import com.inno72.oss.OSSUtil;
import com.inno72.service.CommonService;
import com.inno72.service.XiaoPengService;
import com.inno72.vo.FeedbackCash;
import com.inno72.vo.Result;
import com.inno72.vo.Results;
import com.inno72.vo.XiaoPeng;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class XiaoPengServiceImpl implements XiaoPengService {
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MongoUtil mongoUtil;

	@Autowired
	private CommonService commonService;


	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;


	@Override
	public void save(XiaoPeng xiaoPeng) throws BizException {


		if (!commonService.isSessionExist(xiaoPeng.getSessionUuid())) {
			LOGGER.warn("会话不存在无法提交,session={}", xiaoPeng.getSessionUuid());
			throw new BizException("会话不存在无法提交");
		}

		if (findByPhone(xiaoPeng.getPhone()) != null) {
			LOGGER.warn("该手机号已经参加游戏,phone={}", xiaoPeng.getPhone());
			throw new BizException("该手机号已经参加过活动");
		}

		FeedbackCash feedbackCash = new FeedbackCash();
		feedbackCash.setIsCompleted(true);
		feedbackCash.setPhone(xiaoPeng.getPhone());
		feedbackCash.setSessionUuid(xiaoPeng.getSessionUuid());
		commonService.setFeedBackEx(xiaoPeng.getSessionUuid(), JSON.toJSONString(feedbackCash));

		xiaoPeng.setCreateTime(new Date());
		mongoUtil.save(xiaoPeng);

	}


	@Override
	public Result<Object> feedBackPolling(String sessionUuid) {

		if (!commonService.isSessionExist(sessionUuid)) {
			LOGGER.info("会话不存在! sessionUuid=" + sessionUuid);
			return Results.failure("会话不存在!");
		}

		FeedbackCash feedBackCash = commonService.getFeedBack(sessionUuid);
		if (feedBackCash == null) {
			LOGGER.info("反馈未完成! sessionUuid=" + sessionUuid);
			return Results.failure("反馈未完成!");
		}

		return Results.success(feedBackCash);
	}


	private XiaoPeng findByPhone(String phone) {
		Query query = new Query();
		query.addCriteria(Criteria.where("phone").is(phone));
		List<XiaoPeng> xiaopengs = mongoUtil.find(query, XiaoPeng.class);
		if (xiaopengs != null && xiaopengs.size() > 0) {
			return xiaopengs.get(0);
		}
		return null;
	}


	@Override
	public Result<Object> makeQrCode(String machinedCode, String sessionUuid) {

		String url = String.format("%s?sessionUuid=%s", inno72GameServiceProperties.get("xiaopengUrl"), sessionUuid);


		LOGGER.info("二维码字符串 {} ", url);
		// 二维码存储在本地的路径
		String localUrl = "xiaopeng" + machinedCode + sessionUuid + ".png";

		// 存储在阿里云上的文件名
		String objectName = "qrcode/" + localUrl;
		// 提供给前端用来调用二维码的地址
		String returnUrl = inno72GameServiceProperties.get("staticSrcUrl") + objectName;

		Map<String, String> ret = new HashMap<String, String>();

		try {
			boolean result = QrCodeUtil.createQrCode(localUrl, url, 1800, "png");
			if (result) {
				File f = new File(localUrl);
				if (f.exists()) {
					// 压缩图片
					Thumbnails.of(localUrl).scale(0.5f).outputQuality(0f).toFile(localUrl);
					// 上传阿里云
					OSSUtil.uploadLocalFile(localUrl, objectName);
					// 删除本地文件
					f.delete();
				}

				ret.put("qrCodeUrl", returnUrl);
				ret.put("sessionUuid", sessionUuid);
				// LOGGER.info("二维码生成成功 - result -> {}", JSON.toJSONString(map).replace("\"",
				// "'"));
				LOGGER.info("二维码生成成功 - result -> {}", JsonUtil.toJson(ret));
			} else {
				LOGGER.info("二维码生成失败");
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return Results.success(ret);
	}
}
