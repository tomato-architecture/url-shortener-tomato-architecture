package com.sivalabs.urlshortener.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(
        @NotBlank(message = "Email is required") @Email(message = "Invalid email address") String email,
        @NotBlank(message = "Password is required") String password,
        @NotBlank(message = "Name is required") String name) {}
