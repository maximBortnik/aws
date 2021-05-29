package com.epam.sqssnslambda;

import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnsSender {

    @Value("${aws.sns.topicName}")
    private String topicName;

    public void send(String message) {
        var amazonSNS = AmazonSNSClientBuilder.defaultClient();
        log.info("Send notification: {} to topic: {}", message, topicName);
        amazonSNS.publish("arn:aws:sns:eu-central-1:353246146666:notification-topic", message);
    }

}
