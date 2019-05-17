package com.zd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@MapperScan(basePackages = {"com.zd.dao.mapper"})
@RestController
public class CommonFieldInterceptorApplication {

	public static void main(String[] args) {
        SpringApplication.run(CommonFieldInterceptorApplication.class, args);
	}
}
