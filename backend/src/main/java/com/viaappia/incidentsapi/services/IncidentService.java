package com.viaappia.incidentsapi.services;

import com.viaappia.incidentsapi.dtos.IncidentRequestDTO;
import com.viaappia.incidentsapi.dtos.IncidentResponseDTO;
import com.viaappia.incidentsapi.dtos.StatsResponseDTO;
import com.viaappia.incidentsapi.entities.Incident;
import com.viaappia.incidentsapi.exceptions.ResourceNotFoundException;
import com.viaappia.incidentsapi.mappers.IncidentMapper;
import com.viaappia.incidentsapi.repositories.IncidentRepository;
import com.viaappia.incidentsapi.enums.Prioridade;
import com.viaappia.incidentsapi.enums.Status;
import com.viaappia.incidentsapi.specifications.IncidentSpecification;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;

    public IncidentService(IncidentRepository incidentRepository, IncidentMapper incidentMapper) {
        this.incidentRepository = incidentRepository;
        this.incidentMapper = incidentMapper;
    }

    @Cacheable(value = "incidents", key = "{#status, #prioridade, #q, #pageable}")
    public Page<IncidentResponseDTO> listar(Status status, Prioridade prioridade, String q, Pageable pageable) {
        Specification<Incident> filtro = IncidentSpecification.buildIncidentFilter(status, prioridade, q);
        Page<Incident> page = incidentRepository.findAll(filtro, pageable);
        return page.map(incidentMapper::toResponseDTO);
    }

    @Cacheable(value = "incidentById", key = "#id")
    public IncidentResponseDTO buscarPorId(UUID id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident não encontrado: " + id));
        return incidentMapper.toResponseDTO(incident);
    }

    @Caching(evict = {
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "stats", allEntries = true)
    })
    public IncidentResponseDTO criar(IncidentRequestDTO dto) {
        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());
        incidentMapper.copyToEntity(dto, incident);

        LocalDateTime agora = LocalDateTime.now();
        incident.setDataAbertura(agora);
        incident.setDataAtualizacao(agora);

        incident = incidentRepository.save(incident);
        return incidentMapper.toResponseDTO(incident);
    }

    @Caching(evict = {
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "incidentById", key = "#id"),
            @CacheEvict(value = "stats", allEntries = true)
    })
    public IncidentResponseDTO atualizar(UUID id, IncidentRequestDTO dto) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident não encontrado: " + id));

        incidentMapper.copyToEntity(dto, incident);
        touchUpdate(incident);

        incident = incidentRepository.save(incident);
        return incidentMapper.toResponseDTO(incident);
    }

    @Caching(evict = {
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "incidentById", key = "#id"),
            @CacheEvict(value = "stats", allEntries = true),
            @CacheEvict(value = "commentsByIncident", key = "#id")
    })
    public void excluir(UUID id) {
        if (!incidentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Incident não encontrado: " + id);
        }
        incidentRepository.deleteById(id);
    }

    @Caching(evict = {
            @CacheEvict(value = "incidents", allEntries = true),
            @CacheEvict(value = "incidentById", key = "#id"),
            @CacheEvict(value = "stats", allEntries = true)
    })
    public IncidentResponseDTO atualizarStatus(UUID id, Status novoStatus) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident não encontrado: " + id));

        incident.setStatus(novoStatus);
        touchUpdate(incident);

        incident = incidentRepository.save(incident);
        return incidentMapper.toResponseDTO(incident);
    }

    @Cacheable(value = "stats", key = "{#status, #prioridade, #q}")
    public StatsResponseDTO obterStats(Status status, Prioridade prioridade, String q) {
        Specification<Incident> filtro = IncidentSpecification.buildIncidentFilter(status, prioridade, q);
        List<Incident> incidents = incidentRepository.findAll(filtro);

        Map<String, Long> porStatus = incidents.stream()
                .collect(Collectors.groupingBy(i -> i.getStatus().name(), Collectors.counting()));

        Map<String, Long> porPrioridade = incidents.stream()
                .collect(Collectors.groupingBy(i -> i.getPrioridade().name(), Collectors.counting()));

        return new StatsResponseDTO(porStatus, porPrioridade, incidents.size());
    }

    public void touchUpdate(Incident incident) {
        incident.setDataAtualizacao(LocalDateTime.now());
    }
}