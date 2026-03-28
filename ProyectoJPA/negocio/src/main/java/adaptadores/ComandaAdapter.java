/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import Entidades.Comanda;
import com.dtos.ComandaDTO;

/**
 *
 * @author joser
 */
public class ComandaAdapter {
    public static Comanda dtoAEntidad(ComandaDTO dto){
        if(dto == null){
            return null;
        }
        
        Comanda comanda = new Comanda();
        
        comanda.setIdCliente(dto.getIdCliente());
        comanda.setEstado(dto.getEstado());
        comanda.setTotal(dto.getTotal());
        
        return comanda;
    }
}
