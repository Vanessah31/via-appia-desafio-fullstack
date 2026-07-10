package com.viaappia.incidentsapi.repositories;

import com.viaappia.incidentsapi.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByIncidentIdOrderByDataCriacaoAsc(UUID incidentId);
}