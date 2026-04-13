/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import com.dtos.IngredienteDTO;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author joser
 */
public interface IIngredienteBO {
    
    IngredienteDTO agregarIngrediente(IngredienteDTO ingredienteDTO) throws NegocioException;
    
    IngredienteDTO actualizarStock(Long idIngrediente, Double stock) throws NegocioException;
    
    IngredienteDTO eliminarIngrediente(Long idIngrediente) throws NegocioException;
    
    IngredienteDTO obtenerIngredientePorId(Long idIngrediente) throws NegocioException;
    
    List<IngredienteDTO> obtenerIngredientePorFiltro(String filtro) throws NegocioException;
    
    List<IngredienteDTO> obtenerIngredienteTodos() throws NegocioException;
}
