package com.viaappia.incidentsapi.controllers;

import com.viaappia.incidentsapi.dtos.IncidentRequestDTO;
import com.viaappia.incidentsapi.dtos.IncidentResponseDTO;
import com.viaappia.incidentsapi.services.IncidentService;
import com.viaappia.incidentsapi.enums.Prioridade;
import com.viaappia.incidentsapi.enums.Status;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public ResponseEntity<Page<IncidentResponseDTO>> listar(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Prioridade prioridade,
            @RequestParam(required = false) String q,
            Pageable pageable) {
        return ResponseEntity.ok(incidentService.listar(status, prioridade, q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(incidentService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<IncidentResponseDTO> criar(@Valid @RequestBody IncidentRequestDTO dto) {
        IncidentResponseDTO criado = incidentService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody IncidentRequestDTO dto) {
        return ResponseEntity.ok(incidentService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        incidentService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}