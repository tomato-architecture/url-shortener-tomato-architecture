package com.sivalabs.urlshortener.domain.exceptions;

public class EmailAlreadyExistsException extends BadRequestException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public static EmailAlreadyExistsException of(String email) {
        return new EmailAlreadyExistsException("Email already exists: " + email);
    }
}
