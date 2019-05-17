package com.zd.config;

import com.zd.interceptor.CommonFieldInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zidong
 * @date 2019/5/17 11:24 AM
 */
@Configuration
public class IntercepterConfig {

    @Bean
    public CommonFieldInterceptor commonFieldInterceptor() {
        return new CommonFieldInterceptor();
    }
}
