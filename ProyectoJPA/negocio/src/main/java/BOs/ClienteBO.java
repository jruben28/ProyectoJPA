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
import adaptadores.ComandaAdapter;
import com.dtos.ClienteDTO;
import com.dtos.ClienteFrecuenteDTO;
import com.dtos.ComandaDTO;
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

    private final IClienteDAO clienteDAO;
    private static final Logger LOG = Logger.getLogger(ClienteBO.class.getName());

    private static final String REGEX_CORREO = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String REGEX_TELEFONO = "^\\d{10}$";

    /**
     * Constructor por defecto: crea su propia dependencia.
     */
    public ClienteBO() {
        this.clienteDAO = new ClienteDAO();
    }

    /**
     * Constructor con inyección de dependencia. Útil para pruebas unitarias con
     * mocks.
     *
     * @param clienteDAO implementación del DAO a usar
     */
    public ClienteBO(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Override
    public Integer calcularPuntos(Long idCliente) throws NegocioException {
        Double total = calcularTotalGastado(idCliente);
        int puntos = (int) (total / 20);
        return Math.max(puntos, 0);
    }

    @Override
    public Double calcularTotalGastado(Long idCliente) throws NegocioException {
        List<ComandaDTO> comandas = buscarComandasPorCliente(idCliente);
        double total = 0.0;
        for (ComandaDTO c : comandas) {
            if (c.getTotal() != null) {
                total += c.getTotal();
            }
        }
        return Math.max(total, 0.0);
    }

    @Override
    public void agregarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuenteDTO) throws NegocioException {
        validarClienteFrecuenteDTO(clienteFrecuenteDTO);
        try {
            clienteFrecuenteDTO.setTelefono(Encriptador.encriptar(clienteFrecuenteDTO.getTelefono()));
            ClienteFrecuente clienteF = ClienteFrecuenteAdapter.dtoAEntidad(clienteFrecuenteDTO);
            clienteDAO.agregarClienteFrecuente(clienteF);
        } catch (PersistenciaException ex) {
            LOG.warning("Error en negocio al agregar cliente frecuente: " + ex.getMessage());
            throw new NegocioException("Error al agregar un cliente frecuente");
        }
    }

    @Override
    public void actualizarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuenteDTO) throws NegocioException {
        validarClienteFrecuenteDTO(clienteFrecuenteDTO);
        if (clienteFrecuenteDTO.getId() == null) {
            throw new PersistenciaException("No se puede actualizar el cliente porque no tiene un ID asignado.");
        }
        try {
            clienteFrecuenteDTO.setTelefono(Encriptador.encriptar(clienteFrecuenteDTO.getTelefono()));
            ClienteFrecuente clienteF = ClienteFrecuenteAdapter.dtoAEntidad(clienteFrecuenteDTO);
            clienteDAO.actualizarClienteFrecuente(clienteF);
        } catch (PersistenciaException ex) {
            LOG.warning("Error en negocio al actualizar cliente frecuente: " + ex.getMessage());
            throw new NegocioException("Error al intentar actualizar los datos del cliente en la base de datos.");
        }
    }

    @Override
    public List<ComandaDTO> buscarComandasPorCliente(Long idCliente) throws NegocioException {
        if (idCliente == null || idCliente <= 0) {
            throw new PersistenciaException("El ID del cliente no es válido para realizar la búsqueda de comandas.");
        }
        try {
            List<Comanda> entidades = clienteDAO.buscarComandasPorCliente(idCliente);
            List<ComandaDTO> dtos = new ArrayList<>();
            for (Comanda c : entidades) {
                dtos.add(ComandaAdapter.entidadADTO(c));
            }
            return dtos;
        } catch (PersistenciaException ex) {
            LOG.warning("Error al buscar comandas del cliente con ID " + idCliente + ": " + ex.getMessage());
            throw new NegocioException("Error al obtener el historial de comandas del cliente.");
        }
    }

    @Override
    public List<ClienteFrecuenteDTO> buscarFrecuentesPorFiltro(String filtro, String campoBusqueda) throws NegocioException {
        if (filtro == null || filtro.trim().isEmpty()) {
            throw new PersistenciaException("El filtro de búsqueda no puede estar vacío");
        }
        try {
            List<ClienteFrecuente> clientes = clienteDAO.buscarFrecuentesPorCampo(filtro, campoBusqueda);
            List<ClienteFrecuenteDTO> resultado = new ArrayList<>();

            for (ClienteFrecuente c : clientes) {
                List<Comanda> comandas = clienteDAO.buscarComandasPorCliente(c.getId());

                double totalGastado = 0.0;
                for (Comanda cmd : comandas) {
                    if (cmd.getTotal() != null) {
                        totalGastado += cmd.getTotal();
                    }
                }
                totalGastado = Math.max(totalGastado, 0.0);

                int puntos = (int) (totalGastado / 20);
                int numVisitas = comandas.size();

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
    public ClienteDTO obtenerClienteGeneral() throws NegocioException {
        try {
            ClienteGeneral entidad = clienteDAO.obtenerClienteGeneral();
            if (entidad == null) {
                return null;
            }
            ClienteDTO dto = new ClienteDTO();
            dto.setId(entidad.getId());
            dto.setNombre(entidad.getNombre());
            dto.setTipoCliente("GENERAL");
            return dto;
        } catch (PersistenciaException ex) {
            LOG.warning("Error al obtener cliente general: " + ex.getMessage());
            throw new NegocioException("Error al consultar el cliente general");
        }
    }

    @Override
    public ClienteDTO crearClienteGeneral() throws NegocioException {
        try {
            ClienteGeneral existente = clienteDAO.obtenerClienteGeneral();
            if (existente != null) {
                throw new PersistenciaException("El Cliente General ya existe. Use obtenerClienteGeneral().");
            }
            ClienteGeneral nuevo = new ClienteGeneral("Cliente General");
            clienteDAO.agregar(nuevo);
            LOG.info("Cliente General creado con ID: " + nuevo.getId());

            ClienteDTO dto = new ClienteDTO();
            dto.setId(nuevo.getId());
            dto.setNombre(nuevo.getNombre());
            dto.setTipoCliente("GENERAL");
            return dto;
        } catch (PersistenciaException ex) {
            LOG.warning("Error al crear cliente general: " + ex.getMessage());
            throw new NegocioException("Error al crear el cliente general");
        }
    }

    private void validarClienteFrecuenteDTO(ClienteFrecuenteDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()
                || dto.getNombre().length() > 200) {
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
}
