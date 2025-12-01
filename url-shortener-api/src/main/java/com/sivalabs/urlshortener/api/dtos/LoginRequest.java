package com.sivalabs.urlshortener.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty(message = "Email is required") @Email(message = "Invalid email") String email,

        @NotEmpty(message = "Password is required") String password) {}
