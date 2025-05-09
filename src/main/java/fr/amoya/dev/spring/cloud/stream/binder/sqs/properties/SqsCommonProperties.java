package fr.amoya.dev.spring.cloud.stream.binder.sqs.properties;

import io.awspring.cloud.sqs.listener.QueueNotFoundStrategy;

public abstract class SqsCommonProperties {

    protected QueueNotFoundStrategy queueNotFoundStrategy;

    public QueueNotFoundStrategy getQueueNotFoundStrategy() {
        return queueNotFoundStrategy;
    }

    public void setQueueNotFoundStrategy(QueueNotFoundStrategy queueNotFoundStrategy) {
        this.queueNotFoundStrategy = queueNotFoundStrategy;
    }
}
