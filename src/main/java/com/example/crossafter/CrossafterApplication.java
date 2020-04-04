package com.example.crossafter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.RestController;
/*
@author 16058106蔡剑锋
 */
@RestController
@SpringBootApplication
@EnableCaching
public class CrossafterApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrossafterApplication.class, args);
	}
}
