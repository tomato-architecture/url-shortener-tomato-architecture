package com.sivalabs.urlshortener.api.dtos;

import java.time.Instant;

public record LoginResponse(String token, Instant expiresAt, String email, String name, String role) {}
