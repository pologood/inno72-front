package com.inno72.machine.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.common.*;
import com.inno72.machine.mapper.*;
import com.inno72.machine.model.*;
import com.inno72.machine.service.SupplyChannelService;
import com.inno72.machine.vo.SupplyChannelVo;
import com.inno72.machine.vo.WorkOrderVo;
import com.inno72.redis.IRedisUtil;
import com.inno72.utils.page.Pagination;
import com.mongodb.DBCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
@Service
@Transactional
public class SupplyChannelServiceImpl extends AbstractService<Inno72SupplyChannel> implements SupplyChannelService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private Inno72SupplyChannelMapper inno72SupplyChannelMapper;

	@Resource
	private Inno72SupplyChannelGoodsMapper inno72SupplyChannelGoodsMapper;

	@Resource
	private Inno72MachineMapper inno72MachineMapper;

	@Resource
	private Inno72GoodsMapper inno72GoodsMapper;

	@Resource
	private Inno72SupplyChannelHistoryMapper inno72SupplyChannelHistoryMapper;

	@Resource
	private MongoOperations mongoTpl;

	@Resource
	private IRedisUtil redisUtil;

	@Resource
	private Inno72SupplyChannelOrderMapper inno72SupplyChannelOrderMapper;

	@Override
	public Result<String> merge(Inno72SupplyChannel supplyChannel) {
		String code = supplyChannel.getCode();
		String machineId = supplyChannel.getMachineId();
		if (StringUtil.isEmpty(code) || StringUtil.isEmpty(machineId)) {
			return ResultGenerator.genFailResult("参数有误");
		}
		Inno72Machine machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		String machineCode = machine.getMachineCode();
		Integer codeInt = Integer.parseInt(code);
		String subString = machineCode.substring(0,2);
		if (subString.equals("18") && codeInt > 30) {
			return Results.failure("当前货道不能合并");
		}else if(subString.equals("19") && codeInt >40){
			return Results.failure("当前货道不能合并");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", machineId);
		Integer[] codes = new Integer[2];
		codes[0] = codeInt;
		Integer childCode;
		Integer parentCode;
		if (codeInt % 2 == 0) {
			codes[1] = (codeInt - 1);
			childCode = codes[0];
			parentCode = codes[1];
		} else {
			codes[1] = (codeInt + 1);
			childCode = codes[1];
			parentCode = codes[0];
		}

		map.put("codes", codes);
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
		if (list != null && list.size() == 2) {
			Inno72SupplyChannel parentChannel = new Inno72SupplyChannel();
			parentChannel.setCode(parentCode.toString());
			parentChannel.setMachineId(machineId);
			parentChannel.setGoodsCount(0);
			parentChannel.setUpdateTime(LocalDateTime.now());
			inno72SupplyChannelMapper.updateByParam(parentChannel);// 修改主货道
			Condition childCondition = new Condition(Inno72SupplyChannel.class);
			childCondition.createCriteria().andEqualTo("code", childCode.toString()).andEqualTo("machineId", machineId);
			inno72SupplyChannelMapper.deleteByCondition(childCondition);// 删除子货道
			for (Inno72SupplyChannel supply : list) {
				Condition condition = new Condition(Inno72SupplyChannelGoods.class);
				condition.createCriteria().andEqualTo("supplyChannelId", supply.getId());
				inno72SupplyChannelGoodsMapper.deleteByCondition(condition);// 删除货道关联商品
			}
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult("该货道已合并");
		}
	}

	@Override
	public Result<String> split(Inno72SupplyChannel supplyChannel) {
		String code = supplyChannel.getCode();
		String machineId = supplyChannel.getMachineId();
		if (StringUtil.isEmpty(code) || StringUtil.isEmpty(machineId)) {
			return Results.failure("参数有误");
		}
		int codeInteger = Integer.parseInt(code);
		if (codeInteger % 2 == 0) {
			return Results.failure("当前货道不能拆分");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", machineId);
		map.put("code", code);
		supplyChannel = inno72SupplyChannelMapper.selectByParam(map);
		if (supplyChannel != null) {
			Integer newCode = codeInteger + 1;
			map.put("code", newCode.toString());
			Inno72SupplyChannel childChannel = inno72SupplyChannelMapper.selectByParam(map);
			if (childChannel != null) {
				return Results.failure("货道未合并不能拆分");
			}
			supplyChannel.setGoodsCount(0);
			supplyChannel.setUpdateTime(LocalDateTime.now());
			inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
			Condition condition = new Condition(Inno72SupplyChannelGoods.class);
			condition.createCriteria().andEqualTo("supplyChannelId", supplyChannel.getId());
			inno72SupplyChannelGoodsMapper.deleteByCondition(condition);
			childChannel = new Inno72SupplyChannel();
			childChannel.setId(StringUtil.getUUID());

			int volumeCount = 50;
			if (newCode < 20) {
				volumeCount = 11;
			} else if (newCode > 20 && newCode < 30) {
				volumeCount = 5;
			}
			childChannel.setVolumeCount(volumeCount);
			childChannel.setCode(newCode.toString());
			childChannel.setCreateTime(LocalDateTime.now());
			childChannel.setCreateId("系统");
			childChannel.setUpdateTime(LocalDateTime.now());
			childChannel.setUpdateId("系统");
			childChannel.setName("货道" + newCode);
			childChannel.setGoodsCount(0);
			childChannel.setWorkStatus(0);
			childChannel.setMachineId(supplyChannel.getMachineId());
			inno72SupplyChannelMapper.insertSelective(childChannel);
			return ResultGenerator.genSuccessResult();
		} else {
			return ResultGenerator.genFailResult("操作货道有误");
		}
	}

	/**
	 * 货道清零
	 *
	 */
	@Override
	public Result<String> clear(Inno72SupplyChannel supplyChannel) {
		String[] codes = supplyChannel.getCodes();
		String machineId = supplyChannel.getMachineId();
		if (codes == null || StringUtil.isEmpty(machineId)) {
			return ResultGenerator.genFailResult("参数有误");
		}
		supplyChannel.setGoodsCount(0);
		Map<String, Object> map = new HashMap<>();
		map.put("codes", codes);
		map.put("machineId", machineId);
		supplyChannel.setUpdateTime(LocalDateTime.now());
		inno72SupplyChannelMapper.updateListByParam(supplyChannel);
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
		for (Inno72SupplyChannel channel : list) {
			channel.setRemark("货道商品数量清零");
			addSupplyChannelToMongo(channel);
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<Map<String, Object>> history(Inno72SupplyChannel supplyChannel) {
		String machineId = supplyChannel.getMachineId();
		String code = supplyChannel.getCode();
		if (StringUtil.isEmpty(machineId)) {
			return Results.failure("机器ID不能为空");
		}

		Query query = new Query();
		Criteria criteria = Criteria.where("machineId").is(machineId);
		if (StringUtil.isNotEmpty(code)) {
			criteria.and("code").is(code);
		}
		query.addCriteria(criteria);
		query.with(new Sort(Sort.Direction.DESC, "updateTime"));
		int pageNo = 0;
		int pageSize = 20;
		Long count = mongoTpl.count(query, Inno72SupplyChannel.class, "supplyChannel");
		Pagination pagination = new Pagination(pageNo, pageSize, count.intValue());
		query.skip((pageNo - 1) * pageSize).limit(pageSize);
		List<Inno72SupplyChannel> supplyChannelList = mongoTpl.find(query, Inno72SupplyChannel.class, "supplyChannel");
		logger.info("mongo返回data:{}", JSON.toJSON(supplyChannelList));
		Map<String, Object> map = new HashMap<>();
		map.put("data", supplyChannelList);
		map.put("page", pagination);
		map.put("msg", "成功");
		map.put("code", 0);
		return ResultGenerator.genSuccessResult(map);
	}

	@Override
	public Result<List<Inno72SupplyChannel>> getList(String machineId) {
		if (StringUtil.isEmpty(machineId)) {
			return Results.failure("参数不能为空");
		}
		Condition condition = new Condition(Inno72SupplyChannel.class);
		condition.createCriteria().andEqualTo("machineId", machineId).andEqualTo("status", 0);
		condition.orderBy("code");
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", machineId);
		map.put("status", 0);
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
		return ResultGenerator.genSuccessResult(list);
	}

	@Override
	public Result<List<Inno72Machine>> getMachineLackGoods() {
		Inno72CheckUser checkUser = UserUtil.getUser();
		String checkUserId = checkUser.getId();
		List<Inno72Machine> machineList = inno72MachineMapper.getMachine(checkUserId);
		if (machineList != null && machineList.size() > 0) {
			for (Inno72Machine machine : machineList) {
				Integer lackGoodsStatus = 0;
				List<SupplyChannelVo> supplyChannelVoList = machine.getSupplyChannelVoList();
				if (supplyChannelVoList != null && supplyChannelVoList.size() > 0) {
					Map<String, Integer> map = new HashMap<>();
					for (SupplyChannelVo supplyChannelVo : supplyChannelVoList) {
						String goodsId = supplyChannelVo.getGoodsId();
						if (StringUtil.isNotEmpty(goodsId)) {
							int goodsCount = supplyChannelVo.getGoodsCount();
							if (map.containsKey(goodsId)) {
								int count = map.get(goodsId);
								count += goodsCount;
								map.put(goodsId, count);
							} else {
								map.put(goodsId, goodsCount);
							}
						}
					}
					for (Integer value : map.values()) {
						if (value < 5) {
							lackGoodsStatus = 1;
							break;
						}
					}
				}
				machine.setLackGoodsStatus(lackGoodsStatus);
				machine.setSupplyChannelVoList(null);
			}
		}
		logger.info("机器缺货返回数据：{}", JSON.toJSON(machineList));
		return ResultGenerator.genSuccessResult(machineList);
	}

	@Override
	public Result<List<Inno72Goods>> getGoodsLack() {
		Inno72CheckUser checkUser = UserUtil.getUser();
		String checkUserId = checkUser.getId();
		List<Inno72Goods> inno72GoodsList = inno72GoodsMapper.getLackGoods(checkUserId);
		logger.info("商品数据：{}", JSON.toJSON(inno72GoodsList));
		List<Inno72Goods> resultList = new ArrayList<>();
		if (inno72GoodsList != null && inno72GoodsList.size() > 0) {

			for (Inno72Goods inno72Goods : inno72GoodsList) {
				List<SupplyChannelVo> supplyChannelVoList = inno72Goods.getSupplyChannelVoList();
				if (supplyChannelVoList != null && supplyChannelVoList.size() > 0) {
					int totalVolumeCount = 0;
					int totalGoodsCount = 0;
					for (SupplyChannelVo supplyChannelVo : supplyChannelVoList) {
						int volumeCount = supplyChannelVo.getVolumeCount();
						int goodsCount = supplyChannelVo.getGoodsCount();
						totalVolumeCount += volumeCount;
						totalGoodsCount += goodsCount;
					}
					inno72Goods.setLackGoodsCount(totalVolumeCount - totalGoodsCount);
					inno72Goods.setSupplyChannelVoList(null);
					inno72Goods.setImg(ImageUtil.getLongImageUrl(inno72Goods.getImg()));
					inno72Goods.setTotalGoodsCount(totalGoodsCount);
					resultList.add(inno72Goods);
				}
			}
		}
		return ResultGenerator.genSuccessResult(resultList);
	}

	@Override
	public Result<List<Inno72Machine>> getMachineByLackGoods(String goodsId) {
		if (StringUtil.isEmpty(goodsId)) {
			return Results.failure("参数不能为空");
		}
		Inno72CheckUser checkUser = UserUtil.getUser();
		String checkUserId = checkUser.getId();
		List<Inno72Machine> machineList = inno72MachineMapper.getMachineByLackGoods(checkUserId, goodsId);
		List<Inno72Machine> resultList = new ArrayList<>();
		if (machineList != null && machineList.size() > 0) {
			for (Inno72Machine machine : machineList) {
				List<SupplyChannelVo> supplyChannelVoList = machine.getSupplyChannelVoList();
				int totalVolumeCount = 0;
				int totalGoodsCount = 0;
				for (SupplyChannelVo supplyChannelVo : supplyChannelVoList) {
					int volumeCount = supplyChannelVo.getVolumeCount();
					int goodsCount = supplyChannelVo.getGoodsCount();
					totalVolumeCount += volumeCount;
					totalGoodsCount += goodsCount;
				}
				if (totalVolumeCount - totalGoodsCount > 0) {
					machine.setLackGoodsCount(totalVolumeCount - totalGoodsCount);
					machine.setSupplyChannelVoList(null);
					machine.setTotalGoodsCount(totalGoodsCount);
					resultList.add(machine);
				}
			}
		}
		return ResultGenerator.genSuccessResult(resultList);
	}

	@Override
	public Result<List<Inno72Goods>> getGoodsByMachineId(String machineId) {
		if (StringUtil.isEmpty(machineId)) {
			return Results.failure("参数不能为空");
		}
		List<Inno72Goods> list = inno72GoodsMapper.selectByMachineId(machineId);
		return ResultGenerator.genSuccessResult(list);
	}

	@Override
	public Result<String> clearAll(String machineId) {
		if (StringUtil.isEmpty(machineId)) {
			return Results.failure("参数不能为空");
		}
		Condition condition = new Condition(Inno72SupplyChannel.class);
		condition.createCriteria().andEqualTo("machineId", machineId);
		List<Inno72SupplyChannel> supplyChannelList = inno72SupplyChannelMapper.selectByCondition(condition);
		if (supplyChannelList != null && supplyChannelList.size() > 0) {
			StringBuffer idBuffer = new StringBuffer();
			supplyChannelList.forEach(supplyChannel -> {
				supplyChannel.setUpdateTime(LocalDateTime.now());
				supplyChannel.setGoodsCount(0);
				inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
				idBuffer.append(supplyChannel.getId());
				idBuffer.append(",");
			});
			inno72SupplyChannelGoodsMapper.deleteByIds(idBuffer.substring(0, idBuffer.length() - 1));
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> supplyAll(String machineId) {
		if (StringUtil.isEmpty(machineId)) {
			return Results.failure("参数不能为空");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("machineId", machineId);
		map.put("status", 0);
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
		if (list != null && list.size() > 0) {
			String batchNo = StringUtil.getUUID();
			List<Inno72SupplyChannelHistory> historyList = new ArrayList<>();
			LocalDateTime now = LocalDateTime.now();
			for (Inno72SupplyChannel supplyChannel : list) {
				String goodsId = supplyChannel.getGoodsId();
				if (StringUtil.isEmpty(goodsId)) {
					Inno72SupplyChannelHistory history = new Inno72SupplyChannelHistory();
					history.setId(StringUtil.getUUID());
					history.setBeforeCount(supplyChannel.getGoodsCount());
					history.setAfterCount(supplyChannel.getVolumeCount());
					history.setBatchNo(batchNo);
					history.setMachineId(supplyChannel.getMachineId());
					history.setUserId("");
					history.setCreateTime(now);
					historyList.add(history);
					supplyChannel.setGoodsCount(supplyChannel.getVolumeCount());
					supplyChannel.setUpdateTime(LocalDateTime.now());
					inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
				}
			}
			inno72SupplyChannelHistoryMapper.insertList(historyList);
		}

		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> submit(List<Map<String, Object>> mapList) {
		if (mapList == null || mapList.size() == 0) {
			return Results.failure("参数不能为空");
		}
		StringBuffer ids = new StringBuffer();
		mapList.forEach(map -> {
			String id = map.get("id").toString();
			ids.append("\"");
			ids.append(id);
			ids.append("\"");
			ids.append(",");
		});
		String supplyChannelIds = ids.substring(0, ids.length() - 1);
		List<Inno72SupplyChannel> supplyChannelList = inno72SupplyChannelMapper.selectByIds(supplyChannelIds);
		if (supplyChannelList != null && supplyChannelList.size() > 0) {
			String batchNo = StringUtil.getUUID();
			LocalDateTime now = LocalDateTime.now();
			Inno72SupplyChannelOrder order = new Inno72SupplyChannelOrder();
			order.setId(batchNo);
			order.setCreateTime(now);
			order.setMachineId(supplyChannelList.get(0).getMachineId());
			order.setType(1);
			order.setUserId(UserUtil.getUser().getId());
			inno72SupplyChannelOrderMapper.insertSelective(order);
			supplyChannelList.forEach(supplyChannel -> {
				String supplyChannelId = supplyChannel.getId();
				mapList.forEach(map -> {
					String id = map.get("id").toString();
					if (id.equals(supplyChannelId)) {
						Object goodsCount = map.get("goodsCount");
						int afterGoodsCount = 0;
						if (goodsCount != null) {
							afterGoodsCount = Integer.parseInt(map.get("goodsCount").toString());
						}
						int beforeGoodsCount = supplyChannel.getGoodsCount();
						supplyChannel.setUpdateTime(now);
						Object goodsId = map.get("goodsId");
						String goodsIdStr = "";
						if (goodsId != null) {
							goodsIdStr = map.get("goodsId").toString();
						}
						supplyChannel.setGoodsId(goodsIdStr);
						supplyChannel.setGoodsCount(afterGoodsCount);
						inno72SupplyChannelMapper.updateByPrimaryKeySelective(supplyChannel);
						Condition condition = new Condition(Inno72SupplyChannelGoods.class);
						condition.createCriteria().andEqualTo("supplyChannelId", supplyChannel.getId());
						inno72SupplyChannelGoodsMapper.deleteByCondition(condition);
						Inno72SupplyChannelGoods goods = new Inno72SupplyChannelGoods();
						if (StringUtil.isNotEmpty(goodsIdStr)) {
							goods.setGoodsId(supplyChannel.getGoodsId());
							goods.setId(StringUtil.getUUID());
							goods.setSupplyChannelId(supplyChannelId);
							inno72SupplyChannelGoodsMapper.insertSelective(goods);
						}
						Inno72SupplyChannelHistory history = new Inno72SupplyChannelHistory();
						history.setId(StringUtil.getUUID());
						history.setBeforeCount(beforeGoodsCount);
						history.setAfterCount(afterGoodsCount);
						history.setBatchNo(batchNo);
						history.setSupplyChannelId(supplyChannelId);
						history.setMachineId(supplyChannel.getMachineId());
						history.setUserId(UserUtil.getUser().getId());
						history.setCreateTime(now);
						inno72SupplyChannelHistoryMapper.insertSelective(history);
					}
				});

			});
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public List<WorkOrderVo> findByPage(String keyword, String findTime) {
		Inno72CheckUser checkUser = UserUtil.getUser();
		String checkUserId = checkUser.getId();
		Map<String, Object> map = new HashMap<>();
		map.put("checkUserId", checkUserId);
		if (StringUtil.isNotEmpty(keyword) && StringUtil.isNotEmpty(keyword.trim())) {
			map.put("keyword", keyword.trim());
		}
		if (StringUtil.isNotEmpty(findTime) && StringUtil.isNotEmpty(findTime.trim())) {
			map.put("beginTime", findTime.trim() + " 00:00:00");
			map.put("endTime", findTime.trim() + " 23:59:59");
		}
		return inno72SupplyChannelOrderMapper.selectByPage(map);
	}

	@Override
	public Result<WorkOrderVo> workOrderDetail(String machineId, String batchNo) {
		if (StringUtil.isEmpty(batchNo)) {
			return Results.failure("参数不能为空");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("batchNo", batchNo);
		WorkOrderVo workOrderVo = new WorkOrderVo();
		List<Inno72SupplyChannelHistory> historyList = inno72SupplyChannelHistoryMapper.getWorkOrderGoods(map);
		List<Inno72SupplyChannelHistory> resultList = new ArrayList<>();
		if (historyList != null && historyList.size() > 0) {
			workOrderVo.setBatchNo(batchNo);
			workOrderVo.setMachineCode(historyList.get(0).getMachineCode());
			workOrderVo.setCreateTime(historyList.get(0).getCreateTime());
			workOrderVo.setLocaleStr(historyList.get(0).getLocaleStr());
			workOrderVo.setMachineId(machineId);
			Set<String> set = new HashSet<>();

			for (Inno72SupplyChannelHistory history : historyList) {
				String goodsName = history.getGoodsName();
				if (!set.contains(goodsName)) {
					set.add(goodsName);
					int count = 0;
					for (Inno72SupplyChannelHistory his : historyList) {
						String name = his.getGoodsName();
						if (name.equals(goodsName)) {
							count += his.getSubCount();
						}
					}
					if (count > 0) {
						history.setSubCount(count);
						resultList.add(history);
					}

				}

			}
			workOrderVo.setHistoryList(resultList);
		}
		return ResultGenerator.genSuccessResult(workOrderVo);
	}


	public void addSupplyChannelToMongo(Inno72SupplyChannel supplyChannel) {
		DBCollection dbCollection = mongoTpl.getCollection("supplyChannel");
		if (dbCollection == null) {
			mongoTpl.createCollection("supplyChannel");
		}
		supplyChannel.setId(null);
		mongoTpl.save(supplyChannel, "supplyChannel");
	}

}
