/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Conexion.ConexionBD;
import Entidades.Comanda;
import excepciones.PersistenciaException;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

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
            LOG.info("Se agregó una comanda con ID: " + comanda.getId());
            return comanda;
        } catch (Exception ex) {
            LOG.warning("Error al agregar una comanda");
            throw new PersistenciaException("Error al agregar comanda");
        } finally {
            em.close(); 
        }
        
        
    }
    
}
