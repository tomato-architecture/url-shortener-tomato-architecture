package com.sivalabs.urlshortener.web.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.urlshortener.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
class AdminControllerTests extends BaseIT {

    @Test
    void shouldRedirectToLoginWhenUnauthenticated() {
        var result = mockMvcTester.get().uri("/admin/dashboard").exchange();

        assertThat(result).hasStatus(HttpStatus.FOUND).hasHeader("Location", "/login");
    }

    @Test
    @WithUserDetails("siva@gmail.com")
    void shouldReturnForbiddenForNonAdminUser() {
        var result = mockMvcTester.get().uri("/admin/dashboard").exchange();

        assertThat(result).hasStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldShowDashboardWithDefaultPageForAdmin() {
        var result = mockMvcTester.get().uri("/admin/dashboard").exchange();

        assertThat(result)
                .hasStatusOk()
                .hasViewName("admin-dashboard")
                .model()
                .containsKeys("shortUrls", "baseUrl", "paginationUrl");
    }
}
