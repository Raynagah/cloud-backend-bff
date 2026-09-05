package com.backend.bff.controller;

import com.backend.bff.dto.CarritoEnriquecidoDTO;
import com.backend.bff.dto.ItemCarritoRequestDTO;
import com.backend.bff.service.OrquestadorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bff/carritos")
public class BffController {

    private final OrquestadorService orquestadorService;

    public BffController(OrquestadorService orquestadorService) {
        this.orquestadorService = orquestadorService;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<CarritoEnriquecidoDTO> obtenerCarritoCompleto(@PathVariable String usuarioId) {
        return ResponseEntity.ok(orquestadorService.obtenerCarritoCompleto(usuarioId));
    }

    @PostMapping("/{usuarioId}/items")
    public ResponseEntity<CarritoEnriquecidoDTO> agregarItem(
            @PathVariable String usuarioId,
            @Valid @RequestBody ItemCarritoRequestDTO requestDTO) {
        CarritoEnriquecidoDTO respuesta = orquestadorService.agregarItemYEnriquecer(usuarioId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}