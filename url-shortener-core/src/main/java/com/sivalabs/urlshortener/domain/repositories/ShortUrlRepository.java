package com.sivalabs.urlshortener.domain.repositories;

import com.sivalabs.urlshortener.domain.entities.ShortUrl;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    @Query("select su from ShortUrl su left join fetch su.createdBy where su.isPrivate = false")
    Page<ShortUrl> findPublicShortUrls(Pageable pageable);

    boolean existsByShortKey(String shortKey);

    Optional<ShortUrl> findByShortKey(String shortKey);

    Page<ShortUrl> findByCreatedById(Long userId, Pageable pageable);

    @Modifying
    @Query("delete from ShortUrl su where su.id in :ids and su.createdBy.id = :userId")
    int deleteByIdInAndCreatedById(Set<Long> ids, Long userId);

    @Query("select u from ShortUrl u left join fetch u.createdBy")
    Page<ShortUrl> findAllShortUrls(Pageable pageable);
}
