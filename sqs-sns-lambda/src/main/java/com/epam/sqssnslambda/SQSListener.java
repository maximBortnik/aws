package com.epam.sqssnslambda;

import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.StringJoiner;

@Slf4j
@Component
@RequiredArgsConstructor
public class SQSListener {

    @Value("${aws.sqs.queueName}")
    private String queueName;

    private final SnsSender snsSender;

    public void receive() {
        var amazonSQS = AmazonSQSClientBuilder.defaultClient();
        var queueUrl = amazonSQS.getQueueUrl(queueName).getQueueUrl();

        var receiveMessageRequest = new ReceiveMessageRequest();
        receiveMessageRequest.setQueueUrl(queueUrl);
        receiveMessageRequest.setMaxNumberOfMessages(5);

        var messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();
        if (!CollectionUtils.isEmpty(messages)) {
            var stringJoiner = new StringJoiner(", \n");

            var deleteMessageBatchRequest = new DeleteMessageBatchRequest();
            deleteMessageBatchRequest.setQueueUrl(queueUrl);
            var deleteMessageBatchRequestEntries = new LinkedList<DeleteMessageBatchRequestEntry>();

            messages.forEach(message -> {
                log.info("Received notification {} from {}", message, queueName);
                stringJoiner.add(message.getBody());
                deleteMessageBatchRequestEntries.add(
                        new DeleteMessageBatchRequestEntry(message.getMessageId(), message.getReceiptHandle()));
            });
            deleteMessageBatchRequest.setEntries(deleteMessageBatchRequestEntries);
            snsSender.send(stringJoiner.toString());
            amazonSQS.deleteMessageBatch(deleteMessageBatchRequest);
        }
    }
}
