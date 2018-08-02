package com.inno72.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.inno72.common.datetime.LocalDateTimeUtil;

@Component
public class Inno72OrderNumGenUtil {

	/**
	 *
	 * @param channelNum 渠道编码
	 * @param lastSixMerNum 机器编码后六位
	 * @return 生成的号码
	 */
	public synchronized static String genOrderNum(String channelNum, String lastSixMerNum) {

		assert StringUtils.isNotEmpty(channelNum);
		assert StringUtils.isNotEmpty(lastSixMerNum);

		if (lastSixMerNum.length() > 6) {
			lastSixMerNum = lastSixMerNum.substring(lastSixMerNum.length() - 6);
		}
		String orderNum = channelNum + lastSixMerNum;

		String dateTime = LocalDateTimeUtil.transfer(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyyMMdd"));

		Integer random = (int) ((Math.random() * 9 + 1) * 100000);

		return orderNum + dateTime + random;
	}

}
