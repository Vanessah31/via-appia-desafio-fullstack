package com.viaappia.incidentsapi.dtos;

import com.viaappia.incidentsapi.enums.Prioridade;
import com.viaappia.incidentsapi.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record IncidentResponseDTO(
        UUID id,
        String titulo,
        String descricao,
        Prioridade prioridade,
        Status status,
        String responsavelEmail,
        List<String> tags,
        LocalDateTime dataAbertura,
        LocalDateTime dataAtualizacao
) {
}