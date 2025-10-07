package com.sivalabs.urlshortener.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.urlshortener.BaseIT;
import com.sivalabs.urlshortener.domain.models.PagedResult;
import com.sivalabs.urlshortener.domain.models.ShortUrlDto;
import com.sivalabs.urlshortener.domain.repositories.ShortUrlRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@Sql("/test-data.sql")
class UrlShortenerRestControllerTests extends BaseIT {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Test
    void shouldGetPublicShortenedUrls() {
        MvcTestResult result = mockMvcTester.get().uri("/api/short-urls").exchange();

        assertThat(result).hasStatusOk().bodyJson().convertTo(PagedResult.class).satisfies(pagedResult -> {
            assertThat(pagedResult.data()).hasSize(10);
            assertThat(pagedResult.totalElements()).isEqualTo(16);
            assertThat(pagedResult.pageNumber()).isEqualTo(1);
            assertThat(pagedResult.totalPages()).isEqualTo(2);
            assertThat(pagedResult.hasNext()).isTrue();
            assertThat(pagedResult.hasPrevious()).isFalse();
            assertThat(pagedResult.isFirst()).isTrue();
            assertThat(pagedResult.isLast()).isFalse();
        });
    }

    @Test
    void shouldCreateShortUrl() {
        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/short-urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                            "originalUrl": "https://start.spring.io/"
                        }
                        """)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .convertTo(ShortUrlDto.class)
                .satisfies(shortUrlDto -> {
                    assertThat(shortUrlDto.id()).isNotNull();
                    assertThat(shortUrlDto.shortKey()).isNotEmpty();
                    assertThat(shortUrlDto.originalUrl()).isEqualTo("https://start.spring.io/");
                    assertThat(shortUrlDto.isPrivate()).isFalse();
                    assertThat(shortUrlDto.clickCount()).isEqualTo(0L);
                    assertThat(shortUrlDto.createdAt()).isNotNull();
                    assertThat(shortUrlDto.expiresAt()).isNotNull();
                });
    }

    @Test
    void shouldRedirectToOriginalUrl() {
        MvcTestResult result = mockMvcTester.get().uri("/api/s/docker").exchange();

        assertThat(result).hasStatus(HttpStatus.FOUND).headers().hasValue("Location", "https://www.docker.com");
    }

    @Test
    void shouldReturnErrorWhenShortUrlNotFound() {
        MvcTestResult result = mockMvcTester.get().uri("/api/s/notfound").exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .headers()
                .hasValue("Location", "http://localhost:8080/not-found");
    }

    @Test
    void shouldShowMyUrlsPage() {
        MvcTestResult result = mockMvcTester
                .get()
                .uri("/api/my-urls")
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenHeaderValue("admin@gmail.com"))
                .exchange();

        assertThat(result).hasStatusOk().bodyJson().convertTo(PagedResult.class).satisfies(pagedResult -> {
            assertThat(pagedResult.data()).hasSize(10);
            assertThat(pagedResult.totalElements()).isEqualTo(18);
            assertThat(pagedResult.pageNumber()).isEqualTo(1);
            assertThat(pagedResult.totalPages()).isEqualTo(2);
            assertThat(pagedResult.hasNext()).isTrue();
            assertThat(pagedResult.hasPrevious()).isFalse();
            assertThat(pagedResult.isFirst()).isTrue();
            assertThat(pagedResult.isLast()).isFalse();
        });
    }

    @Test
    void shouldCreatePrivateShortUrl() {
        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/short-urls")
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenHeaderValue("siva@gmail.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                            "originalUrl": "https://github.com",
                            "isPrivate": true
                        }
                        """)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .convertTo(ShortUrlDto.class)
                .satisfies(shortUrlDto -> {
                    assertThat(shortUrlDto.id()).isNotNull();
                    assertThat(shortUrlDto.shortKey()).isNotEmpty();
                    assertThat(shortUrlDto.originalUrl()).isEqualTo("https://github.com");
                    assertThat(shortUrlDto.isPrivate()).isTrue();
                    assertThat(shortUrlDto.clickCount()).isEqualTo(0L);
                    assertThat(shortUrlDto.createdAt()).isNotNull();
                });
    }

    @Test
    void shouldCreateShortUrlWithCustomExpirationDays() {
        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/short-urls")
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenHeaderValue("siva@gmail.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                            "originalUrl": "https://stackoverflow.com",
                            "expirationInDays": 100
                        }
                        """)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .convertTo(ShortUrlDto.class)
                .satisfies(shortUrlDto -> {
                    assertThat(shortUrlDto.id()).isNotNull();
                    assertThat(shortUrlDto.shortKey()).isNotEmpty();
                    assertThat(shortUrlDto.originalUrl()).isEqualTo("https://stackoverflow.com");
                    assertThat(shortUrlDto.isPrivate()).isFalse();
                    assertThat(shortUrlDto.clickCount()).isEqualTo(0L);
                    assertThat(shortUrlDto.createdAt()).isNotNull();
                    assertThat(shortUrlDto.expiresAt()).isNotNull();

                    // Verify expiration date is approximately 100 days from now
                    Instant now = Instant.now();
                    Instant expectedExpiration = now.plus(100, ChronoUnit.DAYS);
                    assertThat(shortUrlDto.expiresAt())
                            .isBetween(
                                    expectedExpiration.minus(1, ChronoUnit.HOURS),
                                    expectedExpiration.plus(1, ChronoUnit.HOURS));
                });
    }

    @Test
    void shouldGetPaginatedPublicShortenedUrls() {
        MvcTestResult result = mockMvcTester.get().uri("/api/short-urls?page=2").exchange();

        assertThat(result).hasStatusOk().bodyJson().convertTo(PagedResult.class).satisfies(pagedResult -> {
            assertThat(pagedResult.data()).hasSize(6); // 16 total, 10 on first page, 6 on second
            assertThat(pagedResult.totalElements()).isEqualTo(16);
            assertThat(pagedResult.pageNumber()).isEqualTo(2);
            assertThat(pagedResult.totalPages()).isEqualTo(2);
            assertThat(pagedResult.hasNext()).isFalse();
            assertThat(pagedResult.hasPrevious()).isTrue();
            assertThat(pagedResult.isFirst()).isFalse();
            assertThat(pagedResult.isLast()).isTrue();
        });
    }

    @Test
    void shouldGetPaginatedMyUrls() {
        MvcTestResult result = mockMvcTester
                .get()
                .uri("/api/my-urls?page=2")
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenHeaderValue("admin@gmail.com"))
                .exchange();

        assertThat(result).hasStatusOk().bodyJson().convertTo(PagedResult.class).satisfies(pagedResult -> {
            assertThat(pagedResult.data()).hasSize(8); // 18 total, 10 on first page, 8 on second
            assertThat(pagedResult.totalElements()).isEqualTo(18);
            assertThat(pagedResult.pageNumber()).isEqualTo(2);
            assertThat(pagedResult.totalPages()).isEqualTo(2);
            assertThat(pagedResult.hasNext()).isFalse();
            assertThat(pagedResult.hasPrevious()).isTrue();
            assertThat(pagedResult.isFirst()).isFalse();
            assertThat(pagedResult.isLast()).isTrue();
        });
    }

    @Test
    void shouldAccessPrivateShortUrlByOwner() {
        // 'spring' is a private URL owned by admin (user ID 1)
        MvcTestResult result = mockMvcTester
                .get()
                .uri("/api/s/spring")
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenHeaderValue("admin@gmail.com"))
                .exchange();

        assertThat(result).hasStatus(HttpStatus.FOUND).headers().hasValue("Location", "https://spring.io");
    }

    @Test
    void shouldNotAccessPrivateShortUrlByNonOwner() {
        // 'w3scho' is a private URL owned by admin (user ID 1)
        MvcTestResult result = mockMvcTester
                .get()
                .uri("/api/s/w3scho")
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenHeaderValue("siva@gmail.com"))
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .headers()
                .hasValue("Location", "http://localhost:8080/not-found");
    }

    @Test
    void adminShouldBeAbleToDeleteUrlsOfOwnAndOthers() {
        MvcTestResult result = mockMvcTester
                .delete()
                .uri("/api/short-urls")
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenHeaderValue("admin@gmail.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                        "ids": [1, 5]
                        }
                        """)
                .exchange();

        assertThat(result).hasStatus(HttpStatus.OK);
        assertThat(shortUrlRepository.existsById(1L)).isFalse();
        assertThat(shortUrlRepository.existsById(5L)).isFalse();
    }

    @Test
    void normalUserShouldBeAbleToDeleteOwnUrls() {
        // First, let's verify that the URLs we want to test with exist
        // These should be URLs created by siva (user ID 2)
        assertThat(shortUrlRepository.existsById(5L)).isTrue();
        assertThat(shortUrlRepository.existsById(6L)).isTrue();

        // Try to delete URLs as siva (user ID 2)
        MvcTestResult result = mockMvcTester
                .delete()
                .uri("/api/short-urls")
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenHeaderValue("siva@gmail.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                        "ids": [5, 6]
                        }
                        """)
                .exchange();

        assertThat(result).hasStatus(HttpStatus.OK);

        // Verify the URLs are deleted
        assertThat(shortUrlRepository.existsById(5L)).isFalse();
        assertThat(shortUrlRepository.existsById(6L)).isFalse();
    }

    @Test
    void normalUsersShouldNotDeleteUrlsOfOtherUsers() {
        // First, let's verify that the URLs we want to test with exist
        // These should be URLs created by admin (user ID 1)
        assertThat(shortUrlRepository.existsById(1L)).isTrue();
        assertThat(shortUrlRepository.existsById(2L)).isTrue();
        assertThat(shortUrlRepository.existsById(3L)).isTrue();

        // Try to delete URLs created by admin (user ID 1) as siva (user ID 2)
        MvcTestResult result = mockMvcTester
                .delete()
                .uri("/api/short-urls")
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenHeaderValue("siva@gmail.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                        "ids": [1, 2, 3]
                        }
                        """)
                .exchange();

        assertThat(result).hasStatus(HttpStatus.FORBIDDEN);

        // Verify the URLs still exist
        assertThat(shortUrlRepository.existsById(1L)).isTrue();
        assertThat(shortUrlRepository.existsById(2L)).isTrue();
        assertThat(shortUrlRepository.existsById(3L)).isTrue();
    }

    @Test
    void shouldHandleInvalidUrl() {
        // Configure the application to validate URLs
        // This test assumes URL validation is enabled in the application properties
        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/short-urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                            "originalUrl": "invalid-url"
                        }
                        """)
                .exchange();

        // The response should have a bad request status
        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotAccessExpiredShortUrl() {
        // 'medium' has an expiration date set in test-data.sql
        // We need to modify it to be expired

        // First, find the ShortUrl with shortKey 'medium'
        var shortUrl = shortUrlRepository.findByShortKey("medium").orElseThrow();

        // Set the expiration date to the past
        shortUrl.setExpiresAt(Instant.now().minus(1, ChronoUnit.DAYS));
        shortUrlRepository.save(shortUrl);

        // Now try to access it
        MvcTestResult result = mockMvcTester.get().uri("/api/s/medium").exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .headers()
                .hasValue("Location", "http://localhost:8080/not-found");
    }
}
