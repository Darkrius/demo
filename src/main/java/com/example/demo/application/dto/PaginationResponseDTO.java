package com.example.demo.application.dto;

import java.util.List;

public record PaginationResponseDTO<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
}
