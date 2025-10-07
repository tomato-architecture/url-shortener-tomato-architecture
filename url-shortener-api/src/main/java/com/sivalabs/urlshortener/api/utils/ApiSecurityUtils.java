package com.sivalabs.urlshortener.api.utils;

import com.sivalabs.urlshortener.domain.entities.User;
import com.sivalabs.urlshortener.domain.models.Role;
import com.sivalabs.urlshortener.domain.repositories.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class ApiSecurityUtils {
    private final UserRepository userRepository;

    public ApiSecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        var principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            return jwt.getClaim("user_id");
        }
        return null;
    }

    public Long getCurrentUserIdOrThrow() {
        Long userId = getCurrentUserId();
        if (userId != null) {
            return userId;
        }
        throw new AccessDeniedException("Access denied");
    }

    public User getCurrentUser() {
        Long userId = getCurrentUserId();
        if (userId != null) {
            return userRepository.findById(userId).orElse(null);
        }
        return null;
    }

    public boolean isCurrentUserAdmin() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRole() == Role.ROLE_ADMIN;
    }
}
