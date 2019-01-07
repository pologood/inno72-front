package com.inno72.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.redis.IRedisUtil;

@Component
public class Inno72OrderNumGenUtil {

	@Resource
	private IRedisUtil $redisUtil;

	private static IRedisUtil redisUtil;

    public static String genRefundNum(String orderNum) {
    	//取订单号后六位
		String start = orderNum.substring(orderNum.length()-6);
		String end = randomStr(4);
		String random = start+end;

		String redisKey = "inno72_common:order_refund_num";
		Set<Object> smembers = redisUtil.smembers(redisKey);

		if (smembers == null || smembers.size() == 0){
			redisUtil.del(redisKey+"*");
			smembers = new HashSet<>();
		}

		while (smembers.contains(random)){
			end = randomStr(4);
			random = start+end;
		}
		redisUtil.sadd(redisKey,random);
		return random;
    }

	private static String randomStr(int num) {
		String[] radmon = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		StringBuffer sb = new StringBuffer();
		Random rd = new Random();
		for (int i = 0; i < num; i++) {
			String s = radmon[rd.nextInt(10)];
			sb.append(s);
		}
		return sb.toString();
	}

	@PostConstruct
	public void init(){
		redisUtil = $redisUtil;
	}

	/**
	 *
	 * @param channelNum 渠道编码
	 * @param lastSixMerNum 机器编码后六位
	 * @return 生成的号码
	 */
	public synchronized static String genOrderNum(String channelNum, String lastSixMerNum) {

		assert StringUtils.isNotEmpty(channelNum);
		assert StringUtils.isNotEmpty(lastSixMerNum);

		String date = LocalDateTimeUtil.transfer(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyyMMdd"));

		String orderNumKey = "inno72_common:order_num:";
		String localOrderNumKey = orderNumKey + date;

		Set<Object> smembers = redisUtil.smembers(localOrderNumKey);
		if (smembers == null || smembers.size() == 0){
			redisUtil.del(orderNumKey+"*");
			smembers = new HashSet<>();
		}

		String random = (int) ((Math.random() * 9 + 1) * 100000)+"";
		while (smembers.contains(random)){
			random = (int) ((Math.random() * 9 + 1) * 100000)+"";
		}

		if (lastSixMerNum.length() > 6) {
			lastSixMerNum = lastSixMerNum.substring(lastSixMerNum.length() - 6);
		}

		redisUtil.sadd(localOrderNumKey, random+"");

		return channelNum + lastSixMerNum + date + random;
	}

	/**
	 *
	 * @return 生成的号码
	 */
	public synchronized static String changeOrderNum(String orderNum) {

		String startOrderNum = orderNum.substring(0,orderNum.length()-14);

		String date = LocalDateTimeUtil.transfer(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyyMMdd"));

		String orderNumKey = "inno72_common:order_num:";
		String localOrderNumKey = orderNumKey + date;

		Set<Object> smembers = redisUtil.smembers(localOrderNumKey);
		if (smembers == null || smembers.size() == 0){
			redisUtil.del(orderNumKey+"*");
			smembers = new HashSet<>();
		}

		String random = (int) ((Math.random() * 9 + 1) * 100000)+"";
		while (smembers.contains(random)){
			random = (int) ((Math.random() * 9 + 1) * 100000)+"";
		}

		redisUtil.sadd(localOrderNumKey, random+"");

		return  startOrderNum +date + random;
	}

}
