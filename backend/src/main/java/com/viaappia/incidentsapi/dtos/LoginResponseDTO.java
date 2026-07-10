package com.viaappia.incidentsapi.dtos;

public record LoginResponseDTO(
        String token,
        String email,
        String role
) {
}