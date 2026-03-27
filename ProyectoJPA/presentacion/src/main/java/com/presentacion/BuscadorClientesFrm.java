package com.presentacion;

import BOs.ClienteBO;
import BOs.IClienteBO;
import com.dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

/**
 *
 * @author Devora
 */

public class BuscadorClientesFrm extends Application {

    private TextField txtBuscar;
    private ToggleButton btnNombre;
    private ToggleButton btnTelefono;
    private ToggleButton btnEmail;
    private Label lblResultados;
    private TableView<ClienteFrecuenteDTO> tblClientes;
    private ObservableList<ClienteFrecuenteDTO> datosTabla;
    private IClienteBO clienteBO;
    private String campoBusquedaActual = "nombre";

    @Override
    public void start(Stage stage) {
        clienteBO = new ClienteBO();
        datosTabla = FXCollections.observableArrayList();

        VBox root = crearVistaPrincipal();
        root.getStyleClass().add("root-pane");

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(
                getClass().getResource("/styles/buscador-clientes.css").toExternalForm()
        );

        stage.setTitle("Buscar Clientes");
        stage.setScene(scene);
        stage.show();
    }

    private VBox crearVistaPrincipal() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(25, 30, 25, 30));

        Label lblTitulo = new Label("Buscar Clientes");
        lblTitulo.getStyleClass().add("titulo");

        HBox searchBar = crearBarraBusqueda();
        HBox filtros = crearFiltros();

        lblResultados = new Label(" ");
        lblResultados.getStyleClass().add("resultados-label");

        tblClientes = crearTabla();
        VBox.setVgrow(tblClientes, Priority.ALWAYS);

        root.getChildren().addAll(lblTitulo, searchBar, filtros, lblResultados, tblClientes);
        return root;
    }

    private HBox crearBarraBusqueda() {
        txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por nombre, telefono o correo...");
        txtBuscar.getStyleClass().add("search-field");
        txtBuscar.setOnAction(e -> buscarClientes());
        HBox.setHgrow(txtBuscar, Priority.ALWAYS);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("btn-buscar");
        btnBuscar.setOnAction(e -> buscarClientes());

        HBox hbox = new HBox(10, txtBuscar, btnBuscar);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private HBox crearFiltros() {
        ToggleGroup grupo = new ToggleGroup();

        btnNombre = new ToggleButton("Nombre");
        btnTelefono = new ToggleButton("Telefono");
        btnEmail = new ToggleButton("Email");

        btnNombre.setToggleGroup(grupo);
        btnTelefono.setToggleGroup(grupo);
        btnEmail.setToggleGroup(grupo);

        btnNombre.getStyleClass().add("filtro-btn");
        btnTelefono.getStyleClass().add("filtro-btn");
        btnEmail.getStyleClass().add("filtro-btn");

        btnNombre.setSelected(true);

        grupo.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null && oldVal != null) {
                oldVal.setSelected(true);
            }
        });

        btnNombre.setOnAction(e -> campoBusquedaActual = "nombre");
        btnTelefono.setOnAction(e -> campoBusquedaActual = "telefono");
        btnEmail.setOnAction(e -> campoBusquedaActual = "correo");

        HBox hbox = new HBox(8, btnNombre, btnTelefono, btnEmail);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    @SuppressWarnings("unchecked")
    private TableView<ClienteFrecuenteDTO> crearTabla() {
        TableView<ClienteFrecuenteDTO> tabla = new TableView<>();
        tabla.setItems(datosTabla);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("Busca un cliente para ver resultados"));

        // Columna: Nombre
        TableColumn<ClienteFrecuenteDTO, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getNombre()));
        colNombre.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label lbl = new Label(item);
                    lbl.getStyleClass().add("nombre-cell");
                    setGraphic(lbl);
                }
            }
        });
        colNombre.setPrefWidth(200);

        // Columna: Telefono
        TableColumn<ClienteFrecuenteDTO, String> colTelefono = new TableColumn<>("Telefono");
        colTelefono.setCellValueFactory(cell ->
                new SimpleStringProperty(enmascararTelefono(cell.getValue().getTelefono())));
        colTelefono.setPrefWidth(110);

        // Columna: Email
        TableColumn<ClienteFrecuenteDTO, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getCorreo() != null ? cell.getValue().getCorreo() : "-"));
        colEmail.setPrefWidth(220);

        // Columna: Puntos
        TableColumn<ClienteFrecuenteDTO, String> colPuntos = new TableColumn<>("Puntos");
        colPuntos.setCellValueFactory(cell -> {
            Integer pts = cell.getValue().getPuntosAcumulados();
            return new SimpleStringProperty((pts != null ? pts : 0) + " pts");
        });
        colPuntos.setPrefWidth(90);

        // Columna: Visitas
        TableColumn<ClienteFrecuenteDTO, Number> colVisitas = new TableColumn<>("Visitas");
        colVisitas.setCellValueFactory(cell -> {
            Integer v = cell.getValue().getNumVisitas();
            return new SimpleIntegerProperty(v != null ? v : 0);
        });
        colVisitas.setPrefWidth(70);

        // Columna: Acciones
        TableColumn<ClienteFrecuenteDTO, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnVerPerfil = new Button("Ver Perfil");
            private final Button btnVincular = new Button("Vincular");
            private final HBox contenedor = new HBox(8, btnVerPerfil, btnVincular);

            {
                btnVerPerfil.getStyleClass().add("btn-ver-perfil");
                btnVincular.getStyleClass().add("btn-vincular");
                contenedor.setAlignment(Pos.CENTER_LEFT);
                contenedor.setPadding(new Insets(0, 0, 0, 5));

                btnVerPerfil.setOnAction(e -> {
                    ClienteFrecuenteDTO dto = getTableView().getItems().get(getIndex());
                    verPerfil(dto);
                });

                btnVincular.setOnAction(e -> {
                    ClienteFrecuenteDTO dto = getTableView().getItems().get(getIndex());
                    vincularCliente(dto);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : contenedor);
            }
        });
        colAcciones.setPrefWidth(200);

        tabla.getColumns().addAll(
                colNombre, colTelefono, colEmail, colPuntos, colVisitas, colAcciones);
        return tabla;
    }

    // ==================== Logica de busqueda ====================

    private void buscarClientes() {
        String filtro = txtBuscar.getText().trim();
        if (filtro.isEmpty()) {
            datosTabla.clear();
            lblResultados.setText(" ");
            return;
        }

        try {
            List<ClienteFrecuenteDTO> clientes =
                    clienteBO.buscarFrecuentesPorFiltro(filtro, campoBusquedaActual);
            datosTabla.setAll(clientes);

            int total = clientes.size();
            lblResultados.setText(total + " resultado" + (total != 1 ? "s" : "")
                    + " encontrado" + (total != 1 ? "s" : "")
                    + " para '" + filtro + "'");
        } catch (NegocioException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error al buscar clientes: " + ex.getMessage());
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    private String enmascararTelefono(String telefono) {
        if (telefono == null || telefono.length() < 4) return "****";
        return "****" + telefono.substring(telefono.length() - 4);
    }

    // ==================== Acciones placeholder ====================

    private void verPerfil(ClienteFrecuenteDTO cliente) {
        SistemaPuntosFrm ventanaPuntos = new SistemaPuntosFrm(cliente);
        ventanaPuntos.show();
    }

    private void vincularCliente(ClienteFrecuenteDTO cliente) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Vincular cliente: " + cliente.getNombre() + " (ID: " + cliente.getId() + ") a comanda");
        alert.setTitle("Vincular Cliente");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    // ==================== Main ====================

    public static void main(String[] args) {
        launch(args);
    }
}
