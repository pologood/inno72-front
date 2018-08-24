package com.inno72.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.service.Inno72FileService;

@RestController
@RequestMapping("/file")
public class Inno72FileController {

	private Inno72FileService inno72FileService;

	@PostMapping("/picCheck")
	public Result<Object> picCheck(String sessionUUid, String base64Pic) {
		return inno72FileService.skindetect(sessionUUid, base64Pic);
	}

}
