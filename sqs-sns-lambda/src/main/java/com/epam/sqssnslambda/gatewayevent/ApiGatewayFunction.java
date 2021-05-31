package com.epam.sqssnslambda.gatewayevent;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.epam.sqssnslambda.SQSListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component("apiGatewayFunction")
@RequiredArgsConstructor
public class ApiGatewayFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final SQSListener sqsListener;

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {
        log.info("APIGatewayProxyRequestEvent: {}", apiGatewayProxyRequestEvent);
        sqsListener.receive();
        var apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
        apiGatewayProxyResponseEvent.setStatusCode(200);
        apiGatewayProxyResponseEvent.setBody("Event was handled successfully");
        return apiGatewayProxyResponseEvent;
    }
}
