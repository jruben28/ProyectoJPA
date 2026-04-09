/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import com.dtos.ComboDTO;
import entidades.Combo;

/**
 * Adapter de Combo de DTO a entidad.
 * @author Adrian Mendoza 
 */
public class ComboAdapter {
   
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
        combo.setActivo(dto.getActivo());
        return combo;
    }
    
}
