package com.viaappia.incidentsapi.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponseDTO(
        UUID id,
        UUID incidentId,
        String autor,
        String mensagem,
        LocalDateTime dataCriacao
) {
}