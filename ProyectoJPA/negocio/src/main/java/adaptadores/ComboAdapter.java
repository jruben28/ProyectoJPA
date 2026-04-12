/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import com.dtos.ComboDTO;
import entidades.Combo;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter de Combo de DTO a entidad, y de entidad a DTO
 * @author Adrian Mendoza 
 */
public class ComboAdapter {
   /**
    * Convierte un DTO de Combo a Entidad
    * @param dto de combo
    * @return Combo
    */
    public static Combo dtoAEntidad(ComboDTO dto){
        if(dto==null){
            return null;
        }
        Combo combo= new Combo();
        combo.setNombre(dto.getNombre());
        combo.setDescripcion(dto.getDescripcion());
        combo.setPrecioCombo(dto.getPrecioCombo());
        combo.setPrecioOriginal(dto.getPrecioOriginal());
        combo.setPorcentajeDescuento(dto.getPorcentajeDescuento());
        if(dto.getActivo()==null){
        combo.setActivo(true);
        }else{
        combo.setActivo(dto.getActivo());    
        }
        return combo;
    }
    /**
     * Convierte una entidad Combo a DTO
     * @param combo entidad de Combo
     * @return ComboDTO
     */
    public static ComboDTO entidadADTO(Combo combo){
        if(combo==null){
            return null;
        }
        ComboDTO dto = new ComboDTO();
        dto.setNombre(combo.getNombre());
        dto.setDescripcion(combo.getDescripcion());
        dto.setPrecioCombo(combo.getPrecioCombo());
        dto.setPrecioOriginal(combo.getPrecioOriginal());
        dto.setPorcentajeDescuento(combo.getPorcentajeDescuento());
        dto.setActivo(combo.getActivo());
        return dto;
    }
    
    /**
     * Convierte una lista de entidades Combo a una lista de DTOs
     * @param combos Lista de entidades Combo
     * @return Lista de ComboDTO
     */
    public static List<ComboDTO> listaEntidadADTO(List<Combo> combos) {
        if (combos == null) {
            return null;
        }
        List<ComboDTO> lista = new ArrayList<>();
        for (Combo c : combos) {
            lista.add(entidadADTO(c));
        }
        return lista;
    }
    
}
