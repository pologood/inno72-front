package com.inno72.service;

import com.taobao.api.ApiException;

public interface Inno72LocalDataSendService {
    void datasend(String[] merchantNames) throws ApiException;
}
