package com.sivalabs.urlshortener.web.controllers;

import com.sivalabs.urlshortener.CoreProperties;
import com.sivalabs.urlshortener.domain.models.PagedResult;
import com.sivalabs.urlshortener.domain.models.ShortUrlDto;
import com.sivalabs.urlshortener.domain.services.ShortUrlService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ShortUrlService shortUrlService;
    private final CoreProperties properties;

    public AdminController(ShortUrlService shortUrlService, CoreProperties properties) {
        this.shortUrlService = shortUrlService;
        this.properties = properties;
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(defaultValue = "1") int page, Model model) {
        PagedResult<ShortUrlDto> allUrls = shortUrlService.findAllShortUrls(page, properties.pageSize());
        model.addAttribute("shortUrls", allUrls);
        model.addAttribute("baseUrl", properties.baseUrl());
        model.addAttribute("paginationUrl", "/admin/dashboard");
        return "admin-dashboard";
    }
}
