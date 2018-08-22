package com.inno72.alarmMsg.service.impl;

import com.inno72.alarmMsg.mapper.Inno72AlarmMsgMapper;
import com.inno72.alarmMsg.model.Inno72AlarmMsg;
import com.inno72.alarmMsg.service.AlarmMsgService;
import com.inno72.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class AlarmMsgServiceImpl extends AbstractService<Inno72AlarmMsg> implements AlarmMsgService {
    @Resource
    private Inno72AlarmMsgMapper inno72AlarmMsgMapper;
}
