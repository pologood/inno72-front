package com.inno72.service.impl;

import com.inno72.mapper.Inno72InteractShopsMapper;
import com.inno72.service.Inno72InteractService;
import com.inno72.service.Inno72WeChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class Inno72WeChatSerciceImpl implements Inno72WeChatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72WeChatSerciceImpl.class);
    @Autowired
    private Inno72InteractShopsMapper inno72InteractShopsMapper;
    @Override
    public List<String> findWeChatQrCodes(String activityId) {
        return inno72InteractShopsMapper.findWeChatQrCodes(activityId);
    }
}
