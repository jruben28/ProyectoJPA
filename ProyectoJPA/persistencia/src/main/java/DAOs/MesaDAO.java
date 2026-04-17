/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import conexion.ConexionBD;
import entidades.Mesa;
import enums.EstadoMesa;
import excepciones.PersistenciaException;
import interfaces.IMesaDAO;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author keppler
 */
public class MesaDAO implements IMesaDAO {

    private static final Logger LOG = Logger.getLogger(MesaDAO.class.getName());

    @Override
    public Mesa agregar(Mesa mesa) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(mesa);
            em.getTransaction().commit();
            LOG.info("Se agrego la mesa numero: " + mesa.getNumero());
            return mesa;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOG.warning("Error al agregar mesa: " + ex.getMessage());
            throw new PersistenciaException("Error al agregar una mesa");
        } finally {
            em.close();
        }
    }

    @Override
    public List<Mesa> obtenerTodas() throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            TypedQuery<Mesa> q = em.createQuery("SELECT m FROM Mesa m ORDER BY m.numero", Mesa.class);
            return q.getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("Error al obtener mesas");
        } finally {
            em.close();
        }
    }

    @Override
    public List<Mesa> obtenerDisponibles() throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            TypedQuery<Mesa> q = em.createQuery(
                    "SELECT m FROM Mesa m WHERE m.estado = :estado ORDER BY m.numero",
                    Mesa.class);
            q.setParameter("estado", EstadoMesa.DISPONIBLE);
            return q.getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("Error al obtener mesas disponibles");
        } finally {
            em.close();
        }
    }

    @Override
    public Mesa buscarPorNumero(Integer numero) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            TypedQuery<Mesa> q = em.createQuery(
                    "SELECT m FROM Mesa m WHERE m.numero = :num", Mesa.class);
            q.setParameter("num", numero);
            List<Mesa> result = q.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } catch (RuntimeException ex) {
            throw new PersistenciaException("Error al buscar mesa por numero");
        } finally {
            em.close();
        }
    }

    @Override
    public void cargaMasiva(int cantidad) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            Long total = em.createQuery("SELECT COUNT(m) FROM Mesa m", Long.class)
                    .getSingleResult();
            if (total >= cantidad) {
                LOG.info("Ya existen " + total + " mesas, carga masiva omitida");
                return;
            }
            em.getTransaction().begin();
            for (int i = total.intValue() + 1; i <= cantidad; i++) {
                em.persist(new Mesa(i));
            }
            em.getTransaction().commit();
            LOG.info("Carga masiva completada: " + cantidad + " mesas");
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error en carga masiva de mesas");
        } finally {
            em.close();
        }
    }
    @Override
    public Mesa buscarPorId(Long id) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            Mesa mesa = em.find(Mesa.class, id);
            if (mesa == null) {
                throw new PersistenciaException("Mesa no encontrada con id: " + id);
            }
            return mesa;
        } catch (RuntimeException ex) {
            throw new PersistenciaException("Error al buscar mesa por ID", ex);
        } finally {
            em.close();
        }
    }

    @Override
    public Mesa actualizar(Mesa mesa) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Mesa actualizada = em.merge(mesa);
            em.getTransaction().commit();
            return actualizada;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar mesa", ex);
        } finally {
            em.close();
        }
    }
}
