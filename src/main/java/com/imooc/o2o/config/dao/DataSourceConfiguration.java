package com.imooc.o2o.config.dao;

import com.imooc.o2o.util.DESUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;


@Configuration   // 将配置写入到Spring容器中
//对应spring-dao中的MapperScannerConfigurer，将DataSource动态注入到dao中，是mybatis mapper的扫描路径
@MapperScan("com.imooc.o2o.dao")
public class DataSourceConfiguration {
    @Value("${jdbc.master.driver}") //读取application.properties文件配置
    private String jdbcDriver;
    @Value("${jdbc.master.url}")
    private String jdbcUrl;
    @Value("${jdbc.master.username}")
    private String jdbcUserName;
    @Value("${jdbc.master.password}")
    private String jdbcPassword;

    @Value("${jdbc.slave.driver}") //读取application.properties文件配置
    private String jdbcSlaveDriver;
    @Value("${jdbc.slave.url}")
    private String jdbcSlaveUrl;
    @Value("${jdbc.slave.username}")
    private String jdbcSlaveUserName;
    @Value("${jdbc.slave.password}")
    private String jdbcSlavePassword;

    /**
     * 生成与spring-dao.xml对应的bean dataSource，返回类型与spring-dao中连接池类型一致
     */
    @Bean(name = "master")
    @Primary
    public DataSource createDataSource() throws PropertyVetoException {
        // 生成dataSource实例，与对应spring-dao.xml数据库连接池对应类一致
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        // 跟配置文件一样设置以下信息
        // 驱动
        dataSource.setDriverClass(jdbcDriver);
        // 连接池url
        dataSource.setJdbcUrl(jdbcUrl);
        // 解密后的username
        dataSource.setUser(DESUtils.getDecryptString(jdbcUserName));
        // 解密后的密码
        dataSource.setPassword(DESUtils.getDecryptString(jdbcPassword));
        // 连接池信息，最多连接数
        dataSource.setMaxPoolSize(30);
        // 闲置最少连接数
        dataSource.setMinPoolSize(10);
        // 关闭连接后不自动commit
        dataSource.setAutoCommitOnClose(false);
        // 获取连接的超时时间
        dataSource.setCheckoutTimeout(10000);
        // 获取连接失败后的重试次数
        dataSource.setAcquireIncrement(10);

        //mysql中使用show variables like '%timeout%'可以看到wait_timeout为8小时
        // 定期连接数据库，防止连接池中连接失效
        dataSource.setPreferredTestQuery("select 1");
        // 18000秒定时执行一次select 1一次
        dataSource.setIdleConnectionTestPeriod(18000);
        dataSource.setTestConnectionOnCheckout(true);
        return dataSource;
    }

    /**
     * 生成与spring-dao.xml对应的bean dataSource，返回类型与spring-dao中连接池类型一致
     */
    @Bean(name = "slave")
    public DataSource createSecondDataSource() throws PropertyVetoException {
        // 生成dataSource实例，与对应spring-dao.xml数据库连接池对应类一致
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        // 跟配置文件一样设置以下信息
        // 驱动
        dataSource.setDriverClass(jdbcSlaveDriver);
        // 连接池url
        dataSource.setJdbcUrl(jdbcSlaveUrl);
        // 解密后的username
        dataSource.setUser(DESUtils.getDecryptString(jdbcSlaveUserName));
        // 解密后的密码
        dataSource.setPassword(DESUtils.getDecryptString(jdbcSlavePassword));
        // 连接池信息，最多连接数
        dataSource.setMaxPoolSize(30);
        // 闲置最少连接数
        dataSource.setMinPoolSize(10);
        // 关闭连接后不自动commit
        dataSource.setAutoCommitOnClose(false);
        // 获取连接的超时时间
        dataSource.setCheckoutTimeout(10000);
        // 获取连接失败后的重试次数
        dataSource.setAcquireIncrement(10);

        //mysql中使用show variables like '%timeout%'可以看到wait_timeout为8小时
        // 定期连接数据库，防止连接池中连接失效
        dataSource.setPreferredTestQuery("select 1");
        // 18000秒定时执行一次select 1一次
        dataSource.setIdleConnectionTestPeriod(18000);
        dataSource.setTestConnectionOnCheckout(true);
        return dataSource;
    }
}
