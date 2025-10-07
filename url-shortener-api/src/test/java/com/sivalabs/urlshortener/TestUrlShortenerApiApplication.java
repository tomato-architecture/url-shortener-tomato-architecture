package com.sivalabs.urlshortener;

import org.springframework.boot.SpringApplication;

public class TestUrlShortenerApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(UrlShortenerApiApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
