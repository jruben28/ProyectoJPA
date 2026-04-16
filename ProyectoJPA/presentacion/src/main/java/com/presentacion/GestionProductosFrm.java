/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.presentacion;

import com.dtos.ProductoDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 *
 * @author icoro
 */
public class GestionProductosFrm {

    private final ControllerProducto controller;
    private VBox root;
    private TextField txtBuscar;
    private TableView<ProductoDTO> tblProductos;
    private ObservableList<ProductoDTO> datosTabla;
    private Label lblResumen;

    public GestionProductosFrm(ControllerProducto controller) {
        this.controller = controller;
        this.datosTabla = FXCollections.observableArrayList();
        initComponents();
    }

    public VBox getRoot() {
        return root;
    }

    private void initComponents() {
        root = new VBox(20);
        root.setPadding(new Insets(25, 30, 25, 30));
        root.getStyleClass().add("root-pane");

        // Título
        Label lblTitulo = new Label("Gestion de Productos");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Barra de búsqueda y botón
        HBox topBar = crearTopBar();

        // Tabla de productos
        tblProductos = crearTabla();
        VBox.setVgrow(tblProductos, Priority.ALWAYS);

        // Footer (Resumen)
        lblResumen = new Label("8 productos totales | 6 activos | 2 inactivos");
        lblResumen.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px;");

        root.getChildren().addAll(lblTitulo, topBar, tblProductos, lblResumen);
    }

    private HBox crearTopBar() {
        txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar productos...");
        txtBuscar.setPrefWidth(300);
        txtBuscar.setStyle("-fx-background-radius: 20; -fx-padding: 8 15;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnNuevo = new Button("Nuevo Producto");
        btnNuevo.setStyle("-fx-background-color: #1e3a8a; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold;");
        btnNuevo.setOnAction(e -> controller.mostrarRegistro(null));

        HBox hbox = new HBox(10, txtBuscar, spacer, btnNuevo);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    @SuppressWarnings("unchecked")
    private TableView<ProductoDTO> crearTabla() {
        TableView<ProductoDTO> tabla = new TableView<>();
        tabla.setItems(datosTabla);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Columna Producto
        TableColumn<ProductoDTO, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));

        // Columna Categoria (Badge)
        TableColumn<ProductoDTO, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ProductoDTO prod = getTableView().getItems().get(getIndex());
                    Label badge = new Label(prod.getTipo()); // ej: "Platos Fuertes"
                    badge.setStyle("-fx-background-color: #e0e7ff; -fx-text-fill: #3730a3; -fx-padding: 3 8; -fx-background-radius: 12; -fx-font-size: 11px;");
                    setGraphic(badge);
                }
            }
        });

        // Columna Precio
        TableColumn<ProductoDTO, String> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(cell -> new SimpleStringProperty("$" + cell.getValue().getPrecio()));

        // Columna Ingredientes
        TableColumn<ProductoDTO, String> colIngredientes = new TableColumn<>("Ingredientes");
        colIngredientes.setCellValueFactory(cell -> new SimpleStringProperty(
                (cell.getValue().getIngredientes() != null ? cell.getValue().getIngredientes().size() : 0) + " ingredientes"
        ));

        // Columna Estado (Toggle Switch simulado)
        TableColumn<ProductoDTO, Void> colEstado = new TableColumn<>("Estado");
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ProductoDTO prod = getTableView().getItems().get(getIndex());
                    boolean activo = prod.getActivo() != null ? prod.getActivo() : false;
                    
                    // Simulacion de ToggleButton y Label
                    ToggleButton toggle = new ToggleButton();
                    toggle.setSelected(activo);
                    Label lblEstado = new Label(activo ? "Activo" : "Inactivo");
                    lblEstado.setStyle(activo ? "-fx-text-fill: #16a34a;" : "-fx-text-fill: #9ca3af;");
                    
                    HBox box = new HBox(5, toggle, lblEstado);
                    box.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(box);
                }
            }
        });

        // Columna Acciones
        TableColumn<ProductoDTO, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Hyperlink btnEditar = new Hyperlink("Editar");
            private final Hyperlink btnVincular = new Hyperlink("Vincular");
            private final HBox box = new HBox(10, btnEditar, btnVincular);

            {
                btnEditar.setStyle("-fx-text-fill: #2563eb;");
                btnVincular.setStyle("-fx-text-fill: #16a34a;");
                box.setAlignment(Pos.CENTER_LEFT);

                btnEditar.setOnAction(e -> {
                    ProductoDTO dto = getTableView().getItems().get(getIndex());
                    controller.mostrarRegistro(dto);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        tabla.getColumns().addAll(colProducto, colCategoria, colPrecio, colIngredientes, colEstado, colAcciones);
        return tabla;
    }
}