/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import DAOs.ClienteDAO;
import DAOs.IClienteDAO;
import Entidades.ClienteFrecuente;
import Entidades.Comanda;
import adaptadores.ClienteFrecuenteAdapter;
import com.dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Business Object de la entidad Cliente.
 *
 * @author keppler
 */
public class ClienteBO implements IClienteBO {

    private IClienteDAO clienteDAO;
    private static final Logger LOG = Logger.getLogger(ClienteBO.class.getName());

    public ClienteBO() {
        this.clienteDAO = new ClienteDAO();
    }

    /**
     * Calcula los puntos generados por las compras del cliente 1 punto por cada
     * 20 pesos
     *
     * @param idCliente
     * @return puntos
     */
    public Integer calcularPuntos(Long idCliente) {
        Double total = calcularTotalGastado(idCliente);
        Integer puntos = (int) (total / 20);

        if (puntos < 0) {
            puntos = 0;
        }
        return puntos;
    }

    /**
     * Calcula el dinero total gastado por el cliente
     *
     * @param idCliente
     * @return total
     */
    public Double calcularTotalGastado(Long idCliente) {
        List<Comanda> comandas = clienteDAO.buscarComandasPorCliente(idCliente);
        Double total = 0.0;
        for (Comanda c : comandas) {
            total += c.getTotal();
        }   
        if (total < 0) {
            total = 0.0;
        }
        return total;
    }

    @Override
    public void agregarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuenteDTO) {
        //validarClienteFrecuenteDTO(clienteFrecuenteDTO);
        try {
            ClienteFrecuente clienteF = ClienteFrecuenteAdapter.dtoAEntidad(clienteFrecuenteDTO);

            clienteDAO.agregarClienteFrecuente(clienteF);

        } catch (PersistenciaException ex) {
            LOG.warning("Error en negocio al agregar cliente frecuente" + ex.getMessage());
            throw new NegocioException("Error al agregar un cliente frecuente");
        }
    }
    @Override
    public void validarClienteFrecuenteDTO(ClienteFrecuenteDTO clienteFrecuenteDTO) {
        //Agregar validacion de cliente Frecuente DTO
    }
;
   // en progreso
    @Override
    public List<Comanda> buscarComandasPorCliente(Long idCliente) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
