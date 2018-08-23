package com.inno72.alarmMsg.service.impl;

import com.inno72.alarmMsg.model.Inno72AlarmMsg;
import com.inno72.alarmMsg.service.AlarmMsgService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlarmMsgServiceImpl extends AbstractService<Inno72AlarmMsg> implements AlarmMsgService {
}
