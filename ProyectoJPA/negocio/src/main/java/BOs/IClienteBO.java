/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package BOs;

import com.dtos.ClienteDTO;
import com.dtos.ClienteFrecuenteDTO;
import excepciones.BOException;

/**
 *
 * @author joser
 */
public interface IClienteBO {
    
    public Integer calcularPuntos(Long idCliente);
    
    public Double calcularTotalGastado(Long idCliente);
    
    public void agregarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuenteDTO) throws BOException;
    
    public void actualizarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuenteDTO) throws BOException;
    
    public void agregarCliente(ClienteDTO clienteDTO) throws BOException;
    
}
