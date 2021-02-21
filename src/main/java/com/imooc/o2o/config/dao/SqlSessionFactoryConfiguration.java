package com.imooc.o2o.config.dao;

import com.imooc.o2o.dao.split.DynamicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SqlSessionFactoryConfiguration {
    // mybatis-config.xml配置文件的路径
    private static String mybatisConfigFile;

    // static属性需要定义好setter方法，注意该方法不是static的
    @Value("${mybatis_config_file}")
    public void setMybatisConfigFile(String mybatisConfigFile) {
        SqlSessionFactoryConfiguration.mybatisConfigFile = mybatisConfigFile;
    }

    // mybatis mapper文件所在路径
    private static String mapperPath;

    @Value("${mapper_locations}")
    public void setMapperPath(String mapperPath) {
        SqlSessionFactoryConfiguration.mapperPath = mapperPath;
    }

    // 实体类所在的package
    @Value("${type_aliases_package}")
    private static String typeAliasesPackage;

//    @Autowired
//    private DataSource dataSource; // 对应sqlSessionFactory中的p:datasource-ref

    @Bean(name="dynamicDataSource")
    public DynamicDataSource createDynamicDataSource(@Qualifier("master") DataSource master,
                                                     @Qualifier("slave") DataSource slave){
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put("master", master);
        targetDataSource.put("slave", slave);
        dynamicDataSource.setTargetDataSources(targetDataSource);
        return dynamicDataSource;
    }

    /**
     * 创建sqlSessionFactory实例，并且设置configuration，设置mapper映射路径
     * 设置mapper映射路径
     */
    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean createSqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置数据源
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 设置mybatis-config.xml
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigFile));
        // 添加mapper扫描路径
        // 带有*路径的资源获取器
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPath;
        sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));
        // 添加typeAlias包扫描路径
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        return sqlSessionFactoryBean;
    }
}
