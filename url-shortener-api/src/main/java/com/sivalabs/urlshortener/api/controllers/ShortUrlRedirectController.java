package com.sivalabs.urlshortener.api.controllers;

import com.sivalabs.urlshortener.CoreProperties;
import com.sivalabs.urlshortener.api.utils.ApiSecurityUtils;
import com.sivalabs.urlshortener.domain.models.ShortUrlDto;
import com.sivalabs.urlshortener.domain.services.ShortUrlService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Tag(name = "Short URLs")
class ShortUrlRedirectController {
    private final ShortUrlService shortUrlService;
    private final ApiSecurityUtils securityUtils;
    private final CoreProperties properties;

    ShortUrlRedirectController(
            ShortUrlService shortUrlService, ApiSecurityUtils securityUtils, CoreProperties properties) {
        this.shortUrlService = shortUrlService;
        this.securityUtils = securityUtils;
        this.properties = properties;
    }

    @GetMapping("/api/s/{shortKey}")
    ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortKey) {
        var currentUserId = securityUtils.getCurrentUserId();
        var optionalShortUrl = shortUrlService.accessShortUrl(shortKey, currentUserId);
        var redirectUrl = optionalShortUrl.map(ShortUrlDto::originalUrl).orElse(properties.baseUrl() + "/not-found");
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }
}
