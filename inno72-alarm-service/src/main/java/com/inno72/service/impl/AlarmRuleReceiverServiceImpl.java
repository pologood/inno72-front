package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.AlarmRuleReceiverMapper;
import com.inno72.model.AlarmRuleReceiver;
import com.inno72.service.AlarmRuleReceiverService;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
@Service
@Transactional
public class AlarmRuleReceiverServiceImpl extends AbstractService<AlarmRuleReceiver> implements AlarmRuleReceiverService {
    @Resource
    private AlarmRuleReceiverMapper alarmRuleReceiverMapper;

}
