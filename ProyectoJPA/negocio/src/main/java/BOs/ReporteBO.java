package BOs;

import DAOs.IReporteDAO;
import DAOs.ReporteDAO;
import Entidades.ClienteFrecuente;
import Entidades.Comanda;
import com.dtos.ReporteClienteFrecuenteDTO;
import com.dtos.ReporteComandaDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import utilerias.GeneradorPDF;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Business Object para la generacion de reportes.
 *
 * @author Devora
 */
public class ReporteBO implements IReporteBO {

    private final IReporteDAO reporteDAO;
    private static final Logger LOG = Logger.getLogger(ReporteBO.class.getName());

    public ReporteBO() {
        this.reporteDAO = new ReporteDAO();
    }

    @Override
    public List<ReporteComandaDTO> generarReporteComandasPorFechas(Date fechaInicio, Date fechaFin) throws NegocioException {
        if (fechaInicio == null || fechaFin == null) {
            throw new NegocioException("Las fechas de inicio y fin son obligatorias");
        }
        if (fechaInicio.after(fechaFin)) {
            throw new NegocioException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        try {
            List<Comanda> comandas = reporteDAO.buscarComandasPorRangoFechas(fechaInicio, fechaFin);
            List<ReporteComandaDTO> resultado = new ArrayList<>();

            for (Comanda c : comandas) {
                String nombreCliente = "Cliente General";
                if (c.getCliente() != null && c.getCliente().getNombre() != null) {
                    nombreCliente = c.getCliente().getNombre();
                }

                ReporteComandaDTO dto = new ReporteComandaDTO(
                        c.getFolio() != null ? c.getFolio() : "OB-" + String.format("%06d", c.getId()),
                        c.getFechaHora(),
                        c.getNumMesa(),
                        c.getTotal(),
                        c.getEstado().name(),
                        nombreCliente
                );
                resultado.add(dto);
            }
            return resultado;
        } catch (PersistenciaException ex) {
            LOG.warning("Error al generar reporte de comandas: " + ex.getMessage());
            throw new NegocioException("Error al generar reporte de comandas");
        }
    }

    @Override
    public Double calcularTotalVentasPeriodo(List<ReporteComandaDTO> comandas) {
        Double total = 0.0;
        for (ReporteComandaDTO c : comandas) {
            if (c.getTotal() != null) {
                total += c.getTotal();
            }
        }
        return total;
    }

    @Override
    public List<ReporteClienteFrecuenteDTO> generarReporteClientesFrecuentes(String filtroNombre, Integer minimoVisitas) throws NegocioException {
        try {
            List<ClienteFrecuente> clientes = reporteDAO.obtenerTodosClientesFrecuentes();
            List<ReporteClienteFrecuenteDTO> resultado = new ArrayList<>();

            for (ClienteFrecuente cf : clientes) {
                // Filtrar por nombre si se proporcionó
                if (filtroNombre != null && !filtroNombre.trim().isEmpty()) {
                    if (!cf.getNombre().toLowerCase().contains(filtroNombre.toLowerCase())) {
                        continue;
                    }
                }

                List<Comanda> comandasCliente = reporteDAO.buscarComandasEntregadasPorCliente(cf.getId());
                int numVisitas = comandasCliente.size();

                // Filtrar por minimo de visitas si se proporcionó
                if (minimoVisitas != null && numVisitas < minimoVisitas) {
                    continue;
                }

                Double totalGastado = 0.0;
                for (Comanda cmd : comandasCliente) {
                    totalGastado += cmd.getTotal();
                }
                if (totalGastado < 0) totalGastado = 0.0;

                Comanda ultimaComanda = reporteDAO.buscarUltimaComandaPorCliente(cf.getId());
                Date fechaUltima = ultimaComanda != null ? ultimaComanda.getFechaHora() : null;

                ReporteClienteFrecuenteDTO dto = new ReporteClienteFrecuenteDTO(
                        cf.getNombre(), numVisitas, totalGastado, fechaUltima
                );
                resultado.add(dto);
            }

            return resultado;
        } catch (PersistenciaException ex) {
            LOG.warning("Error al generar reporte de clientes: " + ex.getMessage());
            throw new NegocioException("Error al generar reporte de clientes frecuentes");
        }
    }

    @Override
    public void generarPDFReporteComanadas(List<ReporteComandaDTO> comandas, Double totalVentas,
                                            Date fechaInicio, Date fechaFin, String rutaArchivo) throws NegocioException {
        try {
            GeneradorPDF.generarReporteComandasPDF(comandas, totalVentas, fechaInicio, fechaFin, rutaArchivo);
        } catch (Exception ex) {
            LOG.warning("Error al generar PDF de comandas: " + ex.getMessage());
            throw new NegocioException("Error al generar el archivo PDF de comandas");
        }
    }

    @Override
    public void generarPDFReporteClientes(List<ReporteClienteFrecuenteDTO> clientes, String rutaArchivo) throws NegocioException {
        try {
            GeneradorPDF.generarReporteClientesPDF(clientes, rutaArchivo);
        } catch (Exception ex) {
            LOG.warning("Error al generar PDF de clientes: " + ex.getMessage());
            throw new NegocioException("Error al generar el archivo PDF de clientes");
        }
    }
}
