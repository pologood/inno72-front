package com;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.monitor.PointPlanTask;
import com.inno72.monitor.Test;
import com.inno72.springboot.web.SpringApplicationBuilder;
import com.inno72.springboot.web.SpringBootServletInitializer;

/**
 * 订单服务
 */
@SpringBootApplication(scanBasePackages = {"com.inno72"})
@EnableFeignClients
@EnableEurekaClient
@EnableCircuitBreaker // 开启熔断
@EnableConfigurationProperties({Inno72GameServiceProperties.class})
public class Inno72FcBoxApplication extends SpringBootServletInitializer implements CommandLineRunner {

	public static SpringApplication application;

	/**
	 * 内嵌Tomcat入口
	 *
	 * @param args
	 * @Date 2017年6月16日
	 * @Author gaoxingang
	 */
	public static void main(String[] args) {
		SpringApplicationBuilder sb = new SpringApplicationBuilder(Inno72FcBoxApplication.class, "inno72-fcbox-service",
				args);
		application = sb.application();
	}

	@Override
	public String setAppNameForLog() {
		return "inno72-fcbox-service";
	}

	@Autowired
	private Test test;
	@Autowired
	private PointPlanTask pointPlanTask;
	@Override
	public void run(String... args) throws IOException {
		pointPlanTask.test3();
	}
}
