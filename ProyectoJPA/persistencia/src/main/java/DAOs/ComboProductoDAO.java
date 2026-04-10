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
    
}
