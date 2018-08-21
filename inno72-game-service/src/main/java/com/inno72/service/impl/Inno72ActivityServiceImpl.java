package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.mapper.Inno72ActivityMapper;
import com.inno72.service.Inno72ActivityService;
import com.inno72.vo.FansActVo;

@Service
public class Inno72ActivityServiceImpl implements Inno72ActivityService {

	@Resource
	private Inno72ActivityMapper inno72ActivityMapper;

	@Override
	public Result<FansActVo> tianMaoSaveAct(String actId) {
		FansActVo vo = inno72ActivityMapper.selectTianMaoActVo(actId);
		return Results.success(vo);
	}
}
