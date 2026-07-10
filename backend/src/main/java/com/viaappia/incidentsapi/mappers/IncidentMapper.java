package com.viaappia.incidentsapi.mappers;

import com.viaappia.incidentsapi.dtos.IncidentRequestDTO;
import com.viaappia.incidentsapi.dtos.IncidentResponseDTO;
import com.viaappia.incidentsapi.entities.Incident;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IncidentMapper {

    public IncidentResponseDTO toResponseDTO(Incident incident) {
        return new IncidentResponseDTO(
                incident.getId(),
                incident.getTitulo(),
                incident.getDescricao(),
                incident.getPrioridade(),
                incident.getStatus(),
                incident.getResponsavelEmail(),
                tagsStringToList(incident.getTags()),
                incident.getDataAbertura(),
                incident.getDataAtualizacao()
        );
    }

    public void copyToEntity(IncidentRequestDTO dto, Incident incident) {
        incident.setTitulo(dto.titulo());
        incident.setDescricao(dto.descricao());
        incident.setPrioridade(dto.prioridade());
        incident.setStatus(dto.status());
        incident.setResponsavelEmail(dto.responsavelEmail());
        incident.setTags(tagsListToString(dto.tags()));
    }

    private String tagsListToString(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        return tags.stream()
                .filter(t -> t != null && !t.isBlank())
                .map(t -> t.trim().toLowerCase())
                .distinct()
                .collect(Collectors.joining(","));
    }

    private List<String> tagsStringToList(String tags) {
        if (tags == null || tags.isBlank()) {
            return List.of();
        }
        return Arrays.asList(tags.split(","));
    }
}