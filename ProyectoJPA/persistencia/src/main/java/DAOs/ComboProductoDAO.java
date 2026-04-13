/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;


import entidades.ComboProducto;
import excepciones.PersistenciaException;
import interfaces.IComboProductoDAO;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import conexion.ConexionBD;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 * Implementación de la interfaz de IComboProductoDAO
 * @author Adrian Mendoza
 */
public class ComboProductoDAO implements IComboProductoDAO{

    private static final Logger LOG = Logger.getLogger(ComboProductoDAO.class.getName());
    
    @Override
    public ComboProducto agregar(ComboProducto comboProducto) throws PersistenciaException {
    EntityManager em= ConexionBD.crearConexion();
        try{
            em.getTransaction().begin();
            em.persist(comboProducto);
            em.getTransaction().commit();
            LOG.info("ComboProducto agregado con el ID:"+comboProducto);
            return comboProducto;
        } catch(RuntimeException ex){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            LOG.warning("Error al agregar un ComboProducto: "+ex.getMessage());
            throw new PersistenciaException("Error al agregar un ComboProducto");
        }finally{
            em.close();
        }
            
        
    }

    @Override
    public List<ComboProducto> obtenerPorCombo(Long idCombo) throws PersistenciaException {
            EntityManager em= ConexionBD.crearConexion();
        try{
          TypedQuery<ComboProducto> query = em.createQuery("SELECT cProducto FROM ComboProducto cProducto WHERE cProducto.combo.id = :idCombo",ComboProducto.class);
          query.setParameter("idCombo", idCombo);
          
          LOG.info("Busqueda con exito");
          return query.getResultList();
        
        }catch(RuntimeException ex){
            LOG.warning("Error al consultar productos del combo");
          throw new PersistenciaException("Error al consultar productos del combo");
        }finally{
            em.close();
        }
    }
    
}
