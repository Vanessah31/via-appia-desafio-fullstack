package com.viaappia.incidentsapi.specifications;

import com.viaappia.incidentsapi.entities.Incident;
import com.viaappia.incidentsapi.enums.Prioridade;
import com.viaappia.incidentsapi.enums.Status;
import org.springframework.data.jpa.domain.Specification;

public class IncidentSpecification {

    public static Specification<Incident> buildIncidentFilter(Status status, Prioridade prioridade, String q) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (status != null) {
                predicates = cb.and(predicates, cb.equal(root.get("status"), status));
            }

            if (prioridade != null) {
                predicates = cb.and(predicates, cb.equal(root.get("prioridade"), prioridade));
            }

            if (q != null && !q.isBlank()) {
                String likePattern = "%" + q.toLowerCase() + "%";
                predicates = cb.and(predicates, cb.or(
                        cb.like(cb.lower(root.get("titulo")), likePattern),
                        cb.like(cb.lower(root.get("descricao")), likePattern)
                ));
            }

            return predicates;
        };
    }
}