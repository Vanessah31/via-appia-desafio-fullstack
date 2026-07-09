package com.viaappia.incidentsapi.dtos;

import java.util.Map;

public record StatsResponseDTO(
        Map<String, Long> porStatus,
        Map<String, Long> porPrioridade,
        long total
) {
}