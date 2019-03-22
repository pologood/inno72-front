package com.inno72.monitor;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;

import com.alibaba.fastjson.JSON;
import com.inno72.model.PointPlan;

public class CheckRunner implements Runnable{

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckRunner.class);
	private MongoOperations mongoOperations;
	private List<PointPlan> pointPlan;
	CheckRunner(List<PointPlan> pointPlan, MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
		this.pointPlan = pointPlan;
	}

	@Override
	public void run() {

		try {

//			if (pointPlan.size() > 0){
//				List<PointPlan> save = new LinkedList<>();
//				for (PointPlan point: pointPlan){
//					boolean pointInPolygon = MapsUtil.isInPolygon(point.getLon(), point.getLat(), 1);
//					LOGGER.info("坐标 - {},{} ，计算结果为 {}", point.getLon(), point.getLat(), pointInPolygon);
//					if (pointInPolygon){
//						save.add(point);
//					}
//				}
//				if (save.size() > 0){
//					mongoOperations.insert(save, "FcBoxPointPlan-1");
//				}
//			}
			mongoOperations.insert(pointPlan, "FcBoxPointPlan-500");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}