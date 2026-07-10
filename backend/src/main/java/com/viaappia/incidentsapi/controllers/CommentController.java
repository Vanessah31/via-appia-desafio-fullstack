package com.viaappia.incidentsapi.controllers;

import com.viaappia.incidentsapi.dtos.CommentRequestDTO;
import com.viaappia.incidentsapi.dtos.CommentResponseDTO;
import com.viaappia.incidentsapi.services.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/incidents/{incidentId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> listar(@PathVariable UUID incidentId) {
        return ResponseEntity.ok(commentService.listarPorIncident(incidentId));
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> criar(@PathVariable UUID incidentId,
                                                    @Valid @RequestBody CommentRequestDTO dto) {
        CommentResponseDTO criado = commentService.criar(incidentId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }
}