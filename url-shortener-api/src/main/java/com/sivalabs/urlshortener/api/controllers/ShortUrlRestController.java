package com.sivalabs.urlshortener.api.controllers;

import com.sivalabs.urlshortener.CoreProperties;
import com.sivalabs.urlshortener.api.dtos.CreateShortUrlRequest;
import com.sivalabs.urlshortener.api.utils.ApiSecurityUtils;
import com.sivalabs.urlshortener.domain.models.CreateShortUrlCmd;
import com.sivalabs.urlshortener.domain.models.PagedResult;
import com.sivalabs.urlshortener.domain.models.ShortUrlDto;
import com.sivalabs.urlshortener.domain.services.ShortUrlService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Short URLs")
class ShortUrlRestController {
    private static final Logger log = LoggerFactory.getLogger(ShortUrlRestController.class);

    private final ShortUrlService shortUrlService;
    private final ApiSecurityUtils securityUtils;
    private final CoreProperties properties;

    ShortUrlRestController(ShortUrlService shortUrlService, ApiSecurityUtils securityUtils, CoreProperties properties) {
        this.shortUrlService = shortUrlService;
        this.securityUtils = securityUtils;
        this.properties = properties;
    }

    @GetMapping("/short-urls")
    PagedResult<ShortUrlDto> showShortUrls(@RequestParam(defaultValue = "1") int page) {
        return shortUrlService.findPublicShortUrls(page, properties.pageSize());
    }

    @PostMapping("/short-urls")
    ResponseEntity<ShortUrlDto> createShortUrl(@RequestBody @Valid CreateShortUrlRequest request) {
        var originalUrl = request.getCleanedOriginalUrl();
        var isPrivate = request.isPrivate() != null && request.isPrivate();
        var cmd = new CreateShortUrlCmd(
                originalUrl, isPrivate, request.expirationInDays(), securityUtils.getCurrentUserId());
        var shortUrlDto = shortUrlService.createShortUrl(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrlDto);
    }

    @GetMapping("/my-urls")
    @SecurityRequirement(name = "Bearer")
    PagedResult<ShortUrlDto> myShortenedUrls(@RequestParam(defaultValue = "1") int page) {
        var currentUserId = securityUtils.getCurrentUserId();
        return shortUrlService.findUserShortUrls(currentUserId, page, properties.pageSize());
    }

    @DeleteMapping("/short-urls")
    @SecurityRequirement(name = "Bearer")
    void deleteUrls(@RequestBody DeleteRequest request) {
        Set<Long> ids = request.ids();
        if (ids == null || ids.isEmpty()) {
            log.info("No short_url ids selected for deletion");
            return;
        }
        boolean isAdmin = securityUtils.isCurrentUserAdmin();
        if (isAdmin) {
            shortUrlService.deleteUserShortUrls(ids);
        } else {
            var currentUserId = securityUtils.getCurrentUserId();
            shortUrlService.deleteUserShortUrls(ids, currentUserId);
        }
    }

    record DeleteRequest(Set<Long> ids) {}
}
