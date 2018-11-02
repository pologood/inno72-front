package com.inno72;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class Inno72BeanConfiguration {

    private final static String appkey = "25101422";
    private final static String secret = "8ac43496a419501705a3dfb20b12dafe";
    private final static String url = "https://eco.taobao.com/router/rest";
    @Bean
    public TaobaoClient TaobaoClient() {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        return client;
    }
}
