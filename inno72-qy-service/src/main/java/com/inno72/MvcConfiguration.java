package com.inno72;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inno72.common.spring.JsonView;
import com.inno72.redis.IRedisUtil;

@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	private Logger logger = LoggerFactory.getLogger(MvcConfiguration.class);

	@Resource
	private IRedisUtil redisUtil;

	public View jsonView() {
		logger.info("***********************init JsonView**************************");
		JsonView view = new JsonView();
		view.setExtractValueFromSingleKeyModel(true);
		Jackson2ObjectMapperFactoryBean objectMapperFactoryBean = new Jackson2ObjectMapperFactoryBean();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapperFactoryBean.setObjectMapper(objectMapper);
		objectMapperFactoryBean.setSerializationInclusion(Include.NON_NULL);
		objectMapperFactoryBean.afterPropertiesSet();
		view.setObjectMapper(objectMapperFactoryBean.getObject());
		return view;
	}

	/**
	 * 配置解析器
	 *
	 * @return
	 * @Date 2017年5月4日
	 * @Author Houkm
	 */
	@Bean
	public ViewResolver viewResolver() {
		logger.info("***********************init ViewResolver**************************");
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		List<View> views = new ArrayList<>();
		views.add(jsonView());
		resolver.setDefaultViews(views);
		return resolver;
	}

	/**
	 * 国际化的消息资源文件
	 *
	 * @return
	 * @Date 2017年5月3日
	 * @Author Houkm
	 */
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		logger.info("***********************init ReloadableResourceBundleMessageSource**************************");
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:conf/messages");
		messageSource.setCacheSeconds(60);
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

}