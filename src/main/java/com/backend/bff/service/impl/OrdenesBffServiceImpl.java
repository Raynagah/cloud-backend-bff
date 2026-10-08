package com.backend.bff.service.impl;

import com.backend.bff.client.OrdenesClient;
import com.backend.bff.dto.OrdenRequestDTO;
import com.backend.bff.service.OrdenesBffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrdenesBffServiceImpl implements OrdenesBffService {

    private final OrdenesClient ordenesClient;

    @Override
    public Object realizarCheckout(OrdenRequestDTO requestDTO) {
        return ordenesClient.crearOrden(requestDTO);
    }

    @Override
    public Object obtenerMisOrdenes() {
        return ordenesClient.obtenerOrdenesPorUsuario();
    }

    @Override
    public Object obtenerOrdenPorId(Long id) {
        return ordenesClient.obtenerOrdenPorId(id);
    }
}