package com.presentacion;

import BOs.IngredienteBO;
import BOs.ProductoBO;
import com.dtos.IngredienteDTO;
import com.dtos.ProductoDTO;
import javafx.scene.control.Alert;
import java.util.List;

/**
 * Controlador para la gestión de productos.
 * Actúa como intermediario entre la capa de presentación y la capa de negocio.
 * * @author icoro
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
            // Tu BO ya maneja toda la lógica de los filtros internamente
            return productoBO.buscarProductos(texto, categoria);
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
            // Corregido: Llamando al método correcto del BO
            productoBO.guardarProducto(dto);
            
            // Refrescar la tabla del buscador
            actualizarVistaBuscador();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    /**
     * Actualiza los datos de un producto existente
     * @param dto DTO con los datos actualizados del producto
     */
    public void actualizarProducto(ProductoDTO dto) {
        try {
            validarProducto(dto);
            // Corregido: Llamando al método correcto del BO
            productoBO.actualizarProducto(dto);
            
            // Refrescar la tabla del buscador
            actualizarVistaBuscador();
        } catch (Exception e) {
             mostrarAlerta(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    /**
     * Cambia el estado (activo/inactivo) de un producto
     * @param idProducto ID del producto a cambiar de estado
     */
    public void cambiarEstado(Long idProducto) {
        try {
            // Tu BO ya hace todo el proceso internamente (buscar, cambiar y guardar)
            productoBO.cambiarEstado(idProducto);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Estado del producto actualizado exitosamente.");
            
            // Refrescar la tabla para ver el cambio
            actualizarVistaBuscador();
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
  /**
     * Obtiene todos los ingredientes disponibles en el sistema
     * @return Lista de ingredientes
     */
    public List<IngredienteDTO> obtenerTodosLosIngredientes() {
        try {
            // Corregido al método exacto que hizo tu compañero en IngredienteBO
            return ingredienteBO.obtenerIngredienteTodos(); 
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
        // Lógica para decirle a tu BuscadorProductosFrm que vuelva a cargar la tabla.
        // Si tu formulario de buscador tiene un método como 'cargarDatos()', llámalo aquí.
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