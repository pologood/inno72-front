package com.inno72.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.inno72.service.CommonService;

@Service
public class CommonServiceImpl implements CommonService {



	/**
	 *
	 * @param type city
	 *             activity
	 *             goods
	 * @param merchantId
	 * @return Object
	 */
	@Override
	public Map<String, Object> baseApi(String type, String merchantId) {

		Map<String, Object> resultMap = new HashMap<>(3);
		switch (type){
			case "city":
				resultMap.put("city", this.getCity(merchantId));
				break;
			case "activity":
				resultMap.put("activity", this.getActivity(merchantId));
				break;
			case "goods":
				resultMap.put("goods", this.getGoods(merchantId));
				break;
			default:
				resultMap.put("city", this.getCity(merchantId));
				resultMap.put("activity", this.getActivity(merchantId));
				resultMap.put("goods", this.getGoods(merchantId));
				break;
		}

		return null;
	}

	private List<Map<String, String>> getGoods(String merchantId) {
		return null;
	}

	private List<Map<String, String>> getActivity(String merchantId) {
		return null;
	}

	public List<Map<String, String>> getCity(String merchantId) {
		return null;
	}
}
