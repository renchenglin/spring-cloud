
package com.example.demo.config;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.pagehelper.PageHelper;

@Configuration
@MapperScan(basePackages = { "com.example.*.mapper" }) /** 注意,这个注解是扫描mapper接口不是xml文件,使用xml模式必须在配置文件中添加xml的配置 **/
@EnableTransactionManagement /**
								 * 启用事物管理 ,在需要事物管理的service类或者方法上使用注解@Transactional
								 **/
public class MybatisConfigurer {
	
	@Resource
	private DataSource dataSource;
	
	//配置mybatis的分页插件pageHelper
    @Bean
    public PageHelper pageHelper(){
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum","true");
        properties.setProperty("rowBoundsWithCount","true");
        properties.setProperty("reasonable","true");
        properties.setProperty("dialect","mysql");    //配置mysql数据库的方言
        pageHelper.setProperties(properties);
        return pageHelper;
    }
    
    /**
	 * 配合注解完成事物管理
	 * 
	 * @return
	 */
	@Bean
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
    
//    @Bean
//    public MapperScannerConfigurer getMapperScannerConfigurer(){
//        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        //这里我特地换成了Mapper 这样你就可以尽量遵循官方的规范 使用mapper注解标识你的Mapper类，同时还可以避免其他的非Mapper接口被无差别扫描
//        mapperScannerConfigurer.setAnnotationClass(org.springframework.stereotype.Repository.class);
//        mapperScannerConfigurer.setBasePackage("com.example.*.mapper.*");
//        return mapperScannerConfigurer;
//        
//        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
//        mapperScannerConfigurer.setBasePackage("com.example.*.mapper.*");
//
//        //配置通用Mapper，详情请查阅官方文档
//        Properties properties = new Properties();
////        properties.setProperty("mappers", MAPPER_INTERFACE_REFERENCE);
//        properties.setProperty("notEmpty", "true");//insert、update是否判断字符串类型!='' 即 test="str != null"表达式内是否追加 and str != ''
//
//        properties.setProperty("IDENTITY", "SELECT UUID()");//使用UUID作為主鍵
//        properties.setProperty("ORDER","BEFORE");//将查询主键作为前置操作
//
//        mapperScannerConfigurer.setProperties(properties);
//
//        return mapperScannerConfigurer;
//    }
    
    
}