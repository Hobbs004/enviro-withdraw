package com.enviro.assessment.junior.nhletelo.dto;

import lombok.*;

/**
 * Generic API response wrapper.
 * Ensures consistent JSON structure across all endpoints: { success, message, data }.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponseDTO<T> ok(String message, T data) {
        return ApiResponseDTO.<T>builder().success(true).message(message).data(data).build();
    }

    public static <T> ApiResponseDTO<T> error(String message) {
        return ApiResponseDTO.<T>builder().success(false).message(message).build();
    }
}
