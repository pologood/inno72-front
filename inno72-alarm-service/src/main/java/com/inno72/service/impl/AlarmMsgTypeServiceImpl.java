package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.AlarmMsgTypeMapper;
import com.inno72.model.AlarmMsgType;
import com.inno72.service.AlarmMsgTypeService;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
@Service
@Transactional
public class AlarmMsgTypeServiceImpl extends AbstractService<AlarmMsgType> implements AlarmMsgTypeService {
    @Resource
    private AlarmMsgTypeMapper alarmMsgTypeMapper;

}
