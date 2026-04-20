package com.presentacion;

import BOs.ComboBO;
import BOs.ProductoBO;
import com.dtos.ComboDTO;
import com.dtos.ProductoDTO;
import excepciones.PersistenciaException;
import interfaces.IComboBO;
import interfaces.IProductoBO;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controlador para el módulo de Combos.
 * Actúa como intermediario entre la vista y la capa de negocio.
 */
public class ControllerCombo {

    private final IComboBO comboBO;
    private final IProductoBO productoBO;
    private final Stage primaryStage;
    private static final String CSS_PATH = "/styles/buscador-clientes.css";

    public ControllerCombo(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.comboBO = new ComboBO();
        this.productoBO = new ProductoBO();
    }

    public void mostrarGestionCombos() {
        ComboFrm frm = new ComboFrm(this);
        Scene scene = new Scene(frm.getRoot(), 1000, 700);
        scene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
        primaryStage.setTitle("Gestión de Combos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void mostrarCreacion() {
        ComboCreacionFrm frm = new ComboCreacionFrm(this);
        frm.construirPantalla();
        Scene scene = new Scene(frm.getRoot(), 1100, 720);
        scene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
        primaryStage.setTitle("Nuevo Combo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void mostrarDisponibilidad() {
        ComboDisponibilidadFrm frm = new ComboDisponibilidadFrm(this);
        Scene scene = new Scene(frm.getRoot(), 900, 650);
        scene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
        primaryStage.setTitle("Disponibilidad de Combos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void volverAlMenu() {
        Presentacion.mostrarMenuPrincipal(primaryStage);
    }

    public List<ComboDTO> obtenerTodosCombos() throws PersistenciaException {
        return comboBO.obtenerTodosCombos();
    }

    public List<ComboDTO> buscarCombosPorNombre(String nombre) throws PersistenciaException {
        return comboBO.buscarCombosPorNombre(nombre);
    }

    public List<ComboDTO> buscarCombosPorProducto(Long idProducto) throws PersistenciaException {
        return comboBO.buscarCombosPorProducto(idProducto);
    }

    public void crearComboConProductos(ComboDTO dto, List<Long> idProductos, List<Integer> cantidades) throws PersistenciaException {
        comboBO.crearComboConProductos(dto, idProductos, cantidades);
    }

    public void actualizarCombo(Long id, ComboDTO dto) throws PersistenciaException {
        comboBO.actualizarComboPorId(id, dto);
    }

    public void cambiarEstadoCombo(Long id, Boolean activo) throws PersistenciaException {
        comboBO.cambiarEstado(id, activo);
    }

    public boolean puedeVenderse(Long idCombo) throws PersistenciaException {
        return comboBO.puedeVenderse(idCombo);
    }

    public List<ProductoDTO> obtenerTodosProductos() throws PersistenciaException {
        try {
            return productoBO.buscarProductos(null, null);
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener productos: " + e.getMessage(), e);
        }
    }
}