package com.inno72.controller;


import com.inno72.common.util.FaceCacheUtil;
import com.inno72.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.service.Inno72FileService;

@RestController
@RequestMapping("/file")
public class Inno72FileController {
	@Autowired
	private Inno72FileService inno72FileService;

	@Autowired
	private FaceCacheUtil util;

	@PostMapping("/picCheck")
	public Result<Object> picCheck(String sessionUUid, String base64Pic) {
		return inno72FileService.skindetect(sessionUUid, base64Pic);
	}

	@GetMapping("/getPicCheckResult")
	public Result<Object> getPicCheckResult(String sessionUUid) {
		return inno72FileService.getSkinScore(sessionUUid);
	}


	@PostMapping("/upSckinChectPic")
	public Result<Object> upSckinChectPic(String sessionUUid, String base64Pic) {
		return inno72FileService.upSckinChectPic(sessionUUid, base64Pic);
	}


	@GetMapping("/getSckinChectPic")
	public Result<Object> getSckinChectPic(String sessionUUid) {
		return inno72FileService.getSckinChectPic(sessionUUid);
	}
}
