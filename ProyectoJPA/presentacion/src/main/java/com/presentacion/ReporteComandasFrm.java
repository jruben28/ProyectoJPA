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
 * Pantalla del Reporte de Ventas (anteriormente Reporte de Comandas).
 * Replica el layout del mockup CU24: barra de filtros unificada, tarjetas KPI,
 * tabla con columnas Fecha/Folio/Mesa/Mesero/Items/Total/Estado y fila de totales.
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
    private Label lblComandasCount;
    private Label lblTicketPromedio;
    private Label lblCanceladas;
    private Label lblTotalesFooter;
    private List<ReporteComandaDTO> ultimoResultado;
    private Double ultimoTotalVentas;
    private int ultimoNumComandas;
    private int ultimoCanceladas;
    private double ultimoTicketPromedio;
    private static final SimpleDateFormat SDF_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    public ReporteComandasFrm(ControllerReportes controller) {
        this.controller = controller;
        this.datosTabla = FXCollections.observableArrayList();
        initComponents();
    }

    private void initComponents() {
        VBox root = new VBox(14);
        root.setPadding(new Insets(25, 30, 25, 30));
        root.getStyleClass().add("root-pane");

        Label titulo = new Label("Reporte de Ventas");
        titulo.getStyleClass().add("titulo");

        HBox filtros = crearFiltrosYAcciones();
        HBox kpis = crearTarjetasKPI();

        tblComandas = crearTabla();
        VBox.setVgrow(tblComandas, Priority.ALWAYS);

        HBox totalesFooter = crearFilaTotales();
        HBox botonCerrar = crearBotonCerrar();

        root.getChildren().addAll(titulo, filtros, kpis, tblComandas, totalesFooter, botonCerrar);

        Scene scene = new Scene(root, 1200, 760);
        setTitle("Reporte de Ventas");
        setScene(scene);
    }

    private HBox crearFiltrosYAcciones() {
        Label lblDesde = new Label("Fecha Inicio");
        lblDesde.getStyleClass().add("form-label");
        dpFechaInicio = new DatePicker(LocalDate.now().minusMonths(1));
        dpFechaInicio.getStyleClass().add("form-field");
        dpFechaInicio.setPrefWidth(160);

        Label lblHasta = new Label("Fecha Fin");
        lblHasta.getStyleClass().add("form-label");
        dpFechaFin = new DatePicker(LocalDate.now());
        dpFechaFin.getStyleClass().add("form-field");
        dpFechaFin.setPrefWidth(160);

        VBox colDesde = new VBox(4, lblDesde, dpFechaInicio);
        VBox colHasta = new VBox(4, lblHasta, dpFechaFin);

        Button btnGenerar = new Button("Generar Reporte");
        btnGenerar.getStyleClass().add("btn-buscar");
        btnGenerar.setOnAction(e -> generarReporte());

        Button btnExportarPDF = new Button("Exportar PDF");
        btnExportarPDF.getStyleClass().add("btn-exportar");
        btnExportarPDF.setOnAction(e -> exportarPDF());

        HBox hbox = new HBox(14, colDesde, colHasta, btnGenerar, btnExportarPDF);
        hbox.setAlignment(Pos.BOTTOM_LEFT);
        hbox.getStyleClass().add("card");
        hbox.setPadding(new Insets(16, 20, 16, 20));
        return hbox;
    }

    private HBox crearTarjetasKPI() {
        lblTotalVentas = new Label("$0.00");
        lblTotalVentas.getStyleClass().addAll("stat-value", "stat-value-azul");
        VBox tarjetaTotal = crearTarjetaKPI("TOTAL VENTAS", lblTotalVentas);

        lblComandasCount = new Label("0");
        lblComandasCount.getStyleClass().addAll("stat-value", "stat-value-oscuro");
        VBox tarjetaComandas = crearTarjetaKPI("COMANDAS", lblComandasCount);

        lblTicketPromedio = new Label("$0.00");
        lblTicketPromedio.getStyleClass().addAll("stat-value", "stat-value-azul");
        VBox tarjetaTicket = crearTarjetaKPI("TICKET PROMEDIO", lblTicketPromedio);

        lblCanceladas = new Label("0");
        lblCanceladas.getStyleClass().addAll("stat-value", "stat-value-rojo");
        VBox tarjetaCanceladas = crearTarjetaKPI("CANCELADAS", lblCanceladas);

        HBox.setHgrow(tarjetaTotal, Priority.ALWAYS);
        HBox.setHgrow(tarjetaComandas, Priority.ALWAYS);
        HBox.setHgrow(tarjetaTicket, Priority.ALWAYS);
        HBox.setHgrow(tarjetaCanceladas, Priority.ALWAYS);

        HBox hbox = new HBox(14, tarjetaTotal, tarjetaComandas, tarjetaTicket, tarjetaCanceladas);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private VBox crearTarjetaKPI(String texto, Label valor) {
        Label lbl = new Label(texto);
        lbl.getStyleClass().add("stat-label");
        VBox vbox = new VBox(6, lbl, valor);
        vbox.getStyleClass().add("stat-card");
        vbox.setAlignment(Pos.CENTER_LEFT);
        return vbox;
    }

    @SuppressWarnings("unchecked")
    private TableView<ReporteComandaDTO> crearTabla() {
        TableView<ReporteComandaDTO> tabla = new TableView<>();
        tabla.setItems(datosTabla);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("Selecciona un rango de fechas y genera el reporte"));

        TableColumn<ReporteComandaDTO, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFechaHora() != null ? SDF_FECHA.format(c.getValue().getFechaHora()) : "-"));
        colFecha.setPrefWidth(100);

        TableColumn<ReporteComandaDTO, String> colFolio = new TableColumn<>("Folio");
        colFolio.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFolio() != null ? c.getValue().getFolio() : "-"));
        colFolio.setPrefWidth(160);

        TableColumn<ReporteComandaDTO, String> colMesa = new TableColumn<>("Mesa");
        colMesa.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getNumMesa() != null ? "Mesa " + c.getValue().getNumMesa() : "-"));
        colMesa.setPrefWidth(90);

        TableColumn<ReporteComandaDTO, String> colMesero = new TableColumn<>("Mesero");
        colMesero.setCellValueFactory(c -> new SimpleStringProperty("-"));
        colMesero.setPrefWidth(130);

        TableColumn<ReporteComandaDTO, String> colItems = new TableColumn<>("Items");
        colItems.setCellValueFactory(c -> new SimpleStringProperty("-"));
        colItems.setPrefWidth(70);

        TableColumn<ReporteComandaDTO, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getTotal() != null ? String.format("$%,.2f", c.getValue().getTotal()) : "-"));
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
                    Label lbl = new Label(capitalizar(item));
                    lbl.getStyleClass().add("estado-" + item.toLowerCase());
                    setGraphic(lbl);
                }
            }
        });
        colEstado.setPrefWidth(120);

        tabla.getColumns().addAll(colFecha, colFolio, colMesa, colMesero, colItems, colTotal, colEstado);
        return tabla;
    }

    private HBox crearFilaTotales() {
        Region spacerLeft = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);

        lblTotalesFooter = new Label("Items: -     Total: $0.00");
        lblTotalesFooter.getStyleClass().add("totales-label");

        HBox hbox = new HBox(lblTotalesFooter);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.getStyleClass().add("totales-row");
        return hbox;
    }

    private HBox crearBotonCerrar() {
        Button btnCerrar = new Button("Cerrar");
        btnCerrar.getStyleClass().add("btn-cancelar");
        btnCerrar.setOnAction(e -> close());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox hbox = new HBox(12, spacer, btnCerrar);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        return hbox;
    }

    private String capitalizar(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.charAt(0) + s.substring(1).toLowerCase();
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
            ultimoNumComandas = ultimoResultado.size();
            ultimoCanceladas = (int) ultimoResultado.stream()
                    .filter(c -> c.getEstado() != null && "CANCELADA".equalsIgnoreCase(c.getEstado()))
                    .count();
            ultimoTicketPromedio = ultimoNumComandas == 0 ? 0.0 : ultimoTotalVentas / ultimoNumComandas;

            datosTabla.setAll(ultimoResultado);

            lblTotalVentas.setText(String.format("$%,.2f", ultimoTotalVentas));
            lblComandasCount.setText(String.valueOf(ultimoNumComandas));
            lblTicketPromedio.setText(String.format("$%,.2f", ultimoTicketPromedio));
            lblCanceladas.setText(String.valueOf(ultimoCanceladas));
            lblTotalesFooter.setText(String.format("Items: -     Total: $%,.2f", ultimoTotalVentas));
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
        fileChooser.setTitle("Guardar Reporte de Ventas");
        fileChooser.setInitialFileName("reporte_ventas.pdf");
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
                        ultimoNumComandas, ultimoCanceladas, ultimoTicketPromedio,
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
