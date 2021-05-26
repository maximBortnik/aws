package com.epam.attachment.service.impl;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.UnsubscribeRequest;
import com.epam.attachment.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    @Value("${aws.sns.topicName}")
    private String topicName;
    @Value("${aws.sns.topicArn}")
    private String topicArm;

    private final AmazonSNS amazonSNS;

    @Override
    public String subscribe(String email) {
        log.info("{} subscribed on topic {}", email, topicName);
        if (getSubscription(email).isPresent()) {
            return String.format("You have subscribed to the topic: %s already.", topicName);
        }
        amazonSNS.subscribe(topicArm, "email", email);
        return String.format("You have subscribed to the topic: %s", topicName);
    }

    @Override
    public String unsubscribe(String email) {
        log.info("{} unsubscribed from topic {}", email, topicName);
        getSubscription(email)
                .ifPresent(s -> {
                    var unsubscribeRequest = new UnsubscribeRequest()
                            .withSubscriptionArn(s.getSubscriptionArn());
                    amazonSNS.unsubscribe(unsubscribeRequest);
                });
        return String.format("You have unsubscribed from topic: %s", topicName);
    }

    private Optional<Subscription> getSubscription(String email) {
        return amazonSNS.listSubscriptions().getSubscriptions()
                .stream()
                .filter(subscription -> subscription.getEndpoint().equalsIgnoreCase(email))
                .findFirst();
    }
}
