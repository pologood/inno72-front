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
@RequestMapping("/machine/channel/h5")
public class SupplyChannelH5Controller {
	@Resource
	private SupplyChannelService supplyChannelService;


	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 货道列表
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET })
	public Result<List<Inno72SupplyChannel>> list(SupplyRequestVo vo) {
		logger.info("货道列表H5接口参数：{}", JSON.toJSON(vo));
		return supplyChannelService.getList(vo.getMachineId());
	}

	/**
	 * 机器维度缺货
	 */
	@RequestMapping(value="machineLack",method = {RequestMethod.GET })
	public Result<List<Inno72Machine>> getMachineLack(){
		logger.info("机器维度缺货H5接口");
		return supplyChannelService.getMachineLackGoods();
	}

	/**
	 * 商品维度缺货
	 */
	@RequestMapping(value="goodsLack",method = {RequestMethod.GET })
	public Result<List<Inno72Goods>> getGoodsLack(){
		logger.info("商品维度缺货H5接口");
		return supplyChannelService.getGoodsLack();
	}

	/**
	 * 查询单个商品缺货的机器
	 */
	@RequestMapping(value="machineByLackGoods",method = {RequestMethod.GET })
	public Result<List<Inno72Machine>> getMachineByLackGoods(SupplyRequestVo vo){
		logger.info("查询单个商品缺货的机器H5接口参数：{}",JSON.toJSON(vo));
		return supplyChannelService.getMachineByLackGoods(vo.getGoodsId());
	}

	/**
	 * 根据机器查询可用商品
	 */
	@RequestMapping(value="getGoodsByMachineId",method = {RequestMethod.GET })
	public Result<List<Inno72Goods>> getGoodsByMachineId(SupplyRequestVo vo){
		logger.info("根据机器查询可用商品H5接口参数：{}",JSON.toJSON(vo));
		return supplyChannelService.getGoodsByMachineId(vo.getMachineId());
	}

	/**
	 * 提交补货
	 */
	@RequestMapping(value="submit",method = { RequestMethod.POST})
	public Result<String> submit(@RequestBody Map<String,Object> map){
		List<Map<String,Object>> mapList = (List<Map<String,Object>>) map.get("list");
		logger.info("提交补货H5接口参数：{}",JSON.toJSON(mapList));
        return supplyChannelService.submit(mapList);
	}

	/**
	 * 工单列表
	 */
	@RequestMapping(value="workOrderList",method = {RequestMethod.GET })
	public ModelAndView workOrderListByPage(SupplyRequestVo vo){
		logger.info("查询工单列表H5接口参数：{}",JSON.toJSON(vo));
		List<WorkOrderVo> list = supplyChannelService.findByPage(vo.getKeyword(),vo.getFindTime());
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 工单详情
	 */
	@RequestMapping(value="workOrderDetail",method = {RequestMethod.GET})
	public Result<WorkOrderVo> workOrderDetail(SupplyRequestVo vo){
		logger.info("查询工单详情H5接口参数：{}",JSON.toJSON(vo));
        return supplyChannelService.workOrderDetail(vo.getMachineId(),vo.getBatchNo());
	}

}
