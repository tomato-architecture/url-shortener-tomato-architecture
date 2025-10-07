package com.sivalabs.urlshortener.api.controllers;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @GetMapping("")
    Map<String, String> info() {
        return Map.of("App", "URL Shortener", "Version", "1.0.0");
    }
}
