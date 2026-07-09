package com.viaappia.incidentsapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequestDTO(
        @NotBlank
        @Size(max = 120)
        String autor,

        @NotBlank
        @Size(min = 1, max = 2000)
        String mensagem
) {
}