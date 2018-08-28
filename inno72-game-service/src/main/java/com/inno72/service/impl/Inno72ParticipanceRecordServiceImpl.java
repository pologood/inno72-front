package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72ParticipanceRecordMapper;
import com.inno72.model.Inno72ParticipanceRecord;
import com.inno72.service.Inno72ParticipanceRecordService;


/**
 * Created by CodeGenerator on 2018/06/28.
 */
@Service
@Transactional
public class Inno72ParticipanceRecordServiceImpl extends AbstractService<Inno72ParticipanceRecord>
		implements Inno72ParticipanceRecordService {
	@Resource
	private Inno72ParticipanceRecordMapper inno72ParticipanceRecordMapper;

}
