package com.epam.attachment.notification.sqs;

import com.epam.attachment.notification.dto.AttachmentNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SQSSender {

    @Value("${aws.sqs.queueName}")
    private String queueName;

    private final QueueMessagingTemplate queueMessagingTemplate;

    public void send(AttachmentNotification attachmentNotification) {
        log.info("Send notification: {} to queue: {}", attachmentNotification, queueName);
        queueMessagingTemplate.convertAndSend(queueName, attachmentNotification);
    }
}
