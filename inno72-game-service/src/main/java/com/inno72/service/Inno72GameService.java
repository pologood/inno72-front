package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72Game;
import com.inno72.vo.UserSessionVo;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
public interface Inno72GameService extends Service<Inno72Game> {
	public Result<String> updateRefOrderId(String inno72OrderId, String refId, String thirdUserId);

	public void updateOrderReport(UserSessionVo userSessionVo);

	public boolean countSuccOrder(String channelId, String channelUserKey, String activityPlanId);

	/**
	 * 计算用户玩的次数（下单成功，并且出货）每天，派样活动
	 * @param channelId
	 * @param channelUserKey
	 * @param activityPlanId
	 * @param goodsId
	 * @return
	 */
	public boolean countSuccOrderPy(String channelId, String channelUserKey, String activityPlanId, String goodsId);

	public boolean countSuccOrderNologin(String channelId, String activityPlanId);
}
