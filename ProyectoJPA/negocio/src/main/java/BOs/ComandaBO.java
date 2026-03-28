/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import DAOs.ComandaDAO;
import DAOs.IComandaDAO;
import Entidades.Comanda;
import adaptadores.ComandaAdapter;
import com.dtos.ComandaDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.util.logging.Logger;

/**
 *
 * @author joser
 */
public class ComandaBO implements IComandaBO{
    
    private IComandaDAO comandaDAO;
    private static final Logger LOG = Logger.getLogger(ComandaBO.class.getName());

    public ComandaBO() {
        this.comandaDAO = new ComandaDAO();
    }
    
    

    @Override
    public void agregarComanda(ComandaDTO comandaDTO) throws NegocioException {
        //validar datos de comanda
        Comanda comanda = ComandaAdapter.dtoAEntidad(comandaDTO);
        try{
            comandaDAO.agregarComanda(comanda);
        }catch (PersistenciaException ex) {
            LOG.warning("Error en negocio al agregar comanda" + ex.getMessage());
            throw new NegocioException("Error al agregar una comanda");
        }
    }
    
}
