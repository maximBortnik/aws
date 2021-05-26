package com.epam.attachment.service;

public interface SubscriptionService {
    String subscribe(String email);
    String unsubscribe(String email);
}
