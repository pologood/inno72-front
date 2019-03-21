package com.inno72.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72GameService;

@RestController
public class DemoController {

	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

	@Resource
	private IRedisUtil redisUtil;

	private final Object object = new Object();

	@Resource
	private MongoOperations mongoOperations;

	@RequestMapping("findExecuteIds")
	public Result test() {
		synchronized (object){
			String rdm_max_id = "runner:fc_box:runner_max_id";
			String s = redisUtil.get(rdm_max_id);
			if (StringUtils.isEmpty(s)){
				s = "0";
			}
			Long curSize = Long.parseLong(s);
			Long i = curSize + 6000;
			Map<String, Long> result = new HashMap<>();

			long fcBoxPointPlan = mongoOperations.count(new Query(), "FcBoxPointPlan-1");

			if (fcBoxPointPlan - curSize <= 0){
				return Results.warn("没有了，都处理完了。恭喜你完成任务!", 3);
			}else if(fcBoxPointPlan < i){
				result.put("min", curSize);
				result.put("max", fcBoxPointPlan);
			}else {
				result.put("min", curSize);
				result.put("max", i);
			}
			logger.info("获取线程处理ID集合 -> {}", JSON.toJSONString(result));
			redisUtil.set(rdm_max_id, result.get("max")+"");
			return Results.success(result);
		}
	}


}
