package com.sivalabs.urlshortener.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.urlshortener.BaseIT;
import com.sivalabs.urlshortener.domain.models.PagedResult;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@Sql("/test-data.sql")
class AdminRestControllerTests extends BaseIT {

    @Test
    void adminUserShouldBeAbleToViewAllUrls() {
        MvcTestResult result = mockMvcTester
                .get()
                .uri("/api/admin/short-urls")
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenHeaderValue("admin@gmail.com"))
                .exchange();

        assertThat(result).hasStatusOk().bodyJson().convertTo(PagedResult.class).satisfies(pagedResult -> {
            assertThat(pagedResult.data()).hasSize(10);
            assertThat(pagedResult.totalElements()).isEqualTo(20);
            assertThat(pagedResult.pageNumber()).isEqualTo(1);
            assertThat(pagedResult.totalPages()).isEqualTo(2);
            assertThat(pagedResult.hasNext()).isTrue();
            assertThat(pagedResult.hasPrevious()).isFalse();
            assertThat(pagedResult.isFirst()).isTrue();
            assertThat(pagedResult.isLast()).isFalse();
        });
    }

    @Test
    void normalUserShouldNotBeAbleToViewAllUrls() {
        MvcTestResult result = mockMvcTester
                .get()
                .uri("/api/admin/short-urls")
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenHeaderValue("siva@gmail.com"))
                .exchange();

        assertThat(result).hasStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    void unauthorizedUserShouldNotBeAbleToViewAllUrls() {
        MvcTestResult result = mockMvcTester.get().uri("/api/admin/short-urls").exchange();

        assertThat(result).hasStatus(HttpStatus.UNAUTHORIZED);
    }
}
