-- admin pwd: admin
-- siva pwd: secret
INSERT INTO users (email, password, name, role)
VALUES ('admin@gmail.com', '$2a$10$Vq7kPWnWwz7isGGE/8gxJOLdZCRFsBzqU6swEprp66kGvK1UdbQjy', 'Administrator', 'ROLE_ADMIN'),
       ('siva@gmail.com', '$2a$10$FjomyZihHtoW7sUFuypX2u3p.orW5nuYOpNA8iK9FPAp1KeFY2iyi', 'Siva', 'ROLE_USER');

INSERT INTO short_urls (short_key, original_url, created_by, created_at, expires_at, is_private, click_count)
VALUES ('rs1Aed', 'https://www.sivalabs.in/code-offline-with-local-ai-ollama', 1, TIMESTAMP '2024-07-15', NULL, FALSE,
        0),
       ('hujfDf', 'https://www.sivalabs.in/run-spring-boot-testcontainers-tests-at-jet-speed', 1,
        TIMESTAMP '2024-07-16', NULL, FALSE, 0),
       ('ertcbn', 'https://www.sivalabs.in/running-custom-spring-initializr', 1, TIMESTAMP '2024-07-17', NULL, FALSE,
        0),
       ('edfrtg', 'https://www.sivalabs.in/an-email-template-of-asking-for-technical-help', 1, TIMESTAMP '2024-07-18',
        NULL, TRUE, 0),
       ('vbgtyh', 'https://www.sivalabs.in/mastering-spring-boot-in-5-stages', 1, TIMESTAMP '2024-07-19', NULL, FALSE,
        0),
       ('rtyfgb', 'https://www.sivalabs.in/thymeleaf-layouts-using-fragment-expressions', 1, TIMESTAMP '2024-07-25',
        NULL, FALSE, 0),
       ('rtvbop', 'https://www.sivalabs.in/spring-ai-rag-using-embedding-models-vector-databases', 1,
        TIMESTAMP '2024-07-26', NULL, FALSE, 0),
       ('2wedfg', 'https://www.sivalabs.in/getting-started-with-spring-ai-and-open-ai', 1, TIMESTAMP '2024-07-27', NULL,
        TRUE, 0),
       ('6yfrd4', 'https://www.sivalabs.in/langchain4j-retrieval-augmented-generation-tutorial', 1,
        TIMESTAMP '2024-07-28', NULL, FALSE, 0),
       ('r5t4tt', 'https://www.sivalabs.in/langchain4j-ai-services-tutorial', 1, TIMESTAMP '2024-07-29', NULL, FALSE,
        0),

       ('ffr4rt', 'https://www.sivalabs.in/generative-ai-conversations-using-langchain4j-chat-memory', 1,
        TIMESTAMP '2024-08-10', NULL, FALSE, 0),
       ('9oui7u', 'https://www.sivalabs.in/getting-started-with-generative-ai-using-java-langchain4j-openai-ollama', 1,
        TIMESTAMP '2024-08-11', NULL, FALSE, 0),
       ('cvbg5t', 'https://www.sivalabs.in/go-for-java-springboot-developers', 1, TIMESTAMP '2024-08-12', NULL, FALSE,
        0),
       ('nm6ytf', 'https://www.sivalabs.in/spring-boot-jooq-tutorial-fetching-many-to-many-associations', 1,
        TIMESTAMP '2024-08-13', NULL, FALSE, 0),

       ('tt5y6r', 'https://www.sivalabs.in/spring-boot-jooq-tutorial-fetching-one-to-many-associations', 1,
        TIMESTAMP '2024-08-14', NULL, FALSE, 0),
       ('fgghty', 'https://www.sivalabs.in/spring-boot-jooq-tutorial-fetching-one-to-one-associations', 1,
        TIMESTAMP '2024-08-15', NULL, FALSE, 0),
       ('f45tre', 'https://www.sivalabs.in/spring-boot-jooq-tutorial-crud-operations', 1, TIMESTAMP '2024-08-16', NULL,
        FALSE, 0),
       ('54rt54', 'https://www.sivalabs.in/spring-boot-jooq-tutorial-getting-started', 1, TIMESTAMP '2024-08-17', NULL,
        FALSE, 0)
;
