package fr.amoya.dev.spring.cloud.stream.binder.sqs.properties;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.binder.AbstractExtendedBindingProperties;
import org.springframework.cloud.stream.binder.BinderSpecificPropertiesProvider;

@ConfigurationProperties("spring.cloud.stream.sqs")
public class SqsExtendedBindingProperties extends
        AbstractExtendedBindingProperties<SqsConsumerProperties, SqsProducerProperties, SqsBindingProperties> {

    private static final String DEFAULTS_PREFIX = "spring.cloud.stream.sqs.default";

    @Override
    public Map<String, SqsBindingProperties> getBindings() {
        return this.doGetBindings();
    }

    @Override
    public String getDefaultsPrefix() {
        return DEFAULTS_PREFIX;
    }

    @Override
    public Class<? extends BinderSpecificPropertiesProvider> getExtendedPropertiesEntryClass() {
        return SqsBindingProperties.class;
    }
}
