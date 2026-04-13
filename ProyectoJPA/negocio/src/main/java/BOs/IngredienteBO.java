/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import DAOs.IngredienteDAO;
import com.dtos.IngredienteDTO;
import excepciones.NegocioException;
import interfaces.IIngredienteBO;
import interfaces.IIngredienteDAO;
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public IngredienteDTO actualizarStock(Long idIngrediente, Double stock) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public IngredienteDTO eliminarIngrediente(Long idIngrediente) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public IngredienteDTO obtenerIngredientePorId(Long idIngrediente) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<IngredienteDTO> obtenerIngredientePorFiltro(String filtro) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<IngredienteDTO> obtenerIngredienteTodos() throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
