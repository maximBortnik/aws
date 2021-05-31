package com.epam.sqssnslambda.s3event;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.epam.sqssnslambda.SnsSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component("s3EventFunction")
@RequiredArgsConstructor
public class S3EventFunction implements Function<S3Event, String> {

    private final SnsSender snsSender;
    @Override
    public String apply(S3Event s3Event) {
        log.info("S3Event: {}", s3Event);
        var records = s3Event.getRecords();
        records.forEach(record -> {
            log.info("Event: {}. Bucket: {}. Object: {}", record.getEventName(),
                    record.getS3().getBucket(), record.getS3().getObject());
            snsSender.send(String.format("Event: %s. Bucket: %s. Object: %s", record.getEventName(),
                    record.getS3().getBucket(), record.getS3().getObject()));
        });
        return "Event was handled successfully";
    }
}
