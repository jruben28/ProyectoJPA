/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

/**
 *
 * @author icoro
 */
// Asegúrate de que estos imports coincidan con tus paquetes reales
import Conexion.ConexionBD;
import Entidades.Cliente;
import Entidades.ClienteFrecuente;
import Entidades.ClienteGeneral;
import Entidades.Comanda;
import enums.EstadoComanda;
import excepciones.PersistenciaException;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ClienteDAO implements IClienteDAO {

    private static final Logger LOG = Logger.getLogger(ClienteDAO.class.getName());

    
    
    @Override
    public Cliente agregar(Cliente cliente) {
        // Obtenemos conexion
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            LOG.info("Se agregó un cliente con ID: " + cliente.getId());
            return cliente;
        } 
        catch(Exception ex){
            LOG.warning("Error al agregar un cliente");
            throw new PersistenciaException("Error al agregar un cliente");
        }
        finally {
            em.close(); // Siempre cerramos la conexión al terminar
        }
    }

    @Override
    public Cliente actualizar(Cliente cliente) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.merge(cliente);
            em.getTransaction().commit();
            LOG.info("Se actualizó un cliente con ID: " + cliente.getId());
            return cliente;
        } 
        catch(Exception ex){
            LOG.warning("Error al actualizar un cliente");
            throw new PersistenciaException("Error al actualizar un cliente");
        } finally {
            em.close();
        }
    }

    @Override
    public Cliente buscarPorId(Long id) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            return em.find(Cliente.class, id);
        } 
        catch(Exception ex){
            LOG.warning("Error al buscar un cliente");
            throw new PersistenciaException("Error al buscar un cliente");
        }finally {
            em.close();
        }
    }

    @Override
    public List<ClienteFrecuente> buscarFrecuentesPorFiltro(String filtro) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            // Buscamos por nombre, teléfono o correo
            String jpql = "SELECT c FROM ClienteFrecuente c "
                    + "WHERE c.nombre LIKE :filtro OR c.telefono LIKE :filtro OR c.correo LIKE :filtro";

            TypedQuery<ClienteFrecuente> query = em.createQuery(jpql, ClienteFrecuente.class);
            // Los % son para que busque coincidencias parciales (como el LIKE de SQL)
            query.setParameter("filtro", "%" + filtro + "%");

            return query.getResultList();
        } catch(Exception ex){
            LOG.warning("Error al buscar clientes frecuentes por id");
            throw new PersistenciaException("Error al buscar clientes frecuentes por id");
        } finally {
            em.close();
        }
    }

    @Override
    public ClienteGeneral obtenerClienteGeneral() {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT c FROM ClienteGeneral c WHERE c.nombre = 'Cliente General'";
            return em.createQuery(jpql, ClienteGeneral.class).getSingleResult();
        } catch (Exception e) {
            // Retornamos null si no existe para manejarlo en la Capa de Negocio
            LOG.warning("Se produjo un error al consultar los clientes generales.");
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Busca las comandas ya entregadas, por cliente
     *
     * @param idCliente
     * @return Lista de comandas entregadas
     */
    public List<Comanda> buscarComandasPorCliente(Long idCliente) {
        EntityManager em = ConexionBD.crearConexion();
        
        try {
            String jpql = "SELECT c FROM Comanda c "
                    + "WHERE c.cliente.id = :id "
                    + "AND c.estado = :estado";
            return em.createQuery(jpql, Comanda.class).setParameter("id", idCliente)
                    .setParameter("estado", EstadoComanda.ENTREGADA).getResultList();
           
        } catch (Exception e) {
            LOG.warning("Se produjo un error al buscar las comandas del cliente con ID: " + idCliente);
            throw new excepciones.PersistenciaException("Error en buscar la comanda por id de cliente" + e.getMessage());
        } finally {
            em.close();
        }

    }

    @Override
    public List<ClienteFrecuente> buscarFrecuentesPorCampo(String filtro, String campo) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql;
            switch (campo) {
                case "nombre":
                    jpql = "SELECT c FROM ClienteFrecuente c WHERE c.nombre LIKE :filtro";
                    break;
                case "telefono":
                    jpql = "SELECT c FROM ClienteFrecuente c WHERE c.telefono LIKE :filtro";
                    break;
                case "correo":
                    jpql = "SELECT c FROM ClienteFrecuente c WHERE c.correo LIKE :filtro";
                    break;
                default:
                    jpql = "SELECT c FROM ClienteFrecuente c WHERE c.nombre LIKE :filtro OR c.telefono LIKE :filtro OR c.correo LIKE :filtro";
                    break;
            }
            TypedQuery<ClienteFrecuente> query = em.createQuery(jpql, ClienteFrecuente.class);
            query.setParameter("filtro", "%" + filtro + "%");
            return query.getResultList();
        } catch (Exception ex) {
            LOG.warning("Error al buscar clientes frecuentes por campo");
            throw new PersistenciaException("Error al buscar clientes frecuentes por campo");
        } finally {
            em.close();
        }
    }

    @Override
    public ClienteFrecuente agregarClienteFrecuente(ClienteFrecuente clienteFrecuente) {
        EntityManager em = ConexionBD.crearConexion();

        try {
            em.getTransaction().begin();
            em.persist(clienteFrecuente);
            em.getTransaction().commit();
            LOG.info("Se agregó un cliente con ID: " + clienteFrecuente.getId());
            return clienteFrecuente;
        } catch (Exception ex) {
            LOG.warning("Se ha producido un error al actualizar cliente");
            throw new PersistenciaException("Error al agregar cliente frecuente" + ex.getMessage());
        } finally {
            em.close();
        }


    }

    @Override
    public ClienteFrecuente actualizarClienteFrecuente(ClienteFrecuente clienteFrecuente) {
        EntityManager em = ConexionBD.crearConexion();

        try {
            em.getTransaction().begin();
            em.merge(clienteFrecuente);
            em.getTransaction().commit();
            LOG.info("Se agregó un cliente con ID: " + clienteFrecuente.getId());
            return clienteFrecuente;
        } catch (Exception ex) {
            LOG.warning("Se ha producido un error al actualizar cliente");
            throw new PersistenciaException("Error al agregar cliente frecuente" + ex.getMessage());
        } finally {
            em.close();
        }

    }

}
