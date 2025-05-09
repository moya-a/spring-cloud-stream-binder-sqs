package fr.amoya.dev.spring.cloud.stream.binder.sqs.health;

import fr.amoya.dev.spring.cloud.stream.binder.sqs.SqsMessageChannelBinder;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;


public class SqsBinderHealthIndicator extends AbstractHealthIndicator {

    private final SqsAsyncClient amazonSqs;

    private final SqsMessageChannelBinder sqsMessageChannelBinder;

    public SqsBinderHealthIndicator(SqsAsyncClient amazonSqs, SqsMessageChannelBinder sqsMessageChannelBinder) {
        this.amazonSqs = amazonSqs;
        this.sqsMessageChannelBinder = sqsMessageChannelBinder;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try {
            amazonSqs.listQueues().join();
            builder.up();
        } catch (Exception e) {
            builder.down(e);
        }
    }
}
