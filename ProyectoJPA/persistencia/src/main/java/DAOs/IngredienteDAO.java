/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import conexion.ConexionBD;
import entidades.Ingrediente;
import excepciones.PersistenciaException;
import interfaces.IIngredienteDAO;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author joser
 */
public class IngredienteDAO implements IIngredienteDAO{

    private static final Logger LOG = Logger.getLogger(IngredienteDAO.class.getName());
    
    /**
     * Agrega el ingrediente dado
     * @return Ingrediente agregado
     * @throws PersistenciaException en caso de que haya error con el ingrediente
     */
    @Override
    public Ingrediente agregarIngrediente(Ingrediente ingrediente) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        
        try{
            em.getTransaction().begin();
            em.persist(ingrediente);
            em.getTransaction().commit();
            
            LOG.info("Se ha agregado correctamente el ingrediente: " + ingrediente);
            return ingrediente;
        }
        catch(Exception ex){
            LOG.warning("Hubo un error al agregar el ingrediente.");
            throw new PersistenciaException("Error al persistir el ingrediente");
        }
        finally{
            em.close();
        }
        
    }

    /**
     * Actualiza el stock del ingrediente dado por id
     * @param stock
     * @return ingrediente actualizado
     * @throws PersistenciaException 
     */
    @Override
    public Ingrediente actualizarStock(Long idIngrediente,Double stock) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        
        try{
            em.getTransaction().begin();
            String jpql = "UPDATE Ingrediente i SET i.stock = :nuevoStock WHERE i.id = :id";
            
            Query query = em.createQuery(jpql);
            query.setParameter("nuevoStock", stock);
            query.setParameter("id", idIngrediente);
            
            int filasActualizadas = query.executeUpdate();
            
            if (filasActualizadas < 1) {
                throw new PersistenciaException("No se encontró el ingrediente con id: " + idIngrediente);
            }
            
            em.getTransaction().commit();
            return em.find(Ingrediente.class, idIngrediente);
            
        }
        catch(Exception ex){
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOG.warning("Error al acutalizar el stock");
            throw new PersistenciaException("Error al actualizar el stock");
        }
        finally{
            em.close();
        }
    }

    /**
     * Elimina el ingrediente dado por id
     * @param idIngrediente
     * @return el ingrediente eliminado
     * @throws PersistenciaException 
     */
    @Override
    public Ingrediente eliminarIngrediente(Long idIngrediente) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();

        try {
            em.getTransaction().begin();
            Ingrediente i = em.find(Ingrediente.class, idIngrediente);
            if (i != null) {
                em.remove(i);
            }

            em.getTransaction().commit();
            LOG.info("Se ha eliminado el ingrediente con id: " + idIngrediente);
            return i;
        } catch (Exception ex) {
            LOG.warning("No se ha podido eliminar el ingrediente con id " + idIngrediente);
            throw new PersistenciaException("Error al querer eliminar un ingrediente");
        }
        finally{
            em.close();
        }
    }

    /**
     * Obtiene datos del ingrediente buscado por id
     * @param idIngrediente
     * @return ingrediente buscado
     * @throws PersistenciaException 
     */
    @Override
    public Ingrediente obtenerIngredientePorId(Long idIngrediente) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        
        try{
            
            Ingrediente i = em.find(Ingrediente.class, idIngrediente);
            if (i == null) {
                throw new PersistenciaException("No existe el ingrediente con el id " + idIngrediente);
            }
            return i;
        }
        catch(Exception ex){
            LOG.warning("Error al buscar un ingrediente por id");
            throw new PersistenciaException("Ha habido un error al buscar el ingrediente con id: " + idIngrediente);
        }
        finally{
            em.close();
        }
    }

    /**
     * Obtiene lista de ingredientes que coincide con el criterio de busqueda
     * @param filtro que es la cadena de texto que se busca en la bdd
     * @return Lista de ingredientes que coinciden 
     * @throws PersistenciaException 
     */
    @Override
    public List<Ingrediente> obtenerIngredientePorFiltro(String filtro) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        
        try{
            String jpql = "SELECT i FROM Ingrediente i WHERE i.nombre LIKE :filtro OR CAST(i.unidadDeMedida AS char) LIKE :filtro OR CAST(i.stock AS char) LIKE :filtro";
            TypedQuery<Ingrediente> query = em.createQuery(jpql, Ingrediente.class);
            query.setParameter("filtro", "%" + filtro + "%");
            
            return query.getResultList();
        }
        catch(Exception ex){
            LOG.warning("Error al buscar los ingredientes con el filtro ingresado");
            throw new PersistenciaException("Se ha producido un error al buscar los ingredientes.");
        }
        finally{
            em.close();
        }
    }

    /**
     * Obtiene todos los ingredientes y sus datos de la base de datos
     * @return lista con los ingredientes
     * @throws PersistenciaException 
     */
    @Override
    public List<Ingrediente> obtenerIngredienteTodos() throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        
        try{
            String jpql = "SELECT i FROM Ingrediente i";
            TypedQuery<Ingrediente> query = em.createQuery(jpql, Ingrediente.class);
            
            List<Ingrediente> ingredientes = query.getResultList();
            
            return ingredientes;
            
            
        }
        catch(Exception ex){
            LOG.warning("Se ha producido un error al querer obtener la lista de ingredientes.");
            throw new PersistenciaException("Error al obtener la lista de ingredientes");
        }
        finally{
            em.close();
        }
    }

    
}
