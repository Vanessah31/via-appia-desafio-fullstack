package com.viaappia.incidentsapi.dtos;

import com.viaappia.incidentsapi.enums.Prioridade;
import com.viaappia.incidentsapi.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record IncidentRequestDTO(
        @NotBlank
        @Size(min = 5, max = 120)
        String titulo,

        @Size(max = 5000)
        String descricao,

        @NotNull
        Prioridade prioridade,

        @NotNull
        Status status,

        @NotBlank
        @Email
        String responsavelEmail,

        List<String> tags
) {
}