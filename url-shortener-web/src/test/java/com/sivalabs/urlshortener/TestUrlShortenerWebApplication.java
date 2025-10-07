package com.sivalabs.urlshortener;

import org.springframework.boot.SpringApplication;

public class TestUrlShortenerWebApplication {

    public static void main(String[] args) {
        SpringApplication.from(UrlShortenerWebApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
