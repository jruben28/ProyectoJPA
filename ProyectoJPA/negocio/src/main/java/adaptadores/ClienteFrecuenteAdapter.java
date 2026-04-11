/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import entidades.ClienteFrecuente;
import com.dtos.ClienteFrecuenteDTO;
import utilidades.Encriptador;

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
        
        clienteF.setId(dto.getId());
        clienteF.setNombre(dto.getNombre());
        clienteF.setCorreo(dto.getCorreo());
        clienteF.setFechaRegistro(dto.getFechaRegistro());
        clienteF.setTelefono(dto.getTelefono());
        
        return clienteF;
    }
    
    /**
     * Convierte una entidad a DTO 
     * puntos  y total gastado en parametros gracias a que estos datos son calculados por el BO
     * @param entidad 
     * @param puntos
     * @param totalGastado
     * @return dto
     */
    public static ClienteFrecuenteDTO entidadADTO(ClienteFrecuente entidad, Integer puntos, Double totalGastado, Integer numVisitas){
        if(entidad==null){
            return null;
        }
        ClienteFrecuenteDTO dto=new ClienteFrecuenteDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setTipoCliente("FRECUENTE");
        dto.setCorreo(entidad.getCorreo());
        dto.setFechaRegistro(entidad.getFechaRegistro());
        dto.setPuntosAcumulados(puntos);
        dto.setTotalGastado(totalGastado);
        dto.setNumVisitas(numVisitas);
        dto.setTelefono(Encriptador.desencriptar(entidad.getTelefono()));
        return dto;
    }
}
