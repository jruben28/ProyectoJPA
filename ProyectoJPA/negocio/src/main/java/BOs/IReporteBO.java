package BOs;

import com.dtos.ReporteClienteFrecuenteDTO;
import com.dtos.ReporteComandaDTO;
import excepciones.NegocioException;
import java.util.Date;
import java.util.List;

/**
 * Interfaz de logica de negocio para reportes.
 *
 * @author Devora
 */
public interface IReporteBO {

    List<ReporteComandaDTO> generarReporteComandasPorFechas(Date fechaInicio, Date fechaFin) throws NegocioException;

    Double calcularTotalVentasPeriodo(List<ReporteComandaDTO> comandas);

    List<ReporteClienteFrecuenteDTO> generarReporteClientesFrecuentes(String filtroNombre, Integer minimoVisitas) throws NegocioException;

    void generarPDFReporteComanadas(List<ReporteComandaDTO> comandas, Double totalVentas,
                                     Date fechaInicio, Date fechaFin, String rutaArchivo) throws NegocioException;

    void generarPDFReporteClientes(List<ReporteClienteFrecuenteDTO> clientes, String rutaArchivo) throws NegocioException;
}
