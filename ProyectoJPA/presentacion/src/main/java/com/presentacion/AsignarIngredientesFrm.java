/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.presentacion;

import com.dtos.IngredienteDTO;
import com.dtos.ProductoDTO;
import com.dtos.ProductoIngredienteDTO;
import com.dtos.ProductoIngredienteRDTO;
import java.util.List;
import javafx.beans.property.*;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author joser
 */
public class AsignarIngredientesFrm extends VBox {

    private final ControllerIngrediente controller;

    private ComboBox<ProductoDTO> cbProductos;
    private VBox productInfoPanel; 
    private TableView<RecetaFila> tableReceta;
    private Label lblContadorReceta;
    private Button btnGuardarVinculacion;

    private TextField txtBuscador;
    private VBox listAvailableIngredients;

    private ObservableList<RecetaFila> ingredientesReceta = FXCollections.observableArrayList();

    public AsignarIngredientesFrm(ControllerIngrediente controller) {
        this.controller = controller;
        initComponents();
        cargarProductos();
        cargarIngredientes(null);
    }

    private void initComponents() {
        setSpacing(20);
        setPadding(new Insets(25));
        getStyleClass().add("root-pane");

        HBox mainContainer = new HBox(25);
        VBox leftCard = new VBox(15);
        leftCard.setPadding(new Insets(20));
        leftCard.getStyleClass().add("card");
        leftCard.setStyle(leftCard.getStyle() + "-fx-pref-width: 550px;");

        Label lblTituloLeft = new Label("Producto a vincular ingredientes");
        lblTituloLeft.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");

        cbProductos = new ComboBox<>();
        cbProductos.setMaxWidth(Double.MAX_VALUE);
        cbProductos.setPromptText("Seleccione un producto para ver su receta...");
        cbProductos.getStyleClass().add("form-field");
        cbProductos.setOnAction(e -> cargarDatosDelProducto(cbProductos.getValue()));

        productInfoPanel = productInfo();

        Label lblSeccionReceta = new Label("Receta / Ingredientes Vinculados");
        lblSeccionReceta.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        tableReceta = createRecipeTable();
        tableReceta.setItems(ingredientesReceta);
        VBox.setVgrow(tableReceta, Priority.ALWAYS);

        lblContadorReceta = new Label("0 ingredientes vinculados");
        lblContadorReceta.setStyle("-fx-text-fill: #95a5a6;");

        btnGuardarVinculacion = new Button("Guardar Vinculacion");
        btnGuardarVinculacion.getStyleClass().add("btn-buscar");
        btnGuardarVinculacion.setMaxWidth(Double.MAX_VALUE);
        btnGuardarVinculacion.setPadding(new Insets(12));
        btnGuardarVinculacion.setOnAction(e -> persistirDatos());

        leftCard.getChildren().addAll(lblTituloLeft, cbProductos, productInfoPanel,
                new Separator(), lblSeccionReceta, tableReceta,
                lblContadorReceta, btnGuardarVinculacion);
        mainContainer.getChildren().add(leftCard);

        VBox rightCard = new VBox(15);
        rightCard.setPadding(new Insets(20));
        rightCard.getStyleClass().add("card");
        HBox.setHgrow(rightCard, Priority.ALWAYS);

        Label lblTituloRight = new Label("Ingredientes Disponibles en Stock");
        lblTituloRight.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        txtBuscador = new TextField();
        txtBuscador.setPromptText("🔍 Buscar ingrediente para agregar a la receta...");
        txtBuscador.getStyleClass().add("form-field");
        txtBuscador.textProperty().addListener((observable, oldValue, newValue) -> {
            
            if (newValue == null || newValue.trim().isEmpty()) {
                
            } else {
                cargarIngredientes(newValue);
                
            }
            
        });

        listAvailableIngredients = new VBox(5);
        ScrollPane scrollMarket = new ScrollPane(listAvailableIngredients);
        scrollMarket.setFitToWidth(true);
        scrollMarket.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        VBox.setVgrow(scrollMarket, Priority.ALWAYS);

        rightCard.getChildren().addAll(lblTituloRight, txtBuscador, scrollMarket);
        mainContainer.getChildren().add(rightCard);

        getChildren().add(mainContainer);
    }


    private VBox productInfo() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));

        panel.setVisible(false);
        panel.setManaged(false);

        ImageView imgView = new ImageView();
        imgView.setFitWidth(510);
        imgView.setFitHeight(120);
        imgView.setPreserveRatio(false);
        imgView.setStyle("-fx-background-radius: 10px; -fx-border-radius: 10px;");
        imgView.setClip(new Rectangle(510, 120, 10, 10));

        HBox badges = new HBox(10);
        Label lblCategoria = createBadge("# Platos Fuertes", "#bdc3c7");
        Label lblPrecio = createBadge("$ 00.00", "#27ae60");
        lblPrecio.setStyle(lblPrecio.getStyle() + "-fx-font-weight: bold;");
        badges.getChildren().addAll(lblCategoria, lblPrecio);

        panel.getChildren().addAll(imgView, badges);
        return panel;
    }

    private Label createBadge(String texto, String colorFondoHex) {
        Label lbl = new Label(texto);
        lbl.setPadding(new Insets(3, 8, 3, 8));
        lbl.setStyle("-fx-background-color: " + colorFondoHex + "; -fx-text-fill: white; -fx-background-radius: 5px; -fx-font-size: 11px;");
        return lbl;
    }

    @SuppressWarnings("unchecked")
    private TableView<RecetaFila> createRecipeTable() {
        TableView<RecetaFila> table = new TableView<>();
        table.getStyleClass().add("card");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<RecetaFila, String> colIng = new TableColumn<>("Ingrediente");
        colIng.setCellValueFactory(new PropertyValueFactory<>("nombreIngrediente"));
        colIng.setPrefWidth(200);

        TableColumn<RecetaFila, String> colCant = new TableColumn<>("Cantidad");
        colCant.setCellValueFactory(new PropertyValueFactory<>("cantidadUnidadText"));
        colCant.setPrefWidth(120);
        colCant.setStyle("-fx-alignment: CENTER;");

        TableColumn<RecetaFila, Button> colQuitar = new TableColumn<>("Quitar");
        colQuitar.setCellValueFactory(new PropertyValueFactory<>("btnQuitar"));
        colQuitar.setPrefWidth(80);
        colQuitar.setStyle("-fx-alignment: CENTER;");

        table.getColumns().addAll(colIng, colCant, colQuitar);
        return table;
    }


    private void cargarProductos() {

        ObservableList<ProductoDTO> productosList = FXCollections.observableArrayList();
        productosList.addAll(controller.obtenerProductosDisponibles());
        cbProductos.setItems(productosList);

        ObservableList<DisponibleFila> marketList = FXCollections.observableArrayList();
        for (IngredienteDTO ing : controller.obtenerIngredientesStockActual()) {
            marketList.add(new DisponibleFila(ing, this));
        }

        listAvailableIngredients.getChildren().clear();
        for (DisponibleFila fila : marketList) {
            listAvailableIngredients.getChildren().add(fila.getNodoVisual());
        }
    }
    
    private void cargarIngredientes(String textoBusqueda){
        listAvailableIngredients.getChildren().clear();
        
        List<IngredienteDTO> resultadosBd;
        
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            resultadosBd = controller.obtenerIngredientesStockActual();
        } else {
            resultadosBd = controller.obtenerIngredientesFiltro(textoBusqueda);
        }

        for (IngredienteDTO ing : resultadosBd) {
            DisponibleFila fila = new DisponibleFila(ing, this);
            listAvailableIngredients.getChildren().add(fila.getNodoVisual());
        }
    }


    private void cargarDatosDelProducto(ProductoDTO prod) {
        if (prod == null) {
            return;
        }

        productInfoPanel.setVisible(true);
        productInfoPanel.setManaged(true);

        //aqui se deberia cargar la imagen de la bdd pero no está correctamente hecho el producto
        // Image img = controller.intentarCargarImagenProducto(prod.getUrlImagen());
        // ((ImageView)productInfoPanel.getChildren().get(0)).setImage(img);
        //actaliza precio
        Label lblPrecio = (Label) ((HBox) productInfoPanel.getChildren().get(1)).getChildren().get(1);
        lblPrecio.setText("$ " + String.format("%.2f", prod.getPrecio()));

        ingredientesReceta.clear();
        for (ProductoIngredienteRDTO vinc : controller.obtenerRecetaPorProducto(prod)) {
            ingredientesReceta.add(new RecetaFila(vinc, this));
        }

        actualizarContador();
    }

    private void actualizarContador() {
        lblContadorReceta.setText(ingredientesReceta.size() + " ingredientes vinculados");
    }


    public void intentarAgregarIngrediente(DisponibleFila filaOrigen) {
        ProductoDTO prodSeleccionado = cbProductos.getValue();

        if (prodSeleccionado == null) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Por favor seleccione primero un producto al cual vincular el ingrediente.");
            a.setTitle("Aviso");
            a.setHeaderText(null);
            a.showAndWait();
            return;
        }

        String cantText = filaOrigen.getCantidadTexto();
        double cantidad;
        try {
            cantidad = Double.parseDouble(cantText);
            if (cantidad <= 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR, "La cantidad debe ser un número válido mayor a 0.");
            error.setTitle("Error");
            error.setHeaderText(null);
            error.showAndWait();
            return;
        }


        ProductoIngredienteRDTO nuevaVinc = new ProductoIngredienteRDTO();
        nuevaVinc.setProducto(prodSeleccionado);
        nuevaVinc.setIngrediente(filaOrigen.getIngredienteDto());
        nuevaVinc.setCantidad(cantidad);

        ingredientesReceta.add(new RecetaFila(nuevaVinc, this));
        filaOrigen.limpiarCantidad();
        actualizarContador();
    }


    public void intentarQuitarIngrediente(RecetaFila filaAQuitar) {
        ingredientesReceta.remove(filaAQuitar);
        actualizarContador();
    }


    private void persistirDatos() {
        ProductoDTO prod = cbProductos.getValue();
        if (prod == null || ingredientesReceta.isEmpty()) {
            return;
        }

        try {
            ObservableList<ProductoIngredienteRDTO> vincs = FXCollections.observableArrayList();
            for (RecetaFila fila : ingredientesReceta) {
                vincs.add(fila.getVinculacionDto());
            }

            controller.guardarRecetaDeProducto(prod, vincs);

            Alert exito = new Alert(Alert.AlertType.INFORMATION, "Receta guardada exitosamente.");
            exito.setTitle("Éxito");
            exito.setHeaderText(null);
            exito.showAndWait();

        } catch (Exception ex) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Error al guardar receta: " + ex.getMessage());
            error.setTitle("Error");
            error.setHeaderText(null);
            error.showAndWait();
        }
    }

    public VBox getRoot() {
        return this;
    }

    public static class RecetaFila {

        private final ProductoIngredienteRDTO vinculacionDto;
        private final StringProperty nombreIngrediente;
        private final StringProperty cantidadUnidadText;
        private final Button btnQuitar;

        public RecetaFila(ProductoIngredienteRDTO vinculacionDto, AsignarIngredientesFrm framePadre) {
            this.vinculacionDto = vinculacionDto;
            this.nombreIngrediente = new SimpleStringProperty(vinculacionDto.getIngrediente().getNombre());

            this.cantidadUnidadText = new SimpleStringProperty(
                    vinculacionDto.getCantidad() + " " + vinculacionDto.getIngrediente().getUnidadDeMedida().name()
            );

            this.btnQuitar = new Button("✖");
            this.btnQuitar.getStyleClass().add("btn-eliminar");
            this.btnQuitar.setStyle("-fx-text-fill: white; -fx-background-color: #d9534f; -fx-cursor: hand; -fx-background-radius: 5px;");
            this.btnQuitar.setPrefSize(30, 30);

            this.btnQuitar.setOnAction(e -> framePadre.intentarQuitarIngrediente(this));
        }

        public ProductoIngredienteRDTO getVinculacionDto() {
            return vinculacionDto;
        }

        public String getNombreIngrediente() {
            return nombreIngrediente.get();
        }

        public StringProperty nombreIngredienteProperty() {
            return nombreIngrediente;
        }

        public String getCantidadUnidadText() {
            return cantidadUnidadText.get();
        }

        public StringProperty cantidadUnidadTextProperty() {
            return cantidadUnidadText;
        }

        public Button getBtnQuitar() {
            return btnQuitar;
        }
    }

    public static class DisponibleFila {

        private final IngredienteDTO ingredienteDto;
        private final VBox nodoVisual;
        private final TextField txtCantidadAAgregar;
        private final Button btnAgregar;

        public DisponibleFila(IngredienteDTO ing, AsignarIngredientesFrm framePadre) {
            this.ingredienteDto = ing;

            Label lblNombre = new Label(ing.getNombre());
            lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

            Label lblStock = new Label("stock: " + ing.getStock() + " " + ing.getUnidadDeMedida().name());
            lblStock.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 11px;");

            VBox vBoxInfo = new VBox(2, lblNombre, lblStock);
            HBox.setHgrow(vBoxInfo, Priority.ALWAYS);

            txtCantidadAAgregar = new TextField();
            txtCantidadAAgregar.setPromptText("0.0");
            txtCantidadAAgregar.setPrefWidth(60);
            txtCantidadAAgregar.setAlignment(Pos.CENTER);
            txtCantidadAAgregar.setStyle("-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-border-color: #bdc3c7;");

            Label lblUnidad = new Label(ing.getUnidadDeMedida().name());
            lblUnidad.setPrefWidth(30);
            lblUnidad.setStyle("-fx-text-fill: #95a5a6;");

            btnAgregar = new Button("Agregar");
            btnAgregar.getStyleClass().add("btn-buscar");
            btnAgregar.setStyle("-fx-background-color: transparent; -fx-text-fill: #27ae60; -fx-border-color: #27ae60; -fx-border-radius: 5px; -fx-cursor: hand;");

            btnAgregar.setOnMouseEntered(e -> btnAgregar.setStyle("-fx-background-color: #eafaf1; -fx-text-fill: #27ae60; -fx-border-color: #27ae60; -fx-border-radius: 5px;"));
            btnAgregar.setOnMouseExited(e -> btnAgregar.setStyle("-fx-background-color: transparent; -fx-text-fill: #27ae60; -fx-border-color: #27ae60; -fx-border-radius: 5px;"));

            btnAgregar.setOnAction(e -> framePadre.intentarAgregarIngrediente(this));

            HBox hBoxAcciones = new HBox(8, txtCantidadAAgregar, lblUnidad, btnAgregar);
            hBoxAcciones.setAlignment(Pos.CENTER_RIGHT);

            nodoVisual = new VBox();
            HBox hboxFilaCompleta = new HBox(15, vBoxInfo, hBoxAcciones);
            hboxFilaCompleta.setPadding(new Insets(10, 5, 10, 5));
            hboxFilaCompleta.setAlignment(Pos.CENTER_LEFT);

            Separator sep = new Separator();

            nodoVisual.getChildren().addAll(hboxFilaCompleta, sep);
        }

        public IngredienteDTO getIngredienteDto() {
            return ingredienteDto;
        }

        public VBox getNodoVisual() {
            return nodoVisual;
        }

        public String getCantidadTexto() {
            return txtCantidadAAgregar.getText().trim();
        }

        public void limpiarCantidad() {
            txtCantidadAAgregar.clear();
        }

        public void setVinculado(boolean vinculado) {
            if (vinculado) {
                btnAgregar.setText("Vinculado ✔");
                btnAgregar.setDisable(true);
                btnAgregar.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #7f8c8d; -fx-border-color: #7f8c8d;");
                txtCantidadAAgregar.setDisable(true);
            } else {
                // ... restaurar estilo original ...
            }
        }
    }
}
