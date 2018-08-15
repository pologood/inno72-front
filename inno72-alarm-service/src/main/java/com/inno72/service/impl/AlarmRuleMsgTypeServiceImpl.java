package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.AlarmRuleMsgTypeMapper;
import com.inno72.model.AlarmRuleMsgType;
import com.inno72.service.AlarmRuleMsgTypeService;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
@Service
@Transactional
public class AlarmRuleMsgTypeServiceImpl extends AbstractService<AlarmRuleMsgType> implements AlarmRuleMsgTypeService {
    @Resource
    private AlarmRuleMsgTypeMapper alarmRuleMsgTypeMapper;

}
