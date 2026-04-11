/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import conexion.ConexionBD;
import entidades.Combo;
import entidades.ComboProducto;
import excepciones.PersistenciaException;
import interfaces.IComboDAO;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

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

    
    @Override
    public List<Combo> obtenerTodosCombos() throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            Query q = em.createQuery("SELECT c FROM Combo c");
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

}
