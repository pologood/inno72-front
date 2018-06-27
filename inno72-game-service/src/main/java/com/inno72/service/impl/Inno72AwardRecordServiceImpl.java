package com.inno72.service.impl;

import com.inno72.mapper.Inno72AwardRecordMapper;
import com.inno72.model.Inno72AwardRecord;
import com.inno72.service.Inno72AwardRecordService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
@Service
@Transactional
public class Inno72AwardRecordServiceImpl extends AbstractService<Inno72AwardRecord> implements Inno72AwardRecordService {
    @Resource
    private Inno72AwardRecordMapper inno72AwardRecordMapper;

}
