package com.inno72.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.alibaba.fastjson.JSON;
import com.inno72.common.BizException;
import com.inno72.mongo.MongoUtil;
import com.inno72.service.CommonService;
import com.inno72.service.XiaoPengService;
import com.inno72.vo.FeedbackCash;
import com.inno72.vo.Result;
import com.inno72.vo.Results;
import com.inno72.vo.XiaoPeng;

public class XiaoPengServiceImpl implements XiaoPengService {
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MongoUtil mongoUtil;

	@Autowired
	private CommonService commonService;

	@Override
	public void save(XiaoPeng xiaoPeng) throws BizException {


		if (commonService.isSessionExist(xiaoPeng.getSessionUuid())) {
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

		if (commonService.isSessionExist(sessionUuid)) {
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
}
