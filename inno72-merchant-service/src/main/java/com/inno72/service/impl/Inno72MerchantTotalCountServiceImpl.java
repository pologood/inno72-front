package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72MerchantTotalCountMapper;
import com.inno72.model.Inno72MerchantTotalCount;
import com.inno72.service.Inno72MerchantTotalCountService;


/**
 * Created by CodeGenerator on 2018/11/07.
 */
@Service
@Transactional
public class Inno72MerchantTotalCountServiceImpl extends AbstractService<Inno72MerchantTotalCount>
		implements Inno72MerchantTotalCountService {
	@Resource
	private Inno72MerchantTotalCountMapper inno72MerchantTotalCountMapper;

}
