/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import interfaces.IComandaDAO;
import conexion.ConexionBD;
import entidades.Comanda;
import excepciones.PersistenciaException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author joser
 */
public class ComandaDAO implements IComandaDAO{

    private static final Logger LOG = Logger.getLogger(ComandaDAO.class.getName());

    @Override
    public Comanda agregarComanda(Comanda comanda) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(comanda);
            em.getTransaction().commit();
            LOG.info("Se agrego comanda con folio: " + comanda.getFolio());
            return comanda;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al agregar comanda: " + ex.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public Integer contarComandasDelDia(LocalDateTime dia) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            LocalDateTime inicio = LocalDateTime.of(dia.toLocalDate(), LocalTime.MIN);
            LocalDateTime fin = LocalDateTime.of(dia.toLocalDate(), LocalTime.MAX);
            Long count = em.createQuery(
                    "SELECT COUNT(c) FROM Comanda c WHERE c.fechaHora BETWEEN :inicio AND :fin",
                    Long.class)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin)
                    .getSingleResult();
            return count == null ? 0 : count.intValue();
        } catch (Exception ex) {
            throw new PersistenciaException("Error al contar comandas del dia");
        } finally {
            em.close();
        }
    }

    @Override
    public List<Comanda> buscarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            TypedQuery<Comanda> q = em.createQuery(
                    "SELECT c FROM Comanda c WHERE c.fechaHora BETWEEN :desde AND :hasta ORDER BY c.fechaHora",
                    Comanda.class);
            q.setParameter("desde", desde);
            q.setParameter("hasta", hasta);
            return q.getResultList();
        } catch (Exception ex) {
            throw new PersistenciaException("Error al buscar comandas por rango");
        } finally {
            em.close();
        }
    }

}
