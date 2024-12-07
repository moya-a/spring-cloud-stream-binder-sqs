package fr.amoya.dev.spring.cloud.stream.binder.sqs.properties;


import io.awspring.cloud.sqs.listener.BackPressureMode;
import io.awspring.cloud.sqs.listener.FifoBatchGroupingStrategy;
import io.awspring.cloud.sqs.listener.ListenerMode;
import io.awspring.cloud.sqs.listener.acknowledgement.AcknowledgementOrdering;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import java.time.Duration;
import java.util.Collection;
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeName;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

/**
 * Set of properties exposed to the Consumer Binder to configure a
 * {@link io.awspring.cloud.sqs.listener.SqsMessageListenerContainer SqsMessageListenerContainer}
 * that will read from a SQS Queue
 *
 * @see io.awspring.cloud.sqs.listener.SqsMessageListenerContainer SqsMessageListenerContainer
 * @see <a
 * href="https://docs.awspring.io/spring-cloud-aws/docs/3.2.1/reference/html/index.html#sqscontaineroptions-descriptions">SqsContainerOptions
 * Descriptions inSpring Cloud AWS Documentation Website</a>
 */
public final class SqsConsumerProperties extends SqsCommonProperties {

    /**
     * @see io.awspring.cloud.sqs.listener.ContainerOptionsBuilder#maxConcurrentMessages(int)
     * ContainerOptionsBuilder.maxConcurrentMessages
     */
    private Integer maxConcurrentMessages;

    /**
     * @see io.awspring.cloud.sqs.listener.ContainerOptionsBuilder#maxMessagesPerPoll(int)
     * ContainerOptionsBuilder.maxMessagesPerPoll
     */
    private Integer maxMessagesPerPoll;

    /**
     * @see io.awspring.cloud.sqs.listener.ContainerOptionsBuilder#autoStartup(boolean)
     * ContainerOptionsBuilder.autoStartup
     */
    private Boolean autoStartup;

    /**
     * @see io.awspring.cloud.sqs.listener.ContainerOptionsBuilder#maxDelayBetweenPolls(Duration)
     * ContainerOptionsBuilder.maxDelayBetweenPolls
     */
    private Duration maxDelayBetweenPolls;

    /**
     * @see io.awspring.cloud.sqs.listener.ContainerOptionsBuilder#pollTimeout(Duration)
     * ContainerOptionsBuilder.pollTimeout
     */
    private Duration pollTimeout;

    /**
     * @see io.awspring.cloud.sqs.listener.ContainerOptionsBuilder#listenerMode(ListenerMode)
     * ContainerOptionsBuilder.listenerMode
     */
    private ListenerMode listenerMode;

    /**
     * @see io.awspring.cloud.sqs.listener.ContainerOptionsBuilder#listenerShutdownTimeout(Duration)
     * ContainerOptionsBuilder.listenerShutdownTimeout
     */
    private Duration listenerShutdownTimeout;

    /**
     * @see io.awspring.cloud.sqs.listener.ContainerOptionsBuilder#acknowledgementShutdownTimeout(Duration)
     * ContainerOptionsBuilder.acknowledgementShutdownTimeout
     */
    private Duration acknowledgementShutdownTimeout;

    /**
     * @see io.awspring.cloud.sqs.listener.ContainerOptionsBuilder#backPressureMode(BackPressureMode)
     * ContainerOptionsBuilder.backPressureMode
     */
    private BackPressureMode backPressureMode;

    /**
     * @see io.awspring.cloud.sqs.listener.ContainerOptionsBuilder#acknowledgementInterval(Duration)
     * ContainerOptionsBuilder.acknowledgementInterval
     */
    private Duration acknowledgementInterval;

    /**
     * @see io.awspring.cloud.sqs.listener.ContainerOptionsBuilder#acknowledgementThreshold(int)
     * ContainerOptionsBuilder.acknowledgementThreshold
     */
    private Integer acknowledgementThreshold;

    /**
     * @see io.awspring.cloud.sqs.listener.ContainerOptionsBuilder#acknowledgementMode(AcknowledgementMode)
     * ContainerOptionsBuilder.acknowledgementMode
     */
    private AcknowledgementMode acknowledgementMode;

    /**
     * @see io.awspring.cloud.sqs.listener.ContainerOptionsBuilder#acknowledgementOrdering(AcknowledgementOrdering)
     * ContainerOptionsBuilder.acknowledgementOrdering
     */
    private AcknowledgementOrdering acknowledgementOrdering;

    /**
     * @see io.awspring.cloud.sqs.listener.SqsContainerOptionsBuilder#queueAttributeNames(Collection)
     * SqsContainerOptionsBuilder.queueAttributeNames
     */
    private Collection<QueueAttributeName> queueAttributeNames;

    /**
     * @see io.awspring.cloud.sqs.listener.SqsContainerOptionsBuilder#messageAttributeNames(Collection)
     * SqsContainerOptionsBuilder.messageAttributeNames
     */
    private Collection<String> messageAttributeNames;

    /**
     * @see io.awspring.cloud.sqs.listener.SqsContainerOptionsBuilder#messageSystemAttributeNames(Collection)
     * SqsContainerOptionsBuilder.messageSystemAttributeNames
     */
    private Collection<MessageSystemAttributeName> messageSystemAttributeNames;

    /**
     * @see io.awspring.cloud.sqs.listener.SqsContainerOptionsBuilder#messageVisibility(Duration)
     * SqsContainerOptionsBuilder.messageVisibility
     */
    private Duration messageVisibility;

    /**
     * @see io.awspring.cloud.sqs.listener.SqsContainerOptionsBuilder#fifoBatchGroupingStrategy(FifoBatchGroupingStrategy)
     * SqsContainerOptionsBuilder.fifoBatchGroupingStrategy
     */
    private FifoBatchGroupingStrategy fifoBatchGroupingStrategy;

    public Integer getMaxConcurrentMessages() {
        return maxConcurrentMessages;
    }

    public void setMaxConcurrentMessages(Integer maxConcurrentMessages) {
        this.maxConcurrentMessages = maxConcurrentMessages;
    }

    public Integer getMaxMessagesPerPoll() {
        return maxMessagesPerPoll;
    }

    public void setMaxMessagesPerPoll(Integer maxMessagesPerPoll) {
        this.maxMessagesPerPoll = maxMessagesPerPoll;
    }

    public Boolean getAutoStartup() {
        return autoStartup;
    }

    public void setAutoStartup(Boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    public Duration getMaxDelayBetweenPolls() {
        return maxDelayBetweenPolls;
    }

    public void setMaxDelayBetweenPolls(Duration maxDelayBetweenPolls) {
        this.maxDelayBetweenPolls = maxDelayBetweenPolls;
    }

    public Duration getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(Duration pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public ListenerMode getListenerMode() {
        return listenerMode;
    }

    public void setListenerMode(ListenerMode listenerMode) {
        this.listenerMode = listenerMode;
    }

    public Duration getListenerShutdownTimeout() {
        return listenerShutdownTimeout;
    }

    public void setListenerShutdownTimeout(Duration listenerShutdownTimeout) {
        this.listenerShutdownTimeout = listenerShutdownTimeout;
    }

    public Duration getAcknowledgementShutdownTimeout() {
        return acknowledgementShutdownTimeout;
    }

    public void setAcknowledgementShutdownTimeout(Duration acknowledgementShutdownTimeout) {
        this.acknowledgementShutdownTimeout = acknowledgementShutdownTimeout;
    }

    public BackPressureMode getBackPressureMode() {
        return backPressureMode;
    }

    public void setBackPressureMode(BackPressureMode backPressureMode) {
        this.backPressureMode = backPressureMode;
    }

    public Duration getAcknowledgementInterval() {
        return acknowledgementInterval;
    }

    public void setAcknowledgementInterval(Duration acknowledgementInterval) {
        this.acknowledgementInterval = acknowledgementInterval;
    }

    public Integer getAcknowledgementThreshold() {
        return acknowledgementThreshold;
    }

    public void setAcknowledgementThreshold(Integer acknowledgementThreshold) {
        this.acknowledgementThreshold = acknowledgementThreshold;
    }

    public AcknowledgementMode getAcknowledgementMode() {
        return acknowledgementMode;
    }

    public void setAcknowledgementMode(AcknowledgementMode acknowledgementMode) {
        this.acknowledgementMode = acknowledgementMode;
    }

    public AcknowledgementOrdering getAcknowledgementOrdering() {
        return acknowledgementOrdering;
    }

    public void setAcknowledgementOrdering(AcknowledgementOrdering acknowledgementOrdering) {
        this.acknowledgementOrdering = acknowledgementOrdering;
    }

    public Collection<QueueAttributeName> getQueueAttributeNames() {
        return queueAttributeNames;
    }

    public void setQueueAttributeNames(Collection<QueueAttributeName> queueAttributeNames) {
        this.queueAttributeNames = queueAttributeNames;
    }

    public Collection<String> getMessageAttributeNames() {
        return messageAttributeNames;
    }

    public void setMessageAttributeNames(Collection<String> messageAttributeNames) {
        this.messageAttributeNames = messageAttributeNames;
    }

    public Collection<MessageSystemAttributeName> getMessageSystemAttributeNames() {
        return messageSystemAttributeNames;
    }

    public void setMessageSystemAttributeNames(
        Collection<MessageSystemAttributeName> messageSystemAttributeNames) {
        this.messageSystemAttributeNames = messageSystemAttributeNames;
    }

    public Duration getMessageVisibility() {
        return messageVisibility;
    }

    public void setMessageVisibility(Duration messageVisibility) {
        this.messageVisibility = messageVisibility;
    }

    public FifoBatchGroupingStrategy getFifoBatchGroupingStrategy() {
        return fifoBatchGroupingStrategy;
    }

    public void setFifoBatchGroupingStrategy(FifoBatchGroupingStrategy fifoBatchGroupingStrategy) {
        this.fifoBatchGroupingStrategy = fifoBatchGroupingStrategy;
    }
}
