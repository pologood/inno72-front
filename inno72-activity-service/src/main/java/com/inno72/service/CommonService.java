package com.inno72.service;

import com.inno72.vo.FeedbackCash;
import com.inno72.vo.Result;

public interface CommonService {
	/**
	 * 发送手机验证码
	 * @param phone
	 * @param code
	 */
	Result<Object> sendSMSVerificationCode(String phone, String code);

	/**
	 * 校验验证码
	 * @param phone
	 * @param code
	 */
	boolean verificationCode(String phone, String code);


	public boolean isSessionExist(String sessionUuid);

	public void setFeedBackEx(String sessionUuid, String value);

	public FeedbackCash getFeedBack(String sessionUuid);
	
	public Result<Object> sendSMSVerificationCodeWithTemplate(String phone, String code, String veriCode, String smsCode);
	
	public boolean verificationCodeWithTemplate(String phone, String code, String veriCode);
}
