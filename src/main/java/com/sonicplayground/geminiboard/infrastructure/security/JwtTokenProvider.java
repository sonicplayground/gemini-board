package com.sonicplayground.geminiboard.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;

    public String createToken(String userInfo) {
        log.debug("createToken userInfo: {}", userInfo);
        return Jwts.builder()
            .signWith(new SecretKeySpec(jwtProperties.getSecretKey().getBytes(), SignatureAlgorithm.HS512.getJcaName()))
            .setSubject(userInfo)
            .setIssuer(jwtProperties.getIssuer())
            .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
            .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusHours(
                jwtProperties.getExpirationHours())))
            .compact();
    }

    public String getUserInfo(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(jwtProperties.getSecretKey().getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

}
