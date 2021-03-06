package com.inno72.controller;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.service.Inno72MerchantTotalCountByDayService;

/**
 * Created by CodeGenerator on 2018/11/08.
 */
@RestController
@RequestMapping(value = "/inno72/merchant/total/count/by/day", method = {RequestMethod.POST, RequestMethod.GET,
		RequestMethod.OPTIONS})
public class Inno72MerchantTotalCountByDayController {
	@Resource
	private Inno72MerchantTotalCountByDayService inno72MerchantTotalCountByDayService;

	@RequestMapping(value = "/search/{label}")
	public Result<Object> searchData(@PathVariable(value = "label") String label, String merchantId, String activityId,
			String city, String startDate, String endDate, String goods) {
		return inno72MerchantTotalCountByDayService.searchData(label, activityId, city, startDate, endDate, goods,
				merchantId);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/search/export")
	public Result<Object> searchDataExport(String label, String body, String activityId, String city, String startDate,
			String endDate, String goods, String merchantId, HttpServletResponse response) {
		Result<Object> objectResult = inno72MerchantTotalCountByDayService.searchData(label, activityId, city,
				startDate, endDate, goods, merchantId);

		if (objectResult.getCode() == Result.FAILURE) {
			return objectResult;
		}
		byte[] bytes = inno72MerchantTotalCountByDayService.getBytes((Map<String, Object>) objectResult.getData(), body,
				label);

		response.setHeader("Content-Disposition", "attachment;filename=" + label + ".xls");
		response.setContentType("application/x-msdownload");
		response.setContentLength(bytes.length);
		this.close(response, bytes);
		return Results.success();
	}

	@RequestMapping(value = "/search/export/user")
	public Result<String> searchDataExportUser(String label, String body, String activityId, String city,
			String startDate, String endDate, String goods, String merchantId, HttpServletResponse response)
			throws IOException {
		byte[] bytes = inno72MerchantTotalCountByDayService.searchUserData(label, activityId, city, startDate, endDate,
				goods, merchantId);

		response.setHeader("Content-Disposition", "attachment;filename=" + label + ".xls");
		response.setContentType("application/x-msdownload");
		response.setContentLength(bytes.length);
		this.close(response, bytes);
		return Results.success();
	}

	private void close(HttpServletResponse response, byte[] bytes) {
		try {
			response.getOutputStream().write(bytes);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
