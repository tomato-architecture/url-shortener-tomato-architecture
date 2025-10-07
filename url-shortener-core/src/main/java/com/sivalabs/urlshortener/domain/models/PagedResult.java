package com.sivalabs.urlshortener.domain.models;

import java.util.List;
import org.springframework.data.domain.Page;

public record PagedResult<T>(
        List<T> data,
        int pageNumber,
        int totalPages,
        long totalElements,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious) {

    public static <T> PagedResult<T> from(Page<T> page) {
        return new PagedResult<>(
                page.getContent(),
                page.getNumber() + 1, // to show 1-based page numbering
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious());
    }
}
