package com.inno72.controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.inno72.vo.PropertiesBean;
import com.inno72.vo.QimenTmallFansAutomachineQureymachinesRequest;
import com.inno72.vo.QimenTmallFansAutomachineSynactoisvRequest;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;

@RestController
public class QimenController {

	@Resource
	private PropertiesBean propertiesBean;

	private TaobaoClient client;

	@PostConstruct
	public void initClient() {
		client = new DefaultTaobaoClient(propertiesBean.getUrl(), propertiesBean.getAppkey(), propertiesBean.getSecret());
	}
	/**
	 * @See http://open.taobao.com/api.htm?docId=39151&docType=2
	 *
	 * 天猫请求查询机器信息
	 * @return
	 */
	@RequestMapping("/qimen/tmal/fnas/automachine/synactioisv")
	private Object synactioisv(@RequestBody QimenTmallFansAutomachineSynactoisvRequest request){


		return JSON.toJSONString(request);
	}

	/**
	 * @See http://open.taobao.com/api.htm?docId=39151&docType=2
	 *
	 * 天猫请求查询机器信息
	 * @return
	 */
	@RequestMapping("/qimen/tmal/fnas/automachine/qureymachines")
	private Object qureymachines(@RequestBody QimenTmallFansAutomachineQureymachinesRequest request){


		return JSON.toJSONString(request);
	}
}
