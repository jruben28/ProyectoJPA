/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.Ingrediente;
import excepciones.PersistenciaException;
import java.util.List;

/**
 *
 * @author joser
 */
public interface IIngredienteDAO {
    
    Ingrediente agregarIngrediente(Ingrediente ingrediente) throws PersistenciaException;
    
    Ingrediente actualizarStock(Long idIngrediente, Double stock) throws PersistenciaException;
    
    Ingrediente eliminarIngrediente(Long idIngrediente) throws PersistenciaException;
    
    Ingrediente obtenerIngredientePorId(Long idIngrediente) throws PersistenciaException;
    
    List<Ingrediente> obtenerIngredientePorFiltro(String filtro) throws PersistenciaException;
    
    List<Ingrediente> obtenerIngredienteTodos() throws PersistenciaException;
    
    boolean duplicadoIngredienteExiste(Ingrediente Ingrediente) throws PersistenciaException;
}
