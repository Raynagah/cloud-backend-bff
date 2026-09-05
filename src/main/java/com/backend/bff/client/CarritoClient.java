package com.backend.bff.client;

import com.backend.bff.dto.CarritoDTO;
import com.backend.bff.dto.ItemCarritoRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "carrito-client", url = "${microservicios.carrito.url}/api/v1/carritos")
public interface CarritoClient {

    @GetMapping("/{usuarioId}")
    CarritoDTO obtenerCarritoActivo(@PathVariable("usuarioId") String usuarioId);

    @PostMapping("/{usuarioId}/items")
    CarritoDTO agregarItem(
            @PathVariable("usuarioId") String usuarioId, 
            @RequestBody ItemCarritoRequestDTO dto
    );

    @DeleteMapping("/{usuarioId}")
    void vaciarCarrito(@PathVariable("usuarioId") String usuarioId);
}