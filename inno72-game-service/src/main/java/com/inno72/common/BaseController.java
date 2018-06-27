package com.inno72.common;

import com.framelib.common.*;

/**
 * Created by zhouzengbao on 29/11/2017.
 */
public class BaseController {

    protected String getCreate(){
        PmpSessionData session = com.framelib.common.CommonConstants.PMP_SESSION_DATA;
        if (session.getUser() == null) {
            return "";
        }
        return session.getUser().getName();

    }
}
