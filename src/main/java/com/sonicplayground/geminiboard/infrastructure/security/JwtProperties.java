package com.sonicplayground.geminiboard.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
    String secretKey,
    long expirationHours,
    String issuer) {

}
