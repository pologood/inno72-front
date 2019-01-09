package com.inno72.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.JSR303Util;
import com.inno72.mapper.Inno72GoodsMapper;
import com.inno72.mapper.Inno72InteractMapper;
import com.inno72.mapper.Inno72MerchantMapper;
import com.inno72.mapper.Inno72MerchantUserMapper;
import com.inno72.mapper.Inno72StoreExpressMapper;
import com.inno72.mapper.Inno72StoreOrderMapper;
import com.inno72.model.Inno72Goods;
import com.inno72.model.Inno72Interact;
import com.inno72.model.Inno72MerchantUser;
import com.inno72.model.Inno72StoreExpress;
import com.inno72.model.Inno72StoreOrder;
import com.inno72.service.Inno72StoreOrderService;
import com.inno72.util.StringUtil;
import com.inno72.vo.Inno72StoreOrderVo;
import com.inno72.vo.Inno72StoreVo;


/**
 * Created by CodeGenerator on 2019/01/02.
 */
@Service
@Transactional
public class Inno72StoreOrderServiceImpl extends AbstractService<Inno72StoreOrder> implements Inno72StoreOrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72StoreOrderServiceImpl.class);

	@Resource
	private Inno72StoreOrderMapper inno72StoreOrderMapper;

	@Resource
	private Inno72StoreExpressMapper inno72StoreExpressMapper;

	@Resource
	private Inno72GoodsMapper inno72GoodsMapper;

	@Resource
	private Inno72MerchantUserMapper inno72MerchantUserMapper;

	@Resource
	private Inno72InteractMapper inno72InteractMapper;

	@Resource
	private Inno72MerchantMapper inno72MerchantMapper;

	@Override
	public Result<String> addOrder(Inno72StoreVo vo) {

		Result<String> valid = JSR303Util.valid(vo);
		if (valid.getCode() == Result.FAILURE) {
			return valid;
		}

		LOGGER.info("添加商品物流单 参数 -> {}", JSON.toJSONString(vo));

		String goodsId = vo.getGoodsId();// 商品ID
		String activityId = vo.getActivityId();// 活动ID
		String merchantId = vo.getMerchantId();// 商户用户ID
		List<Inno72StoreOrderVo> order = vo.getOrder();

		if (order.size() == 0){
			return Results.failure("物流单不存在!");
		}

		Inno72Goods inno72Goods = inno72GoodsMapper.selectByPrimaryKey(goodsId);
		if (inno72Goods == null) {
			LOGGER.info("ID【{}】的商品 不存在！！！", goodsId);
			return Results.failure("商品不存在!");
		}
		String sellerId = inno72Goods.getSellerId();

		Inno72MerchantUser user = inno72MerchantUserMapper.selectByMerchantId(merchantId);
		if (user == null) {
			LOGGER.info("ID【{}】的商户用户 不存在！！！", merchantId);
			return Results.failure("用户错误!");
		}

		Inno72Interact inno72Interact = inno72InteractMapper.selectByPrimaryKey(activityId);
		if (inno72Interact == null) {
			LOGGER.info("ID【{}】的活动 不存在！！！", activityId);
			return Results.failure("活动错误!");
		}

		Map<String, List<Inno72StoreOrderVo>> storeGroup = new HashMap<>();
		for (Inno72StoreOrderVo orderVo : order){
			String storeId = orderVo.getStoreId();
			List<Inno72StoreOrderVo> inno72StoreOrderVos = storeGroup.get(storeId);
			if (inno72StoreOrderVos == null){
				inno72StoreOrderVos = new ArrayList<>();
			}
			inno72StoreOrderVos.add(orderVo);
			storeGroup.put(storeId, inno72StoreOrderVos);
		}

		LocalDateTime now = LocalDateTime.now();

		List<Inno72StoreOrder> insertOrders = new ArrayList<>();
		List<Inno72StoreExpress> insertExpress = new ArrayList<>();

		for (Map.Entry<String, List<Inno72StoreOrderVo>> store : storeGroup.entrySet()){

			/*
			 * @param goods
			 * @param merchant sellerId
			 * @param sender 商户名称
			 * @param sendId 商户ID
			 * @param receiver 仓库名称
			 * @param receiveId 仓库ID
			 * @param number 商品数量
			 * @param creater
			 * @param updater
			 */
			Inno72StoreOrder inno72StoreOrder = new Inno72StoreOrder(
					activityId, goodsId, sellerId,
					user.getMerchantName(), user.getMerchantId(), "",
					"", 0, user.getLoginName());

			List<Inno72StoreOrderVo> value = store.getValue();

			String storeName = null;
			String storeId = null;
			Integer totalGoodsNum = 0;
			for (Inno72StoreOrderVo storeOrderVo : value){
				Integer goodsNum = storeOrderVo.getGoodsNum();
				String logisticsName = storeOrderVo.getLogisticsName();
				String logisticsNo = storeOrderVo.getLogisticsNo();
				if (StringUtil.isEmpty(storeId)) {
					storeId = storeOrderVo.getStoreId();
				}
				if (StringUtil.isEmpty(storeName)){
					storeName = storeOrderVo.getStoreName();
				}
				totalGoodsNum += goodsNum;

				/*
				 * @param orderId 出入库单ID
				 * @param expressNum 物流单号
				 * @param expressCompany 物流公司
				 * @param number 数量
				 * @param creater
				 */
				insertExpress.add(new Inno72StoreExpress(inno72StoreOrder.getId(), logisticsNo, logisticsName, goodsNum, user.getLoginName()));
			}
			inno72StoreOrder.setReceiveId(storeId);
			inno72StoreOrder.setNumber(totalGoodsNum);
			inno72StoreOrder.setReceiver(storeName);

			insertOrders.add(inno72StoreOrder);

		}

		LOGGER.info("插入的订单 -> {}", JSON.toJSONString(insertOrders));
		LOGGER.info("插入的物流单 -> {}", JSON.toJSONString(insertExpress));

		inno72StoreOrderMapper.insertS(insertOrders);
		inno72StoreExpressMapper.insertS(insertExpress);

		return Results.success();
	}

	@Override
	public Result<List<Map<String, String>>> findStoreOrder(String merchantId, String activityId) {
		return Results.success(inno72StoreOrderMapper.findStoreOrder(merchantId, activityId));
	}
}
