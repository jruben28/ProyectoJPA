/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.presentacion;

import BOs.IngredienteBO;
import com.dtos.IngredienteDTO;
import excepciones.NegocioException;
import interfaces.IIngredienteBO;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author joser
 */
public class ControllerIngrediente {
    private final IIngredienteBO ingredienteBO;
    private IngredienteDTO ingredienteDTO;
    private static final String CSS_PATH = "/styles/buscador-clientes.css";
    
    private Stage primaryStage;
    private IngredientePrincipalFrm principalView;

    public ControllerIngrediente(Stage primaryStage) {
        this.ingredienteBO = new IngredienteBO();
        this.primaryStage = primaryStage;
    }
    
    public void mostrarPrincipal() {
        principalView = new IngredientePrincipalFrm(this);
        Scene scene = new Scene(principalView, 1200, 800);
        
        java.net.URL cssUrl = getClass().getResource(CSS_PATH);
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

        primaryStage.setTitle("Restaurante - Módulo de Ingredientes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void mostrarRegistro() {
        AgregarIngredienteFrm registro = new AgregarIngredienteFrm(this);

        java.net.URL cssUrl = getClass().getResource(CSS_PATH);
        if (cssUrl != null) registro.getScene().getStylesheets().add(cssUrl.toExternalForm());
        registro.show();
    }
    
    public void mostrarGestionStockEnPrincipal(IngredientePrincipalFrm principal) {
        GestionStockFrm gestionStock = new GestionStockFrm(this);
        principal.setView(gestionStock);
    }
    
    public void registrarIngrediente(IngredienteDTO ingredienteDTO){
        ingredienteBO.agregarIngrediente(ingredienteDTO);
    }
    
    
    public void mostrarGestionStock() {
        GestionStockFrm gestionStock = new GestionStockFrm(this);
        Scene scene = new Scene(gestionStock.getRoot(), 1100, 700);
        
        java.net.URL cssUrl = getClass().getResource(CSS_PATH);
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.out.println("️ ADVERTENCIA: No se encontró CSS.");
        }
        
        primaryStage.setTitle("Gestión de Stock");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    public ObservableList<GestionStockFrm.MovimientoStockFila> cargarMovimientosInventario() {
        ObservableList<GestionStockFrm.MovimientoStockFila> filas = FXCollections.observableArrayList();
        try {
            List<IngredienteDTO> ingredientes = ingredienteBO.obtenerIngredienteTodos();
            for (IngredienteDTO dto : ingredientes) {
                filas.add(new GestionStockFrm.MovimientoStockFila(dto));
            }
        } catch (NegocioException ex) {
            System.err.println("Error al cargar ingredientes: " + ex.getMessage());
        }
        return filas;
    }
    
    public void guardarMovimientos(List<GestionStockFrm.MovimientoStockFila> rows) throws NegocioException {
        boolean huboCambios = false;
        
        for (GestionStockFrm.MovimientoStockFila fila : rows) {
            if (fila.getEntrada() > 0) {
                
                IngredienteDTO dto = fila.getDto();
                
                dto.setStock(fila.getStockFinal());
                
                ingredienteBO.actualizarStock(dto.getIdIngrediente(), dto.getStock()); 
                
                huboCambios = true;
            }
        }
        
        if (!huboCambios) {
            throw new NegocioException("No se registró ninguna entrada nueva para guardar.");
        }
        System.out.println("Simulando guardado de movimientos en BD...");
        
    }
}
