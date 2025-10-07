package com.sivalabs.urlshortener.api.controllers;

import com.sivalabs.urlshortener.CoreProperties;
import com.sivalabs.urlshortener.domain.models.PagedResult;
import com.sivalabs.urlshortener.domain.models.ShortUrlDto;
import com.sivalabs.urlshortener.domain.services.ShortUrlService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "Bearer")
@Tag(name = "Admin")
class AdminRestController {
    private final ShortUrlService shortUrlService;
    private final CoreProperties properties;

    AdminRestController(ShortUrlService shortUrlService, CoreProperties properties) {
        this.shortUrlService = shortUrlService;
        this.properties = properties;
    }

    @GetMapping("/short-urls")
    PagedResult<ShortUrlDto> getAllShortUrls(@RequestParam(defaultValue = "1") int page) {
        return shortUrlService.findAllShortUrls(page, properties.pageSize());
    }
}
