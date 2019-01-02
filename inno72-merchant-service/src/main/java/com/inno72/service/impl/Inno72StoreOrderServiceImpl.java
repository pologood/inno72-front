package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.JSR303Util;
import com.inno72.mapper.Inno72GoodsMapper;
import com.inno72.mapper.Inno72InteractMapper;
import com.inno72.mapper.Inno72MerchantMapper;
import com.inno72.mapper.Inno72MerchantUserMapper;
import com.inno72.mapper.Inno72StoreOrderMapper;
import com.inno72.model.Inno72Goods;
import com.inno72.model.Inno72Interact;
import com.inno72.model.Inno72MerchantUser;
import com.inno72.model.Inno72StoreOrder;
import com.inno72.service.Inno72StoreOrderService;
import com.inno72.common.AbstractService;
import com.inno72.vo.Inno72StoreVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/01/02.
 */
@Service
@Transactional
public class Inno72StoreOrderServiceImpl extends AbstractService<Inno72StoreOrder> implements Inno72StoreOrderService {

	private static final Logger LOGGER  = LoggerFactory.getLogger(Inno72StoreOrderServiceImpl.class);

    @Resource
    private Inno72StoreOrderMapper inno72StoreOrderMapper;

    @Resource
    private Inno72GoodsMapper inno72GoodsMapper;

    @Resource
    private Inno72MerchantMapper inno72MerchantMapper;

    @Resource
    private Inno72MerchantUserMapper inno72MerchantUserMapper;

    @Resource
    private Inno72InteractMapper inno72InteractMapper;

	@Override
	public Result<String> addOrder(Inno72StoreVo vo) {

		Result<String> valid = JSR303Util.valid(vo);
		if (valid.getCode() == Result.FAILURE){
			return valid;
		}

		LOGGER.info("添加商品物流单 参数 -> {}", JSON.toJSONString(vo));

		String goodsId = vo.getGoodsId();//商品ID
		String activityId = vo.getActivityId();//活动ID
		String merchantId = vo.getMerchantId();// 商户用户ID

		Inno72Goods inno72Goods = inno72GoodsMapper.selectByPrimaryKey(goodsId);
		if (inno72Goods == null){
			LOGGER.info("ID【{}】的商品 不存在！！！", goodsId);
			return Results.failure("商品不存在!");
		}
		String sellerId = inno72Goods.getSellerId();

		Inno72MerchantUser user =
				inno72MerchantUserMapper.selectByPrimaryKey(merchantId);
		if (user == null){
			LOGGER.info("ID【{}】的商户用户 不存在！！！", merchantId);
			return Results.failure("用户错误!");
		}

		Inno72Interact inno72Interact = inno72InteractMapper.selectByPrimaryKey(activityId);
		if (inno72Interact == null){
			LOGGER.info("ID【{}】的活动 不存在！！！", merchantId);
			return Results.failure("活动错误!");
		}



		return null;
	}
}
