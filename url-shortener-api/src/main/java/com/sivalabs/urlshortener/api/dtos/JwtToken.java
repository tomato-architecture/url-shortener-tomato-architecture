package com.sivalabs.urlshortener.api.dtos;

import java.time.Instant;

public record JwtToken(String token, Instant expiresAt) {}
