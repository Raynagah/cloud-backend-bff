package com.backend.bff.controller;

import com.backend.bff.client.AnalyticsClient;
import com.backend.bff.dto.RecomendacionDTO;

import feign.FeignException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bff/analitica")
public class BffAnaliticaController {

    private static final Logger LOG =
            LoggerFactory.getLogger(BffAnaliticaController.class);

    private final AnalyticsClient analyticsClient;

    public BffAnaliticaController(AnalyticsClient analyticsClient) {
        this.analyticsClient = analyticsClient;
    }

    // Recomendaciones personalizadas
    @GetMapping("/recomendaciones")
    public ResponseEntity<List<RecomendacionDTO>> recomendaciones(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) Integer max) {

        try {
            return ResponseEntity.ok(
                    analyticsClient.recomendaciones(productoId, max)
            );

        } catch (Exception e) {
            LOG.warn("No se pudieron obtener recomendaciones: {}",
                    e.getMessage());

            return ResponseEntity.ok(List.of());
        }
    }

    // Metricas globales para administradores
    @GetMapping("/metricas")
    public ResponseEntity<?> metricas() {
        try {
            return ResponseEntity.ok(
                    analyticsClient.metricas()
            );

        } catch (FeignException e) {
            return propagar(e);
        }
    }

    // Historial reciente del usuario autenticado
    @GetMapping("/eventos/recientes")
    public ResponseEntity<?> eventosRecientes(
            @RequestParam(defaultValue = "20") int limite) {

        try {
            return ResponseEntity.ok(
                    analyticsClient.eventosRecientes(limite)
            );

        } catch (FeignException e) {
            return propagar(e);
        }
    }

    // Propagar errores del microservicio
    private ResponseEntity<String> propagar(FeignException e) {

        LOG.warn("ms-analytics respondio con error {}", e.status());

        HttpStatus estado = e.status() > 0
                ? HttpStatus.resolve(e.status())
                : HttpStatus.BAD_GATEWAY;

        if (estado == null) {
            estado = HttpStatus.BAD_GATEWAY;
        }

        String cuerpo = e.contentUTF8();

        if (cuerpo == null || cuerpo.isBlank()) {
            cuerpo = "{\"message\":\"Error comunicando con Analytics\"}";
        }

        return ResponseEntity
                .status(estado)
                .contentType(MediaType.APPLICATION_JSON)
                .body(cuerpo);
    }
}