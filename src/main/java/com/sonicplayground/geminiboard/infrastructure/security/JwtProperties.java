package com.sonicplayground.geminiboard.infrastructure.security;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private final String secretKey;
    private final long expirationHours;
    private final String issuer;

    @ConstructorBinding
    public JwtProperties(String secretKey, long expirationHours, String issuer) {
        this.secretKey = secretKey;
        this.expirationHours = expirationHours;
        this.issuer = issuer;
    }
}
