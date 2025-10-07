# Coding Guidelines

## General Guidelines
* After making code changes, re-run `mvn clean spotless:apply verify` to ensure all tests pass
* Write new tests or update existing tests based on the code changes

## Java Guidelines
* Prefer using Java 8 DateTime API over java.util.Date API
* Prefer using Java 8 Optional API over null

## Spring Boot Guidelines

### 1. Constructor Injection
* Use `final` fields with constructor injection (no `@Autowired` needed for single constructor)
* Avoid field/setter injection in production code

### 2. Package-Private Visibility
* Use package-private (default) visibility for Controllers, `@Configuration` classes, and `@Bean` methods

### 3. Configuration Properties
* Group properties with common prefix in `application.properties/yml`
* Bind to `@ConfigurationProperties` classes with validation annotations for fail-fast behavior
* Use environment variables over profiles for different environments

### 4. Transaction Boundaries
* Annotate service methods: `@Transactional(readOnly = true)` for queries, `@Transactional` for modifications
* Keep transaction scope minimal

### 5. Disable Open Session in View
* Set `spring.jpa.open-in-view=false` in Spring Data JPA applications

### 6. Web/Persistence Separation
* Use explicit request/response records (DTOs) instead of exposing entities
* Apply Jakarta Validation annotations on request records

### 7. REST API Design
* Use versioned URLs: `/api/v{version}/resources`
* Return proper HTTP status codes via `ResponseEntity<T>`
* Paginate collections with unbounded items
* Use JSON objects as top-level structure; maintain consistent property naming (snake_case or camelCase)

### 8. Command Objects
* Use purpose-built command records (e.g., `CreateOrderCommand`) for business operations

### 9. Exception Handling
* Centralize with `@RestControllerAdvice` and `@ExceptionHandler` methods
* Return consistent error responses (consider RFC 9457 ProblemDetails format)

### 10. Actuator Security
* Expose `/health`, `/info`, `/metrics` publicly; secure all other endpoints

### 11. Internationalization
* Externalize user-facing text to ResourceBundles

### 12. Integration Testing
* Use Testcontainers for real services (databases, message brokers)
* Start on random port: `@SpringBootTest(webEnvironment = RANDOM_PORT)`

### 13. Logging
* Use SLF4J (never `System.out.println()`)
* Never log sensitive data (credentials, PII)
* Guard expensive debug/trace calls with level checks or suppliers

