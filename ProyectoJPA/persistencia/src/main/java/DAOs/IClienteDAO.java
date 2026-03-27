/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOs;
import Entidades.Comanda;
import Entidades.Cliente;
import Entidades.ClienteFrecuente;
import Entidades.ClienteGeneral;
import excepciones.PersistenciaException;
import java.util.List;


/**
 *
 * @author icoro
 */
public interface IClienteDAO {
    Cliente agregar(Cliente cliente) throws PersistenciaException;

    Cliente actualizar(Cliente cliente)throws PersistenciaException;
    
    ClienteFrecuente agregarClienteFrecuente(ClienteFrecuente clienteFrecuente)throws PersistenciaException;
    
    ClienteFrecuente actualizarClienteFrecuente(ClienteFrecuente clienteFrecuente)throws PersistenciaException;
    

    Cliente buscarPorId(Long id);

    // Este es el buscador solicitado para el módulo y las comandas
    List<ClienteFrecuente> buscarFrecuentesPorFiltro(String filtro)throws PersistenciaException;

    // Para obtener el registro "Cliente General" cuando no se identifica al comensal
    ClienteGeneral obtenerClienteGeneral()throws PersistenciaException;
    
    List<Comanda> buscarComandasPorCliente(Long idCliente) throws PersistenciaException; 
    
}
