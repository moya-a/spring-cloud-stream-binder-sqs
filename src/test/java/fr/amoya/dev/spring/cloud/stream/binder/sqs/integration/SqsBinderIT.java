package fr.amoya.dev.spring.cloud.stream.binder.sqs.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.awaitility.Awaitility.await;

import fr.amoya.dev.spring.cloud.stream.binder.sqs.IntegrationApplication;
import fr.amoya.dev.spring.cloud.stream.binder.sqs.SqsHeaders;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Testcontainers
@ActiveProfiles("test-integration")
@SpringBootTest(classes = {IntegrationApplication.class,
    SqsBinderIT.FunctionDefinitionsConfiguration.class})
class SqsBinderIT {

    public static final String IN_CONSUMER_QUEUE_NAME = "consumer-simple";

    public static final String IN_CONSUMER_MANUAL_ACK_QUEUE_NAME = "consumer-manual-ack";

    public static final String IN_CONSUMER_FIFO_QUEUE_NAME = "consumer.fifo";

    public static final String OUT_PRODUCER_FIFO_QUEUE_NAME = "producer.fifo";

    public static final String OUT_PRODUCER_CUSTOM_HEADER_FIFO_QUEUE_NAME = "custom-headers.fifo";

    public static final String OUT_PRODUCER_QUEUE_NAME = "producer-simple";

    public static final String OUT_PRODUCER_DELAYED_QUEUE_NAME = "producer-delayed";

    public static final String TEST_MESSAGE = "test message";

    public static final String BODY = "body";

    @Container
    @ServiceConnection
    private static final LocalStackContainer localStack = new LocalStackContainer(
        DockerImageName.parse("localstack/localstack:3.2.0"))
        .withServices(LocalStackContainer.Service.SQS)
        .withEnv("LOCALSTACK_HOST", "localhost.localstack.cloud")
        .withEnv("SQS_ENDPOINT_STRATEGY", "dynamic");

    private static final Sinks.Many<String> simpleConsumerSink = createSink();

    private static final Sinks.Many<Message<String>> manualAckConsumerSink = createSink();

    private static final Sinks.Many<String> fifoConsumerSink = createSink();

    private static final Sinks.Many<Message<String>> fifoProducerSink = createSink();

    private static final Sinks.Many<Message<String>> customHeaderFifoProducerSink = createSink();

    private static final Sinks.Many<String> simpleProducerSink = createSink();

    private static final Sinks.Many<Message<String>> delayedProducerSink = createSink();

    @Autowired
    private SqsAsyncClient amazonSQS;

    private static <T> Sinks.Many<T> createSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @BeforeAll
    static void beforeAll() throws Exception {
        localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name",
            IN_CONSUMER_QUEUE_NAME);
        localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name",
            IN_CONSUMER_MANUAL_ACK_QUEUE_NAME);
        localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name",
            IN_CONSUMER_FIFO_QUEUE_NAME, "--attributes", "FifoQueue=true");

        localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name",
            OUT_PRODUCER_FIFO_QUEUE_NAME, "--attributes", "FifoQueue=true");
        localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name",
            OUT_PRODUCER_CUSTOM_HEADER_FIFO_QUEUE_NAME, "--attributes", "FifoQueue=true");
        localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name",
            OUT_PRODUCER_QUEUE_NAME);
        localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name",
            OUT_PRODUCER_DELAYED_QUEUE_NAME);
    }

    private String getQueueUrl(String queueName) {
        String queueUrl = amazonSQS.getQueueUrl(r -> r.queueName(queueName)).join().queueUrl();
        assumeThat(queueUrl).isNotBlank();
        return queueUrl;
    }

    @TestConfiguration
    static class FunctionDefinitionsConfiguration {

        @Bean
        Consumer<String> simpleConsumer() {
            return simpleConsumerSink::tryEmitNext;
        }

        @Bean
        Consumer<Message<String>> manualAckConsumer() {
            return manualAckConsumerSink::tryEmitNext;
        }

        @Bean
        Consumer<String> fifoConsumer() {
            return fifoConsumerSink::tryEmitNext;
        }


        @Bean
        Supplier<Flux<Message<String>>> fifoProducer() {
            return fifoProducerSink::asFlux;
        }

        @Bean
        Supplier<Flux<Message<String>>> customHeadersFifoProducer() {
            return customHeaderFifoProducerSink::asFlux;
        }

        @Bean
        Supplier<Flux<String>> simpleProducer() {
            return simpleProducerSink::asFlux;
        }

        @Bean
        Supplier<Flux<Message<String>>> delayedProducer() {
            return delayedProducerSink::asFlux;
        }
    }

    @Nested
    @DisplayName("Testing consumer functions")
    class Consumers {

        @Test
        void shouldConsumeMessage() {
            final String queueUrl = getQueueUrl(IN_CONSUMER_QUEUE_NAME);

            amazonSQS.sendMessage(r -> r.queueUrl(queueUrl).messageBody(TEST_MESSAGE)).join();

            StepVerifier.create(simpleConsumerSink.asFlux())
                .assertNext(message -> assertThat(message).isEqualTo(TEST_MESSAGE))
                .verifyTimeout(Duration.ofSeconds(1));
        }

        @Test
        void shouldConsumeMessageFromFifo() {
            final String queueUrl = getQueueUrl(IN_CONSUMER_FIFO_QUEUE_NAME);

            amazonSQS.sendMessage(r -> r.queueUrl(queueUrl)
                .messageGroupId("group")
                .messageDeduplicationId("unique")
                .messageBody(TEST_MESSAGE)
            ).join();

            StepVerifier.create(fifoConsumerSink.asFlux())
                .assertNext(message -> assertThat(message).isEqualTo(TEST_MESSAGE))
                .verifyTimeout(Duration.ofSeconds(1));
        }

        @Test
        void shouldConsumeMessageAndManuallyAck() {
            final String queueUrl = getQueueUrl(IN_CONSUMER_MANUAL_ACK_QUEUE_NAME);

            amazonSQS.sendMessage(r -> r.queueUrl(queueUrl).messageBody(TEST_MESSAGE)).join();

            StepVerifier.create(manualAckConsumerSink.asFlux())
                // message is re-read every PT1S
                .expectNextMatches(message -> TEST_MESSAGE.equals(message.getPayload()))
                .expectNextMatches(message -> TEST_MESSAGE.equals(message.getPayload()))
                // until it is manually ack
                .consumeNextWith(Acknowledgement::acknowledge)
                // then no event is sent
                .expectNoEvent(Duration.ofSeconds(2))
                .verifyTimeout(Duration.ofSeconds(5));

        }
    }

    @Nested
    @DisplayName("Testing producer functions")
    class Producers {

        @Test
        void shouldPublishMessage() {
            final String queueUrl = getQueueUrl(OUT_PRODUCER_QUEUE_NAME);

            simpleProducerSink.tryEmitNext(TEST_MESSAGE);

            await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
                assertThat(amazonSQS.receiveMessage(r -> r.queueUrl(queueUrl)).join().messages())
                    .hasSize(1)
                    .extracting(BODY)
                    .containsExactly(TEST_MESSAGE);
            });
        }

        @Test
        void shouldPublishMessageToFifoQueue() {
            final String queueUrl = getQueueUrl(OUT_PRODUCER_FIFO_QUEUE_NAME);

            final Message<String> message = MessageBuilder
                .withPayload(TEST_MESSAGE)
                .setHeader(SqsHeaders.DEFAULT_SQS_FIFO_MESSAGE_GROUP_ID_HEADER, "my-group")
                .setHeader(SqsHeaders.DEFAULT_SQS_FIFO_MESSAGE_DEDUPLICATION_ID_HEADER, "unique1")
                .build();

            fifoProducerSink.tryEmitNext(message);

            await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
                assertThat(amazonSQS.receiveMessage(r -> r.queueUrl(queueUrl)).join().messages())
                    .hasSize(1)
                    .extracting(BODY)
                    .containsExactly(TEST_MESSAGE);
            });
        }

        @Test
        void shouldPublishMessageToFifoQueueWithCustomHeaders() {
            final String queueUrl = getQueueUrl(OUT_PRODUCER_CUSTOM_HEADER_FIFO_QUEUE_NAME);

            final Message<String> message = MessageBuilder
                .withPayload(TEST_MESSAGE)
                .setHeader("custom-gid", "my-group")
                .setHeader("custom-ddid", "unique1")
                .build();

            customHeaderFifoProducerSink.tryEmitNext(message);

            await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
                assertThat(amazonSQS.receiveMessage(r -> r.queueUrl(queueUrl)).join().messages())
                    .hasSize(1)
                    .extracting(BODY)
                    .containsExactly(TEST_MESSAGE);
            });
        }

        @Test
        void shouldPublishDelayedMessage() {
            final String queueUrl = getQueueUrl(OUT_PRODUCER_DELAYED_QUEUE_NAME);

            final Message<String> message = MessageBuilder
                .withPayload(TEST_MESSAGE)
                .setHeader(SqsHeaders.DEFAULT_SQS_MESSAGE_DELAY_HEADER, 5)
                .build();

            delayedProducerSink.tryEmitNext(message);

            await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
                assertThat(amazonSQS.receiveMessage(r -> r.queueUrl(queueUrl)).join().messages())
                    .hasSize(1)
                    .extracting(BODY)
                    .containsExactly(TEST_MESSAGE);
            });
        }
    }
}
