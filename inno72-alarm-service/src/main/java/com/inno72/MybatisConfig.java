package com.inno72;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.github.miemiedev.mybatis.paginator.OffsetLimitInterceptor;
import com.inno72.utils.page.PagePlugin;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

@Configuration
public class MybatisConfig {

	@Bean
	public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setTypeHandlersPackage("org.apache.ibatis.type.LocalDateTimeTypeHandler");

		Properties props = new Properties();
		props.setProperty("dialect", "mysql");
		props.setProperty("pageSqlId", "(ByPage|ForPage)");
		PagePlugin pagePlugin = new PagePlugin();
		pagePlugin.setProperties(props);

		OffsetLimitInterceptor offsetLimitInterceptor = new OffsetLimitInterceptor();
		Properties offsetLimitProps = new Properties();
		offsetLimitProps.setProperty("dialectClass", "com.github.miemiedev.mybatis.paginator.dialect.MySQLDialect");
		offsetLimitInterceptor.setProperties(offsetLimitProps);

		bean.setPlugins(new Interceptor[] { offsetLimitInterceptor, pagePlugin });

		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

		try {
			bean.setMapperLocations(resolver.getResources("com.inno72.**.mapper"));
			return bean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
		mapperScannerConfigurer.setBasePackage("com.inno72.mapper");

		// 配置通用Mapper，详情请查阅官方文档
		Properties properties = new Properties();
		properties.setProperty("mappers", "com.inno72.common.Mapper");
		properties.setProperty("notEmpty", "false");// insert、update是否判断字符串类型!='' 即 test="str != null"表达式内是否追加 and str
													// != ''
		properties.setProperty("IDENTITY", "MYSQL");
		properties.setProperty("notEmpty", "false");
		// 主键UUID回写方法执行顺序,默认AFTER,可选值为(BEFORE|AFTER)
		properties.setProperty("ORDER", "BEFORE");
		mapperScannerConfigurer.setProperties(properties);

		return mapperScannerConfigurer;
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}