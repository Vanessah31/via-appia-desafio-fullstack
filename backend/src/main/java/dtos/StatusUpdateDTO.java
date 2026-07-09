package com.viaappia.incidentsapi.dtos;

import com.viaappia.incidentsapi.enums.Status;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateDTO(
        @NotNull
        Status status
) {
}