package com.presentacion;

import com.dtos.ReporteComandaDTO;
import excepciones.NegocioException;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Pantalla del Reporte de Comandas.
 * Permite filtrar por rango de fechas, visualizar resultados y exportar a PDF.
 *
 * @author Devora
 */
public class ReporteComandasFrm extends Stage {

    private final ControllerReportes controller;
    private DatePicker dpFechaInicio;
    private DatePicker dpFechaFin;
    private TableView<ReporteComandaDTO> tblComandas;
    private ObservableList<ReporteComandaDTO> datosTabla;
    private Label lblTotalVentas;
    private Label lblResultados;
    private List<ReporteComandaDTO> ultimoResultado;
    private Double ultimoTotalVentas;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public ReporteComandasFrm(ControllerReportes controller) {
        this.controller = controller;
        this.datosTabla = FXCollections.observableArrayList();
        initComponents();
    }

    private void initComponents() {
        VBox root = new VBox(14);
        root.setPadding(new Insets(25, 30, 25, 30));
        root.getStyleClass().add("root-pane");

        Label titulo = new Label("Reporte de Comandas");
        titulo.getStyleClass().add("titulo");

        Label subtitulo = new Label("Consulta las comandas registradas en un rango de fechas");
        subtitulo.getStyleClass().add("resultados-label");

        HBox filtros = crearFiltrosFecha();
        HBox resumen = crearResumen();

        tblComandas = crearTabla();
        VBox.setVgrow(tblComandas, Priority.ALWAYS);

        HBox botonesExportar = crearBotonesExportar();

        root.getChildren().addAll(titulo, subtitulo, filtros, resumen, tblComandas, botonesExportar);

        Scene scene = new Scene(root, 1100, 700);
        setTitle("Reporte de Comandas");
        setScene(scene);
    }

    private HBox crearFiltrosFecha() {
        Label lblDesde = new Label("Desde:");
        lblDesde.getStyleClass().add("form-label");
        dpFechaInicio = new DatePicker(LocalDate.now().minusMonths(1));
        dpFechaInicio.getStyleClass().add("form-field");
        dpFechaInicio.setPrefWidth(160);

        Label lblHasta = new Label("Hasta:");
        lblHasta.getStyleClass().add("form-label");
        dpFechaFin = new DatePicker(LocalDate.now());
        dpFechaFin.getStyleClass().add("form-field");
        dpFechaFin.setPrefWidth(160);

        Button btnGenerar = new Button("Generar Reporte");
        btnGenerar.getStyleClass().add("btn-buscar");
        btnGenerar.setOnAction(e -> generarReporte());

        HBox hbox = new HBox(12, lblDesde, dpFechaInicio, lblHasta, dpFechaFin, btnGenerar);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("card");
        hbox.setPadding(new Insets(16, 20, 16, 20));
        return hbox;
    }

    private HBox crearResumen() {
        lblResultados = new Label(" ");
        lblResultados.getStyleClass().add("resultados-label");

        lblTotalVentas = new Label("");
        lblTotalVentas.getStyleClass().add("reporte-total");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox hbox = new HBox(12, lblResultados, spacer, lblTotalVentas);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    @SuppressWarnings("unchecked")
    private TableView<ReporteComandaDTO> crearTabla() {
        TableView<ReporteComandaDTO> tabla = new TableView<>();
        tabla.setItems(datosTabla);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("Selecciona un rango de fechas y genera el reporte"));

        TableColumn<ReporteComandaDTO, String> colFolio = new TableColumn<>("Folio");
        colFolio.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFolio() != null ? c.getValue().getFolio() : "-"));
        colFolio.setPrefWidth(130);

        TableColumn<ReporteComandaDTO, String> colFecha = new TableColumn<>("Fecha y Hora");
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFechaHora() != null ? SDF.format(c.getValue().getFechaHora()) : "-"));
        colFecha.setPrefWidth(150);

        TableColumn<ReporteComandaDTO, String> colMesa = new TableColumn<>("Mesa");
        colMesa.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getNumMesa() != null ? String.valueOf(c.getValue().getNumMesa()) : "-"));
        colMesa.setPrefWidth(70);

        TableColumn<ReporteComandaDTO, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(c -> new SimpleStringProperty(
                String.format("$%,.2f", c.getValue().getTotal())));
        colTotal.setPrefWidth(110);

        TableColumn<ReporteComandaDTO, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado()));
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label lbl = new Label(item);
                    lbl.getStyleClass().add("estado-" + item.toLowerCase());
                    setGraphic(lbl);
                }
            }
        });
        colEstado.setPrefWidth(110);

        TableColumn<ReporteComandaDTO, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getNombreCliente() != null ? c.getValue().getNombreCliente() : "-"));
        colCliente.setPrefWidth(180);

        tabla.getColumns().addAll(colFolio, colFecha, colMesa, colTotal, colEstado, colCliente);
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
        LocalDate inicio = dpFechaInicio.getValue();
        LocalDate fin = dpFechaFin.getValue();

        if (inicio == null || fin == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecciona ambas fechas para generar el reporte.");
            return;
        }

        Date fechaInicio = Date.from(inicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fechaFin = Date.from(fin.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        try {
            ultimoResultado = controller.generarReporteComanadas(fechaInicio, fechaFin);
            ultimoTotalVentas = controller.calcularTotalVentas(ultimoResultado);

            datosTabla.setAll(ultimoResultado);

            int total = ultimoResultado.size();
            lblResultados.setText(total + " comanda" + (total != 1 ? "s" : "")
                    + " encontrada" + (total != 1 ? "s" : ""));
            lblTotalVentas.setText("Total ventas: " + String.format("$%,.2f", ultimoTotalVentas));
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
        fileChooser.setTitle("Guardar Reporte de Comandas");
        fileChooser.setInitialFileName("reporte_comandas.pdf");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        File archivo = fileChooser.showSaveDialog(this);
        if (archivo != null) {
            Date fechaInicio = Date.from(dpFechaInicio.getValue()
                    .atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date fechaFin = Date.from(dpFechaFin.getValue()
                    .atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
            try {
                controller.exportarPDFComanadas(ultimoResultado, ultimoTotalVentas,
                        fechaInicio, fechaFin, archivo.getAbsolutePath());
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
