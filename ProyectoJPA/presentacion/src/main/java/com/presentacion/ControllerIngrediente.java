/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.presentacion;

import BOs.IngredienteBO;
import com.dtos.IngredienteDTO;
import interfaces.IIngredienteBO;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author joser
 */
public class ControllerIngrediente {
    private final IIngredienteBO ingredienteBO;
    private final Stage primaryStage;
    private IngredienteDTO ingredienteDTO;
    private static final String CSS_PATH = "/styles/buscador-clientes.css";

    public ControllerIngrediente(Stage primaryStage) {
        this.ingredienteBO = new IngredienteBO();
        this.primaryStage = primaryStage;
    }
    
    
    public void mostrarAgregarIngrediente(){
        AgregarIngredienteFrm ventana = new AgregarIngredienteFrm(this);
        
        java.net.URL cssUrl = getClass().getResource(CSS_PATH);
        if (cssUrl != null) {
            ventana.getScene().getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.out.println(" ADVERTENCIA: No se encontró el archivo CSS en la ruta: " + CSS_PATH);
            System.out.println(" La pantalla se mostrará sin estilos por ahora.");
        }

        ventana.show();
    }
    
    public void registrarIngrediente(IngredienteDTO ingredienteDTO){
        ingredienteBO.agregarIngrediente(ingredienteDTO);
    }
}
