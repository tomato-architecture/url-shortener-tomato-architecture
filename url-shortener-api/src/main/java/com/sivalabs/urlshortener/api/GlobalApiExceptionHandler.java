package com.sivalabs.urlshortener.api;

import static org.springframework.http.HttpStatus.*;

import com.sivalabs.urlshortener.domain.exceptions.BadRequestException;
import com.sivalabs.urlshortener.domain.exceptions.EmailAlreadyExistsException;
import com.sivalabs.urlshortener.domain.exceptions.InvalidURLException;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.sivalabs.urlshortener.api")
public class GlobalApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handle(EmailAlreadyExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(CONFLICT, e.getMessage());
        problemDetail.setTitle("Email already exists");
        return problemDetail;
    }

    @ExceptionHandler(InvalidURLException.class)
    public ProblemDetail handle(InvalidURLException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Invalid URL");
        return problemDetail;
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handle(BadRequestException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Bad Request");
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handle(AccessDeniedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(FORBIDDEN, e.getMessage());
        problemDetail.setTitle("Access Denied");
        return problemDetail;
    }
}
