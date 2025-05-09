package fr.amoya.dev.spring.cloud.stream.binder.sqs.provisioning;

import org.springframework.cloud.stream.provisioning.ConsumerDestination;

public class SqsConsumerDestination implements ConsumerDestination {

    private final String name;

    public SqsConsumerDestination(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}
