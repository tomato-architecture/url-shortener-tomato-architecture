package com.sivalabs.urlshortener;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.core")
@Validated
public record CoreProperties(
        @NotBlank @DefaultValue("http://localhost:8080") String baseUrl,
        @Min(1) @Max(365) @DefaultValue("30") int defaultExpirationDays,
        @DefaultValue("true") Boolean validateOriginalUrl,
        @Min(5) @Max(100) @DefaultValue("10") int pageSize) {}
