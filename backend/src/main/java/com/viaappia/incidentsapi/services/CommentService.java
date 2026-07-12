package com.viaappia.incidentsapi.services;

import com.viaappia.incidentsapi.dtos.CommentRequestDTO;
import com.viaappia.incidentsapi.dtos.CommentResponseDTO;
import com.viaappia.incidentsapi.entities.Comment;
import com.viaappia.incidentsapi.entities.Incident;
import com.viaappia.incidentsapi.exceptions.ResourceNotFoundException;
import com.viaappia.incidentsapi.mappers.CommentMapper;
import com.viaappia.incidentsapi.repositories.CommentRepository;
import com.viaappia.incidentsapi.repositories.IncidentRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final IncidentRepository incidentRepository;
    private final CommentMapper commentMapper;
    private final IncidentService incidentService;

    public CommentService(CommentRepository commentRepository,
                          IncidentRepository incidentRepository,
                          CommentMapper commentMapper,
                          IncidentService incidentService) {
        this.commentRepository = commentRepository;
        this.incidentRepository = incidentRepository;
        this.commentMapper = commentMapper;
        this.incidentService = incidentService;
    }

    @Cacheable(value = "commentsByIncident", key = "#incidentId")
    public List<CommentResponseDTO> listarPorIncident(UUID incidentId) {
        return commentRepository.findByIncidentIdOrderByDataCriacaoAsc(incidentId)
                .stream()
                .map(commentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(value = "commentsByIncident", key = "#incidentId"),
            @CacheEvict(value = "incidentById", key = "#incidentId"),
            @CacheEvict(value = "incidents", allEntries = true)
    })
    public CommentResponseDTO criar(UUID incidentId, CommentRequestDTO dto) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident não encontrado: " + incidentId));

        Comment comment = new Comment();
        comment.setId(UUID.randomUUID());
        comment.setIncidentId(incidentId);
        comment.setAutor(dto.autor());
        comment.setMensagem(dto.mensagem());
        comment.setDataCriacao(LocalDateTime.now());

        comment = commentRepository.save(comment);

        // Reutilizando touchUpdate do IncidentService (DRY - item 11 do documento)
        incidentService.touchUpdate(incident);
        incidentRepository.save(incident);

        return commentMapper.toResponseDTO(comment);
    }
}