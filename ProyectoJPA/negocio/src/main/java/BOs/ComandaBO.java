/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import DAOs.ClienteDAO;
import DAOs.ComandaDAO;
import adaptadores.ComandaAdapter;
import com.dtos.ComandaDTO;
import conexion.ConexionBD;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 * Implementación de la interfaz IComandaBO.
 *
 * @author adrian mendoza
 */
public class ComandaBO implements IComandaBO {

    private final IComandaDAO comandaDAO;
    private final IClienteDAO clienteDAO;
    private static final Logger LOG = Logger.getLogger(ComandaBO.class.getName());

    public ComandaBO() {
        this.comandaDAO = new ComandaDAO();
        this.clienteDAO = new ClienteDAO();
    }

    @Override
    public Comanda abrirComanda(ComandaDTO dto) throws NegocioException {
        if (dto == null) {
            throw new NegocioException("La comanda no puede ser nula");
        }
        if (dto.getIdMesa() == null) {
            throw new NegocioException("La comanda debe tener una mesa asignada");
        }

        try {
            if (comandaDAO.mesaTieneComandaAbierta(dto.getIdMesa())) {
                throw new NegocioException("La mesa ya tiene una comanda abierta");
            }

            EntityManager em = ConexionBD.crearConexion();
            Mesa mesa;
            Cliente cliente;
            try {
                mesa = em.find(Mesa.class, dto.getIdMesa());
                if (mesa == null) {
                    throw new NegocioException("Mesa no encontrada con id: " + dto.getIdMesa());
                }

                if (dto.getIdCliente() != null) {
                    cliente = em.find(Cliente.class, dto.getIdCliente());
                    if (cliente == null) {
                        throw new NegocioException("Cliente no encontrado con id: " + dto.getIdCliente());
                    }
                } else {
                    ClienteGeneral cg = clienteDAO.obtenerClienteGeneral();
                    if (cg == null) {
                        cg = new ClienteGeneral("Cliente General");
                        clienteDAO.agregar(cg);
                    }
                    cliente = em.find(Cliente.class, cg.getId());
                }
            } finally {
                em.close();
            }

            String folio = generarFolio();
            Comanda comanda = new Comanda(folio, LocalDateTime.now(), mesa, cliente);
            comanda.setEstado(EstadoComanda.ABIERTA);
            comanda.setTotal(0.0);
            comandaDAO.agregarComanda(comanda);
            LOG.info("Comanda abierta con folio: " + folio);
            return comanda;

        } catch (PersistenciaException ex) {
            LOG.warning("Error de persistencia al abrir comanda: " + ex.getMessage());
            throw new NegocioException("Error al abrir la comanda");
        }
    }

    @Override
    public void agregarDetalleProducto(Long idComanda, Long idProducto,
                                       Integer cantidad, String comentario) throws NegocioException {
        validarParametrosDetalle(idComanda, idProducto, cantidad);
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Comanda comanda = em.find(Comanda.class, idComanda);
            if (comanda == null) {
                throw new NegocioException("Comanda no encontrada");
            }
            if (comanda.getEstado() != EstadoComanda.ABIERTA) {
                throw new NegocioException("Solo se pueden modificar comandas ABIERTA");
            }

            Producto producto = em.find(Producto.class, idProducto);
            if (producto == null) {
                throw new NegocioException("Producto no encontrado");
            }
            if (Boolean.FALSE.equals(producto.getActivo())) {
                throw new NegocioException("El producto no está activo");
            }
            if (!tieneStockSuficiente(producto, cantidad)) {
                throw new NegocioException("El producto no tiene ingredientes suficientes");
            }

            DetalleComanda detalle = new DetalleComanda(cantidad, comentario, producto);
            comanda.agregarDetalle(detalle);
            em.persist(detalle);
            em.merge(comanda);
            em.getTransaction().commit();
            LOG.info("Detalle producto agregado a comanda " + idComanda);

        } catch (NegocioException ne) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ne;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            LOG.warning("Error al agregar detalle de producto: " + ex.getMessage());
            throw new NegocioException("Error al agregar detalle de producto");
        } finally {
            em.close();
        }
    }

    @Override
    public void agregarDetalleCombo(Long idComanda, Long idCombo,
                                    Integer cantidad, String comentario) throws NegocioException {
        validarParametrosDetalle(idComanda, idCombo, cantidad);
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Comanda comanda = em.find(Comanda.class, idComanda);
            if (comanda == null) {
                throw new NegocioException("Comanda no encontrada");
            }
            if (comanda.getEstado() != EstadoComanda.ABIERTA) {
                throw new NegocioException("Solo se pueden modificar comandas ABIERTA");
            }

            Combo combo = em.find(Combo.class, idCombo);
            if (combo == null) {
                throw new NegocioException("Combo no encontrado");
            }
            if (Boolean.FALSE.equals(combo.getActivo())) {
                throw new NegocioException("El combo no está activo");
            }
            if (!comboTieneStockSuficiente(combo, cantidad)) {
                throw new NegocioException("El combo no tiene ingredientes suficientes");
            }

            DetalleComanda detalle = new DetalleComanda(cantidad, comentario, combo);
            comanda.agregarDetalle(detalle);
            em.persist(detalle);
            em.merge(comanda);
            em.getTransaction().commit();
            LOG.info("Detalle combo agregado a comanda " + idComanda);

        } catch (NegocioException ne) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ne;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            LOG.warning("Error al agregar detalle de combo: " + ex.getMessage());
            throw new NegocioException("Error al agregar detalle de combo");
        } finally {
            em.close();
        }
    }

    @Override
    public void entregarComanda(Long idComanda) throws NegocioException {
        if (idComanda == null) {
            throw new NegocioException("Id de comanda inválido");
        }
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Comanda comanda = em.find(Comanda.class, idComanda);
            if (comanda == null) {
                throw new NegocioException("Comanda no encontrada");
            }
            if (comanda.getEstado() != EstadoComanda.ABIERTA) {
                throw new NegocioException("Solo se pueden entregar comandas ABIERTA");
            }
            if (comanda.getDetalles() == null || comanda.getDetalles().isEmpty()) {
                throw new NegocioException("No se puede entregar una comanda sin productos");
            }

            // Descontar stock de ingredientes
            for (DetalleComanda d : comanda.getDetalles()) {
                if (d.getProducto() != null) {
                    descontarStockProducto(d.getProducto(), d.getCantidad());
                } else if (d.getCombo() != null) {
                    for (ComboProducto cp : d.getCombo().getProductos()) {
                        int total = cp.getCantidad() * d.getCantidad();
                        descontarStockProducto(cp.getProducto(), total);
                    }
                }
            }

            comanda.setEstado(EstadoComanda.ENTREGADA);
            comanda.calcularTotal();

            Mesa mesa = comanda.getMesa();
            if (mesa != null) {
                mesa.setEstado(EstadoMesa.DISPONIBLE);
            }

            em.getTransaction().commit();
            LOG.info("Comanda " + idComanda + " entregada");

        } catch (NegocioException ne) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ne;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            LOG.warning("Error al entregar comanda: " + ex.getMessage());
            throw new NegocioException("Error al entregar la comanda");
        } finally {
            em.close();
        }
    }

    @Override
    public void cancelarComanda(Long idComanda) throws NegocioException {
        if (idComanda == null) {
            throw new NegocioException("Id de comanda inválido");
        }
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Comanda comanda = em.find(Comanda.class, idComanda);
            if (comanda == null) {
                throw new NegocioException("Comanda no encontrada");
            }
            if (comanda.getEstado() != EstadoComanda.ABIERTA) {
                throw new NegocioException("Solo se pueden cancelar comandas ABIERTA");
            }
            comanda.setEstado(EstadoComanda.CANCELADA);
            if (comanda.getMesa() != null) {
                comanda.getMesa().setEstado(EstadoMesa.DISPONIBLE);
            }
            em.getTransaction().commit();
            LOG.info("Comanda " + idComanda + " cancelada");

        } catch (NegocioException ne) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ne;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            LOG.warning("Error al cancelar comanda: " + ex.getMessage());
            throw new NegocioException("Error al cancelar la comanda");
        } finally {
            em.close();
        }
    }

    @Override
    public ComandaDTO buscarPorId(Long idComanda) throws NegocioException {
        if (idComanda == null) {
            throw new NegocioException("Id de comanda inválido");
        }
        try {
            Comanda comanda = comandaDAO.buscarPorId(idComanda);
            return ComandaAdapter.entidadADTO(comanda);
        } catch (PersistenciaException ex) {
            LOG.warning("Error al buscar comanda: " + ex.getMessage());
            throw new NegocioException("Error al buscar comanda por id");
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
            throw new NegocioException("Error al buscar comandas por rango");
        }
    }

    // -------------------- Métodos privados --------------------

    private String generarFolio() throws PersistenciaException {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int consecutivo = comandaDAO.contarComandasDelDia(LocalDateTime.now()) + 1;
        return String.format("OB-%s-%03d", fecha, consecutivo);
    }

    private void validarParametrosDetalle(Long idComanda, Long idOtro, Integer cantidad) {
        if (idComanda == null) {
            throw new NegocioException("Id de comanda inválido");
        }
        if (idOtro == null) {
            throw new NegocioException("Id de producto o combo inválido");
        }
        if (cantidad == null || cantidad < 1) {
            throw new NegocioException("La cantidad debe ser mayor a 0");
        }
    }

    private boolean tieneStockSuficiente(Producto producto, int cantidadPedida) {
        if (producto.getIngredientes() == null) {
            return true;
        }
        for (ProductoIngrediente pi : producto.getIngredientes()) {
            double requerido = pi.getCantidad() * cantidadPedida;
            Double stock = pi.getIngrediente().getStock();
            if (stock == null || stock < requerido) {
                return false;
            }
        }
        return true;
    }

    private boolean comboTieneStockSuficiente(Combo combo, int cantidadPedida) {
        if (combo.getProductos() == null) {
            return false;
        }
        for (ComboProducto cp : combo.getProductos()) {
            int totalPorProducto = cp.getCantidad() * cantidadPedida;
            if (!tieneStockSuficiente(cp.getProducto(), totalPorProducto)) {
                return false;
            }
        }
        return true;
    }

    private void descontarStockProducto(Producto producto, int cantidadPedida) {
        if (producto.getIngredientes() == null) {
            return;
        }
        for (ProductoIngrediente pi : producto.getIngredientes()) {
            double consumido = pi.getCantidad() * cantidadPedida;
            Double stockActual = pi.getIngrediente().getStock();
            if (stockActual == null) {
                stockActual = 0.0;
            }
            pi.getIngrediente().setStock(stockActual - consumido);
        }
    }
}
