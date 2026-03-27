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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import utilidades.Encriptador;

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
        //No tiene que tener id, no será mapeado
        validarClienteFrecuenteDTO(clienteFrecuenteDTO);

        try {

            clienteFrecuenteDTO.setTelefono(Encriptador.encriptar(clienteFrecuenteDTO.getTelefono()));

            ClienteFrecuente clienteF = ClienteFrecuenteAdapter.dtoAEntidad(clienteFrecuenteDTO);

            clienteDAO.agregarClienteFrecuente(clienteF);

        } catch (PersistenciaException ex) {
            LOG.warning("Error en negocio al agregar cliente frecuente" + ex.getMessage());
            throw new NegocioException("Error al agregar un cliente frecuente");
        }
    }

    public void validarClienteFrecuenteDTO(ClienteFrecuenteDTO dto) {
        //Agregar validacion de cliente Frecuente DTO
        //Agregar validacion de cliente Frecuente DTO
        String REGEX_CORREO = "^[A-Za-z0-9+_.-]+@(.+)$";
        String REGEX_TELEFONO = "^\\d{10}$";

        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty() || dto.getNombre().length() > 200) {
            throw new NegocioException("El nombre del cliente no es valido");
        }

        //modificado para que sea opcional
        if (dto.getCorreo() != null && !dto.getCorreo().trim().isEmpty()
                && !Pattern.matches(REGEX_CORREO, dto.getCorreo())) {
            throw new NegocioException("El correo del cliente no es valido");
        }

        if (dto.getTelefono() == null || !Pattern.matches(REGEX_TELEFONO, dto.getTelefono())) {
            throw new NegocioException("El telefono del cliente no es valido");
        }

        if (dto.getFechaRegistro() == null || dto.getFechaRegistro().after(new Date())) {
            throw new NegocioException("La fecha de registro no es valida");
        }
    }

    /**
     *  Aplica filtros a la busqueda de cllientesFrecuentes,  
     * @param filtro
     * @param campoBusqueda
     * @return
     * @throws NegocioException 
     */
   @Override
    public List<ClienteFrecuenteDTO> buscarFrecuentesPorFiltro(String filtro, String campoBusqueda) throws NegocioException {
        if (filtro == null || filtro.trim().isEmpty()) {
            throw new NegocioException("El filtro de busqueda no puede estar vacio");
        }
        try {
            List<ClienteFrecuente> clientes = clienteDAO.buscarFrecuentesPorCampo(filtro, campoBusqueda);
            List<ClienteFrecuenteDTO> resultado = new ArrayList<>();

            for (ClienteFrecuente c : clientes) {
                List<Comanda> comandas = clienteDAO.buscarComandasPorCliente(c.getId());

                Double totalGastado = 0.0;
                for (Comanda cmd : comandas) {
                    totalGastado += cmd.getTotal();
                }
                if (totalGastado < 0) {
                    totalGastado = 0.0;
                }

                Integer puntos = (int) (totalGastado / 20);
                if (puntos < 0) {
                    puntos = 0;
                }

                Integer numVisitas = comandas.size();

                ClienteFrecuenteDTO dto = ClienteFrecuenteAdapter.entidadADTO(c, puntos, totalGastado, numVisitas);
                
                // Desencriptar el telefono
                dto.setTelefono(Encriptador.desencriptar(c.getTelefono()));
                
                resultado.add(dto);
            }

            return resultado;
        } catch (PersistenciaException ex) {
            LOG.warning("Error al buscar clientes frecuentes: " + ex.getMessage());
            throw new NegocioException("Error al buscar clientes frecuentes");
        }
    }

    // en progreso
    @Override
    public List<Comanda> buscarComandasPorCliente(Long idCliente) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuenteDTO) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
   
}
