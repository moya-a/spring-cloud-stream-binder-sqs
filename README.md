# Spring Cloud Stream Binder for AWS SQS

[![Continuous Integration (CI)](https://github.com/moya-a/spring-cloud-stream-binder-sqs/actions/workflows/ci.yaml/badge.svg?branch=main)](https://github.com/moya-a/spring-cloud-stream-binder-sqs/actions/workflows/ci.yaml)

## Quality

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=moya-a_spring-cloud-stream-binder-sqs&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=moya-a_spring-cloud-stream-binder-sqs)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=moya-a_spring-cloud-stream-binder-sqs&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=moya-a_spring-cloud-stream-binder-sqs)

[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=moya-a_spring-cloud-stream-binder-sqs&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=moya-a_spring-cloud-stream-binder-sqs)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=moya-a_spring-cloud-stream-binder-sqs&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=moya-a_spring-cloud-stream-binder-sqs)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=moya-a_spring-cloud-stream-binder-sqs&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=moya-a_spring-cloud-stream-binder-sqs)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=moya-a_spring-cloud-stream-binder-sqs&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=moya-a_spring-cloud-stream-binder-sqs)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=moya-a_spring-cloud-stream-binder-sqs&metric=coverage)](https://sonarcloud.io/summary/new_code?id=moya-a_spring-cloud-stream-binder-sqs)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=moya-a_spring-cloud-stream-binder-sqs&metric=bugs)](https://sonarcloud.io/summary/new_code?id=moya-a_spring-cloud-stream-binder-sqs)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=moya-a_spring-cloud-stream-binder-sqs&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=moya-a_spring-cloud-stream-binder-sqs)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=moya-a_spring-cloud-stream-binder-sqs&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=moya-a_spring-cloud-stream-binder-sqs)

## Abstract

A [Spring Cloud Stream](https://spring.io/projects/spring-cloud-stream) Binder implementation
to interact with
AWS [Simple Queue Service (SQS)](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/welcome.html)
and based
upon [Spring Cloud AWS SQS library](https://docs.awspring.io/spring-cloud-aws/docs/3.2.1/reference/html/index.html#sqs-integration).

## Installation

```xml
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/moya-a/spring-cloud-stream-binder-sqs</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>fr.amoya.dev</groupId>
        <artifactId>spring-cloud-stream-binder-sqs</artifactId>
        <version>4.0.0</version>
    </dependency>
</dependencies>
```

**Note**: Pulling from GitHub Packages (even public ones) requires a personal access token. You can create one by following the [GitHub documentation on personal access tokens](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-fine-grained-personal-access-token).

For Gradle users, add the following to your `build.gradle`:

```groovy
repositories {
    maven {
        url "https://maven.pkg.github.com/moya-a/spring-cloud-stream-binder-sqs"
        credentials {
            username = System.getenv("GITHUB_USERNAME")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}
```

## Compatibility Matrix

| spring-cloud-stream-binder-sqs | spring-boot | spring-cloud-aws | spring-cloud | aws sdk | java compiler/runtime |
|--------------------------------|-------------|------------------|--------------|---------|-----------------------|
| 1.9.0                          | 2.7.x       | 2.4.x            | 2021.0.5     | 1.x     | 8                     |
| 3.0.0                          | 3.1.x       | 3.0.x            | 2022.0.3     | 2.x     | 17                    |
| 4.0.0                          | 3.4.x       | 3.3.x            | 2024.0.1     | 2.x     | 21                    |

## Usage

You just need the library in your dependencies, and you can configure it the same way you'd do for
any other binder

### Producer Properties

| Property                             | Type                    | Description                                                     | Possible Values                                                 | Default Value                  |
|--------------------------------------|-------------------------|-----------------------------------------------------------------|-----------------------------------------------------------------|--------------------------------|
| queue-not-found-strategy             | `QueueNotFoundStrategy` | The strategy to use when the queue is not found.                | `QueueNotFoundStrategy.FAIL`<br/>`QueueNotFoundStrategy.CREATE` | `QueueNotFoundStrategy.CREATE` |
| message-header-prefix                | `String`                | Common prefix used in all message headers.                      |                                                                 | `x-sqs-`                       |
| message-delay-header                 | `String`                | The header name used to map the delay property.                 |                                                                 | `delay`                        |
| fifo-message-group-id-header         | `String`                | The header name used to map the fifo group id property.         |                                                                 | `group-id`                     |
| fifo-message-deduplication-id-header | `String`                | The header name used to map the fifo deduplication id property. |                                                                 | `deduplication-id`             |

`Note`: changing those headers won't have any change in the way that the binder communicates with
SQS.

They are only used to bind Spring `Message` object's headers to the properties in the SQS client

See [Spring Message](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/messaging/Message.html)
and
[AWS SDK SendMessageRequest](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/sqs/model/SendMessageRequest.html)
for more info.

### Consumer Properties

| Property                         | Type                             | Description                                                                                                                                                                                                                                                                                                                                                                       | Possible Values                                                                                                                              | Default value                                                                                                                                                            |
|----------------------------------|----------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| queue-not-found-strategy         | `QueueNotFoundStrategy`          | The strategy to use when the queue is not found.                                                                                                                                                                                                                                                                                                                                  | `QueueNotFoundStrategy.FAIL`<br/>`QueueNotFoundStrategy.CREATE`                                                                              | `QueueNotFoundStrategy.CREATE`                                                                                                                                           |
| max-concurrent-messages          | `Integer`                        | Represents the maximum number of messages from each queue that can be processed simultaneously in this container. This number will be used for defining the thread pool size for the container following (`maxConcurrentMessages` * number of queues). For batching acknowledgements a message is considered as no longer inflight when it’s handed to the acknowledgement queue. | `1` to `Integer.MAX_VALUE`                                                                                                                   | `10`                                                                                                                                                                     |
| max-messages-per-poll            | `Integer`                        | Represents the maximum number of messages that will be received by a poll to a SQS queue in this container. If a value greater than 10 is provided, the result of multiple polls will be combined, which can be useful for batch listeners.                                                                                                                                       | `1` to `Integer.MAX_VALUE`                                                                                                                   | `10`                                                                                                                                                                     |
| auto-Startup                     | `Boolean`                        | Determines wherever container should start automatically. When set to `false` the container will not launch on startup, requiring manual intervention to start it.                                                                                                                                                                                                                |                                                                                                                                              | `true`                                                                                                                                                                   |
| max-delay-between-polls          | `Duration`                       | Represents the maximum time the framework will wait for permits to be available for a queue before attempting the next poll. After that period, the framework will try to perform a partial acquire with the available permits, resulting in a poll for less than `maxMessagesPerPoll` messages, unless otherwise configured.                                                     | `PT1S` to `PT10S`                                                                                                                            | `PT10S`                                                                                                                                                                  |
| poll-timeout                     | `Duration`                       | Represents the maximum duration for a poll to a SQS queue before returning empty. Longer polls decrease the chance of empty polls when messages are available.                                                                                                                                                                                                                    | `PT1S` to `PT10S`                                                                                                                            | `PT10S`                                                                                                                                                                  |
| listener-mode                    | `ListenerMode`                   | Configures whether this container will use single message or batch listeners.                                                                                                                                                                                                                                                                                                     | `ListenerMode.SINGLE_MESSAGE`<br/>`ListenerMode.BATCH`                                                                                       | `ListenerMode.SINGLE_MESSAGE`                                                                                                                                            |
| listener-shutdown-timeout        | `Duration`                       | Represents the amount of time the container will wait for a queue to complete message processing before attempting to forcefully shutdown.                                                                                                                                                                                                                                        |                                                                                                                                              | `PT20S`                                                                                                                                                                  |
| acknowledgement-shutdown-timeout | `Duration`                       | Represents the amount of time the container will wait for acknowledgements to complete for a queue after message processing has ended.                                                                                                                                                                                                                                            |                                                                                                                                              | `PT20S`                                                                                                                                                                  |
| back-pressure-mode               | `BackPressureMode`               | Configures the backpressure strategy to be used by the container. See [Configuring BackPressureMode](https://docs.awspring.io/spring-cloud-aws/docs/3.2.0/reference/html/index.html#configuring-backpressuremode).                                                                                                                                                                | `BackPressureMode.AUTO`<br/>`BackPressureMode.ALWAYS_POLL_MAX_MESSAGES`<br/>`BackPressureMode.FIXED_HIGH_THROUGHPUT`                         | `BackPressureMode.AUTO`                                                                                                                                                  |
| acknowledgement-interval         | `Duration`                       | Configures the interval between acknowledges for batching. Set to `Duration.ZERO` along with `acknowledgementThreshold` to `0` to enable immediate acknowledgement.                                                                                                                                                                                                               |                                                                                                                                              | `PT1S` for Standard SQS Queue<br/>`Duration.ZERO` for Fifo SQS Queue                                                                                                     |
| acknowledgement-threshold        | `Integer`                        | Configures the minimal amount of messages in the acknowledgement queue to trigger acknowledgement of a batch. Set to `0` along with `acknowledgementInterval` to `Duration.ZERO` to enable immediate acknowledgement.                                                                                                                                                             |                                                                                                                                              | `10` for Standard SQS Queue<br/>`0` for Fifo SQS Queue                                                                                                                   |
| acknowledgement-mode             | `AcknowledgementMode`            | Configures the processing outcomes that will trigger automatic acknowledging of messages.                                                                                                                                                                                                                                                                                         | `AcknowledgementMode.ON_SUCCESS`<br/>`AcknowledgementMode.ALWAYS`<br/>`AcknowledgementMode.MANUAL`                                           | `AcknowledgementMode.ON_SUCCESS`                                                                                                                                         |
| acknowledgement-ordering         | `AcknowledgementOrdering`        | Configures the order acknowledgements should be made. Fifo queues can be acknowledged in parallel for immediate acknowledgement since the next message for a message group will only start being processed after the previous one has been acknowledged.                                                                                                                          | `AcknowledgementMode.PARALLEL`<br/>`AcknowledgementMode.ORDERED`                                                                             | `AcknowledgementOrdering.PARALLEL` for queues with immediate acknowledgement<br/>`AcknowledgementOrdering.ORDERED` for FIFO queues with acknowledgement batching enabled |
| queue-attribute-names            | `Collection<QueueAttributeName>` | Configures the QueueAttributes that will be retrieved from SQS when a container starts. See [Retrieving Attributes from SQS](https://docs.awspring.io/spring-cloud-aws/docs/3.2.0/reference/html/index.html#retrieving-attributes-from-sqs)                                                                                                                                       |                                                                                                                                              | `[]`                                                                                                                                                                     |
| message-attribute-names          | `Collection<String>`             | Configures the MessageAttributes that will be retrieved from SQS for each message.                                                                                                                                                                                                                                                                                                |                                                                                                                                              | `["ALL"]`                                                                                                                                                                |
| message-system-attribute-names   | `Collection<String>`             | Configures the MessageSystemAttribute that will be retrieved from SQS for each message.                                                                                                                                                                                                                                                                                           |                                                                                                                                              | `["ALL"]`                                                                                                                                                                |
| message-visibility               | `Duration`                       | Specify the message visibility duration for messages polled in this container. For FIFO queues, visibility is extended for all messages in a message group before each message is processed.                                                                                                                                                                                      |                                                                                                                                              | `null`                                                                                                                                                                   |
| fifo-batch-grouping-strategy     | `FifoBatchGroupingStrategy`      | Specifies how messages from FIFO queues should be grouped when retrieved by the container when listener mode is batch.                                                                                                                                                                                                                                                            | `FifoBatchGroupingStrategy.PROCESS_MESSAGE_GROUPS_IN_PARALLEL_BATCHES`<br/>`FifoBatchGroupingStrategy.PROCESS_MULTIPLE_GROUPS_IN_SAME_BATCH` | `FifoBatchGroupingStrategy.PROCESS_MESSAGE_GROUPS_IN_PARALLEL_BATCHES`                                                                                                   |

### Example

```yaml
spring:
  cloud:
    function.definition: input;output;fifoOutput;delayedOutput;queueNotFound;manualAck
    stream:
      bindings:
        input-in-0.destination: consumerQueue
        queueNotFound-in-0.destination: notExistingQueue
        manualAck-in-0.destination: manualAckQueue
        output-out-0.destination: producerQueue
        fifoOutput-out-0.destination: producerQueue.fifo
        delayedOutput-out-0.destination: delayQueue
      sqs:
        bindings:
          queueNotFound-in-0:
            consumer: # binder creation fails if the queue `notExistingQueue` does not exist
              queue-not-found-strategy: fail
          manualAck-in-0:
            consumer: # if the message is not ack manually in less than 1 sec, it will be visible again
              acknowledgement-mode: manual
              message-visibility: PT1S
```

### Fifo Queues

[SQS Fifo Queues](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-fifo-queues.html)
are
natively supported.
To use a Fifo queue, you simply need to add the suffix `.fifo` in the destination name.

You'll have to pass `message-group-id` and/or `message-deduplication-id` in the headers, for
example:

```java

@Configuration
class FifoFunctionDefinitionConfiguration {

    @Bean
    public Function<String, Message<String>> sendToFifoQueue() {
        return payload -> MessageBuilder.withPayload(payload)
                .setHeader(SqsHeaders.DEFAULT_SQS_FIFO_MESSAGE_GROUP_ID_HEADER, "app-group-id")
                .setHeader(SqsHeaders.DEFAULT_SQS_FIFO_MESSAGE_DEDUPLICATION_ID_HEADER, "message-unique-id-" + UUID.randomUUID())
                .build();
    }
}
```