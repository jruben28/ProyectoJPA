/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import DAOs.ClienteDAO;
import Entidades.Cliente;
import Entidades.ClienteFrecuente;
import Entidades.ClienteGeneral;
import Entidades.Comanda;
import adaptadores.ClienteFrecuenteAdapter;
import com.dtos.ClienteFrecuenteDTO;
import excepciones.BOException;
import excepciones.DAOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Business Object de la entidad Cliente.
 *
 * @author keppler
 */
public class ClienteBO implements IClienteBO{

    private ClienteDAO clienteDAO;
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
        if(total<0){
            total=0.0;
        }
        return total;
    }

    @Override
    public void agregarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuenteDTO) {
        //validar datos cliente
        validarClienteFrecuenteDTO(clienteFrecuenteDTO);
        
        //clienteFrecuenteDTO no debe tener id pues no será mapeado a entity
        try{
            ClienteFrecuente clienteF = ClienteFrecuenteAdapter.dtoAEntidad(clienteFrecuenteDTO);
            
            clienteDAO.agregarClienteFrecuente(clienteF);
            
        }
        catch(DAOException ex){
            LOG.warning("Error en negocio al agregar cliente frecuente" + ex.getMessage());
            throw new BOException("Error al agregar un cliente frecuente");
        }
    }

    public void validarClienteFrecuenteDTO(ClienteFrecuenteDTO dto){
        //Agregar validacion de cliente Frecuente DTO
        String REGEX_CORREO = "^[A-Za-z0-9+_.-]+@(.+)$";
        String REGEX_TELEFONO = "^\\d{10}$";
        
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty() || dto.getNombre().length() > 200) {
            throw new BOException("El nombre del cliente no es valido");
        }

        if (dto.getCorreo() == null || !Pattern.matches(REGEX_CORREO, dto.getCorreo())) {
            throw new BOException("El correo del cliente no es valido");
        }

        if (dto.getTelefono() == null || !Pattern.matches(REGEX_TELEFONO, dto.getTelefono())) {
            throw new BOException("El telefono del cliente no es valido");
        }

        if (dto.getFechaRegistro()== null || dto.getFechaRegistro().after(new Date())) {
            throw new BOException("La fecha de registro no es valida");
        }

    };

    @Override
    public void actualizarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuenteDTO) throws BOException {
        //validacion datos cliente
        validarClienteFrecuenteDTO(clienteFrecuenteDTO);
        
        //validacion id del cliente
        if (clienteFrecuenteDTO.getId() == null || clienteFrecuenteDTO.getId() < 0) {
            throw new BOException("El id del cliente que se quiere actualizar no es valido.");
        }
        
        
        try{
            ClienteFrecuente clienteF = ClienteFrecuenteAdapter.dtoAEntidad(clienteFrecuenteDTO);
            clienteF.setId(clienteFrecuenteDTO.getId());
            
            clienteDAO.agregarClienteFrecuente(clienteF);
            
        }
        catch(DAOException ex){
            LOG.warning("Error en negocio al agregar cliente frecuente" + ex.getMessage());
            throw new BOException("Error al agregar un cliente frecuente");
        }
    }
    
}
