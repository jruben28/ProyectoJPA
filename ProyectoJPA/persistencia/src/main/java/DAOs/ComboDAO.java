/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import conexion.ConexionBD;
import entidades.Combo;
import excepciones.PersistenciaException;
import interfaces.IComboDAO;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * implementacion de IComboDAO
 *
 * @author Adrian Mendoza
 */
public class ComboDAO implements IComboDAO {

    private static final Logger LOG = Logger.getLogger(ComboDAO.class.getName());

    /**
     * Agrega un combo a la bd
     *
     * @param combo
     * @return
     * @throws PersistenciaException
     */
    @Override
    public Combo agregarCombo(Combo combo) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(combo);
            em.getTransaction().commit();
            LOG.info("Se agrego un combo con ID: " + combo.getId());
            return combo;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOG.warning("Error al agregar un combo");
            throw new PersistenciaException("Error al agregar un combo");
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza un combo en la bd
     *
     * @param combo
     * @return
     * @throws PersistenciaException
     */
    @Override
    public Combo actualizarCombo(Combo combo) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.merge(combo);
            em.getTransaction().commit();
            LOG.info("Combo actualizado con ID: " + combo.getId());
            return combo;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOG.warning("Error al actualizar combo");
            throw new PersistenciaException("Error al actualizar combo");
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todos los combos registrados
     *
     * @return Lista de todos los combos
     * @throws PersistenciaException si ocurre error en la base de datos
     */
    @Override
    public List<Combo> obtenerTodosCombos() throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            TypedQuery<Combo> q = em.createQuery("SELECT c FROM Combo c", Combo.class);
            List<Combo> combos = q.getResultList();
            LOG.info("Se obtuvieron " + combos.size() + " combos");
            return combos;
        } catch (RuntimeException ex) {
            LOG.warning("Error al obtener combos: " + ex.getMessage());
            throw new PersistenciaException("Error al obtener combos");
        } finally {
            em.close();
        }
    }

    /**
     * Busca un combo por su ID
     *
     * @param id ID del combo a buscar
     * @return Combo encontrado
     * @throws PersistenciaException si no se encuentra o hay error
     */
    @Override
    public Combo buscarComboPorId(Long id) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            Combo combo = em.find(Combo.class, id);
            if (combo == null) {
                LOG.warning("Combo no encontrado con id: " + id);
                throw new PersistenciaException("Combo no encontrado");
            }
            LOG.info("Combo encontrado: " + combo.getNombre());
            return combo;
        } catch (RuntimeException ex) {
            LOG.warning("Error al buscar combo: " + ex.getMessage());
            throw new PersistenciaException("Error al buscar combo");
        } finally {
            em.close();
        }
    }

    /**
     * Busca combos cuyo nombre contenga el texto dado
     *
     * @param nombre Texto a buscar en el nombre
     * @return Lista de combos que coinciden
     * @throws PersistenciaException si ocurre error en la base de datos
     */
    @Override
    public List<Combo> buscarCombosPorNombre(String nombre) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT c FROM Combo c WHERE c.nombre LIKE :nombre";
            TypedQuery<Combo> query = em.createQuery(jpql, Combo.class);
            query.setParameter("nombre", "%" + nombre + "%");
            List<Combo> resultado = query.getResultList();
            LOG.info("Se encontraron " + resultado.size() + " combos con nombre: " + nombre);
            return resultado;
        } catch (RuntimeException ex) {
            LOG.warning("Error al buscar combos por nombre: " + ex.getMessage());
            throw new PersistenciaException("Error al buscar combos por nombre");
        } finally {
            em.close();
        }
    }

    @Override
    public List<Combo> buscarCombosPorProducto(Long idProducto) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            TypedQuery<Combo> query = em.createQuery(
                    "SELECT DISTINCT cp.combo FROM ComboProducto cp WHERE cp.producto.id = :idProducto",
                    Combo.class);
            query.setParameter("idProducto", idProducto);
            List<Combo> combos = query.getResultList();
            LOG.info("Se encontraron " + combos.size() + " combos con producto id: " + idProducto);
            return combos;
        } catch (RuntimeException ex) {
            LOG.warning("Error al buscar combos por producto: " + ex.getMessage());
            throw new PersistenciaException("Error al buscar combos por producto");
        } finally {
            em.close();
        }
    }

    @Override
    public Combo cambiarEstado(Long id, Boolean activo) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Combo combo = em.find(Combo.class, id);
            if (combo == null) {
                throw new PersistenciaException("Combo no encontrado con id: " + id);
            }
            combo.setActivo(activo);
            em.getTransaction().commit();
            LOG.info("Estado del combo cambiado a " + activo + " para id: " + id);
            return combo;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOG.warning("Error al cambiar estado del combo: " + ex.getMessage());
            throw new PersistenciaException("Error al cambiar estado del combo");
        } finally {
            em.close();
        }
    }
}
