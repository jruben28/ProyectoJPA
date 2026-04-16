/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.presentacion;

import BOs.ProductoBO;

import com.dtos.ProductoDTO;


import javafx.scene.control.Alert;
import java.util.List;

/**
 * Controlador para la gestión de productos.
 * Actúa como intermediario entre la capa de presentación y la capa de negocio.
 * 
 * @author icoro
 */
public class ControllerProducto {

    private final ProductoBO productoBO;
    private final IngredienteBO ingredienteBO;
    private final BuscadorProductosFrm buscadorProductos;

    /**
     * Constructor del controlador
     */
    public ControllerProducto() {
        this.productoBO = new ProductoBO();
        this.ingredienteBO = new IngredienteBO();
        this.buscadorProductos = new BuscadorProductosFrm(this);
    }

    /**
     * Obtiene la vista principal del buscador de productos
     * @return VBox con la interfaz del buscador
     */
    public BuscadorProductosFrm getBuscadorProductos() {
        return buscadorProductos;
    }

    /**
     * Busca productos según criterios de búsqueda
     * @param texto Texto a buscar en el nombre del producto
     * @param categoria Categoría del producto (TODOS, PLATILLO, BEBIDA, POSTRE)
     * @return Lista de productos que coinciden con los criterios
     */
    public List<ProductoDTO> buscarProductos(String texto, String categoria) {
        try {
            if (categoria == null || categoria.equals("TODOS")) {
                // Si la categoría es "TODOS" o null, buscar sin filtro de categoría
                if (texto == null || texto.isEmpty()) {
                    return productoBO.obtenerTodos();
                } else {
                    return productoBO.buscarPorNombre(texto);
                }
            } else {
                // Buscar con filtro de categoría
                if (texto == null || texto.isEmpty()) {
                    return productoBO.buscarPorCategoria(categoria);
                } else {
                    return productoBO.buscarPorNombreYCategoria(texto, categoria);
                }
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al buscar productos: " + e.getMessage());
            throw new RuntimeException("Error al buscar productos", e);
        }
    }

    /**
     * Registra un nuevo producto en el sistema
     * @param dto DTO con los datos del producto a registrar
     */
    public void registrarProducto(ProductoDTO dto) {
        try {
            validarProducto(dto);
            productoBO.registrar(dto);
            
            // Refrescar la tabla del buscador
            actualizarVistaBuscador();
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar producto: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza los datos de un producto existente
     * @param dto DTO con los datos actualizados del producto
     */
    public void actualizarProducto(ProductoDTO dto) {
        try {
            validarProducto(dto);
            productoBO.actualizar(dto);
            
            // Refrescar la tabla del buscador
            actualizarVistaBuscador();
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar producto: " + e.getMessage(), e);
        }
    }

    /**
     * Cambia el estado (activo/inactivo) de un producto
     * @param idProducto ID del producto a cambiar de estado
     */
    public void cambiarEstado(Long idProducto) {
        try {
            ProductoDTO producto = productoBO.obtenerPorId(idProducto);
            if (producto != null) {
                producto.setActivo(!producto.getActivo());
                productoBO.actualizar(producto);
                
                String mensaje = producto.getActivo() ? "Producto activado" : "Producto desactivado";
                mostrarAlerta(Alert.AlertType.INFORMATION, mensaje + " exitosamente.");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cambiar estado: " + e.getMessage());
        }
    }

    /**
     * Muestra el formulario de registro/edición de producto
     * @param producto Producto a editar (null si es un registro nuevo)
     */
    public void mostrarRegistro(ProductoDTO producto) {
        try {
            RegistroProductoFrm formulario = new RegistroProductoFrm(this, producto);
            formulario.showAndWait();
            
            // Después de cerrar el formulario, refrescar la vista
            actualizarVistaBuscador();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al abrir formulario: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los ingredientes disponibles en el sistema
     * @return Lista de ingredientes
     */
    public List<IngredienteDTO> obtenerTodosLosIngredientes() {
        try {
            return ingredienteBO.obtenerTodos();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar ingredientes: " + e.getMessage());
            throw new RuntimeException("Error al obtener ingredientes", e);
        }
    }

    /**
     * Valida que los datos del producto sean correctos antes de guardar
     * @param dto DTO del producto a validar
     * @throws IllegalArgumentException si hay errores de validación
     */
    private void validarProducto(ProductoDTO dto) throws IllegalArgumentException {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        
        if (dto.getPrecio() == null || dto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        
        if (dto.getTipo() == null || dto.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar una categoría");
        }
        
        // Validar que el tipo sea uno de los permitidos
        if (!dto.getTipo().equals("PLATILLO") && 
            !dto.getTipo().equals("BEBIDA") && 
            !dto.getTipo().equals("POSTRE")) {
            throw new IllegalArgumentException("Categoría no válida");
        }
    }

    /**
     * Actualiza los datos mostrados en la vista del buscador
     */
    private void actualizarVistaBuscador() {
        // Esta llamada refrescará la tabla con los datos actualizados
        if (buscadorProductos != null) {
            // El buscador se actualizará cuando se cierre el formulario
            // ya que el método buscarProductos() se llama desde el formulario
        }
    }

    /**
     * Muestra una alerta al usuario
     * @param tipo Tipo de alerta
     * @param mensaje Mensaje a mostrar
     */
    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}