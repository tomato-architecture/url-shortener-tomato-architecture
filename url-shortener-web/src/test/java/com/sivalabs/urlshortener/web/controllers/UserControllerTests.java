package com.sivalabs.urlshortener.web.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.sivalabs.urlshortener.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
class UserControllerTests extends BaseIT {
    @Test
    void shouldShowLoginForm() {
        var result = mockMvcTester.get().uri("/login").exchange();

        assertThat(result).hasStatusOk().hasViewName("login");
    }

    @Test
    void shouldLoginSuccessfullyWithValidCredentials() {
        var result = mockMvcTester
                .post()
                .uri("/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "siva@gmail.com")
                .param("password", "secret")
                .exchange();

        assertThat(result).hasStatus(HttpStatus.FOUND).hasHeader("Location", "/");
    }

    @Test
    void shouldNotLoginWithInvalidCredentials() {
        var result = mockMvcTester
                .post()
                .uri("/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "siva@gmail.com")
                .param("password", "wrongpwd")
                .exchange();

        assertThat(result).hasStatus(HttpStatus.FOUND).hasHeader("Location", "/login?error");
    }

    @Test
    void shouldShowRegistrationForm() {
        var result = mockMvcTester.get().uri("/register").exchange();

        assertThat(result).hasStatusOk().hasViewName("register").model().containsKey("user");
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        var result = mockMvcTester
                .post()
                .uri("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Test User")
                .param("email", "test@example.com")
                .param("password", "password")
                .exchange();

        assertThat(result).hasStatus(HttpStatus.FOUND).hasHeader("Location", "/login");
    }

    @Test
    void shouldReturnToRegistrationFormWhenValidationErrors() {
        var result = mockMvcTester
                .post()
                .uri("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "")
                .param("email", "invalid-email")
                .param("password", "")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasViewName("register")
                .model()
                .extractingBindingResult("user")
                .hasErrorsCount(3)
                .hasFieldErrors("name", "email", "password");
    }

    @Test
    void shouldRedirectToRegistrationWithErrorMessageWhenDuplicateEmail() {
        var result = mockMvcTester
                .post()
                .uri("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Another User")
                .param("email", "siva@gmail.com")
                .param("password", "password")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .hasHeader("Location", "/register")
                .flash()
                .containsKey("errorMessage")
                .hasEntrySatisfying("errorMessage", value -> assertThat((String) value)
                        .contains("Email already exists: siva@gmail.com"));
    }
}
