package com.sivalabs.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UrlShortenerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerApiApplication.class, args);
    }
}
