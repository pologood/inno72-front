package com.inno72.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inno72.mapper.Inno72MerchantTotalCountByDayMapper;
import com.inno72.mapper.Inno72MerchantTotalCountMapper;
import com.inno72.model.Inno72AdminArea;
import com.inno72.service.CommonService;
import com.inno72.service.Inno72AdminAreaService;

@Service
public class CommonServiceImpl implements CommonService {


	/**
	 *
	 * @param type city
	 *             activity
	 *             goods
	 * @param sellerId
	 * @return Object
	 */
	@Override
	public Map<String, Object> baseApi(String type, String sellerId) {

		Map<String, Object> resultMap = new HashMap<>(3);
		switch (type) {
			case "city":
				resultMap.put("city", this.getCity(sellerId));
				break;
			case "activity":
				resultMap.put("activity", this.getActivity(sellerId));
				break;
			case "goods":
				resultMap.put("goods", this.getGoods(sellerId));
				break;
			default:
				resultMap.put("city", this.getCity(sellerId));
				resultMap.put("activity", this.getActivity(sellerId));
				resultMap.put("goods", this.getGoods(sellerId));
				break;
		}

		return resultMap;
	}

	@Resource
	private Inno72MerchantTotalCountMapper inno72MerchantTotalCountMapper;

	@Resource
	private Inno72MerchantTotalCountByDayMapper inno72MerchantTotalCountByDayMapper;

	private List<Map<String, String>> getGoods(String sellerId) {
		return inno72MerchantTotalCountByDayMapper.findGoodsBySellerId(sellerId);
	}

	private List<Map<String, String>> getActivity(String sellerId) {
		return inno72MerchantTotalCountByDayMapper.findActivityBySellerId(sellerId);
	}

	@Resource
	private Inno72AdminAreaService inno72AdminAreaService;

	public List<Inno72AdminArea> getCity(String sellerId) {
		return inno72AdminAreaService.findCity();
	}
}
