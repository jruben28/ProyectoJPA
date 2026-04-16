/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import DAOs.ComboDAO;
import DAOs.ComboProductoDAO;
import adaptadores.ComboAdapter;
import com.dtos.ComboDTO;
import entidades.Combo;
import entidades.ComboProducto;
import entidades.ProductoIngrediente;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IComboBO;
import interfaces.IComboDAO;
import interfaces.IComboProductoDAO;
import java.util.List;
import java.util.logging.Logger;

/**
 * Capa de negocio para el módulo de Combos.
 * Valida reglas de negocio antes de comunicar a la capa de persistencia.
 *
 * @author Adrian Mendoza
 */
public class ComboBO implements IComboBO {

    private static final Logger LOG = Logger.getLogger(ComboBO.class.getName());

    private final IComboDAO comboDAO;
    private final IComboProductoDAO comboProductoDAO;

    public ComboBO() {
        this.comboDAO = new ComboDAO();
        this.comboProductoDAO = new ComboProductoDAO();
    }


    private void validarDTO(ComboDTO dto) throws NegocioException {
        if (dto == null) {
            throw new NegocioException("El DTO de Combo no puede ser nulo");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new NegocioException("El nombre del combo es obligatorio");
        }
        if (dto.getPrecioOriginal() == null || dto.getPrecioOriginal() < 0) {
            throw new NegocioException("El precio original no puede ser negativo ni nulo");
        }
        if (dto.getPrecioCombo() == null || dto.getPrecioCombo() < 0) {
            throw new NegocioException("El precio del combo no puede ser negativo ni nulo");
        }
        if (dto.getPorcentajeDescuento() == null
                || dto.getPorcentajeDescuento() < 0
                || dto.getPorcentajeDescuento() > 100) {
            throw new NegocioException("El porcentaje de descuento debe estar entre 0 y 100");
        }
    }

    /**
     * Valida que un ID sea no nulo y positivo.
     *
     * @param id    valor a verificar
     * @param campo nombre del campo para el mensaje de error
     * @throws NegocioException si el ID es inválido
     */
    private void validarId(Long id, String campo) throws NegocioException {
        if (id == null || id <= 0) {
            throw new NegocioException("El " + campo + " debe ser un valor positivo");
        }
    }

    @Override
    public Combo agregarCombo(ComboDTO dto) throws NegocioException {
        validarDTO(dto);
        try {
            Combo combo = ComboAdapter.dtoAEntidad(dto);
            return comboDAO.agregarCombo(combo);
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public Combo crearComboConProductos(ComboDTO dto, List<Long> idProductos, List<Integer> cantidades)
            throws NegocioException {

        validarDTO(dto);

        if (idProductos == null || cantidades == null) {
            throw new NegocioException("Las listas de productos y cantidades no pueden ser nulas");
        }
        if (idProductos.size() != cantidades.size()) {
            throw new NegocioException("Las listas de productos y cantidades deben tener el mismo tamaño");
        }

        int totalUnidades = cantidades.stream().mapToInt(Integer::intValue).sum();
        if (totalUnidades < 2) {
            throw new NegocioException(
                    "Un combo debe tener al menos 2 unidades de producto en total");
        }

        try {
            Combo combo = ComboAdapter.dtoAEntidad(dto);
            Combo guardado = comboDAO.agregarCombo(combo);

            for (int i = 0; i < idProductos.size(); i++) {
                comboProductoDAO.agregar(guardado.getId(), idProductos.get(i), cantidades.get(i));
            }

            LOG.info("Combo con productos creado. ID: " + guardado.getId());
            return guardado;

        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public Combo actualizarComboPorId(Long id, ComboDTO dto) throws NegocioException {
        if (id == null) {
            throw new NegocioException("El ID del combo no puede ser nulo");
        }
        validarDTO(dto);
        try {
            Combo existente = comboDAO.buscarComboPorId(id);
            existente.setNombre(dto.getNombre());
            existente.setDescripcion(dto.getDescripcion());
            existente.setPrecioOriginal(dto.getPrecioOriginal());
            existente.setPrecioCombo(dto.getPrecioCombo());
            existente.setPorcentajeDescuento(dto.getPorcentajeDescuento());
            if (dto.getActivo() != null) {
                existente.setActivo(dto.getActivo());
            }
            return comboDAO.actualizarCombo(existente);
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public ComboDTO cambiarEstado(Long id, Boolean activo) throws NegocioException {
        validarId(id, "ID del combo");
        if (activo == null) {
            throw new NegocioException("El estado no puede ser nulo");
        }
        try {
            return ComboAdapter.entidadADTO(comboDAO.cambiarEstado(id, activo));
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    
    @Override
    public List<ComboDTO> obtenerTodosCombos() throws NegocioException {
        try {
            return ComboAdapter.listaEntidadADTO(comboDAO.obtenerTodosCombos());
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public List<ComboDTO> buscarCombosPorNombre(String nombre) throws NegocioException {
        try {
            if (nombre == null || nombre.isBlank()) {
                return ComboAdapter.listaEntidadADTO(comboDAO.obtenerTodosCombos());
            }
            return ComboAdapter.listaEntidadADTO(comboDAO.buscarCombosPorNombre(nombre));
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public ComboDTO buscarComboPorId(Long id) throws NegocioException {
        validarId(id, "ID del combo");
        try {
            return ComboAdapter.entidadADTO(comboDAO.buscarComboPorId(id));
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public List<ComboDTO> buscarCombosPorProducto(Long idProducto) throws NegocioException {
        validarId(idProducto, "ID del producto");
        try {
            return ComboAdapter.listaEntidadADTO(comboDAO.buscarCombosPorProducto(idProducto));
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }


    @Override
    public boolean puedeVenderse(Long idCombo) throws NegocioException {
        try {
            Combo combo = comboDAO.buscarComboPorIdConDetalles(idCombo);

            if (!combo.getActivo()) {
                return false;
            }

            for (ComboProducto cp : combo.getProductos()) {
                if (!cp.getProducto().getActivo()) {
                    return false;
                }
                for (ProductoIngrediente pi : cp.getProducto().getIngredientes()) {
                    if (pi.getIngrediente().getStock() < pi.getCantidad()) {
                        return false;
                    }
                }
            }

            return true;

        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }
}