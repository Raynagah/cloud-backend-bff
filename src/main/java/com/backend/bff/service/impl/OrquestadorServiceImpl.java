package com.backend.bff.service.impl;

import com.backend.bff.client.CarritoClient;
import com.backend.bff.client.ProductoClient;
import com.backend.bff.dto.*;
import com.backend.bff.service.OrquestadorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrquestadorServiceImpl implements OrquestadorService {

    private final CarritoClient carritoClient;
    private final ProductoClient productoClient;

    public OrquestadorServiceImpl(CarritoClient carritoClient, ProductoClient productoClient) {
        this.carritoClient = carritoClient;
        this.productoClient = productoClient;
    }

    @Override
    public CarritoEnriquecidoDTO obtenerCarritoCompleto(String usuarioId) {
        CarritoDTO carrito = carritoClient.obtenerCarritoActivo(usuarioId);
        return enriquecerCarrito(carrito);
    }

    @Override
    public CarritoEnriquecidoDTO agregarItemYEnriquecer(String usuarioId, ItemCarritoRequestDTO requestDTO) {
        CarritoDTO carritoActualizado = carritoClient.agregarItem(usuarioId, requestDTO);
        return enriquecerCarrito(carritoActualizado);
    }

    private CarritoEnriquecidoDTO enriquecerCarrito(CarritoDTO carrito) {
        List<ItemCarritoEnriquecidoDTO> itemsEnriquecidos = carrito.items().stream()
                .map(this::enriquecerItem)
                .toList();

        return new CarritoEnriquecidoDTO(
                carrito.id(),
                carrito.usuarioId(),
                carrito.fechaCreacion(),
                carrito.total(),
                carrito.estado(),
                itemsEnriquecidos
        );
    }

    private ItemCarritoEnriquecidoDTO enriquecerItem(ItemCarritoDTO item) {
        // Consultamos ms-producto utilizando OpenFeign
        ProductoDTO producto = productoClient.obtenerProductoPorId(item.productoId());

        String nombre = (producto != null) ? producto.nombre() : "Producto no disponible";
        String descripcion = (producto != null) ? producto.descripcion() : "Sin descripción";

        return new ItemCarritoEnriquecidoDTO(
                item.id(),
                item.productoId(),
                nombre,
                descripcion,
                item.cantidad(),
                item.precioUnitario(),
                item.subtotal()
        );
    }
}