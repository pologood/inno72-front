package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.mapper.Inno72ActivityPlanGameResultMapper;
import com.inno72.mapper.Inno72GoodsMapper;
import com.inno72.mapper.Inno72SupplyChannelMapper;
import com.inno72.model.Inno72ActivityPlanGameResult;
import com.inno72.model.Inno72Goods;
import com.inno72.model.Inno72Order;
import com.inno72.model.Inno72SupplyChannel;
import com.inno72.vo.GameResultVo;
import com.inno72.vo.UserSessionVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/activity")
public class Inno72ActivityController {

	@Resource
	private GameSessionRedisUtil gameSessionRedisUtil;

	@Resource
	private Inno72ActivityPlanGameResultMapper inno72ActivityPlanGameResultMapper;

	@Resource
	private Inno72GoodsMapper inno72GoodsMapper;

	@Resource
	private Inno72SupplyChannelMapper inno72SupplyChannelMapper;

	@Value("${top_h5_err_url}")
	private String topH5ErrUrl;

	/**
	 * 获得商品信息
	 * @param sessionUuid
	 * @return
	 */
	@RequestMapping(value = "/findGoods", method = {RequestMethod.POST, RequestMethod.GET})
	public Result findGoods(String sessionUuid) {

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);

		if (userSessionVo == null) {
			return Results.failure("会话不存在!" + sessionUuid);
		}

		List<GameResultVo> gameResultVoList = new ArrayList<>();

		Map<String, String> map = new HashMap<>();
		map.put("activityPlanId", userSessionVo.getActivityPlanId());
		map.put("prizeType", Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT.getKey().toString());
		List<Inno72ActivityPlanGameResult> inno72ActivityPlanGameResults = inno72ActivityPlanGameResultMapper.selectAllResultByCode(map);
		for (Inno72ActivityPlanGameResult inno72ActivityPlanGameResult : inno72ActivityPlanGameResults) {
			GameResultVo gameResultVo = new GameResultVo();

			String prizeId = inno72ActivityPlanGameResult.getPrizeId();
			Integer resultCode = inno72ActivityPlanGameResult.getResultCode();
			Inno72Goods inno72Goods = inno72GoodsMapper.selectByPrimaryKey(prizeId);

			gameResultVo.setGoodsId(prizeId);
			gameResultVo.setGoodsCode(inno72Goods.getCode());
			gameResultVo.setResultCode(resultCode);

			// 根据商品id查询货道
			Map<String, String> channelParam = new HashMap<String, String>();
			channelParam.put("goodId", prizeId);
			channelParam.put("machineId", userSessionVo.getMachineId());
			List<Inno72SupplyChannel> inno72SupplyChannels = inno72SupplyChannelMapper
					.selectByGoodsId(channelParam);

			Integer goodsCount = 0;
			if (CollectionUtils.isNotEmpty(inno72SupplyChannels)) {
				// 所有具有相同商品id的货道中中道商品数量相加
				for (Inno72SupplyChannel channel : inno72SupplyChannels) {
					Integer isDel = channel.getIsDelete();
					if (isDel != 0) {
						continue;
					}
					goodsCount += channel.getGoodsCount();
				}
			}
			gameResultVo.setGoodsName(inno72Goods.getName());
			gameResultVo.setImg(inno72Goods.getImg());
			gameResultVo.setBanner(inno72Goods.getBanner());
			gameResultVo.setGoodsCount(goodsCount);
			gameResultVo.setSpecRemark(inno72Goods.getSpecRemark());
			gameResultVo.setPrice(inno72Goods.getPrice() != null ? inno72Goods.getPrice().setScale(2).toString() : "");
			gameResultVoList.add(gameResultVo);
		}
		return Results.success(gameResultVoList);
	}

}
