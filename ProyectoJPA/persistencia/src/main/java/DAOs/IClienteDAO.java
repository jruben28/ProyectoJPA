/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOs;

import Entidades.Cliente;
import Entidades.ClienteFrecuente;
import Entidades.ClienteGeneral;
import java.util.List;


/**
 *
 * @author icoro
 */
public interface IClienteDAO {
    Cliente agregar(Cliente cliente);

    Cliente actualizar(Cliente cliente);
    
    ClienteFrecuente agregarClienteFrecuente(ClienteFrecuente clienteFrecuente);
    
    ClienteFrecuente actualizarClienteFrecuente(ClienteFrecuente clienteFrecuente);
    

    Cliente buscarPorId(Long id);

    // Este es el buscador solicitado para el módulo y las comandas
    List<ClienteFrecuente> buscarFrecuentesPorFiltro(String filtro);

    // Para obtener el registro "Cliente General" cuando no se identifica al comensal
    ClienteGeneral obtenerClienteGeneral();
}
