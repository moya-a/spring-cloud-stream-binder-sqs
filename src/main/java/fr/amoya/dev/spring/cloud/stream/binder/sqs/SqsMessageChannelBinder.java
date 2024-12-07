package fr.amoya.dev.spring.cloud.stream.binder.sqs;

import fr.amoya.dev.spring.cloud.stream.binder.sqs.properties.SqsConsumerProperties;
import fr.amoya.dev.spring.cloud.stream.binder.sqs.properties.SqsExtendedBindingProperties;
import fr.amoya.dev.spring.cloud.stream.binder.sqs.properties.SqsProducerProperties;
import fr.amoya.dev.spring.cloud.stream.binder.sqs.provisioning.SqsStreamProvisioner;
import io.awspring.cloud.sqs.listener.SqsContainerOptions;
import io.awspring.cloud.sqs.listener.SqsContainerOptionsBuilder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.cloud.stream.binder.AbstractMessageChannelBinder;
import org.springframework.cloud.stream.binder.BinderSpecificPropertiesProvider;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.binder.ExtendedPropertiesBinder;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.integration.aws.inbound.SqsMessageDrivenChannelAdapter;
import org.springframework.integration.aws.outbound.SqsMessageHandler;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;


public class SqsMessageChannelBinder
    extends
    AbstractMessageChannelBinder<ExtendedConsumerProperties<SqsConsumerProperties>, ExtendedProducerProperties<SqsProducerProperties>, SqsStreamProvisioner>
    implements
    ExtendedPropertiesBinder<MessageChannel, SqsConsumerProperties, SqsProducerProperties> {

    private final SqsAsyncClient sqsAsyncClient;

    private final SqsExtendedBindingProperties extendedBindingProperties;

    private final Map<String, MessageProducer> activeConsumers;

    private final Map<String, MessageHandler> activeProducers;

    private final PropertyMapper propertyMapper;

    public SqsMessageChannelBinder(
        SqsAsyncClient amazonSqs,
        SqsStreamProvisioner provisioningProvider,
        SqsExtendedBindingProperties extendedBindingProperties, PropertyMapper propertyMapper
    ) {
        super(new String[0], provisioningProvider);
        this.sqsAsyncClient = amazonSqs;
        this.extendedBindingProperties = extendedBindingProperties;
        this.activeConsumers = new HashMap<>();
        this.activeProducers = new HashMap<>();
        this.propertyMapper = propertyMapper;
    }

    public SqsAsyncClient getSqsAsyncClient() {
        return sqsAsyncClient;
    }

    public Map<String, MessageProducer> getActiveConsumers() {
        return Map.copyOf(activeConsumers);
    }

    public Map<String, MessageHandler> getActiveProducers() {
        return Map.copyOf(activeProducers);
    }

    @Override
    public SqsConsumerProperties getExtendedConsumerProperties(String channelName) {
        return this.extendedBindingProperties.getExtendedConsumerProperties(channelName);
    }

    @Override
    public SqsProducerProperties getExtendedProducerProperties(String channelName) {
        return this.extendedBindingProperties.getExtendedProducerProperties(channelName);
    }

    @Override
    public String getDefaultsPrefix() {
        return this.extendedBindingProperties.getDefaultsPrefix();
    }

    @Override
    public Class<? extends BinderSpecificPropertiesProvider> getExtendedPropertiesEntryClass() {
        return this.extendedBindingProperties.getExtendedPropertiesEntryClass();
    }

    private void mapProducerPropertiesToMessageHandlerConfiguration(
        SqsProducerProperties producerProperties,
        SqsMessageHandler messageHandler
    ) {
        propertyMapper.from(producerProperties.getQueueNotFoundStrategy()).to(messageHandler::setQueueNotFoundStrategy);
        propertyMapper.from(producerProperties.getMessageDelayHeaderExpression()).to(messageHandler::setDelayExpressionString);
        propertyMapper.from(producerProperties.getFifoMessageGroupIdHeaderExpression()).to(messageHandler::setMessageGroupIdExpressionString);
        propertyMapper.from(producerProperties.getFifoMessageDeduplicationIdHeaderExpression()).to(messageHandler::setMessageDeduplicationIdExpressionString);
    }

    private void mapConsumerPropertiesToListenerContainerConfiguration(
        SqsConsumerProperties consumerProperties,
        SqsContainerOptionsBuilder containerOptionsBuilder
    ) {
        propertyMapper.from(consumerProperties.getMaxConcurrentMessages()).to(containerOptionsBuilder::maxConcurrentMessages);
        propertyMapper.from(consumerProperties.getMaxMessagesPerPoll()).to(containerOptionsBuilder::maxMessagesPerPoll);
        propertyMapper.from(consumerProperties.getAutoStartup()).to(containerOptionsBuilder::autoStartup);
        propertyMapper.from(consumerProperties.getMaxDelayBetweenPolls()).to(containerOptionsBuilder::maxDelayBetweenPolls);
        propertyMapper.from(consumerProperties.getPollTimeout()).to(containerOptionsBuilder::pollTimeout);
        propertyMapper.from(consumerProperties.getListenerMode()).to(containerOptionsBuilder::listenerMode);
        propertyMapper.from(consumerProperties.getListenerShutdownTimeout()).to(containerOptionsBuilder::listenerShutdownTimeout);
        propertyMapper.from(consumerProperties.getAcknowledgementShutdownTimeout()).to(containerOptionsBuilder::acknowledgementShutdownTimeout);
        propertyMapper.from(consumerProperties.getBackPressureMode()).to(containerOptionsBuilder::backPressureMode);
        propertyMapper.from(consumerProperties.getAcknowledgementInterval()).to(containerOptionsBuilder::acknowledgementInterval);
        propertyMapper.from(consumerProperties.getAcknowledgementThreshold()).to(containerOptionsBuilder::acknowledgementThreshold);
        propertyMapper.from(consumerProperties.getAcknowledgementMode()).to(containerOptionsBuilder::acknowledgementMode);
        propertyMapper.from(consumerProperties.getAcknowledgementOrdering()).to(containerOptionsBuilder::acknowledgementOrdering);
        propertyMapper.from(consumerProperties.getMessageVisibility()).to(containerOptionsBuilder::messageVisibility);
        propertyMapper.from(consumerProperties.getFifoBatchGroupingStrategy()).to(containerOptionsBuilder::fifoBatchGroupingStrategy);
        propertyMapper.from(consumerProperties.getQueueNotFoundStrategy()).to(containerOptionsBuilder::queueNotFoundStrategy);
        propertyMapper.from(consumerProperties.getQueueAttributeNames()).to(containerOptionsBuilder::queueAttributeNames);
        propertyMapper.from(consumerProperties.getMessageAttributeNames()).to(containerOptionsBuilder::messageAttributeNames);
        propertyMapper.from(consumerProperties.getMessageSystemAttributeNames()).to(containerOptionsBuilder::messageSystemAttributeNames);
    }

    @Override
    protected void postProcessOutputChannel(
        MessageChannel outputChannel,
        ExtendedProducerProperties<SqsProducerProperties> producerProperties
    ) {
        ((AbstractMessageChannel) outputChannel).addInterceptor(new SqsPayloadConvertingChannelInterceptor());
    }

    @Override
    protected MessageHandler createProducerMessageHandler(
        ProducerDestination destination,
        ExtendedProducerProperties<SqsProducerProperties> properties,
        MessageChannel errorChannel
    ) {
        final SqsMessageHandler sqsMessageHandler = new SqsMessageHandler(sqsAsyncClient);
        sqsMessageHandler.setQueue(destination.getName());
        sqsMessageHandler.setBeanFactory(getBeanFactory());

        mapProducerPropertiesToMessageHandlerConfiguration(properties.getExtension(), sqsMessageHandler);

        this.activeProducers.put(destination.getName(), sqsMessageHandler);
        return sqsMessageHandler;
    }

    @Override
    protected MessageProducer createConsumerEndpoint(
        ConsumerDestination destination, String group,
        ExtendedConsumerProperties<SqsConsumerProperties> properties
    ) {
        final SqsContainerOptionsBuilder sqsContainerOptions = SqsContainerOptions.builder();
        final SqsMessageDrivenChannelAdapter adapter = new SqsMessageDrivenChannelAdapter(sqsAsyncClient, destination.getName());

        mapConsumerPropertiesToListenerContainerConfiguration(properties.getExtension(), sqsContainerOptions);

        adapter.setSqsContainerOptions(sqsContainerOptions.build());
        adapter.setErrorChannel(registerErrorInfrastructure(destination, group, properties).getErrorChannel());
        this.activeConsumers.put(destination.getName(), adapter);
        return adapter;
    }

    protected static class SqsPayloadConvertingChannelInterceptor implements ChannelInterceptor {

        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            return MessageBuilder.createMessage(
                new String((byte[]) message.getPayload(), StandardCharsets.UTF_8),
                message.getHeaders()
            );
        }

    }
}
