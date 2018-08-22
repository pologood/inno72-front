package com.inno72.machine.controller;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.model.Inno72Goods;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.service.SupplyChannelService;
import com.inno72.machine.vo.SupplyRequestVo;
import com.inno72.machine.vo.WorkOrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@RestController
@CrossOrigin
@RequestMapping("/machine/channel")
public class SupplyChannelController {
	@Resource
	private SupplyChannelService supplyChannelService;


	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 货道列表
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.POST })
	public Result<List<Inno72SupplyChannel>> list(@RequestBody SupplyRequestVo vo) {
		logger.info("货道列表接口参数：{}", JSON.toJSON(vo));
		return supplyChannelService.getList(vo.getMachineId());
	}

	/**
	 * 合并货道
	 * 
	 */
	@RequestMapping(value = "/merge", method = { RequestMethod.POST})
	public Result<String> merge(@RequestBody Inno72SupplyChannel supplyChannel) {
		logger.info("合并货道接口参数：{}", JSON.toJSON(supplyChannel));
        return supplyChannelService.merge(supplyChannel);
	}

	/**
	 * 拆分货道
	 */
	@RequestMapping(value = "/split", method = { RequestMethod.POST})
	public Result<String> split(@RequestBody Inno72SupplyChannel supplyChannel) {
		logger.info("拆分货道接口参数：{}", JSON.toJSON(supplyChannel));
        return supplyChannelService.split(supplyChannel);
	}

	/**
	 * 货道清零
	 */
	@RequestMapping(value = "/clear", method = { RequestMethod.POST})
	public Result<String> clear(@RequestBody Inno72SupplyChannel supplyChannel) {
		logger.info("货道清零接口参数：{}", JSON.toJSON(supplyChannel));
        return supplyChannelService.clear(supplyChannel);
	}

	/**
	 * 查询货道操作历史
	 */
	@RequestMapping(value = "history", method = { RequestMethod.POST})
	public Result<Map<String, Object>> history(@RequestBody Inno72SupplyChannel supplyChannel) {
		logger.info("货道操作历史接口参数：{}", JSON.toJSON(supplyChannel));
        return supplyChannelService.history(supplyChannel);
	}

	/**
	 * 机器维度缺货
	 */
	@RequestMapping(value="machineLack",method = {RequestMethod.POST })
	public Result<List<Inno72Machine>> getMachineLack(){
		logger.info("机器维度缺货接口");
		return supplyChannelService.getMachineLackGoods();
	}

	/**
	 * 商品维度缺货
	 */
	@RequestMapping(value="goodsLack",method = {RequestMethod.POST })
	public Result<List<Inno72Goods>> getGoodsLack(){
		logger.info("商品维度缺货接口");
		return supplyChannelService.getGoodsLack();
	}

	/**
	 * 查询单个商品缺货的机器
	 */
	@RequestMapping(value="machineByLackGoods",method = {RequestMethod.POST })
	public Result<List<Inno72Machine>> getMachineByLackGoods(@RequestBody SupplyRequestVo vo){
		logger.info("查询单个商品缺货的机器接口参数：{}",JSON.toJSON(vo));
		return supplyChannelService.getMachineByLackGoods(vo.getGoodsId());
	}

	/**
	 * 根据机器查询可用商品
	 */
	@RequestMapping(value="getGoodsByMachineId",method = {RequestMethod.POST })
	public Result<List<Inno72Goods>> getGoodsByMachineId(@RequestBody SupplyRequestVo vo){
		logger.info("根据机器查询可用商品接口参数：{}",JSON.toJSON(vo));
		return supplyChannelService.getGoodsByMachineId(vo.getMachineId());
	}

	/**
	 * 一键清空
	 */
	@RequestMapping(value="clearAll", method = {RequestMethod.POST})
	public Result<String> clearAll(@RequestBody SupplyRequestVo vo){
		logger.info("一键清空接口参数：{}",JSON.toJSON(vo));
        return supplyChannelService.clearAll(vo.getMachineId());
	}

	/**
	 * 一键补货
	 */
	@RequestMapping(value="supplyAll", method = {RequestMethod.POST})
	public Result<String> supplyAll(@RequestBody SupplyRequestVo vo){
		logger.info("一键补货接口参数：{}",JSON.toJSON(vo));
        return supplyChannelService.supplyAll(vo.getMachineId());
	}

	/**
	 * 提交补货
	 */
	@RequestMapping(value="submit",method = { RequestMethod.POST})
	public Result<String> submit(@RequestBody Map<String,Object> map){
		List<Map<String,Object>> mapList = (List<Map<String,Object>>) map.get("list");
		logger.info("提交补货接口参数：{}",JSON.toJSON(mapList));
        return supplyChannelService.submit(mapList);
	}

	/**
	 * 工单列表
	 */
	@RequestMapping(value="workOrderList",method = {RequestMethod.POST})
	public ModelAndView workOrderListByPage(@RequestBody SupplyRequestVo vo){
		logger.info("查询工单列表接口参数：{}",JSON.toJSON(vo));
		List<WorkOrderVo> list = supplyChannelService.findByPage(vo.getKeyword(),vo.getFindTime());
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 工单详情
	 */
	@RequestMapping(value="workOrderDetail",method = {RequestMethod.POST})
	public Result<WorkOrderVo> workOrderDetail(@RequestBody SupplyRequestVo vo){
		logger.info("查询工单详情接口参数：{}",JSON.toJSON(vo));
        return supplyChannelService.workOrderDetail(vo.getMachineId(),vo.getBatchNo());
	}


	/**
	 * 查询缺货货道并发送push
	 */
	@RequestMapping(value = "findAndPushByTaskParam",method = {RequestMethod.POST})
	public void findAndPushByTaskParam(@RequestBody SupplyRequestVo vo){
		logger.info("查询缺货货道并发送push接口参数：{}",JSON.toJSON(vo));
		supplyChannelService.findAndPushByTaskParam(vo.getLackGoodsType());
	}


}
