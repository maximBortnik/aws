package com.epam.sqssnslambda;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

public class SqsEventHandler extends SpringBootRequestHandler<SQSEvent, String> {
}
