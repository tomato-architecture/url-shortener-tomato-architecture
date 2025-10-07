package com.sivalabs.urlshortener.domain.services;

import static com.sivalabs.urlshortener.domain.services.RandomUtils.generateRandomShortKey;
import static java.time.temporal.ChronoUnit.*;

import com.sivalabs.urlshortener.CoreProperties;
import com.sivalabs.urlshortener.domain.entities.ShortUrl;
import com.sivalabs.urlshortener.domain.exceptions.BadRequestException;
import com.sivalabs.urlshortener.domain.exceptions.InvalidURLException;
import com.sivalabs.urlshortener.domain.models.CreateShortUrlCmd;
import com.sivalabs.urlshortener.domain.models.PagedResult;
import com.sivalabs.urlshortener.domain.models.ShortUrlDto;
import com.sivalabs.urlshortener.domain.repositories.ShortUrlRepository;
import com.sivalabs.urlshortener.domain.repositories.UserRepository;
import java.time.Instant;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final EntityMapper entityMapper;
    private final CoreProperties properties;
    private final UserRepository userRepository;

    private static final int MAX_EXPIRATION_DAYS = 365;

    public ShortUrlService(
            ShortUrlRepository shortUrlRepository,
            EntityMapper entityMapper,
            CoreProperties properties,
            UserRepository userRepository) {
        this.shortUrlRepository = shortUrlRepository;
        this.entityMapper = entityMapper;
        this.properties = properties;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public PagedResult<ShortUrlDto> findPublicShortUrls(int pageNo, int pageSize) {
        Pageable pageable = getPageable(pageNo, pageSize);
        Page<ShortUrlDto> shortUrlDtoPage =
                shortUrlRepository.findPublicShortUrls(pageable).map(entityMapper::toShortUrlDto);
        return PagedResult.from(shortUrlDtoPage);
    }

    @Transactional(readOnly = true)
    public PagedResult<ShortUrlDto> findAllShortUrls(int page, int pageSize) {
        Pageable pageable = getPageable(page, pageSize);
        var shortUrlsPage = shortUrlRepository.findAllShortUrls(pageable).map(entityMapper::toShortUrlDto);
        return PagedResult.from(shortUrlsPage);
    }

    @Transactional(readOnly = true)
    public PagedResult<ShortUrlDto> findUserShortUrls(Long userId, int page, int pageSize) {
        Pageable pageable = getPageable(page, pageSize);
        var shortUrlsPage =
                shortUrlRepository.findByCreatedById(userId, pageable).map(entityMapper::toShortUrlDto);
        return PagedResult.from(shortUrlsPage);
    }

    @Transactional
    public void deleteUserShortUrls(Set<Long> ids, Long userId) {
        if (ids != null && !ids.isEmpty() && userId != null) {
            int count = shortUrlRepository.deleteByIdInAndCreatedById(ids, userId);
            if (count != ids.size()) {
                throw new AccessDeniedException("You don't have permission to delete the given short urls");
            }
        }
    }

    private Pageable getPageable(int page, int size) {
        page = page > 1 ? page - 1 : 0;
        return PageRequest.of(page, size, Sort.Direction.DESC, "id");
    }

    @Transactional
    public ShortUrlDto createShortUrl(CreateShortUrlCmd cmd) {
        var normalizedOriginalUrl = normalizeOriginalUrl(cmd.originalUrl());
        if (properties.validateOriginalUrl()) {
            boolean urlExists = UrlExistenceValidator.isUrlExists(normalizedOriginalUrl);
            if (!urlExists) {
                throw InvalidURLException.of(normalizedOriginalUrl);
            }
        }
        Integer expirationDays = resolveExpirationDays(cmd.expirationInDays());
        var shortKey = generateUniqueShortKey();
        var shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(normalizedOriginalUrl);
        shortUrl.setShortKey(shortKey);
        if (cmd.userId() == null) {
            shortUrl.setCreatedBy(null);
            shortUrl.setIsPrivate(false);
            shortUrl.setExpiresAt(Instant.now().plus(properties.defaultExpirationDays(), DAYS));
        } else {
            shortUrl.setCreatedBy(userRepository.findById(cmd.userId()).orElseThrow());
            shortUrl.setIsPrivate(cmd.isPrivate() != null && cmd.isPrivate());
            shortUrl.setExpiresAt(expirationDays != null ? Instant.now().plus(expirationDays, DAYS) : null);
        }
        shortUrl.setClickCount(0L);
        shortUrl.setCreatedAt(Instant.now());
        shortUrlRepository.save(shortUrl);
        return entityMapper.toShortUrlDto(shortUrl);
    }

    @Transactional
    public Optional<ShortUrlDto> accessShortUrl(String shortKey, Long userId) {
        Optional<ShortUrl> shortUrlOptional = shortUrlRepository.findByShortKey(shortKey);
        if (shortUrlOptional.isEmpty()) {
            return Optional.empty();
        }
        ShortUrl shortUrl = shortUrlOptional.get();
        if (shortUrl.getExpiresAt() != null && shortUrl.getExpiresAt().isBefore(Instant.now())) {
            return Optional.empty();
        }
        if (shortUrl.getIsPrivate() != null
                && shortUrl.getIsPrivate()
                && shortUrl.getCreatedBy() != null
                && !Objects.equals(shortUrl.getCreatedBy().getId(), userId)) {
            return Optional.empty();
        }
        int updatedRows = shortUrlRepository.incrementClickCountById(shortUrl.getId());
        if (updatedRows == 0) {
            return Optional.empty();
        }
        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        return Optional.of(entityMapper.toShortUrlDto(shortUrl));
    }

    private String generateUniqueShortKey() {
        String shortKey;
        do {
            shortKey = generateRandomShortKey();
        } while (shortUrlRepository.existsByShortKey(shortKey));
        return shortKey;
    }

    @Transactional
    public void deleteUserShortUrls(Set<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            shortUrlRepository.deleteAllByIdInBatch(ids);
        }
    }

    private Integer resolveExpirationDays(Integer expirationInDays) {
        if (expirationInDays == null) {
            return null;
        }
        if (expirationInDays < 1 || expirationInDays > MAX_EXPIRATION_DAYS) {
            throw new BadRequestException("Expiration must be between 1 and " + MAX_EXPIRATION_DAYS + " days");
        }
        return expirationInDays;
    }

    private String normalizeOriginalUrl(String originalUrl) {
        if (originalUrl == null) {
            throw new InvalidURLException("Original URL is required");
        }
        String trimmed = originalUrl.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidURLException("Original URL is required");
        }
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed;
        }
        return "http://" + trimmed;
    }
}
