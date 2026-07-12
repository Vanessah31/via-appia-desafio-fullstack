package com.viaappia.incidentsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IncidentsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IncidentsApiApplication.class, args);
	}

}