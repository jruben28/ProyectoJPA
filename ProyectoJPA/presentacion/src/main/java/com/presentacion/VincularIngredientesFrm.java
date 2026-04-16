/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.presentacion;

import com.dtos.ProductoDTO;
import com.dtos.ProductoIngredienteDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 *
 * @author icoro
 */
public class VincularIngredientesFrm extends Stage {

    private final ControllerProducto controller;
    private ProductoDTO producto;
    private ObservableList<ProductoIngredienteDTO> ingredientesVinculados;
    private TableView<ProductoIngredienteDTO> tblVinculados;

    public VincularIngredientesFrm(ControllerProducto controller, ProductoDTO producto) {
        this.controller = controller;
        this.producto = producto;
        this.ingredientesVinculados = FXCollections.observableArrayList();
        if (producto != null && producto.getIngredientes() != null) {
            this.ingredientesVinculados.addAll(producto.getIngredientes());
        }
        initComponents();
    }

    private void initComponents() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f3f4f6;"); // Fondo gris claro de toda la pantalla

        Label lblTitulo = new Label("Vincular Ingredientes");
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox mainContainer = new HBox(20);
        VBox panelIzquierdo = crearPanelProducto();
        VBox panelDerecho = crearPanelIngredientesDisponibles();

        // Para que ambos paneles tomen mitad y mitad
        HBox.setHgrow(panelIzquierdo, Priority.ALWAYS);
        HBox.setHgrow(panelDerecho, Priority.ALWAYS);
        panelIzquierdo.setPrefWidth(400);
        panelDerecho.setPrefWidth(400);

        mainContainer.getChildren().addAll(panelIzquierdo, panelDerecho);
        root.getChildren().addAll(lblTitulo, mainContainer);

        javafx.scene.Scene scene = new javafx.scene.Scene(root, 1000, 650);
        setTitle("Vincular Ingredientes");
        setScene(scene);
    }

    private VBox crearPanelProducto() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label lblSubtitle = new Label("PRODUCTO");
        lblSubtitle.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 10px; -fx-font-weight: bold;");

        Label lblNombre = new Label(producto != null ? producto.getNombre() : "Tacos al Pastor");
        lblNombre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox tags = new HBox(10);
        Label lblCategoria = new Label("Platos Fuertes");
        lblCategoria.setStyle("-fx-background-color: #e0e7ff; -fx-text-fill: #3730a3; -fx-padding: 3 10; -fx-background-radius: 12;");
        Label lblPrecio = new Label("$85.00");
        lblPrecio.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
        tags.getChildren().addAll(lblCategoria, lblPrecio);
        tags.setAlignment(Pos.CENTER_LEFT);

        // Placeholder para la imagen
        Region imgPlaceholder = new Region();
        imgPlaceholder.setPrefHeight(120);
        imgPlaceholder.setStyle("-fx-background-color: #e5e7eb; -fx-background-radius: 8;");

        Label lblReceta = new Label("Receta / Ingredientes Vinculados");
        lblReceta.setStyle("-fx-font-weight: bold;");

        tblVinculados = new TableView<>();
        tblVinculados.setItems(ingredientesVinculados);
        tblVinculados.setPrefHeight(200);
        
        TableColumn<ProductoIngredienteDTO, String> colIng = new TableColumn<>("Ingrediente");
        colIng.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombreIngrediente()));
        
        TableColumn<ProductoIngredienteDTO, String> colCant = new TableColumn<>("Cantidad");
        colCant.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getCantidad())));

        TableColumn<ProductoIngredienteDTO, Void> colQuitar = new TableColumn<>("Quitar");
        colQuitar.setCellFactory(col -> new TableCell<>() {
            private final Button btnX = new Button("x");
            {
                btnX.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #ef4444; -fx-background-radius: 15;");
                btnX.setOnAction(e -> ingredientesVinculados.remove(getIndex()));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnX);
                setAlignment(Pos.CENTER);
            }
        });

        tblVinculados.getColumns().addAll(colIng, colCant, colQuitar);

        Label lblConteo = new Label(ingredientesVinculados.size() + " ingredientes vinculados");
        lblConteo.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12px;");

        Button btnGuardar = new Button("Guardar Vinculacion");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnGuardar.setStyle("-fx-background-color: #1e3a8a; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 6;");

        panel.getChildren().addAll(lblSubtitle, lblNombre, tags, imgPlaceholder, lblReceta, tblVinculados, lblConteo, btnGuardar);
        return panel;
    }

    private VBox crearPanelIngredientesDisponibles() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label lblTitulo = new Label("Ingredientes Disponibles");
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar ingrediente...");
        txtBuscar.setStyle("-fx-background-radius: 20; -fx-padding: 8 15;");

        // Lista de ingredientes (usamos un VBox dentro de un ScrollPane en lugar de TableView para dar el estilo de "tarjetas")
        VBox listaIngredientes = new VBox(10);
        
        // Simulación de datos (Esto vendría de tu controlador)
        listaIngredientes.getChildren().add(crearFilaIngrediente("Tortilla de Maiz", "500 pz", true));
        listaIngredientes.getChildren().add(crearFilaIngrediente("Carne al Pastor", "12.5 kg", true));
        listaIngredientes.getChildren().add(crearFilaIngrediente("Queso Cheddar", "2.3 kg", false));
        listaIngredientes.getChildren().add(crearFilaIngrediente("Lechuga", "5.0 kg", false));

        ScrollPane scroll = new ScrollPane(listaIngredientes);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-control-inner-background: white;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        panel.getChildren().addAll(lblTitulo, txtBuscar, scroll);
        return panel;
    }

    private HBox crearFilaIngrediente(String nombre, String stock, boolean vinculado) {
        HBox fila = new HBox(10);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(10, 0, 10, 0));
        fila.setStyle("-fx-border-color: #f3f4f6; -fx-border-width: 0 0 1 0;"); // Borde inferior

        VBox info = new VBox(3);
        Label lblNombre = new Label(nombre);
        lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label lblStock = new Label("stock: " + stock);
        lblStock.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 11px;");
        info.getChildren().addAll(lblNombre, lblStock);
        HBox.setHgrow(info, Priority.ALWAYS);

        if (vinculado) {
            Label lblVinculado = new Label("Vinculado");
            lblVinculado.setStyle("-fx-text-fill: #16a34a; -fx-font-style: italic; -fx-font-size: 12px;");
            fila.getChildren().addAll(info, lblVinculado);
        } else {
            TextField txtCantidad = new TextField();
            txtCantidad.setPrefWidth(50);
            Label lblUnidad = new Label("kg"); // Esto puede ser dinámico
            Button btnAgregar = new Button("Agregar");
            btnAgregar.setStyle("-fx-background-color: white; -fx-border-color: #16a34a; -fx-text-fill: #16a34a; -fx-border-radius: 5; -fx-background-radius: 5;");
            
            fila.getChildren().addAll(info, txtCantidad, lblUnidad, btnAgregar);
        }

        return fila;
    }
}
