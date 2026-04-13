/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Entidades.Producto;
import conexion.ConexionBD;
import enums.TipoProducto;
import interfaces.IProductoDAO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author icoro
 */
public class ProductoDAO implements IProductoDAO {

    @Override
    public void guardar(Producto producto) {
       
        EntityManager em = ConexionBD.crearConexion(); 
        try {
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizar(Producto producto) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.merge(producto); // merge actualiza el registro
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public Producto buscarPorNombre(String nombre) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            // Buscamos si ya existe alguien con ese nombre exacto 
            String jpql = "SELECT p FROM Producto p WHERE p.nombre = :nombre";
            return em.createQuery(jpql, Producto.class)
                     .setParameter("nombre", nombre)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null; // Si no hay resultados, regresamos null 
        } finally {
            em.close();
        }
    }

    @Override
    public List<Producto> buscarPorFiltros(String textoBusqueda, TipoProducto categoria) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            // Buscador dinámico para el módulo de comandas
            StringBuilder jpql = new StringBuilder("SELECT p FROM Producto p WHERE 1=1 ");
            
            if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
                jpql.append("AND p.nombre LIKE :texto ");
            }
            if (categoria != null) {
                jpql.append("AND p.tipo = :categoria ");
            }
            
            TypedQuery<Producto> query = em.createQuery(jpql.toString(), Producto.class);
            
            if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
                query.setParameter("texto", "%" + textoBusqueda + "%");
            }
            if (categoria != null) {
                query.setParameter("categoria", categoria);
            }
            
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Producto buscarPorId(Long id) {
        EntityManager em = conexion.ConexionBD.crearConexion();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }    }
}