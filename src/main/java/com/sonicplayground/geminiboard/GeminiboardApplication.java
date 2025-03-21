package com.sonicplayground.geminiboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GeminiboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeminiboardApplication.class, args);
    }

}
