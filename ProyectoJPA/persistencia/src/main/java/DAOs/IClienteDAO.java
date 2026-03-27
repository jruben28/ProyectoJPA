/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOs;

import Entidades.Cliente;
import Entidades.ClienteFrecuente;
import Entidades.ClienteGeneral;
import excepciones.DAOException;
import java.util.List;


/**
 *
 * @author icoro
 */
public interface IClienteDAO {
    Cliente agregar(Cliente cliente) throws DAOException;

    Cliente actualizar(Cliente cliente)throws DAOException;
    
    ClienteFrecuente agregarClienteFrecuente(ClienteFrecuente clienteFrecuente)throws DAOException;
    
    ClienteFrecuente actualizarClienteFrecuente(ClienteFrecuente clienteFrecuente)throws DAOException;
    

    Cliente buscarPorId(Long id);

    // Este es el buscador solicitado para el módulo y las comandas
    List<ClienteFrecuente> buscarFrecuentesPorFiltro(String filtro)throws DAOException;

    // Para obtener el registro "Cliente General" cuando no se identifica al comensal
    ClienteGeneral obtenerClienteGeneral()throws DAOException;
}
