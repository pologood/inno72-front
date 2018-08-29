package com.inno72.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.inno72.common.util.FaceCacheUtil;
import com.inno72.common.util.FileUtil;
import com.inno72.oss.OSSUtil;
import com.inno72.vo.Result;
import com.inno72.vo.Results;
import com.inno72.vo.Skin;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.Inno72FileService;
import com.inno72.vo.UserSessionVo;

@Service
public class Inno72FileServiceImpl implements Inno72FileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72FileServiceImpl.class);

	@Autowired
	private Inno72GameServiceProperties inno72GameServiceProperties;

	@Autowired
	private GameSessionRedisUtil gameSessionRedisUtil;

	@Autowired
	private FaceCacheUtil faceCacheUtil;

	@Override
	public Result<Object> skindetect(String sessionUUid, String picBase64) {

		LOGGER.info("肌肤检测接口 -- 参数 sessionUUid=>({})", sessionUUid);
		if (StringUtils.isEmpty(sessionUUid) || StringUtils.isEmpty(picBase64)) {
			LOGGER.info("参数缺失.");
			return Results.failure("参数缺失!");
		}

		UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(sessionUUid);

		if (sessionKey == null || StringUtils.isEmpty(sessionKey.getUserId())) {
			return Results.failure("登录超时!");
		}


		String jstUrl = inno72GameServiceProperties.get("jstUrl");

		try {

			String requestUrl = jstUrl + "/api/top/" + sessionKey.getAccessToken();
			Map<String, String> requestParams = new HashMap<>();
			requestParams.put("image", picBase64);
			requestParams.put("mixnick", sessionKey.getUserNick());
			requestParams.put("source", "isv_001");
//			LOGGER.info("调用聚石塔肌肤测试接口requestUrl={}requestParams={},",requestUrl,new Gson().toJson(requestParams));
			String respJson = HttpClient.form(requestUrl, requestParams, null);
			if (StringUtils.isEmpty(respJson)) {
				LOGGER.info("调用聚石塔 肌肤检测接口返回空 url => {}", requestUrl, JSON.toJSONString(requestParams));
				return Results.failure("服务调用失败!");
			}

			JSONObject jsonObject = JSONObject.parseObject(respJson);
			JSONObject tmp = jsonObject.getJSONObject("tmall_marketing_face_skindetect_response");
			String tmpString = tmp.getString("detect_result");
			JSONObject result = JSONObject.parseObject(tmpString);
			int code = result.getInteger("code");
			if(code == 200){
				String data = result.getString("data");
				int successcode = JSONObject.parseObject(data).getInteger("code");
				try{
					if(successcode == 0){
						faceCacheUtil.save2RedisFace(sessionUUid,data);
						faceCacheUtil.save2MongoFace(sessionUUid,data);
						return Results.success(JSON.parseObject(data));
					}else{
						faceCacheUtil.deleteSkinByUserId(sessionUUid);
						return Results.warn("肌肤检测失败",successcode);
					}
				}catch (Exception e){
					LOGGER.info("调用聚石塔 肌肤检测接口返回值{}",respJson);
					LOGGER.error("faceCacheUtil保存失败",e);
					return Results.failure("服务调用失败!");
				}
			}else{
				LOGGER.info("调用聚石塔失败 肌肤检测接口返回值{}",respJson);
				Results.failure("服务调用失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("调用聚石塔 肌肤检测接口失败 ");
		}
		return Results.failure("服务调用失败!");
	}

	@Override
	public Result<Object> getSkinScore(String sessionUUid) {
		try{
			UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(sessionUUid);

			if (sessionKey == null) {
				return Results.failure("登录超时!");
			}
			Skin result = faceCacheUtil.getFaceFromMongo(sessionUUid);
			LOGGER.info("获取肌肤检测结果【sessionUUid={}】,【返回结果{}】",sessionUUid,result);
			if(result!=null){
				return Results.success(result);
			}else{
				return Results.warn("没有检测结果",-1);
			}
		}catch (Exception e){
			LOGGER.error("getSkinScore",e);
			return Results.failure("系统繁忙");
		}
	}

	@Override
	public Result<Object> upSckinChectPic(String sessionUUid, String base64Pic) {
		LOGGER.info("上传肌肤检测图片sessionUUid={}",sessionUUid);
		if(StringUtils.isEmpty(sessionUUid)||StringUtils.isEmpty(base64Pic)){
			Results.failure("参数异常");
		}
		UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(sessionUUid);

		if (sessionKey == null) {
			return Results.failure("登录超时!");
		}
		try{
			String url = saveFile(base64Pic,sessionUUid);
			LOGGER.info("上传肌肤检测图片到oss，path={}",url);
			if(StringUtils.isEmpty(url)) return Results.failure("保存文件失败");
			Result result = faceCacheUtil.updateSkinPicUrl(sessionUUid,url);
			LOGGER.info("上传肌肤检测图片保存到mongo");
			return result;
		}catch (Exception e){
			LOGGER.error("上传肌肤检测图片失败",e);
			return Results.failure("上传肌肤检测图片失败");
		}
	}

	@Override
	public Result<Object> getSckinChectPic(String sessionUUid) {
		UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(sessionUUid);

		if (sessionKey == null) {
			return Results.failure("登录超时!");
		}
		String url = faceCacheUtil.getSckinChectPicByUserId(sessionUUid);
		if(StringUtils.isEmpty(url)) {
			LOGGER.error("根据sessionUUid={},userId={}未查找到肌肤检测图片路径",sessionUUid,sessionKey.getUserId());
			return Results.warn("未找到图片地址",-1);
		}
		return Results.success(url);
	}

	private String saveFile(String base64Pic,String userId){
		if(StringUtils.isEmpty(base64Pic)||StringUtils.isEmpty(userId)) return null;
		byte[] bytes = FileUtil.base64ToByte(base64Pic);
		if(bytes==null) return null;
		StringBuilder path = new StringBuilder("activity/");
		Calendar c = Calendar.getInstance();
		path.append(c.get(Calendar.YEAR));
		path.append(c.get(Calendar.MONTH));
		path.append(c.get(Calendar.DATE));
		path.append("/");
//		path.append(userId);
//		path.append("_");
		path.append(System.currentTimeMillis());
		path.append(".png");
		OSSUtil.uploadImgByBytes(bytes,path.toString());
		return path.toString();
	}

}
