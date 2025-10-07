package com.sivalabs.urlshortener.api.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateShortUrlRequest(
        @NotBlank(message = "Original URL is required") String originalUrl,
        Boolean isPrivate,
        @Min(value = 1, message = "Expiration must be at least 1 day") @Max(value = 365, message = "Expiration cannot exceed 365 days") Integer expirationInDays) {}
