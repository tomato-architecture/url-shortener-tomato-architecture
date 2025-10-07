package com.sivalabs.urlshortener.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;

public record CreateShortUrlRequest(
        @NotBlank(message = "Original URL is required") String originalUrl,
        Boolean isPrivate,
        Integer expirationInDays) {
    @JsonIgnore
    public String getCleanedOriginalUrl() {
        String originalUrl = this.originalUrl();
        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "http://" + originalUrl;
        }
        return originalUrl;
    }
}
