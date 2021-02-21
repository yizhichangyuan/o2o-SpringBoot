package com.imooc.o2o.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

@Configuration //配置类，自动加载到spring容器中
@EnableTransactionManagement // 开启事务支持，在service方法上添加注解@Transactional便可
// 实现tx:annotation-driven需要实现TransactionManagementConfigurer
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {
    @Autowired
    private DataSource dataSource;

    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
