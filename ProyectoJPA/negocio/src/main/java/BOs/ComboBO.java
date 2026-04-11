/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import DAOs.ComboDAO;
import DAOs.ComboProductoDAO;
import adaptadores.ComboAdapter;
import com.dtos.ComboDTO;
import entidades.Combo;
import entidades.ComboProducto;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IComboBO;
import interfaces.IComboDAO;
import interfaces.IComboProductoDAO;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementacion de la interfaz IComboBO
 * @author Adrian Mendoza
 */
public class ComboBO implements IComboBO{
  private IComboDAO comboDAO;
  private IComboProductoDAO comboProductoDAO;
  private static final Logger LOG= Logger.getLogger(ComboBO.class.getName());
  
  public ComboBO(){
      this.comboDAO=new ComboDAO();
      this.comboProductoDAO=new ComboProductoDAO();
  }
/**
 * Validaciones al agregar un comboDTO.  
 * @param comboDTO
 * @throws NegocioException 
 */
  @Override
  public Combo agregarCombo(ComboDTO comboDTO) throws NegocioException {
    try {
        validarComboDTO(comboDTO); 
        
        if(comboDTO.getActivo() == null) {
            LOG.warning("Cambio del atributo activo de null a true");
            comboDTO.setActivo(true);
        }
        
        Combo combo = ComboAdapter.dtoAEntidad(comboDTO);
        comboDAO.agregarCombo(combo);
        LOG.info("Combo agregado exitosamente, nombre y id: " + combo.getNombre() + "," + combo.getId());
        return combo;
        
    } catch(PersistenciaException ex) {
        LOG.warning("Error de persistencia al agregar el combo " + ex.getMessage());
        throw new NegocioException("Error al agregar el combo mediante persistencia");
    }
}

    @Override
public Combo crearComboConProductos(ComboDTO dto, List<Long> idProductos, List<Integer> cantidades)throws NegocioException {
    try {
       
        if(idProductos == null || idProductos.size() < 2) {
            LOG.warning("Error al crear el combo, tiene menos de 2 productos");
            throw new NegocioException("El combo al menos debe tener 2 productos asociados");
        }    
        if(comboDAO.estaRepetido(idProductos, cantidades)) {
            LOG.warning("Intento de crear combo duplicado");
            throw new NegocioException("Ya existe un combo con esta misma combinación de productos");
        }
        Combo agregado = this.agregarCombo(dto);
        for(int i = 0; i < idProductos.size(); i++) {
            ComboProducto comboP = new ComboProducto(agregado, idProductos.get(i), cantidades.get(i));
            comboProductoDAO.agregar(comboP);
        }
        LOG.info("Combo con productos creado " + agregado.getNombre());
        return agregado;
    } catch(PersistenciaException ex) {
        LOG.warning("Error de persistencia " + ex.getMessage());
        throw new NegocioException("Error al crear combo con productos");
    }
}
  @Override
  public Combo actualizarComboPorId(Long id, ComboDTO comboDTO) throws NegocioException {
    try {
        if(id == null || id < 1) {
            LOG.warning("Id inválido");
            throw new NegocioException("No se puede actualizar con id inválido");
        }
        
        validarComboDTO(comboDTO); 
        
        Combo combo = ComboAdapter.dtoAEntidad(comboDTO);
        combo.setId(id);
        Combo actualizado = comboDAO.actualizarCombo(combo);
        LOG.info("Combo actualizado con éxito, nombre y id " + actualizado.getNombre() + " " + actualizado.getId());
        return actualizado;
        
    } catch(PersistenciaException ex) {
        LOG.warning("Error de persistencia al actualizar combo " + ex.getMessage());
        throw new NegocioException("Error al actualizar el combo mediante persistencia");
    }
}
     /**
     * Valida que un ComboDTO tenga todos los datos correctos
     * @param comboDTO
     * @throws NegocioException 
     */
    private void validarComboDTO(ComboDTO comboDTO) throws NegocioException {
        if(comboDTO == null) {
            LOG.warning("ComboDTO nulo");
            throw new NegocioException("El combo no puede ser nulo");
        }
        
        if(comboDTO.getNombre() == null || comboDTO.getNombre().trim().isEmpty()) {
            LOG.warning("Nombre del combo vacío o nulo");
            throw new NegocioException("El nombre del combo no puede estar vacío o nulo");
        }
        
        if(comboDTO.getPrecioOriginal() == null || comboDTO.getPrecioOriginal() < 0) {
            LOG.warning("Precio original negativo o nulo");
            throw new NegocioException("El precio original no puede ser negativo o nulo");
        }
        
        if(comboDTO.getPrecioCombo() == null || comboDTO.getPrecioCombo() < 0) {
            LOG.warning("Precio del combo negativo o nulo");
            throw new NegocioException("El precio del combo no puede ser negativo o nulo");
        }
        
        if(comboDTO.getPorcentajeDescuento() == null || 
           comboDTO.getPorcentajeDescuento() < 0 || 
           comboDTO.getPorcentajeDescuento() > 100) {
            LOG.warning("Porcentaje de descuento del combo fuera de límites o nulo");
            throw new NegocioException("El porcentaje no es un valor entre 0 y 100");
        }
    }
}
