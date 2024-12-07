package fr.amoya.dev.spring.cloud.stream.binder.sqs;

import static fr.amoya.dev.spring.cloud.stream.binder.sqs.properties.SqsProducerProperties.DEFAULT_SQS_FIFO_MESSAGE_DEDUPLICATION_ID_HEADER_PART;
import static fr.amoya.dev.spring.cloud.stream.binder.sqs.properties.SqsProducerProperties.DEFAULT_SQS_FIFO_MESSAGE_GROUP_ID_HEADER_PART;
import static fr.amoya.dev.spring.cloud.stream.binder.sqs.properties.SqsProducerProperties.DEFAULT_SQS_HEADER_PREFIX;
import static fr.amoya.dev.spring.cloud.stream.binder.sqs.properties.SqsProducerProperties.DEFAULT_SQS_MESSAGE_DELAY_HEADER_PART;

public final class SqsHeaders {

    public static final String DEFAULT_SQS_MESSAGE_DELAY_HEADER =
        DEFAULT_SQS_HEADER_PREFIX + DEFAULT_SQS_MESSAGE_DELAY_HEADER_PART;

    public static final String DEFAULT_SQS_FIFO_MESSAGE_GROUP_ID_HEADER =
        DEFAULT_SQS_HEADER_PREFIX + DEFAULT_SQS_FIFO_MESSAGE_GROUP_ID_HEADER_PART;

    public static final String DEFAULT_SQS_FIFO_MESSAGE_DEDUPLICATION_ID_HEADER =
        DEFAULT_SQS_HEADER_PREFIX + DEFAULT_SQS_FIFO_MESSAGE_DEDUPLICATION_ID_HEADER_PART;


    private SqsHeaders() {
    }

}
