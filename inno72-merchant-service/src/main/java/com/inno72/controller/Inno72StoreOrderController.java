package com.inno72.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.model.Inno72StoreOrder;
import com.inno72.service.Inno72StoreOrderService;
import com.inno72.vo.Inno72StoreVo;

/**
* Created by CodeGenerator on 2019/01/02.
*/
@RestController
@RequestMapping("/inno72/store/order")
public class Inno72StoreOrderController {
	@Resource
	private Inno72StoreOrderService inno72StoreOrderService;

	@RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> add(Inno72StoreOrder inno72StoreOrder) {
		inno72StoreOrderService.save(inno72StoreOrder);
		return Results.success();
	}

	@RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> save(@RequestBody Inno72StoreVo vo) {

		return inno72StoreOrderService.addOrder(vo);
	}

	/**
	 * @param merchantId 商户号
	 * @param activityId 活动ID
	 * @return
	 */
	@RequestMapping(value = "/findStoreOrder", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<List<Map<String, String>>> findStoreOrder(String merchantId, String activityId) {
		return inno72StoreOrderService.findStoreOrder(merchantId, activityId);
	}

	@RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> delete(@RequestParam String id) {
		inno72StoreOrderService.deleteById(id);
		return Results.success();
	}

	@RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> update(Inno72StoreOrder inno72StoreOrder) {
		inno72StoreOrderService.update(inno72StoreOrder);
		return Results.success();
	}

	@RequestMapping(value = "/detail", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<Inno72StoreOrder> detail(@RequestParam String id) {
		return Results.success(inno72StoreOrderService.findById(id));
	}

	@RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<Object> list(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "0") Integer size) {
		PageHelper.startPage(page, size);
		List<Inno72StoreOrder> list = inno72StoreOrderService.findAll();
		@SuppressWarnings({"rawtypes", "unchecked"})
		PageInfo pageInfo = new PageInfo(list);
		return ResultGenerator.genSuccessResult(pageInfo);
	}
}
