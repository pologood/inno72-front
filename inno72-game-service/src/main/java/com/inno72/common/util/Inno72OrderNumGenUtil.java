package com.inno72.common.util;

import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.redis.IRedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Inno72OrderNumGenUtil {

	@Autowired
	private IRedisUtil redisUtil;

	private static IRedisUtil $redisUtil;

	@PostConstruct
	public void init(){
		$redisUtil = this.redisUtil;
	}

	/**
	 *
	 * @param channelNum 渠道编码
	 * @param lastSixMerNum 机器编码后六位
	 * @return 生成的号码
	 */
	public synchronized static String genOrderNum(String channelNum, String lastSixMerNum){
		assert StringUtils.isNotEmpty(channelNum) ;
		assert StringUtils.isNotEmpty(lastSixMerNum) ;
		if (lastSixMerNum.length() > 6){
			lastSixMerNum = lastSixMerNum.substring(lastSixMerNum.length() - 6);
		}
		String orderNum = channelNum+lastSixMerNum;

		String dateTime = LocalDateTimeUtil
				.transfer(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyyMMdd"));

//		String incrKey = "order_num:";
//		Long v = $redisUtil.incr(incrKey + dateTime);
//
//		return orderNum + dateTime + addZeroIndex(String.valueOf(v));
		Integer random = (int)((Math.random()*9+1)*100000);
		return orderNum + dateTime + random;
	}

	private static String addZeroIndex(String endIndex){
		StringBuilder endIndexBuilder = new StringBuilder(endIndex);
		while ( endIndexBuilder.length() < 6 ){
			endIndexBuilder.insert(0, "0");
		}
		endIndex = endIndexBuilder.toString();
		return endIndex;
	}

}
