package com.backend.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.backend.bff.dto.NotificacionDTO;
import java.util.List;

// 1. Corregida la variable para que lea la del application.yml
@FeignClient(name = "notificacion-client", url = "${microservicios.notificaciones.url}/api/v1/notificaciones")
public interface NotificacionClient {

    // 2. Añadido el /v1 a las rutas
    @GetMapping("/usuario/{correo}")
    List<NotificacionDTO> obtenerPorUsuario(@PathVariable("correo") String correo);

    @DeleteMapping("/{id}")
    void eliminarNotificacion(@PathVariable("id") Long id);

    @PutMapping("/{id}/leer")
    NotificacionDTO marcarComoLeida(@PathVariable("id") Long id);
}