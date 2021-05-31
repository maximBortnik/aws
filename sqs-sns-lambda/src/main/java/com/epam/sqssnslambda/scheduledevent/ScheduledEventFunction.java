package com.epam.sqssnslambda.scheduledevent;

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.epam.sqssnslambda.SQSListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Component("scheduledEventFunction")
public class ScheduledEventFunction implements Function<ScheduledEvent, String> {

    private final SQSListener sqsListener;

    @Override
    public String apply(ScheduledEvent scheduledEvent) {
        log.info("ScheduledEvent: {}", scheduledEvent);
        sqsListener.receive();
        return "Event was handled successfully";
    }
}
