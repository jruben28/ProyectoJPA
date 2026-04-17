/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import adaptadores.ComandaAdapter;
import com.dtos.ComandaDTO;
import entidades.Cliente;
import entidades.ClienteGeneral;
import entidades.Comanda;
import entidades.Combo;
import entidades.ComboProducto;
import entidades.DetalleComanda;
import entidades.Mesa;
import entidades.Producto;
import entidades.ProductoIngrediente;
import enums.EstadoComanda;
import enums.EstadoMesa;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IClienteDAO;
import interfaces.IComandaBO;
import interfaces.IComandaDAO;
import interfaces.IComboDAO;
import interfaces.IIngredienteDAO;
import interfaces.IMesaDAO;
import interfaces.IProductoDAO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementación de la interfaz IComandaBO.
 *
 * @author adrian mendoza
 */
public class ComandaBO implements IComandaBO {

    private static final Logger LOG = Logger.getLogger(ComandaBO.class.getName());

    private final IComandaDAO comandaDAO;
    private final IClienteDAO clienteDAO;
    private final IMesaDAO mesaDAO;
    private final IProductoDAO productoDAO;
    private final IComboDAO comboDAO;
    private final IIngredienteDAO ingredienteDAO;


    public ComandaBO() {
        this.comandaDAO = new DAOs.ComandaDAO();
        this.clienteDAO = new DAOs.ClienteDAO();
        this.mesaDAO = new DAOs.MesaDAO();
        this.productoDAO = new DAOs.ProductoDAO();
        this.comboDAO = new DAOs.ComboDAO();
        this.ingredienteDAO = new DAOs.IngredienteDAO();
    }

   
    public ComandaBO(IComandaDAO comandaDAO, IClienteDAO clienteDAO,
            IMesaDAO mesaDAO, IProductoDAO productoDAO,
            IComboDAO comboDAO, IIngredienteDAO ingredienteDAO) {
        this.comandaDAO = comandaDAO;
        this.clienteDAO = clienteDAO;
        this.mesaDAO = mesaDAO;
        this.productoDAO = productoDAO;
        this.comboDAO = comboDAO;
        this.ingredienteDAO = ingredienteDAO;
    }

    @Override
    public Comanda abrirComanda(ComandaDTO dto) throws NegocioException {
        if (dto == null || dto.getIdMesa() == null) {
            throw new NegocioException("Datos insuficientes para abrir comanda");
        }

        try {
            Mesa mesa = mesaDAO.buscarPorId(dto.getIdMesa());
            if (mesa == null) {
                throw new NegocioException("Mesa no encontrada con id: " + dto.getIdMesa());
            }
            if (comandaDAO.mesaTieneComandaAbierta(mesa.getId())) {
                throw new NegocioException("La mesa ya tiene una comanda abierta");
            }

            Cliente cliente = (dto.getIdCliente() != null)
                    ? clienteDAO.buscarPorId(dto.getIdCliente())
                    : obtenerClienteGeneral();

            if (cliente == null) {
                throw new NegocioException("No se pudo asignar un cliente a la comanda");
            }

            String folio = generarFolio();
            Comanda comanda = new Comanda(folio, LocalDateTime.now(), mesa, cliente);
            comanda.setEstado(EstadoComanda.ABIERTA);
            comanda.setTotal(0.0);

            Comanda guardada = comandaDAO.agregarComanda(comanda);
            LOG.info("Comanda abierta con folio: " + folio);
            return guardada;

        } catch (PersistenciaException ex) {
            LOG.warning("Error de persistencia al abrir comanda: " + ex.getMessage());
            throw new NegocioException("Error al abrir la comanda", ex);
        }
    }

    @Override
    public void agregarDetalleProducto(Long idComanda, Long idProducto,
            Integer cantidad, String comentario) throws NegocioException {
        validarDetalleParams(idComanda, idProducto, cantidad);

        try {
            Comanda comanda = comandaDAO.buscarPorId(idComanda);
            validarComandaAbierta(comanda);

            Producto producto = productoDAO.buscarPorId(idProducto);
            validarProductoDisponible(producto, cantidad);

            DetalleComanda detalle = new DetalleComanda(cantidad, comentario, producto);
            comanda.agregarDetalle(detalle);
            comandaDAO.actualizar(comanda);

            LOG.info("Detalle producto agregado a comanda " + idComanda);
        } catch (PersistenciaException ex) {
            LOG.warning("Error al agregar detalle de producto: " + ex.getMessage());
            throw new NegocioException("Error al agregar detalle de producto", ex);
        }
    }

    @Override
    public void agregarDetalleCombo(Long idComanda, Long idCombo,
            Integer cantidad, String comentario) throws NegocioException {
        validarDetalleParams(idComanda, idCombo, cantidad);

        try {
            Comanda comanda = comandaDAO.buscarPorId(idComanda);
            validarComandaAbierta(comanda);

            Combo combo = comboDAO.buscarComboPorIdConDetalles(idCombo);
            validarComboDisponible(combo, cantidad);

            DetalleComanda detalle = new DetalleComanda(cantidad, comentario, combo);
            comanda.agregarDetalle(detalle);
            comandaDAO.actualizar(comanda);

            LOG.info("Detalle combo agregado a comanda " + idComanda);
        } catch (PersistenciaException ex) {
            LOG.warning("Error al agregar detalle de combo: " + ex.getMessage());
            throw new NegocioException("Error al agregar detalle de combo", ex);
        }
    }

    @Override
    public void entregarComanda(Long idComanda) throws NegocioException {
        validarId(idComanda, "ID de comanda");

        try {
            Comanda comanda = comandaDAO.buscarPorId(idComanda);
            validarComandaAbierta(comanda);
            if (comanda.getDetalles() == null || comanda.getDetalles().isEmpty()) {
                throw new NegocioException("No se puede entregar una comanda sin productos");
            }

    
            descontarStockDeDetalles(comanda.getDetalles());

            comanda.setEstado(EstadoComanda.ENTREGADA);
            comanda.calcularTotal();

            Mesa mesa = comanda.getMesa();
            mesa.setEstado(EstadoMesa.DISPONIBLE);
            mesaDAO.actualizar(mesa);

            comandaDAO.actualizar(comanda);
            LOG.info("Comanda " + idComanda + " entregada");
        } catch (PersistenciaException ex) {
            LOG.warning("Error al entregar comanda: " + ex.getMessage());
            throw new NegocioException("Error al entregar la comanda", ex);
        }
    }

    @Override
    public void cancelarComanda(Long idComanda) throws NegocioException {
        validarId(idComanda, "ID de comanda");

        try {
            Comanda comanda = comandaDAO.buscarPorId(idComanda);
            validarComandaAbierta(comanda);

            comanda.setEstado(EstadoComanda.CANCELADA);
            Mesa mesa = comanda.getMesa();
            mesa.setEstado(EstadoMesa.DISPONIBLE);
            mesaDAO.actualizar(mesa);

            comandaDAO.actualizar(comanda);
            LOG.info("Comanda " + idComanda + " cancelada");
        } catch (PersistenciaException ex) {
            LOG.warning("Error al cancelar comanda: " + ex.getMessage());
            throw new NegocioException("Error al cancelar la comanda", ex);
        }
    }

    @Override
    public ComandaDTO buscarPorId(Long idComanda) throws NegocioException {
        validarId(idComanda, "ID de comanda");
        try {
            Comanda comanda = comandaDAO.buscarPorId(idComanda);
            return ComandaAdapter.entidadADTO(comanda);
        } catch (PersistenciaException ex) {
            LOG.warning("Error al buscar comanda: " + ex.getMessage());
            throw new NegocioException("Error al buscar comanda por id", ex);
        }
    }

    @Override
    public List<ComandaDTO> buscarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta) throws NegocioException {
        if (desde == null || hasta == null || desde.isAfter(hasta)) {
            throw new NegocioException("Rango de fechas inválido");
        }
        try {
            List<Comanda> comandas = comandaDAO.buscarPorRangoFechas(desde, hasta);
            List<ComandaDTO> dtos = new ArrayList<>();
            for (Comanda c : comandas) {
                dtos.add(ComandaAdapter.entidadADTO(c));
            }
            return dtos;
        } catch (PersistenciaException ex) {
            LOG.warning("Error al buscar comandas por rango: " + ex.getMessage());
            throw new NegocioException("Error al buscar comandas por rango", ex);
        }
    }

    private void validarDetalleParams(Long idComanda, Long idProductoOCombo, Integer cantidad) throws NegocioException {
        if (idComanda == null || idComanda <= 0) {
            throw new NegocioException("ID de comanda inválido");
        }
        if (idProductoOCombo == null || idProductoOCombo <= 0) {
            throw new NegocioException("ID de producto o combo inválido");
        }
        if (cantidad == null || cantidad < 1) {
            throw new NegocioException("La cantidad debe ser mayor a 0");
        }
    }

    private void validarId(Long id, String campo) {
        if (id == null || id <= 0) {
            throw new NegocioException(campo + " inválido");
        }
    }

    private void validarComandaAbierta(Comanda comanda) {
        if (comanda == null) {
            throw new NegocioException("Comanda no encontrada");
        }
        if (comanda.getEstado() != EstadoComanda.ABIERTA) {
            throw new NegocioException("Solo se pueden modificar comandas en estado ABIERTA");
        }
    }

    private void validarProductoDisponible(Producto producto, int cantidad) throws NegocioException {
        if (producto == null) {
            throw new NegocioException("Producto no encontrado");
        }
        if (Boolean.FALSE.equals(producto.getActivo())) {
            throw new NegocioException("El producto no está activo");
        }

        if (producto.getIngredientes() != null) {
            for (ProductoIngrediente pi : producto.getIngredientes()) {
                Double stock = pi.getIngrediente().getStock();
                double requerido = pi.getCantidad() * cantidad;
                if (stock == null || stock < requerido) {
                    throw new NegocioException("Stock insuficiente para el producto: " + producto.getNombre());
                }
            }
        }
    }

    private void validarComboDisponible(Combo combo, int cantidad) throws NegocioException {
        if (combo == null) {
            throw new NegocioException("Combo no encontrado");
        }
        if (Boolean.FALSE.equals(combo.getActivo())) {
            throw new NegocioException("El combo no está activo");
        }
        for (ComboProducto cp : combo.getProductos()) {
            Producto p = cp.getProducto();
            int cantidadTotal = cp.getCantidad() * cantidad;
            validarProductoDisponible(p, cantidadTotal);
        }
    }

    private void descontarStockDeDetalles(List<DetalleComanda> detalles) throws NegocioException {
        for (DetalleComanda d : detalles) {
            if (d.getProducto() != null) {
                descontarStockDeProducto(d.getProducto(), d.getCantidad());
            } else if (d.getCombo() != null) {
                for (ComboProducto cp : d.getCombo().getProductos()) {
                    int cantidadTotal = cp.getCantidad() * d.getCantidad();
                    descontarStockDeProducto(cp.getProducto(), cantidadTotal);
                }
            }
        }
    }

    private void descontarStockDeProducto(Producto producto, int cantidad) throws NegocioException {
        if (producto.getIngredientes() == null) {
            return;
        }
        for (ProductoIngrediente pi : producto.getIngredientes()) {
            double consumido = pi.getCantidad() * cantidad;
            Double stockActual = pi.getIngrediente().getStock();
            double nuevoStock = (stockActual == null ? 0 : stockActual) - consumido;
            ingredienteDAO.actualizarStock(pi.getIngrediente().getId(), nuevoStock);
        }
    }

    private String generarFolio() throws NegocioException {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int consecutivo = comandaDAO.contarComandasDelDia(LocalDateTime.now()) + 1;
        return String.format("OB-%s-%03d", fecha, consecutivo);
    }

    private Cliente obtenerClienteGeneral() throws NegocioException {
        ClienteGeneral clienteG = clienteDAO.obtenerClienteGeneral();
        if (clienteG == null) {
            clienteG = new ClienteGeneral("Cliente General");
            clienteDAO.agregar(clienteG);
        }
        return clienteG;
    }
}
