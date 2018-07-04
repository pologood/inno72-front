package com.inno72.util;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;

public class TaobaoClientUtil {
	public static final String URL = "https://eco.taobao.com/router/rest";
    public static final String APPKEY = "24791535";
    public static final String SECRET = "c0799e02efbb606288c51f02a987ba43";

    public static TaobaoClient client = new DefaultTaobaoClient(URL, APPKEY, SECRET);

}
