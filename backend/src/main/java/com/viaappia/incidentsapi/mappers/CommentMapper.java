package com.viaappia.incidentsapi.mappers;

import com.viaappia.incidentsapi.dtos.CommentResponseDTO;
import com.viaappia.incidentsapi.entities.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentResponseDTO toResponseDTO(Comment comment) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getIncidentId(),
                comment.getAutor(),
                comment.getMensagem(),
                comment.getDataCriacao()
        );
    }
}