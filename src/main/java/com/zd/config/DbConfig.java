package com.zd.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


/**
 * 数据库连接池初始化
 * 需要引入 spring-boot-configuration-processor依赖，否则提示：Configuration Annotation Proessor not found in classpath
 */
@Data
@Slf4j
@Configuration
@EnableTransactionManagement
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DbConfig {

    public String url;
    public String username;
    public String password;
    public String driverClassName;

    public Integer initialSize;
    public Integer maxActive;
    public Integer minIdle;
    public Integer maxWait;
    public Integer timeBetweenEvictionRunsMillis;
    public Integer minEvictableIdleTimeMillis;

    @Bean(name = "dataSource")
    @Qualifier("dataSource")
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minIdle);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        return dataSource;
    }
}
