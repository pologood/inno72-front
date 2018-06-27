package com;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.inno72.springboot.web.SpringApplicationBuilder;
import com.inno72.springboot.web.SpringBootServletInitializer;

/**
 * 订单服务
 */
@SpringBootApplication(scanBasePackages = { "com.inno72" })
@EnableFeignClients
@EnableEurekaClient
@EnableCircuitBreaker // 开启熔断
public class Inno72Application extends SpringBootServletInitializer {

    /**
     * 内嵌Tomcat入口
     *
     * @param args
     * @Date 2017年6月16日
     * @Author gaoxingang
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(Inno72Application.class, "inno72-service", args);
    }

    @Override
    public String setAppNameForLog() {
        return "order-service";
    }
}
