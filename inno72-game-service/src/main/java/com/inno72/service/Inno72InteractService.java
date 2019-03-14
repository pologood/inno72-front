package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72Interact;
import com.inno72.vo.Inno72InteractVo;

/**
 * 派样活动表
 */
public interface Inno72InteractService extends Service<Inno72Interact> {
	Inno72InteractVo findPlanCodeByMid(String mid);
}
