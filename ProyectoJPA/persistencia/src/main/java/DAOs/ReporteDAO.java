package DAOs;

import Conexion.ConexionBD;
import Entidades.ClienteFrecuente;
import Entidades.Comanda;
import enums.EstadoComanda;
import excepciones.PersistenciaException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * DAO para las consultas de reportes.
 *
 * @author Devora
 */
public class ReporteDAO implements IReporteDAO {

    private static final Logger LOG = Logger.getLogger(ReporteDAO.class.getName());

    @Override
    public List<Comanda> buscarComandasPorRangoFechas(Date fechaInicio, Date fechaFin) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT c FROM Comanda c "
                    + "WHERE c.fechaHora BETWEEN :inicio AND :fin "
                    + "ORDER BY c.fechaHora DESC";
            TypedQuery<Comanda> query = em.createQuery(jpql, Comanda.class);
            query.setParameter("inicio", fechaInicio);
            query.setParameter("fin", fechaFin);
            return query.getResultList();
        } catch (Exception ex) {
            LOG.warning("Error al buscar comandas por rango de fechas: " + ex.getMessage());
            throw new PersistenciaException("Error al buscar comandas por rango de fechas");
        } finally {
            em.close();
        }
    }

    @Override
    public List<ClienteFrecuente> obtenerTodosClientesFrecuentes() throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT c FROM ClienteFrecuente c ORDER BY c.nombre ASC";
            return em.createQuery(jpql, ClienteFrecuente.class).getResultList();
        } catch (Exception ex) {
            LOG.warning("Error al obtener clientes frecuentes: " + ex.getMessage());
            throw new PersistenciaException("Error al obtener clientes frecuentes");
        } finally {
            em.close();
        }
    }

    @Override
    public List<Comanda> buscarComandasEntregadasPorCliente(Long idCliente) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT c FROM Comanda c "
                    + "WHERE c.cliente.id = :id AND c.estado = :estado";
            return em.createQuery(jpql, Comanda.class)
                    .setParameter("id", idCliente)
                    .setParameter("estado", EstadoComanda.ENTREGADA)
                    .getResultList();
        } catch (Exception ex) {
            LOG.warning("Error al buscar comandas entregadas del cliente: " + ex.getMessage());
            throw new PersistenciaException("Error al buscar comandas del cliente");
        } finally {
            em.close();
        }
    }

    @Override
    public Comanda buscarUltimaComandaPorCliente(Long idCliente) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT c FROM Comanda c "
                    + "WHERE c.cliente.id = :id AND c.estado = :estado "
                    + "ORDER BY c.fechaHora DESC";
            List<Comanda> resultado = em.createQuery(jpql, Comanda.class)
                    .setParameter("id", idCliente)
                    .setParameter("estado", EstadoComanda.ENTREGADA)
                    .setMaxResults(1)
                    .getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } catch (Exception ex) {
            LOG.warning("Error al buscar ultima comanda del cliente: " + ex.getMessage());
            throw new PersistenciaException("Error al buscar ultima comanda del cliente");
        } finally {
            em.close();
        }
    }
}
