/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import com.dtos.IngredienteDTO;
import entidades.Ingrediente;

/**
 *
 * @author joser
 */
public class IngredienteAdapter {
    public static Ingrediente dtoAEntidad(IngredienteDTO dto){
        
        if (dto == null) {
            return null;
        }
        
        Ingrediente entidad = new Ingrediente();
        
        entidad.setId(dto.getIdIngrediente());
        entidad.setNombre(dto.getNombre());
        entidad.setStock(dto.getStock());
        entidad.setUnidadDeMedida(dto.getUnidadDeMedida());
        entidad.setUrlImagen(dto.getUrlImagen());
        
        return entidad;
        
    }
    
    public static IngredienteDTO entidadADTO(Ingrediente ingrediente){
        if (ingrediente == null) {
           return null; 
        }
        IngredienteDTO dto = new IngredienteDTO();
        
        dto.setIdIngrediente(ingrediente.getId());
        dto.setNombre(ingrediente.getNombre());
        dto.setStock(ingrediente.getStock());
        dto.setUnidadDeMedida(ingrediente.getUnidadDeMedida());
        dto.setUrlImagen(ingrediente.getUrlImagen());
        
        return dto;
        
    }
}
