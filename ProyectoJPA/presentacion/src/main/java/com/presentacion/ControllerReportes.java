package com.presentacion;

import BOs.IReporteBO;
import BOs.ReporteBO;
import com.dtos.ReporteClienteFrecuenteDTO;
import com.dtos.ReporteComandaDTO;
import excepciones.NegocioException;

import java.util.Date;
import java.util.List;

/**
 * Controlador del modulo de Reportes.
 * Las pantallas de reportes delegan toda la logica aqui.
 *
 * @author Devora
 */
public class ControllerReportes {

    private final IReporteBO reporteBO;
    private static final String CSS_PATH = "/styles/buscador-clientes.css";

    public ControllerReportes() {
        this.reporteBO = new ReporteBO();
    }

    // ==================== Navegacion ====================

    public void mostrarReporteComanadas() {
        ReporteComandasFrm ventana = new ReporteComandasFrm(this);
        ventana.getScene().getStylesheets().add(
                getClass().getResource(CSS_PATH).toExternalForm());
        ventana.show();
    }

    public void mostrarReporteClientes() {
        ReporteClientesFrecuentesFrm ventana = new ReporteClientesFrecuentesFrm(this);
        ventana.getScene().getStylesheets().add(
                getClass().getResource(CSS_PATH).toExternalForm());
        ventana.show();
    }

    // ==================== Logica de negocio ====================

    public List<ReporteComandaDTO> generarReporteComanadas(Date fechaInicio, Date fechaFin) throws NegocioException {
        return reporteBO.generarReporteComandasPorFechas(fechaInicio, fechaFin);
    }

    public Double calcularTotalVentas(List<ReporteComandaDTO> comandas) {
        return reporteBO.calcularTotalVentasPeriodo(comandas);
    }

    public List<ReporteClienteFrecuenteDTO> generarReporteClientes(String filtroNombre, Integer minimoVisitas) throws NegocioException {
        return reporteBO.generarReporteClientesFrecuentes(filtroNombre, minimoVisitas);
    }

    public void exportarPDFComanadas(List<ReporteComandaDTO> comandas, Double totalVentas,
                                      Date fechaInicio, Date fechaFin, String rutaArchivo) throws NegocioException {
        reporteBO.generarPDFReporteComanadas(comandas, totalVentas, fechaInicio, fechaFin, rutaArchivo);
    }

    public void exportarPDFClientes(List<ReporteClienteFrecuenteDTO> clientes, String rutaArchivo) throws NegocioException {
        reporteBO.generarPDFReporteClientes(clientes, rutaArchivo);
    }
}
