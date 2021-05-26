package com.epam.attachment.notification.sns;

import com.epam.attachment.notification.dto.AttachmentNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SNSSender {

    @Value("${aws.sns.topicName}")
    private String topicName;

    private final NotificationMessagingTemplate notificationMessagingTemplate;

    public void send(AttachmentNotification attachmentNotification) {
        log.info("Send notification: {} to topic: {}", attachmentNotification, topicName);
        notificationMessagingTemplate.sendNotification(topicName, attachmentNotification,
                String.format(attachmentNotification.getType().getSubject(), attachmentNotification.getFilename()));
    }

}
