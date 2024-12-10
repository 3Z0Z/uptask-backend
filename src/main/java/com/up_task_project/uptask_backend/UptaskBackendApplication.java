package com.up_task_project.uptask_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class UptaskBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(UptaskBackendApplication.class, args);
	}

}
