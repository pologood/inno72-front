package com.inno72.service;

import java.util.List;

public interface Inno72WeChatService {
    List<String> findWeChatQrCodes(String activityId);
}
