package com.inno72.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SuperOpenService {

	public String adpter(HttpServletRequest request, HttpServletResponse response, String serviceName, String v);

}
