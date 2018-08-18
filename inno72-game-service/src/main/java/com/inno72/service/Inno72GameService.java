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
}
