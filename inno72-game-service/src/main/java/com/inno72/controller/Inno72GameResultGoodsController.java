package com.inno72.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72GameResultGoods;
import com.inno72.service.Inno72GameResultGoodsService;

/**
 * Created by CodeGenerator on 2018/07/02.
 */
@RestController
@RequestMapping("/inno72/game/result/goods")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Inno72GameResultGoodsController {
	@Resource
	private Inno72GameResultGoodsService inno72GameResultGoodsService;

	@RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
	public Result add(Inno72GameResultGoods inno72GameResultGoods) {
		inno72GameResultGoodsService.save(inno72GameResultGoods);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public Result delete(@RequestParam Integer id) {
		inno72GameResultGoodsService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
	public Result update(Inno72GameResultGoods inno72GameResultGoods) {
		inno72GameResultGoodsService.update(inno72GameResultGoods);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = {RequestMethod.POST, RequestMethod.GET})
	public Result detail(@RequestParam Integer id) {
		Inno72GameResultGoods inno72GameResultGoods = inno72GameResultGoodsService.findById(id);
		return ResultGenerator.genSuccessResult(inno72GameResultGoods);
	}

	@RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
	public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
		PageHelper.startPage(page, size);
		List<Inno72GameResultGoods> list = inno72GameResultGoodsService.findAll();
		PageInfo pageInfo = new PageInfo(list);
		return ResultGenerator.genSuccessResult(pageInfo);
	}
}
