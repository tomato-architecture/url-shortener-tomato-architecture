package com.sivalabs.urlshortener.domain.exceptions;

public class InvalidURLException extends RuntimeException {
    public InvalidURLException(String message) {
        super(message);
    }

    public static InvalidURLException of(String url) {
        return new InvalidURLException("Invalid URL: " + url);
    }
}
