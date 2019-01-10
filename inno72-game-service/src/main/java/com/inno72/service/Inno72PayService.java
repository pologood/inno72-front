package com.inno72.service;

import com.inno72.model.Inno72Order;

public interface Inno72PayService {
    String pay(Inno72Order order,String sessionUuid);
}
