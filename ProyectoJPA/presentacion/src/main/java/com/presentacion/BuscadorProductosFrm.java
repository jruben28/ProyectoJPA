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
import java.util.List;

/**
 *
 * @author icoro
 */
public class BuscadorProductosFrm {

    private final ControllerProducto controller;
    private VBox root;
    private TextField txtBuscar;
    private ComboBox<String> cmbCategoria;
    private Label lblResultados;
    private TableView<ProductoDTO> tblProductos;
    private ObservableList<ProductoDTO> datosTabla;

    public BuscadorProductosFrm(ControllerProducto controller) {
        this.controller = controller;
        this.datosTabla = FXCollections.observableArrayList();
        initComponents();
        buscarProductos(); // Cargar todos al inicio
    }

    public VBox getRoot() {
        return root;
    }

    private void initComponents() {
        root = new VBox(12);
        root.setPadding(new Insets(25, 30, 25, 30));
        root.getStyleClass().add("root-pane");

        Label lblTitulo = new Label("Catálogo de Productos");
        lblTitulo.getStyleClass().add("titulo");

        HBox header = crearHeader();
        HBox searchBar = crearBarraBusqueda();

        lblResultados = new Label(" ");
        lblResultados.getStyleClass().add("resultados-label");

        tblProductos = crearTabla();
        VBox.setVgrow(tblProductos, Priority.ALWAYS);

        root.getChildren().addAll(lblTitulo, header, searchBar, lblResultados, tblProductos);
    }

    private HBox crearHeader() {
        Button btnNuevoProducto = new Button("+ Nuevo Producto");
        btnNuevoProducto.getStyleClass().add("btn-buscar");
        btnNuevoProducto.setStyle("-fx-padding: 8px 15px;");
        btnNuevoProducto.setOnAction(e -> controller.mostrarRegistro(null));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox hbox = new HBox(10, btnNuevoProducto, spacer);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private HBox crearBarraBusqueda() {
        txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por nombre...");
        txtBuscar.getStyleClass().add("search-field");
        txtBuscar.setOnAction(e -> buscarProductos());
        HBox.setHgrow(txtBuscar, Priority.ALWAYS);

        cmbCategoria = new ComboBox<>();
        cmbCategoria.setItems(FXCollections.observableArrayList("TODOS", "PLATILLO", "BEBIDA", "POSTRE"));
        cmbCategoria.setValue("TODOS");
        cmbCategoria.getStyleClass().add("form-field");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("btn-buscar");
        btnBuscar.setOnAction(e -> buscarProductos());

        HBox hbox = new HBox(10, txtBuscar, cmbCategoria, btnBuscar);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    @SuppressWarnings("unchecked")
    private TableView<ProductoDTO> crearTabla() {
        TableView<ProductoDTO> tabla = new TableView<>();
        tabla.setItems(datosTabla);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("No hay productos registrados"));

        TableColumn<ProductoDTO, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        
        TableColumn<ProductoDTO, String> colTipo = new TableColumn<>("Categoría");
        colTipo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTipo()));

        TableColumn<ProductoDTO, String> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(cell -> new SimpleStringProperty("$" + cell.getValue().getPrecio()));

        TableColumn<ProductoDTO, String> colEstado = new TableColumn<>("Estado");
        // Nota: Asumo que en ProductoDTO tienes un booleano getActivo() o un String getEstado()
        colEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getActivo() ? "Activo" : "Inactivo"));

        TableColumn<ProductoDTO, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEstado = new Button("Activar/Desactivar");
            private final HBox contenedor = new HBox(8, btnEditar, btnEstado);

            {
                btnEditar.getStyleClass().add("btn-ver-perfil");
                btnEstado.getStyleClass().add("btn-vincular");
                contenedor.setAlignment(Pos.CENTER_LEFT);

                btnEditar.setOnAction(e -> {
                    ProductoDTO dto = getTableView().getItems().get(getIndex());
                    controller.mostrarRegistro(dto); // Abre pantalla de edición
                });

                btnEstado.setOnAction(e -> {
                    ProductoDTO dto = getTableView().getItems().get(getIndex());
                    controller.cambiarEstado(dto.getId());
                    buscarProductos(); // Recargar tabla
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : contenedor);
            }
        });

        tabla.getColumns().addAll(colNombre, colTipo, colPrecio, colEstado, colAcciones);
        return tabla;
    }

    public void buscarProductos() {
        try {
            String texto = txtBuscar.getText().trim();
            String categoria = cmbCategoria.getValue();
            
            List<ProductoDTO> resultados = controller.buscarProductos(texto, categoria);
            datosTabla.setAll(resultados);
            lblResultados.setText(resultados.size() + " productos encontrados.");
        } catch (Exception ex) {
            mostrarError("Error al buscar productos: " + ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
