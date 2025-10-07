package com.sivalabs.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UrlShortenerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerWebApplication.class, args);
    }
}
