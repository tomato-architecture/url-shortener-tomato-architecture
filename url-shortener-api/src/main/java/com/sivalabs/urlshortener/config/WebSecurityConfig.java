package com.sivalabs.urlshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        String[] publicResources = {
            "/", "/favicon.ico", "/actuator/**", "/error",
        };
        http.securityMatcher("/api/**");
        http.csrf(CsrfConfigurer::disable);
        http.cors(CorsConfigurer::disable);

        http.authorizeHttpRequests(c -> c.requestMatchers(publicResources)
                .permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .requestMatchers("/api/login")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/short-urls", "/api/short-urls/*", "/api/s/*")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/short-urls")
                .permitAll()
                .requestMatchers("/api/admin/**")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated());

        http.oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()));
        http.exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
