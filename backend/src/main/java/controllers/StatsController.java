package com.viaappia.incidentsapi.controllers;

import com.viaappia.incidentsapi.dtos.StatsResponseDTO;
import com.viaappia.incidentsapi.enums.Prioridade;
import com.viaappia.incidentsapi.enums.Status;
import com.viaappia.incidentsapi.services.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

    private final IncidentService incidentService;

    public StatsController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping("/stats/incidents")
    public ResponseEntity<StatsResponseDTO> obterStats(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Prioridade prioridade,
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(incidentService.obterStats(status, prioridade, q));
    }
}