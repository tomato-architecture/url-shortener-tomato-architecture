package com.sivalabs.urlshortener.api.utils;

import com.sivalabs.urlshortener.JwtProperties;
import com.sivalabs.urlshortener.api.dtos.JwtToken;
import com.sivalabs.urlshortener.domain.entities.User;
import java.time.Instant;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenHelper {
    private final JwtEncoder encoder;
    private final JwtProperties properties;

    JwtTokenHelper(JwtEncoder encoder, JwtProperties properties) {
        this.encoder = encoder;
        this.properties = properties;
    }

    public JwtToken generateToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(properties.expiresInSeconds());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.issuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getEmail())
                .claim("user_id", user.getId())
                .claim("roles", user.getRole().name())
                .claim("scope", user.getRole().name())
                .build();
        var token = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new JwtToken(token, expiresAt);
    }
}
