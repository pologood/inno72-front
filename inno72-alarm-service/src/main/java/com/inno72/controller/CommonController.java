package com.inno72.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.service.CommonService;
import com.inno72.vo.RespCommonVo;

@RestController
public class CommonController {

	@Autowired
	private CommonService commonService;


	@RequestMapping("/common/queryInitParam")
	public Result<RespCommonVo> queryInitParam(){

		return commonService.queryInitParam();
	}
}
