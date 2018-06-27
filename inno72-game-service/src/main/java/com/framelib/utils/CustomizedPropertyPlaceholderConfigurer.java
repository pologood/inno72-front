package com.framelib.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 获取spring中的properties属性文件中的内容
 * @Project 	: 9t-framelib
 * @Program Name: com.framelib.utils.CustomizedPropertyPlaceholderConfigurer.java
 * @ClassName	: CustomizedPropertyPlaceholderConfigurer 
 * @Author 		: zhangyan 
 * @CreateDate  : 2015-1-14 下午4:20:21
 */
public class CustomizedPropertyPlaceholderConfigurer extends
        PropertyPlaceholderConfigurer {

	private static Map<String, Object> ctxPropertiesMap; 
	 
    @Override 
    protected void processProperties( 
            ConfigurableListableBeanFactory beanFactoryToProcess,
            Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props); 
        ctxPropertiesMap = new HashMap<String, Object>(); 
        for (Object key : props.keySet()) { 
            String keyStr = key.toString(); 
            String value = props.getProperty(keyStr); 
            ctxPropertiesMap.put(keyStr, value); 
        }   
    } 
   
    public static Object getContextProperty(String name) { 
    		if(ctxPropertiesMap==null){
    			return null;
    		}
        return ctxPropertiesMap.get(name); 
    } 
 
} 
 