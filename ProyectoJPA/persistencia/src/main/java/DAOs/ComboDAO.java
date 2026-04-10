/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;
import conexion.ConexionBD;
import entidades.Combo;
import excepciones.PersistenciaException;
import interfaces.IComboDAO;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
/**
 * implementacion de IComboDAO
 * @author Adrian Mendoza
 */
public class ComboDAO implements IComboDAO {
    private static final Logger LOG=Logger.getLogger(ComboDAO.class.getName());
    
    @Override
    public Combo agregarCombo(Combo combo) throws PersistenciaException {
      EntityManager em= ConexionBD.crearConexion();
      try{
          em.getTransaction().begin();
          em.persist(combo);
          em.getTransaction().commit();
          LOG.info("Se agrego un combo con ID: "+combo.getId());
          return combo;
      }catch(RuntimeException ex){
          if(em.getTransaction().isActive()){
              em.getTransaction().rollback();
          }
          LOG.warning("Error al agregar un combo");
          throw new PersistenciaException("Error al agregar un combo");
      }finally{
          em.close();
      }
    }
    
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
    
}
