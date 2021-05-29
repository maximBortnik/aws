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
// https://s3.eu-central-1.amazonaws.com/mb-lambda-sqs-sns/sqs-sns-lambda-2.3.4.RELEASE-aws.jar
}
