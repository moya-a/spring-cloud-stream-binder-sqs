package fr.amoya.dev.spring.cloud.stream.binder.sqs.provisioning;

import org.springframework.cloud.stream.provisioning.ProducerDestination;

public class SqsProducerDestination implements ProducerDestination {

    private final String name;

    public SqsProducerDestination(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNameForPartition(int partition) {
        throw new UnsupportedOperationException("Partitioning is not supported for SQS");
    }

}
