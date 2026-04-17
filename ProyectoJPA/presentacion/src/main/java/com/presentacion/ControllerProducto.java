package com.presentacion;

import BOs.IngredienteBO;
import BOs.ProductoBO;
import com.dtos.IngredienteDTO;
import com.dtos.ProductoDTO;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.util.List;

/**
 * Controlador para la gestión de productos.
 * Actúa como intermediario entre la capa de presentación y la capa de negocio.
 * @author icoro
 */
public class ControllerProducto {

    private final ProductoBO productoBO;
    private final IngredienteBO ingredienteBO;
    private final GestionProductosFrm gestionProductos; 
    private final Stage escenario;

    public ControllerProducto(Stage escenario) {
        this.escenario = escenario;
        this.productoBO = new ProductoBO();
        this.ingredienteBO = new IngredienteBO();
        this.gestionProductos = new GestionProductosFrm(this); 
    }

    public void mostrarPrincipal() {
        Scene scene = new Scene(gestionProductos.getRoot(), 900, 600); 
        escenario.setTitle("Gestión de Productos");
        escenario.setScene(scene);
        escenario.show();
    }

    /**
     * Obtiene la vista principal de la gestión de productos
     */
    public GestionProductosFrm getGestionProductos() {
        return gestionProductos;
    }

    /**
     * Busca productos según criterios de búsqueda
     */
    public List<ProductoDTO> buscarProductos(String texto, String categoria) {
        try {
            return productoBO.buscarProductos(texto, categoria);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al buscar productos: " + e.getMessage());
            throw new RuntimeException("Error al buscar productos", e);
        }
    }

    /**
     * Registra un nuevo producto en el sistema
     */
    public void registrarProducto(ProductoDTO dto) {
        try {
            validarProducto(dto);
            productoBO.guardarProducto(dto);
            actualizarVista(); 
            mostrarAlerta(Alert.AlertType.INFORMATION, "Producto guardado con éxito.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    /**
     * Actualiza los datos de un producto existente
     */
    public void actualizarProducto(ProductoDTO dto) {
        try {
            validarProducto(dto);
            productoBO.actualizarProducto(dto);
            actualizarVista();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Producto actualizado con éxito.");
        } catch (Exception e) {
             mostrarAlerta(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    /**
     * Cambia el estado (activo/inactivo) de un producto
     */
    public void cambiarEstado(Long idProducto) {
        try {
            productoBO.cambiarEstado(idProducto);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Estado del producto actualizado exitosamente.");
            actualizarVista();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cambiar estado: " + e.getMessage());
        }
    }

    /**
     * Muestra el formulario de registro/edición de producto
     */
    public void mostrarRegistro(ProductoDTO producto) {
        try {
            RegistroProductoFrm formulario = new RegistroProductoFrm(this, producto);
            formulario.showAndWait();
            actualizarVista();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al abrir formulario: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los ingredientes disponibles en el sistema
     */
    public List<IngredienteDTO> obtenerTodosLosIngredientes() {
        try {
            return ingredienteBO.obtenerIngredienteTodos(); 
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar ingredientes: " + e.getMessage());
            throw new RuntimeException("Error al obtener ingredientes", e);
        }
    }

    /**
     * Valida que los datos del producto sean correctos antes de guardar
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
        if (!dto.getTipo().equals("PLATILLO") && 
            !dto.getTipo().equals("BEBIDA") && 
            !dto.getTipo().equals("POSTRE")) {
            throw new IllegalArgumentException("Categoría no válida");
        }
    }

    /**
     * Actualiza los datos mostrados en la vista principal
     */
    private void actualizarVista() {
        if (gestionProductos != null) {
            // gestionProductos.buscarProductos(); // <-- OJO AQUÍ
        }
    }

    /**
     * Muestra una alerta al usuario
     */
    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}