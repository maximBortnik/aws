package com.epam.attachment.controller;

import com.epam.attachment.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping(value = "/add/{email}")
    public ResponseEntity<String> subscribe(@PathVariable String email) {
        var subscribe = subscriptionService.subscribe(email);
        return ResponseEntity.ok(subscribe + " Check to your email to confirm subscription");
    }

    @DeleteMapping(value = "/remove/{email}")
    public ResponseEntity<String> unsubscribe(@PathVariable String email) {
        var unsubscribe = subscriptionService.unsubscribe(email);
        return ResponseEntity.ok(unsubscribe);
    }
}
