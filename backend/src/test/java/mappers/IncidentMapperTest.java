package com.viaappia.incidentsapi.mappers;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IncidentMapperTest {

    private final IncidentMapper mapper = new IncidentMapper();

    @Test
    void deveRetornarListaVaziaQuandoTagsForNull() {
        List<String> resultado = mapper.normalizeTags(null);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveRemoverNulosEVaziosDaLista() {
        List<String> resultado = mapper.normalizeTags(Arrays.asList("bug", null, "", "  "));
        assertEquals(List.of("bug"), resultado);
    }

    @Test
    void deveAplicarTrimEMinusculas() {
        List<String> resultado = mapper.normalizeTags(List.of("  Bug ", "URGENTE"));
        assertEquals(List.of("bug", "urgente"), resultado);
    }

    @Test
    void deveRemoverDuplicatas() {
        List<String> resultado = mapper.normalizeTags(List.of("bug", "Bug", " bug "));
        assertEquals(1, resultado.size());
        assertEquals("bug", resultado.get(0));
    }

    @Test
    void devePreservarOrdemDeInsercao() {
        List<String> resultado = mapper.normalizeTags(List.of("login", "api", "bug"));
        assertEquals(List.of("login", "api", "bug"), resultado);
    }
}