package com.inno72.controller;


import com.inno72.common.BizException;
import com.inno72.common.DateUtil;
import com.inno72.common.ParamException;
import com.inno72.service.CommonService;
import com.inno72.service.GoodFatherService;
import com.inno72.vo.GoodFather;
import com.inno72.vo.Result;
import com.inno72.vo.Results;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/goodFather")
public class GoodFatherController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GoodFatherController.class);

	@Autowired
	private GoodFatherService goodFatherService;

	@Autowired
	private CommonService commonService;

	@Value("${goodfather.openprize.time}")
	private String openprizeTime;

	@ResponseBody
	@RequestMapping(value = "/getVerificationCode", method = {RequestMethod.GET, RequestMethod.POST})
	public Result<Object> getVerificationCode(String phone) {
		String code = genVerificationCode();
		try {
			return commonService.sendSMSVerificationCode(phone,code);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Results.failure("短信发送异常");
		}
	}

	/**
	 * 生成四位随机数
	 * @return
	 */
	private String genVerificationCode() {
		int num= (int)(Math.random()*9000+1000);
		return ""+num;
	}

	@PostMapping("/save")
	public Result<Object> save(GoodFather goodFather,String code) {
		try{
			checkSaveParam(goodFather,code);
			//校验验证码
			if(commonService.verificationCode(goodFather.getPhone(),code)){
				return goodFatherService.save(goodFather);
			}else{
				return Results.warn("验证码错误",-1);
			}
		}catch(ParamException e){
			LOGGER.error("参数异常",e);
			return Results.failure("参数异常");
		}catch(Exception e){
			LOGGER.error("系统异常",e);
			return Results.failure("系统异常");
		}
	}

	private void checkSaveParam(GoodFather goodFather, String code) throws ParamException {
		if(goodFather==null ||
				StringUtils.isEmpty(code)||
				StringUtils.isEmpty(goodFather.getName()) ||
				StringUtils.isEmpty(goodFather.getPhone()) ||
				StringUtils.isEmpty(goodFather.getTaoboId()) ||
				StringUtils.isEmpty(goodFather.getWeboId()) ){
			throw new ParamException("参数异常");
		}
	}

	@PostMapping("/attend")
	public Result<Object> attend(String phone) {

		try{
			checkLotteryDrawResultParam(phone);
			return goodFatherService.attend(phone);
		}catch(ParamException e){
			LOGGER.error("参数异常",e);
			return Results.failure("参数异常");
		}catch(Exception e){
			LOGGER.error("系统异常",e);
			return Results.failure("系统异常");
		}
	}




	@GetMapping("/getLotteryDrawResult")
	public Result<Object> getlotteryDrawResult(String phone) {

		try{
			checkOpenprizeTime();
			checkLotteryDrawResultParam(phone);
			//校验验证码
			return goodFatherService.getlotteryDrawResult(phone);
		}catch(BizException e){
			LOGGER.info("获取中奖结果时间还未到");
			return Results.failure(e.getMessage());
		}catch(ParamException e){
			LOGGER.info("参数异常",e);
			return Results.failure("参数异常");
		}catch(Exception e){
			LOGGER.error("系统异常",e);
			return Results.failure("系统异常");
		}
	}

	private void checkOpenprizeTime() {
		Date openDate = DateUtil.parse(openprizeTime,DateUtil.PATTERN_YYYY_MM_DDHH_MM_SS);
		Date now = new Date();
		if(now.getTime()<openDate.getTime()){
			throw new BizException("开奖时间：9月10日 18:00pm");
		}
	}

	private void checkLotteryDrawResultParam(String phone) throws ParamException {
		if(StringUtils.isEmpty(phone) ){
			throw new ParamException("参数异常");
		}
	}
}
