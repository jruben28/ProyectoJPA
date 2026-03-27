/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import Entidades.ClienteFrecuente;
import com.dtos.ClienteFrecuenteDTO;

/**
 *
 * @author joser
 */
public class ClienteFrecuenteAdapter {
    public static ClienteFrecuente dtoAEntidad(ClienteFrecuenteDTO dto){
        if (dto == null) {
            return null;
        }
        
        ClienteFrecuente clienteF = new ClienteFrecuente();
        
        clienteF.setNombre(dto.getNombre());
        clienteF.setCorreo(dto.getCorreo());
        clienteF.setFechaRegistro(dto.getFechaRegistro());
        clienteF.setTelefono(dto.getTelefono());
        
        return clienteF;
    }
}
