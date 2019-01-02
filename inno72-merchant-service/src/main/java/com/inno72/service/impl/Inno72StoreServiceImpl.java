package com.inno72.service.impl;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.mapper.Inno72StoreMapper;
import com.inno72.model.Inno72Store;
import com.inno72.service.Inno72StoreService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/01/02.
 */
@Service
@Transactional
public class Inno72StoreServiceImpl extends AbstractService<Inno72Store> implements Inno72StoreService {
    @Resource
    private Inno72StoreMapper inno72StoreMapper;

	@Override
	public Result<List<Inno72Store>> selectAll() {
		return Results.success(inno72StoreMapper.selectAllOrderCreateTime());
	}
}
