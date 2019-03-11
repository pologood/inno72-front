package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72Interact;

/**
 * 派样活动表
 */
public interface Inno72InteractService extends Service<Inno72Interact> {
	String findPlanCodeByMid(String mid);
}
