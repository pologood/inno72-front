package com.inno72.service;

import java.io.IOException;

public interface BeideMaClient {
    String shipment(String openId,String orderNo,Integer isSuccess);
}
