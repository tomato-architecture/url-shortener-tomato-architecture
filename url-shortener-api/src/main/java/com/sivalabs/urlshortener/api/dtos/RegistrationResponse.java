package com.sivalabs.urlshortener.api.dtos;

import com.sivalabs.urlshortener.domain.models.Role;

public record RegistrationResponse(String email, String name, Role role) {}
