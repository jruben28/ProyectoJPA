/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import DAOs.IngredienteDAO;
import adaptadores.IngredienteAdapter;
import com.dtos.IngredienteDTO;
import entidades.Ingrediente;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IIngredienteBO;
import interfaces.IIngredienteDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author joser
 */
public class IngredienteBO implements IIngredienteBO{
    IIngredienteDAO ingredienteDAO;
    private static final Logger LOG = Logger.getLogger(IngredienteBO.class.getName());

    public IngredienteBO(IIngredienteDAO ingredienteDAO) {
        this.ingredienteDAO = new IngredienteDAO();
    }

    
    
    @Override
    public IngredienteDTO agregarIngrediente(IngredienteDTO ingredienteDTO) throws NegocioException {
        try{
            validarDatosIngrediente(ingredienteDTO);
            
            Ingrediente entidadIngrediente = IngredienteAdapter.dtoAEntidad(ingredienteDTO);
            Ingrediente ingredienteRecibido = ingredienteDAO.agregarIngrediente(entidadIngrediente);
            IngredienteDTO ingredienteRecibidoDTO = IngredienteAdapter.entidadADTO(ingredienteRecibido);
            
            return ingredienteRecibidoDTO;
        }
        catch(Exception ex){
            throw new NegocioException("Error al gregar ingrediente: " + ex.getMessage());
        }
    }

    @Override
    public IngredienteDTO actualizarStock(Long idIngrediente, Double stock) throws NegocioException {
        if (idIngrediente == null || idIngrediente <= 0 || stock == null || stock < 0) {
            throw new NegocioException("Los datos ingresados para actualizar el stock no son validos");
        }

        try{
            Ingrediente ingrediente = ingredienteDAO.actualizarStock(idIngrediente, stock);
            
            return IngredienteAdapter.entidadADTO(ingrediente);
            
        }
        catch(Exception ex){
            throw new NegocioException("Error al actualizar stock" + ex.getMessage());
        }
    }

    @Override
    public IngredienteDTO eliminarIngrediente(Long idIngrediente) throws NegocioException {
        if (idIngrediente == null || idIngrediente <= 0) {
            throw new NegocioException("El id del ingrediente a eliminar no es valido");
        }
        try{
            Ingrediente ingredienteEliminado = ingredienteDAO.eliminarIngrediente(idIngrediente);
            return IngredienteAdapter.entidadADTO(ingredienteEliminado);
        }
        catch(Exception ex){
            throw new NegocioException("Error al tratar de eliminar el ingrediente con id " + idIngrediente);
        }
        
    }

    @Override
    public IngredienteDTO obtenerIngredientePorId(Long idIngrediente) throws NegocioException {
        if (idIngrediente == null || idIngrediente <= 0) {
            throw new NegocioException("El id del ingrediente a obtener no es valido");
        }
        
        try {
            Ingrediente ingredienteBuscado = ingredienteDAO.obtenerIngredientePorId(idIngrediente);

            return IngredienteAdapter.entidadADTO(ingredienteBuscado);
        }
        catch(Exception ex){
            throw new NegocioException("Error al obtener ingrediente por id: " + ex.getMessage());
        }
    }

    @Override
    public List<IngredienteDTO> obtenerIngredientePorFiltro(String filtro) throws NegocioException {
        try{
            List<Ingrediente> listaEncontrada = ingredienteDAO.obtenerIngredientePorFiltro(filtro);
            List<IngredienteDTO> listaRetornada = new ArrayList<>();
            listaEncontrada.forEach(i -> listaRetornada.add(IngredienteAdapter.entidadADTO(i)));
            
            return listaRetornada;
            
        }
        catch(Exception ex){
            throw new NegocioException("Error al tratar de obtener el ingrediente por el filtro buscado.");
        }
    }

    @Override
    public List<IngredienteDTO> obtenerIngredienteTodos() throws NegocioException {
        try{
            List<Ingrediente> listaEncontrada = ingredienteDAO.obtenerIngredienteTodos();
            List<IngredienteDTO> listaRetornada = new ArrayList<>();
            listaEncontrada.forEach(i -> listaRetornada.add(IngredienteAdapter.entidadADTO(i)));
            
            return listaRetornada;
        }
        catch(Exception ex){
            throw new NegocioException("Error al obtener la lista de ingredientes");
        }
    }
    
    /**
     * Metodo auxiliar para validar los parametros del dto ingresado pre mapeo
     * @param dto a validar
     */
    private void validarDatosIngrediente(IngredienteDTO dto){
        // 1. Validar que el objeto en sí no sea nulo
        if (dto == null) {
            throw new NegocioException("El ingrediente proporcionado está vacío o es nulo.");
        }

        // 2. Validar el nombre (que no sea nulo, ni esté en blanco, ni exceda un límite)
        String nombre = dto.getNombre();
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new NegocioException("El nombre del ingrediente es obligatorio.");
        }
        if (nombre.length() > 100) { // Puedes ajustar este límite al tamaño de tu columna en BD
            throw new NegocioException("El nombre del ingrediente es demasiado largo (máximo 100 caracteres).");
        }

        // 3. Validar el stock (que no sea nulo y que sea mayor o igual a 0)
        Double stock = dto.getStock();
        if (stock == null) {
            throw new NegocioException("El stock del ingrediente es obligatorio.");
        }
        if (stock < 0) {
            throw new NegocioException("El stock no puede ser un número negativo.");
        }

        // 4. Validar la Unidad de Medida (que se haya seleccionado una del Enum)
        if (dto.getUnidadDeMedida() == null) {
            throw new NegocioException("Debe seleccionar una unidad de medida válida.");
        }
    }
    
}
