package com.example.crossafter;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.RestController;
/*
@author 16058106蔡剑锋
 */
@RestController
@SpringBootApplication
@EnableCaching
public class CrossafterApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(CrossafterApplication.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(Application.class);
	}

}
