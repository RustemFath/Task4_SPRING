package ru.study.t4_spring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "logger")
@Configuration
@Data
public class LoggerConfig {
    private String dir;
}
