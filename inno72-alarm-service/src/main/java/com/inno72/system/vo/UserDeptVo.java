package com.inno72.system.vo;

import com.alibaba.fastjson.JSONArray;
import com.inno72.system.model.Inno72User;

public class UserDeptVo {
	private Inno72User user;
	private JSONArray deptIds;

	public Inno72User getUser() {
		return user;
	}

	public void setUser(Inno72User user) {
		this.user = user;
	}

	public JSONArray getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(JSONArray deptIds) {
		this.deptIds = deptIds;
	}

}
