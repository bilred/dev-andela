package dev.andela.assessment1.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public record ErrorResponseDto(@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime timestamp, String error) {
    public ErrorResponseDto {
        Objects.requireNonNull(error);
    }
}

