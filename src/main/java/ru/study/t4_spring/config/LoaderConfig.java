package ru.study.t4_spring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "loader")
@Configuration
@Data
public class LoaderConfig {
    private String in;
    private String archive;
}
