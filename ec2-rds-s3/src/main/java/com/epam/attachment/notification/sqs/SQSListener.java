package com.epam.attachment.notification.sqs;

import com.epam.attachment.notification.dto.AttachmentNotification;
import com.epam.attachment.notification.sns.SNSSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SQSListener {

    @Value("${aws.sqs.queueName}")
    private String queueName;

    private final SNSSender snsSender;
    private final QueueMessagingTemplate queueMessagingTemplate;

//    @SqsListener(value = "upload-notification-queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    public void listener(UploadNotification uploadNotification) {
//        log.info("Received notification {} from upload-notification-queue", uploadNotification);
//        snsSender.send(uploadNotification);
//    }

    @Async
    @Scheduled(cron = "${aws.sqs.read.cron}")
    public void scheduler() {
        var uploadNotification = queueMessagingTemplate.receiveAndConvert(queueName, AttachmentNotification.class);
        if (uploadNotification != null) {
            log.info("Received notification {} from {}", uploadNotification, queueName);
            snsSender.send(uploadNotification);
        }
    }
}
