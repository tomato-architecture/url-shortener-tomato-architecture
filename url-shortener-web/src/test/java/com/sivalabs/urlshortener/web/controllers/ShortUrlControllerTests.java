package com.sivalabs.urlshortener.web.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.sivalabs.urlshortener.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
class ShortUrlControllerTests extends BaseIT {

    @Test
    void shouldShowHomePage() {
        var result = mockMvcTester.get().uri("/").exchange();

        assertThat(result)
                .hasStatusOk()
                .hasViewName("index")
                .model()
                .containsKeys("shortUrls", "baseUrl", "paginationUrl", "createShortUrlForm");
    }

    @Test
    void shouldReturnToHomeWhenValidationErrorsOnCreate() {
        var result = mockMvcTester
                .post()
                .uri("/short-urls")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("originalUrl", "")
                .param("isPrivate", "false")
                .param("expirationInDays", "0")
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .hasViewName("index")
                .model()
                .extractingBindingResult("createShortUrlForm")
                .hasErrorsCount(2)
                .hasFieldErrors("originalUrl", "expirationInDays");
    }

    @Test
    @WithUserDetails("siva@gmail.com")
    void shouldCreateShortUrlAndRedirectWithSuccessMessage() {
        // Use a well-known URL; the app validates existence by default.
        var result = mockMvcTester
                .post()
                .uri("/short-urls")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("originalUrl", "https://www.google.com")
                .param("isPrivate", "false")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .hasHeader("Location", "/")
                .flash()
                .containsKey("successMessage");
    }

    @Test
    @WithUserDetails("siva@gmail.com")
    void shouldRedirectWithErrorMessageWhenCreateFails() {
        // Use a non-existent domain to force UrlExistenceValidator failure
        var result = mockMvcTester
                .post()
                .uri("/short-urls")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("originalUrl", "https://nonexistent-example-xyz-12345.com")
                .param("isPrivate", "false")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .hasHeader("Location", "/")
                .flash()
                .containsKey("errorMessage");
    }

    @Test
    void shouldRedirectToOriginalUrlWhenShortKeyExists() {
        var result = mockMvcTester.get().uri("/s/docker").exchange();

        assertThat(result).hasStatus(HttpStatus.FOUND).hasHeader("Location", "https://www.docker.com");
    }

    @Test
    void shouldShow404ErrorViewWhenShortKeyNotFound() {
        var result = mockMvcTester.get().uri("/s/notfound").exchange();

        assertThat(result).hasStatusOk().hasViewName("error/404");
    }

    @Test
    void shouldRedirectToLoginWhenAccessingMyUrlsUnauthenticated() {
        var result = mockMvcTester.get().uri("/my-urls").exchange();

        assertThat(result).hasStatus(HttpStatus.FOUND).hasHeader("Location", "/login");
    }

    @Test
    @WithUserDetails("siva@gmail.com")
    void shouldShowMyUrlsForAuthenticatedUser() {
        var result = mockMvcTester.get().uri("/my-urls").exchange();

        assertThat(result)
                .hasStatusOk()
                .hasViewName("my-urls")
                .model()
                .containsKeys("shortUrls", "baseUrl", "paginationUrl");
    }

    @Test
    @WithUserDetails("siva@gmail.com")
    void shouldNotDeleteUrlsWhenNoIdsSelected() {
        var result = mockMvcTester
                .post()
                .uri("/delete-urls")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .hasHeader("Location", "/my-urls")
                .flash()
                .containsKey("errorMessage")
                .containsEntry("errorMessage", "No URLs selected for deletion");
        ;
    }

    @Test
    @WithUserDetails("siva@gmail.com")
    void shouldDeleteSelectedUrlsForCurrentUser() {
        // IDs 5 and 6 belong to user with email siva@gmail.com as per test-data.sql
        var result = mockMvcTester
                .post()
                .uri("/delete-urls")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("ids", "5", "6")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .hasHeader("Location", "/my-urls")
                .flash()
                .containsKey("successMessage");
    }

    @Test
    @WithUserDetails("siva@gmail.com")
    void shouldShowErrorWhenDeletingUrlsNotOwnedByUser() {
        // ID 1 belongs to admin user; trying to delete it as siva should fail
        var result = mockMvcTester
                .post()
                .uri("/delete-urls")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("ids", "1")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .hasHeader("Location", "/my-urls")
                .flash()
                .containsKey("errorMessage");
    }
}
