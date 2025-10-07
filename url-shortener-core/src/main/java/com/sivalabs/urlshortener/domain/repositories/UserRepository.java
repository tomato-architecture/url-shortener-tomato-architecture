package com.sivalabs.urlshortener.domain.repositories;

import com.sivalabs.urlshortener.domain.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
