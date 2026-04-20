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
import excepciones.PersistenciaException;
import interfaces.IComboBO;
import interfaces.IComboDAO;
import interfaces.IComboProductoDAO;
import java.util.logging.Level;
import java.util.List;
import java.util.logging.Logger;

/**
 * Capa de negocio para el módulo de Combos. Valida reglas de negocio antes de
 * comunicar a la capa de persistencia.
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

    public ComboBO(IComboDAO comboDAO, IComboProductoDAO comboProductoDAO) {
        this.comboDAO = comboDAO;
        this.comboProductoDAO = comboProductoDAO;
    }

    /**
     * Valida que un DTO de Combo contenga todos los datos obligatorios y en
     * rangos correctos.
     */
    private void validarDTO(ComboDTO dto) throws PersistenciaException {
        if (dto == null) {
            throw new PersistenciaException("El DTO de Combo no puede ser nulo");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new PersistenciaException("El nombre del combo es obligatorio");
        }
        if (dto.getPrecioOriginal() == null || dto.getPrecioOriginal() < 0) {
            throw new PersistenciaException("El precio original no puede ser negativo ni nulo");
        }
        if (dto.getPrecioCombo() == null || dto.getPrecioCombo() < 0) {
            throw new PersistenciaException("El precio del combo no puede ser negativo ni nulo");
        }
        if (dto.getPorcentajeDescuento() == null
                || dto.getPorcentajeDescuento() < 0
                || dto.getPorcentajeDescuento() > 100) {
            throw new PersistenciaException("El porcentaje de descuento debe estar entre 0 y 100");
        }
    }

    private void validarId(Long id, String campo) throws PersistenciaException {
        if (id == null || id <= 0) {
            throw new PersistenciaException("El " + campo + " debe ser un valor positivo");
        }
    }

    @Override
    public Combo agregarCombo(ComboDTO dto) throws PersistenciaException {
        validarDTO(dto);
        validarNombreDuplicado(dto.getNombre(), null);
        try {
            Combo combo = ComboAdapter.dtoAEntidad(dto);
            return comboDAO.agregarCombo(combo);
        } catch (PersistenciaException ex) {
            LOG.log(Level.WARNING, "Error al agregar combo", ex);
            throw new PersistenciaException("Error al agregar el combo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Combo crearComboConProductos(ComboDTO dto, List<Long> idProductos, List<Integer> cantidades)
            throws PersistenciaException {

        validarDTO(dto);

        if (idProductos == null || cantidades == null) {
            throw new PersistenciaException("Las listas de productos y cantidades no pueden ser nulas");
        }
        if (idProductos.size() != cantidades.size()) {
            throw new PersistenciaException("Las listas de productos y cantidades deben tener el mismo tamaño");
        }

        int totalUnidades = cantidades.stream().mapToInt(Integer::intValue).sum();
        if (totalUnidades < 2) {
            throw new PersistenciaException(
                    "Un combo debe tener al menos 2 unidades de producto en total");
        }

        validarNombreDuplicado(dto.getNombre(), null);
        validarComposicionDuplicada(idProductos, cantidades, null);

        try {
            Combo combo = ComboAdapter.dtoAEntidad(dto);
            Combo guardado = comboDAO.agregarCombo(combo);

            for (int i = 0; i < idProductos.size(); i++) {
                comboProductoDAO.agregar(guardado.getId(), idProductos.get(i), cantidades.get(i));
            }

            LOG.info("Combo con productos creado. ID: " + guardado.getId());
            return guardado;

        } catch (PersistenciaException ex) {
            LOG.log(Level.WARNING, "Error al crear combo con productos", ex);
            throw new PersistenciaException("Error al crear el combo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Combo actualizarComboPorId(Long id, ComboDTO dto) throws PersistenciaException {
        if (id == null) {
            throw new PersistenciaException("El ID del combo no puede ser nulo");
        }
        validarDTO(dto);
        validarNombreDuplicado(dto.getNombre(), id);
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
            LOG.log(Level.WARNING, "Error al actualizar combo", ex);
            throw new PersistenciaException("Error al actualizar el combo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ComboDTO cambiarEstado(Long id, Boolean activo) throws PersistenciaException {
        validarId(id, "ID del combo");
        if (activo == null) {
            throw new PersistenciaException("El estado no puede ser nulo");
        }
        try {
            return ComboAdapter.entidadADTO(comboDAO.cambiarEstado(id, activo));
        } catch (PersistenciaException ex) {
            LOG.log(Level.WARNING, "Error al cambiar estado del combo", ex);
            throw new PersistenciaException("Error al cambiar estado: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<ComboDTO> obtenerTodosCombos() throws PersistenciaException {
        try {
            return ComboAdapter.listaEntidadADTO(comboDAO.obtenerTodosCombos());
        } catch (PersistenciaException ex) {
            LOG.log(Level.WARNING, "Error al obtener todos los combos", ex);
            throw new PersistenciaException("Error al obtener combos: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<ComboDTO> buscarCombosPorNombre(String nombre) throws PersistenciaException {
        try {
            if (nombre == null || nombre.isBlank()) {
                return ComboAdapter.listaEntidadADTO(comboDAO.obtenerTodosCombos());
            }
            return ComboAdapter.listaEntidadADTO(comboDAO.buscarCombosPorNombre(nombre));
        } catch (PersistenciaException ex) {
            LOG.log(Level.WARNING, "Error al buscar combos por nombre", ex);
            throw new PersistenciaException("Error en la búsqueda: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ComboDTO buscarComboPorId(Long id) throws PersistenciaException {
        validarId(id, "ID del combo");
        try {
            return ComboAdapter.entidadADTO(comboDAO.buscarComboPorId(id));
        } catch (PersistenciaException ex) {
            LOG.log(Level.WARNING, "Error al buscar combo por ID", ex);
            throw new PersistenciaException("Error al buscar el combo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<ComboDTO> buscarCombosPorProducto(Long idProducto) throws PersistenciaException {
        validarId(idProducto, "ID del producto");
        try {
            return ComboAdapter.listaEntidadADTO(comboDAO.buscarCombosPorProducto(idProducto));
        } catch (PersistenciaException ex) {
            LOG.log(Level.WARNING, "Error al buscar combos por producto", ex);
            throw new PersistenciaException("Error en la búsqueda: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean puedeVenderse(Long idCombo) throws PersistenciaException {
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
            LOG.log(Level.WARNING, "Error al verificar disponibilidad del combo", ex);
            throw new PersistenciaException("Error al verificar disponibilidad: " + ex.getMessage(), ex);
        }
    }

    /**
     * Valida que no exista otro combo con el mismo nombre en el sistema.
     *
     * @param nombreCandidato el nombre que se quiere usar
     * @param idOmitir el ID del combo que se esta editando (null si es
     * nuevo)
     * @throws PersistenciaException si ya existe un combo con ese nombre
     */
    private void validarNombreDuplicado(String nombreCandidato, Long idOmitir)
            throws PersistenciaException {
        if (nombreCandidato == null) {
            return;
        }

        String nombreLimpio = nombreCandidato.trim();
        List<Combo> listaDeCombosEncontrados = comboDAO.buscarCombosPorNombre(nombreLimpio);

        for (Combo comboExistente : listaDeCombosEncontrados) {
            if (idOmitir != null && idOmitir.equals(comboExistente.getId())) {
                continue;
            }

            String nombreEnBaseDatos = comboExistente.getNombre().trim();
            if (nombreEnBaseDatos.equalsIgnoreCase(nombreLimpio)) {
                throw new PersistenciaException(
                        "Ya existe un combo registrado con el nombre: " + nombreLimpio);
            }
        }
    }

    /**
     * Valida que no exista otro combo que tenga exactamente los mismos
     * productos y cantidades.
     *
     * @param listaIdsNuevos lista de IDs de productos
     * @param listaCantidadesNuevas lista de cantidades correspondientes
     * @param idParaOmitir el ID del combo que se esta editando (null si es
     * nuevo)
     * @throws PersistenciaException si ya existe un combo con esa composicion
     */
    private void validarComposicionDuplicada(List<Long> listaIdsNuevos,
            List<Integer> listaCantidadesNuevas,
            Long idParaOmitir) throws PersistenciaException {

        List<Combo> todosLosCombosDelSistema = comboDAO.obtenerTodosCombosConProductos();

        for (Combo comboBD : todosLosCombosDelSistema) {
            if (idParaOmitir != null && idParaOmitir.equals(comboBD.getId())) {
                continue;
            }

            List<ComboProducto> productosEnComboBD = comboBD.getProductos();

            if (listaIdsNuevos.size() != productosEnComboBD.size()) {
                continue;
            }

            boolean laComposicionEsIdentica = true;

            for (int indice = 0; indice < listaIdsNuevos.size(); indice++) {
                Long idProductoPropuesto = listaIdsNuevos.get(indice);
                Integer cantidadPropuesta = listaCantidadesNuevas.get(indice);

                boolean seEncontroElProducto = false;

                for (ComboProducto detalleBD : productosEnComboBD) {
                    Long idProductoBD = detalleBD.getProducto().getId();
                    Integer cantidadBD = detalleBD.getCantidad();

                    if (idProductoBD.equals(idProductoPropuesto)
                            && cantidadBD.equals(cantidadPropuesta)) {
                        seEncontroElProducto = true;
                        break;
                    }
                }
                if (!seEncontroElProducto) {
                    laComposicionEsIdentica = false;
                    break;
                }
            }
            if (laComposicionEsIdentica) {
                throw new PersistenciaException("Ya existe un combo con la misma combinacion de productos y cantidades: '"
                        + comboBD.getNombre() + "'");
            }
        }
    }
}
