package com.epam.sqssnslambda.sqsevent;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.epam.sqssnslambda.SnsSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component("sqsEventFunction")
public class SqsToSnsFunction implements Function<SQSEvent, String> {

    private final SnsSender snsSender;

    @Override
    public String apply(SQSEvent sqsEvent) {
        log.info("SQSEvent: {}", sqsEvent);
        var records = sqsEvent.getRecords();
        var message = records.stream()
                .map(SQSEvent.SQSMessage::getBody)
                .collect(Collectors.joining("\n"));
        snsSender.send(message);
        return "Event was handled successfully";
    }
}
