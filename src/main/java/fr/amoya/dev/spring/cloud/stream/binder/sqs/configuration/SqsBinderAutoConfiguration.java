package fr.amoya.dev.spring.cloud.stream.binder.sqs.configuration;

import fr.amoya.dev.spring.cloud.stream.binder.sqs.SqsMessageChannelBinder;
import fr.amoya.dev.spring.cloud.stream.binder.sqs.health.SqsBinderHealthIndicator;
import fr.amoya.dev.spring.cloud.stream.binder.sqs.properties.SqsExtendedBindingProperties;
import fr.amoya.dev.spring.cloud.stream.binder.sqs.provisioning.SqsStreamProvisioner;
import io.awspring.cloud.autoconfigure.core.CredentialsProviderAutoConfiguration;
import io.awspring.cloud.autoconfigure.core.RegionProviderAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@AutoConfiguration
@AutoConfigureAfter({
    CredentialsProviderAutoConfiguration.class,
    RegionProviderAutoConfiguration.class
})
@ConditionalOnMissingBean(Binder.class)
@EnableConfigurationProperties({SqsExtendedBindingProperties.class})
public class SqsBinderAutoConfiguration {

    private final SqsAsyncClient amazonSqs;

    private final SqsStreamProvisioner sqsStreamProvisioner;

    private final SqsExtendedBindingProperties extendedBindingProperties;

    public SqsBinderAutoConfiguration(SqsAsyncClient amazonSqs,
                                      SqsExtendedBindingProperties extendedBindingProperties) {
        this.amazonSqs = amazonSqs;
        this.extendedBindingProperties = extendedBindingProperties;
        this.sqsStreamProvisioner = new SqsStreamProvisioner();
    }


    @Bean
    public SqsStreamProvisioner provisioningProvider() {
        return sqsStreamProvisioner;
    }

    @Bean
    public SqsMessageChannelBinder sqsMessageHandlerBinder() {
        return new SqsMessageChannelBinder(amazonSqs, sqsStreamProvisioner,
            extendedBindingProperties,
            PropertyMapper.get().alwaysApplyingWhenNonNull());
    }

    @Configuration
    @ConditionalOnClass(HealthIndicator.class)
    @ConditionalOnEnabledHealthIndicator("binders")
    protected static class SqsBinderHealthIndicatorConfiguration {

        private final SqsAsyncClient amazonSqs;

        private final SqsMessageChannelBinder sqsMessageChannelBinder;

        public SqsBinderHealthIndicatorConfiguration(SqsAsyncClient amazonSqs,
                                                     SqsMessageChannelBinder sqsMessageChannelBinder) {
            this.amazonSqs = amazonSqs;
            this.sqsMessageChannelBinder = sqsMessageChannelBinder;
        }

        @Bean
        @ConditionalOnMissingBean(name = "sqsBinderHealthIndicator")
        public SqsBinderHealthIndicator sqsBinderHealthIndicator() {
            return new SqsBinderHealthIndicator(amazonSqs, sqsMessageChannelBinder);
        }

    }
}
