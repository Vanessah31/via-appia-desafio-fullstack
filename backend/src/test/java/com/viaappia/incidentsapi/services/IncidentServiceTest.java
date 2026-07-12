package com.viaappia.incidentsapi.services;

import com.viaappia.incidentsapi.entities.Incident;
import com.viaappia.incidentsapi.mappers.IncidentMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IncidentServiceTest {

    // touchUpdate não depende do banco nem do mapper, então podemos
    // passar null nessas dependências só para este teste específico.
    private final IncidentService incidentService = new IncidentService(null, new IncidentMapper());

    @Test
    void touchUpdateDeveAtualizarDataDeAtualizacao() {
        Incident incident = new Incident();
        LocalDateTime dataAntiga = LocalDateTime.now().minusDays(1);
        incident.setDataAtualizacao(dataAntiga);

        incidentService.touchUpdate(incident);

        assertNotNull(incident.getDataAtualizacao());
        assertTrue(incident.getDataAtualizacao().isAfter(dataAntiga));
    }
}