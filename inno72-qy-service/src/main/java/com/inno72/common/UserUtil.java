package com.inno72.common;

import com.inno72.check.model.Inno72CheckUser;

import java.util.Optional;

public class UserUtil {

    public static Inno72CheckUser getUser(){
        SessionData session = CommonConstants.SESSION_DATA;
        Inno72CheckUser checkUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
        return checkUser;
    }
}
