package com.presentacion;

import com.dtos.ProductoDTO;
import com.dtos.ProductoIngredienteDTO;
import java.util.ArrayList;
import java.util.List; 
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 *
 * @author icoro
 */
public class RegistroProductoFrm extends Stage {

    private final ControllerProducto controller;
    private ProductoDTO productoEditando;
    
    // Campos del Producto
    private TextField txtNombre;
    private TextArea txtDescripcion;
    private TextField txtPrecio;
    private ComboBox<String> cmbTipo;
    
    // Campos de Ingredientes
    private ComboBox<IngredienteDTO> cmbIngredientesDisponibles;
    private TextField txtCantidadIngrediente;
    private TableView<ProductoIngredienteDTO> tblIngredientesSeleccionados;
    private ObservableList<ProductoIngredienteDTO> listaIngredientesTemporales;

    public RegistroProductoFrm(ControllerProducto controller, ProductoDTO producto) {
        this.controller = controller;
        this.productoEditando = producto;
        this.listaIngredientesTemporales = FXCollections.observableArrayList();
        initComponents();
        cargarIngredientesAlComboBox(); 
        
        if (producto != null) {
            precargarDatos(producto);
        }
    }

    private void initComponents() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25, 35, 25, 35));
        root.getStyleClass().add("root-pane");

        boolean esEdicion = productoEditando != null;
        Label titulo = new Label(esEdicion ? "Editar Producto" : "Registrar Producto");
        titulo.getStyleClass().add("titulo");

        HBox paneles = new HBox(20);
        paneles.getChildren().addAll(crearFormularioBasico(), crearSeccionIngredientes());
        
        HBox botones = crearBotones();

        root.getChildren().addAll(titulo, paneles, botones);

        Scene scene = new Scene(root, 850, 600);
        setTitle(esEdicion ? "Editar Producto" : "Registrar Producto");
        setScene(scene);
    }

    private VBox crearFormularioBasico() {
        VBox form = new VBox(10);
        form.getStyleClass().add("card");
        form.setPadding(new Insets(20));
        form.setPrefWidth(350);

        Label lblNombre = new Label("Nombre del Producto *");
        txtNombre = new TextField();
        
        Label lblDesc = new Label("Descripción");
        txtDescripcion = new TextArea();
        txtDescripcion.setPrefRowCount(3);
        
        Label lblPrecio = new Label("Precio *");
        txtPrecio = new TextField();
        
        Label lblTipo = new Label("Categoría *");
        cmbTipo = new ComboBox<>(FXCollections.observableArrayList("PLATILLO", "BEBIDA", "POSTRE"));

        form.getChildren().addAll(lblNombre, txtNombre, lblDesc, txtDescripcion, lblPrecio, txtPrecio, lblTipo, cmbTipo);
        return form;
    }

    private VBox crearSeccionIngredientes() {
        VBox form = new VBox(10);
        form.getStyleClass().add("card");
        form.setPadding(new Insets(20));
        HBox.setHgrow(form, Priority.ALWAYS);

        Label lblTitulo = new Label("Receta / Ingredientes");
        lblTitulo.setStyle("-fx-font-weight: bold;");

        cmbIngredientesDisponibles = new ComboBox<>();
        cmbIngredientesDisponibles.setPromptText("Selecciona ingrediente...");
        
        txtCantidadIngrediente = new TextField();
        txtCantidadIngrediente.setPromptText("Cantidad");
        txtCantidadIngrediente.setPrefWidth(80);

        Button btnAgregar = new Button("+ Agregar");
        btnAgregar.setOnAction(e -> agregarIngredienteATabla());

        HBox controles = new HBox(10, cmbIngredientesDisponibles, txtCantidadIngrediente, btnAgregar);

        tblIngredientesSeleccionados = new TableView<>();
        tblIngredientesSeleccionados.setItems(listaIngredientesTemporales);
        
        TableColumn<ProductoIngredienteDTO, String> colNom = new TableColumn<>("Ingrediente");
        colNom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombreIngrediente()));
        
        TableColumn<ProductoIngredienteDTO, String> colCant = new TableColumn<>("Cantidad");
        colCant.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getCantidad())));
        
        TableColumn<ProductoIngredienteDTO, Void> colEliminar = new TableColumn<>("Quitar");
        colEliminar.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("X");
            {
                btn.setOnAction(e -> {
                    ProductoIngredienteDTO dto = getTableView().getItems().get(getIndex());
                    listaIngredientesTemporales.remove(dto);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tblIngredientesSeleccionados.getColumns().addAll(colNom, colCant, colEliminar);
        VBox.setVgrow(tblIngredientesSeleccionados, Priority.ALWAYS);

        form.getChildren().addAll(lblTitulo, controles, tblIngredientesSeleccionados);
        return form;
    }

    private HBox crearBotones() {
        Button btnGuardar = new Button(productoEditando != null ? "Actualizar" : "Guardar");
        btnGuardar.getStyleClass().add("btn-buscar");
        btnGuardar.setOnAction(e -> guardarProducto());

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("btn-cancelar");
        btnCancelar.setOnAction(e -> close());

        HBox hbox = new HBox(12, btnCancelar, btnGuardar);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        return hbox;
    }

    private void agregarIngredienteATabla() {
        IngredienteDTO seleccionado = cmbIngredientesDisponibles.getValue();
        String cantidadStr = txtCantidadIngrediente.getText().trim();

        if (seleccionado == null || cantidadStr.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Debes seleccionar un ingrediente y poner la cantidad.");
            return;
        }

        ProductoIngredienteDTO pi = new ProductoIngredienteDTO();
        pi.setIdIngrediente(seleccionado.getId());
        pi.setNombreIngrediente(seleccionado.getNombre());
        // CORRECCIÓN: Cambiado a Double.parseDouble asumiendo que ProductoIngredienteDTO.setCantidad recibe un Double
        pi.setCantidad(Double.parseDouble(cantidadStr)); 

        listaIngredientesTemporales.add(pi);
        cmbIngredientesDisponibles.setValue(null);
        txtCantidadIngrediente.clear();
    }

    private void guardarProducto() {
        try {
            ProductoDTO dto = new ProductoDTO();
            if (productoEditando != null) {
                dto.setId(productoEditando.getId());
            }

            dto.setNombre(txtNombre.getText().trim());
            dto.setDescripcion(txtDescripcion.getText().trim());
            // CORRECCIÓN: Cambiado de Float.parseFloat a Double.parseDouble
            dto.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
            dto.setTipo(cmbTipo.getValue());
            dto.setIngredientes(new ArrayList<>(listaIngredientesTemporales));

            if (productoEditando != null) {
                controller.actualizarProducto(dto);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Producto actualizado exitosamente.");
            } else {
                controller.registrarProducto(dto);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Producto registrado exitosamente.");
            }
            close();
        } catch (NumberFormatException ex) {
            // Se añade manejo específico por si el usuario introduce letras en el precio
            mostrarAlerta(Alert.AlertType.ERROR, "Error: El precio y las cantidades deben ser números válidos.");
        } catch (Exception ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar: " + ex.getMessage());
        }
    }

    private void cargarIngredientesAlComboBox() {
        try {
            // El controller debe tener un método que consulte al IngredienteBO
            List<IngredienteDTO> ingredientesBD = controller.obtenerTodosLosIngredientes();
            cmbIngredientesDisponibles.setItems(FXCollections.observableArrayList(ingredientesBD));
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "No se pudieron cargar los ingredientes.");
        }
    }

    private void precargarDatos(ProductoDTO p) {
        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        cmbTipo.setValue(p.getTipo());
        if (p.getIngredientes() != null) {
            listaIngredientesTemporales.addAll(p.getIngredientes());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}