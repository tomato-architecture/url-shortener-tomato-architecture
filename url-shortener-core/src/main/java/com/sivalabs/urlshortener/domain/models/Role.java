package com.sivalabs.urlshortener.domain.models;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    public static String getRoleHierarchy() {
        return "ROLE_ADMIN > ROLE_USER";
    }
}
