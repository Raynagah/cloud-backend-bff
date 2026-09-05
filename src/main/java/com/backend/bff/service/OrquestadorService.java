package com.backend.bff.service;

import com.backend.bff.dto.CarritoEnriquecidoDTO;
import com.backend.bff.dto.ItemCarritoRequestDTO;

public interface OrquestadorService {
    CarritoEnriquecidoDTO obtenerCarritoCompleto(String usuarioId);
    CarritoEnriquecidoDTO agregarItemYEnriquecer(String usuarioId, ItemCarritoRequestDTO requestDTO);
}