package com.github.binarywang.demo.wx.mp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@SpringBootApplication
public class WxMpDemoApplication extends SpringBootServletInitializer {

    @Bean
    public FilterRegistrationBean crossFilter() {
        Filter filter = new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Access-Control-Allow-Methods", "POST, PUT, DELETE, OPTIONS, HEAD");
                response.setHeader("Access-Control-Allow-Headers",
                    "User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,AccessToken,lf-None-Matoh");
                response.setHeader("Access-Control-Max-Age", "1209600");
                response.setHeader("Access-Control-Expose-Headers", "lf-None-Matoh");
                response.setHeader("Access-Control-Request-Headers", "lf-None-Matoh");
                response.setHeader("Expires", "-1");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("pragma", "no-cache");
                filterChain.doFilter(request, response);
            }
        };

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("crossFilter");
        registration.setOrder(1);
        return registration;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WxMpDemoApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WxMpDemoApplication.class, args);
    }
}
