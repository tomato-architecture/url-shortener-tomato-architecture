delete from short_urls;
delete from users;

alter sequence users_id_seq restart with 100;
alter sequence short_urls_id_seq restart with 100;

INSERT INTO users (id, email, password, name, role) VALUES
(1, 'admin@gmail.com', '$2a$10$Ewl5M5WzAvGl3./qaT8od.Sz1vj34GkYPMEOnZSpR6351NCRIVr2e', 'Administrator', 'ROLE_ADMIN'),
(2, 'siva@gmail.com', '$2a$10$NTH5YKrxFns/DYNc.qVbfOQpHbMZ/SExTorPBcVO1b2exW4QHljm.', 'Siva', 'ROLE_USER')
;

INSERT INTO short_urls (id, short_key, original_url, created_by, created_at, expires_at, is_private, click_count) VALUES
(1, 'github', 'https://github.com', 1, TIMESTAMP '2024-07-15', NULL, FALSE, 0),
(2, 'stacko', 'https://stackoverflow.com', 1, TIMESTAMP '2024-07-14', NULL, FALSE, 0),
(3, 'mdnmoz', 'https://developer.mozilla.org', 1, TIMESTAMP '2024-07-13', NULL, FALSE, 0),
(4, 'w3scho', 'https://www.w3schools.com', 1, TIMESTAMP '2024-07-12', NULL, TRUE, 0),
(5, 'medium', 'https://medium.com', 2, TIMESTAMP '2024-07-11', TIMESTAMP '2024-08-10', FALSE, 0),
(6, 'devtow', 'https://dev.to', 2, TIMESTAMP '2024-07-10', TIMESTAMP '2024-08-09', FALSE, 0),
(7, 'hackrn', 'https://news.ycombinator.com', 1, TIMESTAMP '2024-07-09', NULL, FALSE, 0),
(8, 'reddit', 'https://www.reddit.com/r/programming', 1, TIMESTAMP '2024-07-08', NULL, FALSE, 0),
(9, 'codeac', 'https://www.codecademy.com', 1, TIMESTAMP '2024-07-07', NULL, FALSE, 0),
(10, 'freeco', 'https://www.freecodecamp.org', 1, TIMESTAMP '2024-07-06', NULL, FALSE, 0),
(11, 'leetco', 'https://leetcode.com', 1, TIMESTAMP '2024-07-05', NULL, FALSE, 0),
(12, 'spring', 'https://spring.io', 1, TIMESTAMP '2024-07-04', NULL, TRUE, 0),
(13, 'javdoc', 'https://docs.oracle.com/en/java', 1, TIMESTAMP '2024-07-03', NULL, FALSE, 0),
(14, 'pytorg', 'https://www.python.org', 1, TIMESTAMP '2024-07-02', NULL, FALSE, 0),
(15, 'npmjsc', 'https://www.npmjs.com', 1, TIMESTAMP '2024-07-01', NULL, TRUE, 0),
(16, 'docker', 'https://www.docker.com', 1, TIMESTAMP '2024-03-15', NULL, FALSE, 0),
(17, 'kubern', 'https://kubernetes.io', 1, TIMESTAMP '2024-03-10', NULL, FALSE, 0),
(18, 'awsdoc', 'https://docs.aws.amazon.com', 1, TIMESTAMP '2024-02-05', NULL, TRUE, 0),
(19, 'azured', 'https://docs.microsoft.com/en-us/azure', 1, TIMESTAMP '2024-02-02', NULL, FALSE, 0),
(20, 'gcpdoc', 'https://cloud.google.com/docs', 1, TIMESTAMP '2024-01-15', NULL, FALSE, 0);
