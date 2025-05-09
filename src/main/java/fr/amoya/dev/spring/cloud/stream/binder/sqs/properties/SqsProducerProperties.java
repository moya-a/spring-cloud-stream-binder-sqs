package fr.amoya.dev.spring.cloud.stream.binder.sqs.properties;

public final class SqsProducerProperties extends SqsCommonProperties {

    public static final String HEADERS_GET_EXPRESSION_TEMPLATE = "headers.get('%s')";

    public static final String DEFAULT_SQS_HEADER_PREFIX = "x-sqs-";

    public static final String DEFAULT_SQS_MESSAGE_DELAY_HEADER_PART = "delay";

    public static final String DEFAULT_SQS_FIFO_MESSAGE_GROUP_ID_HEADER_PART = "group-id";

    public static final String DEFAULT_SQS_FIFO_MESSAGE_DEDUPLICATION_ID_HEADER_PART = "deduplication-id";

    private String messageHeaderPrefix = DEFAULT_SQS_HEADER_PREFIX;

    private String messageDelayHeader = DEFAULT_SQS_MESSAGE_DELAY_HEADER_PART;

    private String fifoMessageGroupIdHeader = DEFAULT_SQS_FIFO_MESSAGE_GROUP_ID_HEADER_PART;

    private String fifoMessageDeduplicationIdHeader = DEFAULT_SQS_FIFO_MESSAGE_DEDUPLICATION_ID_HEADER_PART;

    private String getHeaderExpressionString(String header) {
        return String.format(HEADERS_GET_EXPRESSION_TEMPLATE, header);
    }

    public SqsProducerProperties setMessageHeaderPrefix(String messageHeaderPrefix) {
        this.messageHeaderPrefix = messageHeaderPrefix;
        return this;
    }

    public String getMessageDelayHeader() {
        return messageHeaderPrefix + messageDelayHeader;
    }

    public SqsProducerProperties setMessageDelayHeader(String messageDelayHeader) {
        this.messageDelayHeader = messageDelayHeader;
        return this;
    }

    public String getMessageDelayHeaderExpression() {
        return getHeaderExpressionString(getMessageDelayHeader());
    }

    public String getFifoMessageGroupIdHeader() {
        return messageHeaderPrefix + fifoMessageGroupIdHeader;
    }

    public SqsProducerProperties setFifoMessageGroupIdHeader(String fifoMessageGroupIdHeader) {
        this.fifoMessageGroupIdHeader = fifoMessageGroupIdHeader;
        return this;
    }

    public String getFifoMessageGroupIdHeaderExpression() {
        return getHeaderExpressionString(getFifoMessageGroupIdHeader());
    }

    public String getFifoMessageDeduplicationIdHeader() {
        return messageHeaderPrefix + fifoMessageDeduplicationIdHeader;
    }

    public SqsProducerProperties setFifoMessageDeduplicationIdHeader(
        String fifoMessageDeduplicationIdHeader) {
        this.fifoMessageDeduplicationIdHeader = fifoMessageDeduplicationIdHeader;
        return this;
    }

    public String getFifoMessageDeduplicationIdHeaderExpression() {
        return getHeaderExpressionString(getFifoMessageDeduplicationIdHeader());
    }
}
