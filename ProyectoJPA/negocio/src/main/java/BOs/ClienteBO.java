/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import interfaces.IClienteBO;
import DAOs.ClienteDAO;
import interfaces.IClienteDAO;
import entidades.ClienteFrecuente;
import entidades.ClienteGeneral;
import entidades.Comanda;
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

    private static final Logger LOG = Logger.getLogger(ClienteBO.class.getName()); // 👈 ¡AGREGADO!

    private final IClienteDAO clienteDAO;

    public ClienteBO() {
        this.clienteDAO = new ClienteDAO();
    }

    public ClienteBO(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Override
    public Integer calcularPuntos(Long idCliente) {
        Double total = calcularTotalGastado(idCliente);
        Integer puntos = (int) (total / 20);
        return puntos < 0 ? 0 : puntos;
    }

    @Override
    public Double calcularTotalGastado(Long idCliente) {
        List<Comanda> comandas = clienteDAO.buscarComandasPorCliente(idCliente);
        Double total = 0.0;
        for (Comanda c : comandas) {
            total += c.getTotal();
        }
        return total < 0 ? 0.0 : total;
    }

    @Override
    public void agregarClienteFrecuente(ClienteFrecuenteDTO dto) {
        validarClienteFrecuenteDTO(dto); 

        try {
            dto.setTelefono(Encriptador.encriptar(dto.getTelefono()));
            ClienteFrecuente clienteF = ClienteFrecuenteAdapter.dtoAEntidad(dto);
            clienteDAO.agregarClienteFrecuente(clienteF);
        } catch (PersistenciaException ex) {
            LOG.warning("Error en negocio al agregar cliente frecuente: " + ex.getMessage());
            throw new NegocioException("Error al agregar un cliente frecuente");
        }
    }

 
    private void validarClienteFrecuenteDTO(ClienteFrecuenteDTO dto) {
        String REGEX_CORREO = "^[A-Za-z0-9+_.-]+@(.+)$";
        String REGEX_TELEFONO = "^\\d{10}$";

        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty() || dto.getNombre().length() > 200) {
            throw new NegocioException("El nombre del cliente no es válido");
        }
        if (dto.getCorreo() != null && !dto.getCorreo().trim().isEmpty()
                && !Pattern.matches(REGEX_CORREO, dto.getCorreo())) {
            throw new NegocioException("El correo del cliente no es válido");
        }
        if (dto.getTelefono() == null || !Pattern.matches(REGEX_TELEFONO, dto.getTelefono())) {
            throw new NegocioException("El teléfono del cliente no es válido");
        }
        if (dto.getFechaRegistro() == null || dto.getFechaRegistro().after(new Date())) {
            throw new NegocioException("La fecha de registro no es válida");
        }
    }

    @Override
    public List<ClienteFrecuenteDTO> buscarFrecuentesPorFiltro(String filtro, String campoBusqueda) {
        if (filtro == null || filtro.trim().isEmpty()) {
            throw new NegocioException("El filtro de búsqueda no puede estar vacío");
        }
        try {
            List<ClienteFrecuente> clientes = clienteDAO.buscarFrecuentesPorCampo(filtro, campoBusqueda);
            List<ClienteFrecuenteDTO> resultado = new ArrayList<>();

            for (ClienteFrecuente c : clientes) {
                
                Double totalGastado = calcularTotalGastado(c.getId());
                Integer puntos = calcularPuntos(c.getId());
                Integer numVisitas = clienteDAO.buscarComandasPorCliente(c.getId()).size();

                ClienteFrecuenteDTO dto = ClienteFrecuenteAdapter.entidadADTO(c, puntos, totalGastado, numVisitas);
                dto.setTelefono(Encriptador.desencriptar(c.getTelefono()));
                resultado.add(dto);
            }
            return resultado;
        } catch (PersistenciaException ex) {
            LOG.warning("Error al buscar clientes frecuentes: " + ex.getMessage());
            throw new NegocioException("Error al buscar clientes frecuentes");
        }
    }

    @Override
    public List<Comanda> buscarComandasPorCliente(Long idCliente) {
        if (idCliente == null || idCliente <= 0) {
            throw new NegocioException("El ID del cliente no es válido para realizar la búsqueda de comandas.");
        }
        try {
            return clienteDAO.buscarComandasPorCliente(idCliente);
        } catch (PersistenciaException ex) {
            LOG.warning("Error al buscar las comandas del cliente con ID " + idCliente + ": " + ex.getMessage());
            throw new NegocioException("Error al obtener el historial de comandas del cliente.");
        }
    }

    @Override
    public void actualizarClienteFrecuente(ClienteFrecuenteDTO dto) {
        validarClienteFrecuenteDTO(dto);
        if (dto.getId() == null) {
            throw new NegocioException("No se puede actualizar el cliente porque no tiene un ID asignado.");
        }
        try {
            dto.setTelefono(Encriptador.encriptar(dto.getTelefono()));
            ClienteFrecuente clienteF = ClienteFrecuenteAdapter.dtoAEntidad(dto);
            clienteDAO.actualizarClienteFrecuente(clienteF);
        } catch (PersistenciaException ex) {
            LOG.warning("Error en negocio al actualizar cliente frecuente: " + ex.getMessage());
            throw new NegocioException("Error al intentar actualizar los datos del cliente en la base de datos.");
        }
    }

    @Override
    public String obtenerOCrearClienteGeneral() {
        try {
            ClienteGeneral clienteGeneral = clienteDAO.obtenerClienteGeneral();
            if (clienteGeneral == null) {
                clienteGeneral = new ClienteGeneral("Cliente General");
                clienteDAO.agregar(clienteGeneral);
            }
            return clienteGeneral.getNombre();
        } catch (PersistenciaException ex) {
            LOG.warning("Error al obtener o crear cliente general: " + ex.getMessage());
            throw new NegocioException("Error al preparar el cliente general");
        }
    }
}