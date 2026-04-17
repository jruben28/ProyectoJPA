package com.presentacion;

import com.dtos.ReporteClienteFrecuenteDTO;
import excepciones.NegocioException;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Pantalla del Reporte de Clientes Frecuentes.
 * Permite filtrar por nombre o minimo de visitas, visualizar y exportar a PDF.
 *
 * @author Devora
 */
public class ReporteClientesFrecuentesFrm extends Stage {

    private final ControllerReportes controller;
    private TextField txtFiltroNombre;
    private Spinner<Integer> spnMinimoVisitas;
    private TableView<ReporteClienteFrecuenteDTO> tblClientes;
    private ObservableList<ReporteClienteFrecuenteDTO> datosTabla;
    private Label lblResultados;
    private List<ReporteClienteFrecuenteDTO> ultimoResultado;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    public ReporteClientesFrecuentesFrm(ControllerReportes controller) {
        this.controller = controller;
        this.datosTabla = FXCollections.observableArrayList();
        initComponents();
    }

    private void initComponents() {
        VBox root = new VBox(14);
        root.setPadding(new Insets(25, 30, 25, 30));
        root.getStyleClass().add("root-pane");

        Label titulo = new Label("Reporte de Clientes Frecuentes");
        titulo.getStyleClass().add("titulo");

        Label subtitulo = new Label("Consulta el comportamiento de consumo de los clientes registrados");
        subtitulo.getStyleClass().add("resultados-label");

        HBox filtros = crearFiltros();

        lblResultados = new Label(" ");
        lblResultados.getStyleClass().add("resultados-label");

        tblClientes = crearTabla();
        VBox.setVgrow(tblClientes, Priority.ALWAYS);

        HBox botonesExportar = crearBotonesExportar();

        root.getChildren().addAll(titulo, subtitulo, filtros, lblResultados, tblClientes, botonesExportar);

        Scene scene = new Scene(root, 1000, 650);
        setTitle("Reporte de Clientes Frecuentes");
        setScene(scene);
    }

    private HBox crearFiltros() {
        Label lblNombre = new Label("Nombre:");
        lblNombre.getStyleClass().add("form-label");
        txtFiltroNombre = new TextField();
        txtFiltroNombre.setPromptText("Filtrar por nombre...");
        txtFiltroNombre.getStyleClass().add("form-field");
        txtFiltroNombre.setPrefWidth(200);

        Label lblVisitas = new Label("Min. visitas:");
        lblVisitas.getStyleClass().add("form-label");
        spnMinimoVisitas = new Spinner<>(0, 9999, 0);
        spnMinimoVisitas.setPrefWidth(90);
        spnMinimoVisitas.setEditable(true);

        Button btnGenerar = new Button("Generar Reporte");
        btnGenerar.getStyleClass().add("btn-buscar");
        btnGenerar.setOnAction(e -> generarReporte());

        HBox hbox = new HBox(12, lblNombre, txtFiltroNombre, lblVisitas, spnMinimoVisitas, btnGenerar);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("card");
        hbox.setPadding(new Insets(16, 20, 16, 20));
        return hbox;
    }

    @SuppressWarnings("unchecked")
    private TableView<ReporteClienteFrecuenteDTO> crearTabla() {
        TableView<ReporteClienteFrecuenteDTO> tabla = new TableView<>();
        tabla.setItems(datosTabla);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("Genera el reporte para ver los resultados"));

        TableColumn<ReporteClienteFrecuenteDTO, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
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
        colNombre.setPrefWidth(250);

        TableColumn<ReporteClienteFrecuenteDTO, Number> colVisitas = new TableColumn<>("Visitas");
        colVisitas.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getNumVisitas() != null ? c.getValue().getNumVisitas() : 0));
        colVisitas.setPrefWidth(100);

        TableColumn<ReporteClienteFrecuenteDTO, String> colTotal = new TableColumn<>("Total Gastado");
        colTotal.setCellValueFactory(c -> new SimpleStringProperty(
                String.format("$%,.2f", c.getValue().getTotalGastado())));
        colTotal.setPrefWidth(150);

        TableColumn<ReporteClienteFrecuenteDTO, String> colUltima = new TableColumn<>("Ultima Comanda");
        colUltima.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFechaUltimaComanda() != null
                        ? SDF.format(c.getValue().getFechaUltimaComanda())
                        : "Sin comandas"));
        colUltima.setPrefWidth(150);

        tabla.getColumns().addAll(colNombre, colVisitas, colTotal, colUltima);
        return tabla;
    }

    private HBox crearBotonesExportar() {
        Button btnExportarPDF = new Button("Exportar a PDF");
        btnExportarPDF.getStyleClass().add("btn-exportar");
        btnExportarPDF.setOnAction(e -> exportarPDF());

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.getStyleClass().add("btn-cancelar");
        btnCerrar.setOnAction(e -> close());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox hbox = new HBox(12, spacer, btnCerrar, btnExportarPDF);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        return hbox;
    }

    // ==================== Logica (delega al controlador) ====================

    private void generarReporte() {
        String filtroNombre = txtFiltroNombre.getText().trim();
        Integer minimoVisitas = spnMinimoVisitas.getValue();

        if (filtroNombre.isEmpty()) filtroNombre = null;
        if (minimoVisitas != null && minimoVisitas == 0) minimoVisitas = null;

        try {
            ultimoResultado = controller.generarReporteClientes(filtroNombre, minimoVisitas);
            datosTabla.setAll(ultimoResultado);

            int total = ultimoResultado.size();
            lblResultados.setText(total + " cliente" + (total != 1 ? "s" : "")
                    + " encontrado" + (total != 1 ? "s" : ""));
        } catch (NegocioException ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al generar reporte: " + ex.getMessage());
        }
    }

    private void exportarPDF() {
        if (ultimoResultado == null || ultimoResultado.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "No hay datos para exportar. Genera el reporte primero.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte de Clientes Frecuentes");
        fileChooser.setInitialFileName("reporte_clientes.pdf");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        File archivo = fileChooser.showSaveDialog(this);
        if (archivo != null) {
            try {
                controller.exportarPDFClientes(ultimoResultado, archivo.getAbsolutePath());
                mostrarAlerta(Alert.AlertType.INFORMATION, "PDF generado exitosamente en:\n" + archivo.getAbsolutePath());
            } catch (NegocioException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al generar PDF: " + ex.getMessage());
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
