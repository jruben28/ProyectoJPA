/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import conexion.ConexionBD;
import entidades.Comanda;
import enums.EstadoComanda;
import excepciones.PersistenciaException;
import interfaces.IComandaDAO;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ComandaDAO implements IComandaDAO {

    private static final Logger LOG = Logger.getLogger(ComandaDAO.class.getName());

    @Override
    public Comanda agregarComanda(Comanda comanda) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(comanda);
            em.getTransaction().commit();
            LOG.info("Comanda agregada con folio: " + comanda.getFolio());
            return comanda;
        } catch (Exception e) {
            LOG.warning("Error al agregar comanda: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al agregar comanda: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public Comanda actualizar(Comanda comanda) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Comanda comandaActualizada = em.merge(comanda);
            em.getTransaction().commit();
            LOG.info("Comanda actualizada con id: " + comandaActualizada.getId());
            return comandaActualizada;
        } catch (Exception e) {
            LOG.warning("Error al actualizar comanda: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar comanda: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public Comanda buscarPorId(Long id) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            return em.find(Comanda.class, id);
        } catch (Exception e) {
            LOG.warning("Error al buscar comanda por id: " + e.getMessage());
            throw new PersistenciaException("Error al buscar comanda por id: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public Comanda buscarPorFolio(String folio) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            TypedQuery<Comanda> query = em.createQuery(
                    "SELECT c FROM Comanda c WHERE c.folio = :folio", Comanda.class);
            query.setParameter("folio", folio);
            List<Comanda> resultados = query.getResultList();
            if (resultados.isEmpty()) {
                return null;
            }
            return resultados.get(0);
        } catch (Exception e) {
            LOG.warning("Error al buscar comanda por folio: " + e.getMessage());
            throw new PersistenciaException("Error al buscar comanda por folio: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public Integer contarComandasDelDia(LocalDateTime dia) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            LocalDateTime inicio = dia.toLocalDate().atStartOfDay();
            LocalDateTime fin = dia.toLocalDate().atTime(LocalTime.MAX);

            Long cantidad = em.createQuery(
                    "SELECT COUNT(c) FROM Comanda c WHERE c.fechaHora BETWEEN :inicio AND :fin", Long.class)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin)
                    .getSingleResult();

            return cantidad != null ? cantidad.intValue() : 0;
        } catch (Exception e) {
            LOG.warning("Error al contar comandas del día: " + e.getMessage());
            throw new PersistenciaException("Error al contar comandas del día: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public List<Comanda> buscarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            TypedQuery<Comanda> query = em.createQuery(
                    "SELECT c FROM Comanda c WHERE c.fechaHora BETWEEN :desde AND :hasta ORDER BY c.fechaHora",
                    Comanda.class);
            query.setParameter("desde", desde);
            query.setParameter("hasta", hasta);
            return query.getResultList();
        } catch (Exception e) {
            LOG.warning("Error al buscar comandas por rango de fechas: " + e.getMessage());
            throw new PersistenciaException("Error al buscar comandas por rango de fechas: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public boolean mesaTieneComandaAbierta(Long idMesa) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            Long total = em.createQuery(
                    "SELECT COUNT(c) FROM Comanda c WHERE c.mesa.id = :idMesa AND c.estado = :estado",
                    Long.class)
                    .setParameter("idMesa", idMesa)
                    .setParameter("estado", EstadoComanda.ABIERTA)
                    .getSingleResult();
            return total > 0;
        } catch (Exception e) {
            LOG.warning("Error al verificar mesa ocupada: " + e.getMessage());
            throw new PersistenciaException("Error al verificar si la mesa tiene comanda abierta: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
