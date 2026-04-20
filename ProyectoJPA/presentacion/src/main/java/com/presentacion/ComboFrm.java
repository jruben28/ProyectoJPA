package com.presentacion;

import com.dtos.ComboDTO;
import excepciones.PersistenciaException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

/**
 * Pantalla de gestión de Combos .
 */
public class ComboFrm {

    private final ControllerCombo controller;
    private VBox root;
    private TextField txtBuscar;
    private TableView<ComboDTO> tblCombos;
    private ObservableList<ComboDTO> datosTabla;
    private Label lblResultados;

    public ComboFrm(ControllerCombo controller) {
        this.controller = controller;
        this.datosTabla = FXCollections.observableArrayList();
        construirPantalla();
        cargarTodosLosCombos();
    }

    public VBox getRoot() {
        return root;
    }

    private void construirPantalla() {
        root = new VBox(15);
        root.setPadding(new Insets(25, 30, 25, 30));
        root.getStyleClass().add("root-pane");

        Label lblTitulo = new Label("Gestión de Combos");
        lblTitulo.getStyleClass().add("titulo");

        // Barra superior con botones de navegación
        HBox barraNavegacion = new HBox(10);
        barraNavegacion.setAlignment(Pos.CENTER_LEFT);

        Button btnMenu = new Button("← Menú principal");
        btnMenu.getStyleClass().add("btn-cancelar");
        btnMenu.setOnAction(e -> controller.volverAlMenu());

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        Button btnDisponibilidad = new Button("Ver Disponibilidad");
        btnDisponibilidad.getStyleClass().add("btn-buscar");
        btnDisponibilidad.setOnAction(e -> controller.mostrarDisponibilidad());

        Button btnNuevo = new Button("+ Nuevo Combo");
        btnNuevo.getStyleClass().add("btn-buscar");
        btnNuevo.setOnAction(e -> controller.mostrarCreacion());

        barraNavegacion.getChildren().addAll(btnMenu, espaciador, btnDisponibilidad, btnNuevo);

        // Barra de búsqueda
        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);

        txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por nombre...");
        txtBuscar.getStyleClass().add("search-field");
        txtBuscar.setPrefWidth(300);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("btn-buscar");
        btnBuscar.setOnAction(e -> buscarCombos());

        Button btnMostrarTodos = new Button("Mostrar todos");
        btnMostrarTodos.getStyleClass().add("btn-cancelar");
        btnMostrarTodos.setOnAction(e -> cargarTodosLosCombos());

        barraBusqueda.getChildren().addAll(txtBuscar, btnBuscar, btnMostrarTodos);

        lblResultados = new Label(" ");
        lblResultados.getStyleClass().add("resultados-label");

        tblCombos = construirTabla();
        VBox.setVgrow(tblCombos, Priority.ALWAYS);

        root.getChildren().addAll(lblTitulo, barraNavegacion, barraBusqueda, lblResultados, tblCombos);
    }

    @SuppressWarnings("unchecked")
    private TableView<ComboDTO> construirTabla() {
        TableView<ComboDTO> tabla = new TableView<>();
        tabla.setItems(datosTabla);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("No hay combos registrados"));

        TableColumn<ComboDTO, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(fila -> new SimpleStringProperty(fila.getValue().getNombre()));

        TableColumn<ComboDTO, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(fila -> {
            String desc = fila.getValue().getDescripcion();
            return new SimpleStringProperty(desc != null ? desc : "-");
        });

        TableColumn<ComboDTO, String> colPrecioOriginal = new TableColumn<>("Precio Original");
        colPrecioOriginal.setCellValueFactory(fila
                -> new SimpleStringProperty(String.format("$%.2f", fila.getValue().getPrecioOriginal())));

        TableColumn<ComboDTO, String> colPrecioCombo = new TableColumn<>("Precio Combo");
        colPrecioCombo.setCellValueFactory(fila
                -> new SimpleStringProperty(String.format("$%.2f", fila.getValue().getPrecioCombo())));

        TableColumn<ComboDTO, String> colAhorro = new TableColumn<>("Ahorro");
        colAhorro.setCellValueFactory(fila -> {
            double ahorro = fila.getValue().getPrecioOriginal() - fila.getValue().getPrecioCombo();
            return new SimpleStringProperty(String.format("$%.2f", ahorro));
        });

        TableColumn<ComboDTO, String> colDescuento = new TableColumn<>("Descuento");
        colDescuento.setCellValueFactory(fila
                -> new SimpleStringProperty(fila.getValue().getPorcentajeDescuento() + "%"));

        TableColumn<ComboDTO, Boolean> colActivo = new TableColumn<>("Estado");
        colActivo.setCellValueFactory(fila -> new SimpleBooleanProperty(fila.getValue().getActivo()));
        colActivo.setCellFactory(col -> new TableCell<ComboDTO, Boolean>() {
            @Override
            protected void updateItem(Boolean activo, boolean vacio) {
                super.updateItem(activo, vacio);
                if (vacio || activo == null) {
                    setText(null);
                    setStyle("");
                } else if (activo) {
                    setText("Activo");
                    setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
                } else {
                    setText("Inactivo");
                    setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                }
            }
        });

        TableColumn<ComboDTO, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<ComboDTO, Void>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEstado = new Button("Activar/Desactivar");
            private final HBox caja = new HBox(8, btnEditar, btnEstado);

            {
                btnEditar.getStyleClass().add("btn-ver-perfil");
                btnEstado.getStyleClass().add("btn-vincular");
                caja.setAlignment(Pos.CENTER_LEFT);

                btnEditar.setOnAction(e -> {
                    ComboDTO dto = getTableView().getItems().get(getIndex());
                    mostrarFormularioEdicion(dto);
                });
                btnEstado.setOnAction(e -> {
                    ComboDTO dto = getTableView().getItems().get(getIndex());
                    cambiarEstado(dto);
                });
            }

            @Override
            protected void updateItem(Void item, boolean vacio) {
                super.updateItem(item, vacio);
                setGraphic(vacio ? null : caja);
            }
        });

        tabla.getColumns().addAll(colNombre, colDescripcion, colPrecioOriginal,
                colPrecioCombo, colAhorro, colDescuento, colActivo, colAcciones);
        return tabla;
    }

    private void cargarTodosLosCombos() {
        try {
            List<ComboDTO> combos = controller.obtenerTodosCombos();
            datosTabla.setAll(combos);
            lblResultados.setText(combos.size() + " combos encontrados.");
            txtBuscar.clear();
        } catch (PersistenciaException e) {
            mostrarAlerta("Error al cargar combos: " + e.getMessage());
        }
    }

    private void buscarCombos() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            cargarTodosLosCombos();
            return;
        }
        try {
            List<ComboDTO> combos = controller.buscarCombosPorNombre(texto);
            datosTabla.setAll(combos);
            lblResultados.setText(combos.size() + " resultados para '" + texto + "'");
        } catch (PersistenciaException e) {
            mostrarAlerta("Error al buscar: " + e.getMessage());
        }
    }

    private void mostrarFormularioEdicion(ComboDTO combo) {
        Dialog<ComboDTO> dialogo = new Dialog<>();
        dialogo.setTitle("Editar Combo");
        dialogo.setHeaderText("Modifica los datos del combo");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialogo.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        TextField txtNombre = new TextField(combo.getNombre());
        TextField txtDescripcion = new TextField(combo.getDescripcion() != null ? combo.getDescripcion() : "");
        TextField txtDescuento = new TextField(String.valueOf(combo.getPorcentajeDescuento()));

        double precioOriginal = combo.getPrecioOriginal();
        Label lblPrecioOriginal = new Label(String.format("Precio original: $%.2f", precioOriginal));
        Label lblPrecioFinal = new Label(String.format("Precio final: $%.2f", combo.getPrecioCombo()));

        txtDescuento.textProperty().addListener((obs, anterior, nuevo) -> {
            try {
                int descuento = Integer.parseInt(nuevo.trim());
                if (descuento >= 0 && descuento <= 100) {
                    double precioFinal = precioOriginal * (1 - descuento / 100.0);
                    lblPrecioFinal.setText(String.format("Precio final: $%.2f", precioFinal));
                }
            } catch (NumberFormatException ex) {
                lblPrecioFinal.setText("Precio final: -");
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1);
        grid.add(txtDescripcion, 1, 1);
        grid.add(lblPrecioOriginal, 0, 2, 2, 1);
        grid.add(new Label("% Descuento:"), 0, 3);
        grid.add(txtDescuento, 1, 3);
        grid.add(lblPrecioFinal, 0, 4, 2, 1);
        dialogo.getDialogPane().setContent(grid);

        dialogo.setResultConverter(boton -> {
            if (boton == btnGuardar) {
                try {
                    int descuento = Integer.parseInt(txtDescuento.getText().trim());
                    ComboDTO dto = new ComboDTO();
                    dto.setNombre(txtNombre.getText().trim());
                    dto.setDescripcion(txtDescripcion.getText().trim());
                    dto.setPrecioOriginal(precioOriginal);
                    dto.setPrecioCombo(precioOriginal * (1 - descuento / 100.0));
                    dto.setPorcentajeDescuento(descuento);
                    dto.setActivo(combo.getActivo());
                    return dto;
                } catch (NumberFormatException e) {
                    mostrarAlerta("El descuento debe ser un número entero.");
                    return null;
                }
            }
            return null;
        });

        dialogo.showAndWait().ifPresent(dto -> {
            try {
                controller.actualizarCombo(combo.getId(), dto);
                cargarTodosLosCombos();
            } catch (PersistenciaException e) {
                mostrarAlerta("Error al actualizar: " + e.getMessage());
            }
        });
    }

    private void cambiarEstado(ComboDTO combo) {
        try {
            controller.cambiarEstadoCombo(combo.getId(), !combo.getActivo());
            cargarTodosLosCombos();
        } catch (PersistenciaException e) {
            mostrarAlerta("Error al cambiar estado: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR, mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}
