package com.example.demo.application.dto;

import java.util.List;

public record PaginacionResponseDto<T>(
    List<T> content,
    int currentPage,
    int totalPages,
    long totalItems){
}
