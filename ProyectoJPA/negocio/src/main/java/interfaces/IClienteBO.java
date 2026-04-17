/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import com.dtos.ClienteDTO;
import com.dtos.ClienteFrecuenteDTO;
import com.dtos.ComandaDTO;
import excepciones.PersistenciaException;
import java.util.List;

/**
 *
 * @author joser
 */
public interface IClienteBO {

    public Integer calcularPuntos(Long idCliente) throws PersistenciaException;

    public Double calcularTotalGastado(Long idCliente) throws PersistenciaException;

    public void agregarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuenteDTO) throws PersistenciaException;

    public void actualizarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuenteDTO) throws PersistenciaException;

    public List<ComandaDTO> buscarComandasPorCliente(Long idCliente) throws PersistenciaException;

    public List<ClienteFrecuenteDTO> buscarFrecuentesPorFiltro(String filtro, String campoBusqueda) throws PersistenciaException;

    public ClienteDTO obtenerClienteGeneral() throws PersistenciaException;
    
    ClienteDTO crearClienteGeneral() throws PersistenciaException;
}
