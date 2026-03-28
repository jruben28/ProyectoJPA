/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package BOs;

import Entidades.Comanda;
import com.dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author joser
 */
public interface IClienteBO {
    
    public Integer calcularPuntos(Long idCliente) throws NegocioException;
    
    public Double calcularTotalGastado(Long idCliente)throws NegocioException;
    
    public void agregarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuenteDTO)throws NegocioException;
    
    public void actualizarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuenteDTO) throws NegocioException;
    
    List<Comanda> buscarComandasPorCliente(Long idCliente) throws NegocioException;

    List<ClienteFrecuenteDTO> buscarFrecuentesPorFiltro(String filtro, String campoBusqueda) throws NegocioException;

    String obtenerOCrearClienteGeneral() throws NegocioException;
}
