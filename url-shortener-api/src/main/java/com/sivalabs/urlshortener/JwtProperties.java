package com.sivalabs.urlshortener;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.jwt")
@Validated
public record JwtProperties(
        @NotBlank String issuer,
        @NotNull @Positive Long expiresInSeconds,
        @NotNull RSAPublicKey publicKey,
        @NotNull RSAPrivateKey privateKey) {}
