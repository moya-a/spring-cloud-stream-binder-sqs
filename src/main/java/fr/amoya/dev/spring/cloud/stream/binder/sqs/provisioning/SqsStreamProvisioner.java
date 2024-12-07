package fr.amoya.dev.spring.cloud.stream.binder.sqs.provisioning;

import fr.amoya.dev.spring.cloud.stream.binder.sqs.properties.SqsConsumerProperties;
import fr.amoya.dev.spring.cloud.stream.binder.sqs.properties.SqsProducerProperties;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;

public class SqsStreamProvisioner implements
        ProvisioningProvider<ExtendedConsumerProperties<SqsConsumerProperties>, ExtendedProducerProperties<SqsProducerProperties>> {

    @Override
    public ProducerDestination provisionProducerDestination(String name,
                                                            ExtendedProducerProperties<SqsProducerProperties> properties) {
        return new SqsProducerDestination(name);
    }

    @Override
    public ConsumerDestination provisionConsumerDestination(String name, String group,
                                                            ExtendedConsumerProperties<SqsConsumerProperties> properties) {
        return new SqsConsumerDestination(name);
    }
}
