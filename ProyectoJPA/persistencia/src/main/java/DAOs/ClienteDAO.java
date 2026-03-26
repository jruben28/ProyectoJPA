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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ClienteDAO implements IClienteDAO {

    @Override
    public void agregar(Cliente cliente) {
        // Obtenemos conexion
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
        } finally {
            em.close(); // Siempre cerramos la conexión al terminar
        }
    }

    @Override
    public void actualizar(Cliente cliente) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.merge(cliente);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public Cliente buscarPorId(Long id) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<ClienteFrecuente> buscarFrecuentesPorFiltro(String filtro) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            // Buscamos por nombre, teléfono o correo
            String jpql = "SELECT c FROM ClienteFrecuente c " +
                          "WHERE c.nombre LIKE :filtro OR c.telefono LIKE :filtro OR c.correo LIKE :filtro";
            
            TypedQuery<ClienteFrecuente> query = em.createQuery(jpql, ClienteFrecuente.class);
            // Los % son para que busque coincidencias parciales (como el LIKE de SQL)
            query.setParameter("filtro", "%" + filtro + "%");
            
            return query.getResultList();
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
            return null;
        } finally {
            em.close();
        }
    }
}