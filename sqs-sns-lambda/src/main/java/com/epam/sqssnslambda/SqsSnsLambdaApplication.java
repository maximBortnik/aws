package com.epam.sqssnslambda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SqsSnsLambdaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqsSnsLambdaApplication.class, args);
	}
}
