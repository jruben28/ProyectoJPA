/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.presentacion;

import BOs.IngredienteBO;
import com.dtos.IngredienteDTO;
import enums.UnidadDeMedida;
import excepciones.NegocioException;
import java.io.File;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author joser
 */
public class AgregarIngredienteFrm extends Stage{
    
    private final ControllerIngrediente controller; 

    private TextField txtNombre;
    private TextField txtStock; 
    private ComboBox<UnidadDeMedida> cbUnidadMedida; 
    private TextField txtRutaImagen;

    public AgregarIngredienteFrm(ControllerIngrediente controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25, 35, 25, 35));
        root.getStyleClass().add("root-pane");

        Label titulo = new Label("Registrar Ingrediente");
        titulo.getStyleClass().add("titulo");

        Label subtitulo = new Label("Agrega un nuevo ingrediente al catálogo del restaurante");
        subtitulo.getStyleClass().add("resultados-label");

        VBox formulario = crearFormulario();
        HBox botones = crearBotones();

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        root.getChildren().addAll(titulo, subtitulo, formulario, spacer, botones);

        Scene scene = new Scene(root, 620, 580);
        setTitle("Registrar Ingrediente");
        setScene(scene);
    }

    private VBox crearFormulario() {
        Label lblNombre = new Label("Nombre del ingrediente *");
        lblNombre.getStyleClass().add("form-label");
        txtNombre = new TextField();
        txtNombre.setPromptText("Ej: Tomate bola");
        txtNombre.getStyleClass().add("form-field");

        Label lblStock = new Label("Stock inicial *");
        lblStock.getStyleClass().add("form-label");
        txtStock = new TextField();
        txtStock.setPromptText("Ej: 15.5");
        txtStock.getStyleClass().add("form-field");

        Label lblUnidad = new Label("Unidad de Medida *");
        lblUnidad.getStyleClass().add("form-label");
        
        cbUnidadMedida = new ComboBox<>();
        cbUnidadMedida.getItems().addAll(UnidadDeMedida.values()); 
        cbUnidadMedida.setPromptText("Seleccione una unidad...");
        cbUnidadMedida.getStyleClass().add("form-field");
        cbUnidadMedida.setMaxWidth(Double.MAX_VALUE); 
        
        
        Label lblImagen = new Label("Imagen del ingrediente (Opcional)");
        lblImagen.getStyleClass().add("form-label");
        
        txtRutaImagen = new TextField();
        txtRutaImagen.setPromptText("Ningún archivo seleccionado");
        txtRutaImagen.setEditable(false);
        txtRutaImagen.getStyleClass().add("form-field");
        
        Button btnExaminar = new Button("Examinar...");
        btnExaminar.getStyleClass().add("btn-buscar"); 
        btnExaminar.setOnAction(e -> abrirSelectorDeImagen());

        HBox cajaImagen = new HBox(10, txtRutaImagen, btnExaminar);
        HBox.setHgrow(txtRutaImagen, Priority.ALWAYS);

        VBox form = new VBox(10,
                lblNombre, txtNombre,
                lblStock, txtStock,
                lblUnidad, cbUnidadMedida,
                lblImagen, cajaImagen
        );
        form.getStyleClass().add("card");
        form.setPadding(new Insets(25));
        
        
        

        return form;
    }

    private HBox crearBotones() {
        Button btnGuardar = new Button("Guardar Ingrediente");
        btnGuardar.getStyleClass().add("btn-buscar");
        
        btnGuardar.setOnAction(e -> guardarIngrediente());

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("btn-cancelar");
        btnCancelar.setOnAction(e -> close());

        HBox hbox = new HBox(12, btnCancelar, btnGuardar);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        return hbox;
    }

    private void guardarIngrediente() {
        IngredienteDTO dto = new IngredienteDTO();
        dto.setNombre(txtNombre.getText().trim());
        dto.setUnidadDeMedida(cbUnidadMedida.getValue());
        String rutaSeleccionada = txtRutaImagen.getText().trim();
        dto.setUrlImagen(rutaSeleccionada.isEmpty() ? null : rutaSeleccionada);

        try {
            String stockTexto = txtStock.getText().trim();
            if (!stockTexto.isEmpty()) {
                dto.setStock(Double.parseDouble(stockTexto));
            } else {
                dto.setStock(null);
            }
        } catch (NumberFormatException e) {
            mostrarAlertaError("Formato incorrecto", "El stock debe ser un número válido.");
            return;
        }

        try {
            controller.registrarIngrediente(dto);
            mostrarAlertaExito("Ingrediente Registrado", "Ingrediente '" + dto.getNombre() + "' registrado exitosamente.");
            limpiarFormulario();
            close(); 
            
        } catch (NegocioException ex) {
            mostrarAlertaError("Error al guardar ingrediente", ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtStock.clear();
        cbUnidadMedida.setValue(null);
        txtRutaImagen.clear();
    }
    
    private void mostrarAlertaExito(String titulo, String mensaje) {
        Alert exito = new Alert(Alert.AlertType.INFORMATION, mensaje);
        exito.setHeaderText(null);
        exito.setTitle(titulo);
        exito.showAndWait();
    }
    
    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert error = new Alert(Alert.AlertType.ERROR, mensaje);
        error.setHeaderText(titulo);
        error.setTitle("Error");
        error.showAndWait();
    }
    
    
    private void abrirSelectorDeImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen del Ingrediente");
        
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg")
        );

        File archivoSeleccionado = fileChooser.showOpenDialog(this); 

        if (archivoSeleccionado != null) {
            txtRutaImagen.setText(archivoSeleccionado.getAbsolutePath());
        }
    }
    
}
