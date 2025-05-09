package fr.amoya.dev.spring.cloud.stream.binder.sqs.configuration;

import java.util.Map;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.cloud.stream.config.BindingHandlerAdvise.MappingsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ExtendedBindingHandlerMappingsProviderConfiguration {

    @Bean
    public MappingsProvider sqsExtendedPropertiesDefaultMappingProvider() {
        return () -> Map.of(
            ConfigurationPropertyName.of("spring.cloud.stream.sqs.default"),
            ConfigurationPropertyName.of("spring.cloud.stream.sqs.bindings")
        );
    }
}
