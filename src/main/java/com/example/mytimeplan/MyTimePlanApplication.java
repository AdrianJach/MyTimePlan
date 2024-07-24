package com.example.mytimeplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyTimePlanApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyTimePlanApplication.class, args);
	}

}
