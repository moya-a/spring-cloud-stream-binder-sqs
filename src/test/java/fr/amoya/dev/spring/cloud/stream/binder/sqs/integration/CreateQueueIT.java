package fr.amoya.dev.spring.cloud.stream.binder.sqs.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.amoya.dev.spring.cloud.stream.binder.sqs.IntegrationApplication;
import java.util.function.Consumer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;


public class CreateQueueIT {

    private static final String QUEUE_NAME = "consumer-simple";


    @TestConfiguration
    static class FunctionDefinitionsConfiguration {

        @Bean
        Consumer<String> simpleConsumer() {
            return m -> {
            };
        }

    }

    @Nested
    @Testcontainers
    @ActiveProfiles("test-integration-creation")
    @SpringBootTest(classes = {
        IntegrationApplication.class,
        CreateQueueIT.FunctionDefinitionsConfiguration.class
    }, properties = {
        "spring.cloud.stream.sqs.bindings.simpleConsumer-in-0.consumer.queue-not-found-strategy=fail"})
    class FailWhenNotFound {

        @Container
        @ServiceConnection
        private static final LocalStackContainer localStack = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:3.2.0"))
            .withServices(LocalStackContainer.Service.SQS)
            .withEnv("LOCALSTACK_HOST", "localhost.localstack.cloud")
            .withEnv("SQS_ENDPOINT_STRATEGY", "dynamic");

        @Autowired
        private SqsAsyncClient sqsAsyncClient;

        @Test
        void shouldNotHaveQueue() {
            assertThatThrownBy(() -> sqsAsyncClient.getQueueUrl(r -> r.queueName(QUEUE_NAME)).join())
                .isNotNull();
        }
    }

    @Nested
    @Testcontainers
    @ActiveProfiles("test-integration-creation")
    @SpringBootTest(classes = {
        IntegrationApplication.class,
        CreateQueueIT.FunctionDefinitionsConfiguration.class
    }, properties = {
        "spring.cloud.stream.sqs.bindings.simpleConsumer-in-0.consumer.queue-not-found-strategy=create"})
    class CreateWhenNotFound {

        @Container
        @ServiceConnection
        private static final LocalStackContainer localStack = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:3.2.0"))
            .withServices(LocalStackContainer.Service.SQS)
            .withEnv("LOCALSTACK_HOST", "localhost.localstack.cloud")
            .withEnv("SQS_ENDPOINT_STRATEGY", "dynamic");

        @Autowired
        private SqsAsyncClient sqsAsyncClient;

        @Test
        void shouldCreateQueue() {
            assertThat(sqsAsyncClient.getQueueUrl(r -> r.queueName(QUEUE_NAME)).join().queueUrl())
                .isNotNull();
        }
    }

}
