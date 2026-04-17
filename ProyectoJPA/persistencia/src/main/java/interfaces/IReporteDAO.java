package interfaces;

import entidades.ClienteFrecuente;
import entidades.Comanda;
import entidades.Cliente;
import excepciones.PersistenciaException;
import java.util.Date;
import java.util.List;

/**
 * Interfaz para las consultas de reportes.
 *
 * @author Devora
 */
public interface IReporteDAO {

    List<Comanda> buscarComandasPorRangoFechas(Date fechaInicio, Date fechaFin) throws PersistenciaException;

    List<ClienteFrecuente> obtenerTodosClientesFrecuentes() throws PersistenciaException;

    List<Comanda> buscarComandasEntregadasPorCliente(Long idCliente) throws PersistenciaException;

    Comanda buscarUltimaComandaPorCliente(Long idCliente) throws PersistenciaException;

    Cliente buscarClientePorId(Long id) throws PersistenciaException;
}
