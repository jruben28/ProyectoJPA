package DAOs;

import conexion.ConexionBD;
import entidades.Combo;
import entidades.ComboProducto;
import entidades.Producto;
import excepciones.PersistenciaException;
import interfaces.IComboProductoDAO;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ComboProductoDAO implements IComboProductoDAO {

    private static final Logger LOG = Logger.getLogger(ComboProductoDAO.class.getName());

    @Override
    public ComboProducto agregar(Long idCombo, Long idProducto, Integer cantidad) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Combo combo = em.find(Combo.class, idCombo);
            Producto producto = em.find(Producto.class, idProducto);

            if (combo == null) {
                throw new PersistenciaException("Combo no encontrado con id " + idCombo);
            }
            if (producto == null) {
                throw new PersistenciaException("Producto no encontrado con id " + idProducto);
            }

            ComboProducto cp = new ComboProducto(combo, producto, cantidad);
            em.persist(cp);
            em.getTransaction().commit();
            LOG.info("ComboProducto agregado con id: " + cp.getId());
            return cp;
        } catch (Exception e) {
            LOG.warning("Error al agregar ComboProducto: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al agregar ComboProducto: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public List<ComboProducto> obtenerPorCombo(Long idCombo) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            TypedQuery<ComboProducto> q = em.createQuery(
                "SELECT cp FROM ComboProducto cp WHERE cp.combo.id = :idCombo",
                ComboProducto.class);
            q.setParameter("idCombo", idCombo);
            return q.getResultList();
        } catch (Exception e) {
            LOG.warning("Error al consultar productos del combo: " + e.getMessage());
            throw new PersistenciaException("Error al consultar productos del combo");
        } finally {
            em.close();
        }
    }
}