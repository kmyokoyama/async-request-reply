package io.kmyokoyama.asyncrequestreply;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "meta")
public class ConfigProperties {

    private long retryAfter;
    private long delay;
}
