/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import com.dtos.ComboDTO;
import entidades.Combo;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Interfaz para las operaciones de negocio de Combo
 *
 * @author Adrian Mendoza
 */
public interface IComboBO {

    /**
     * Agrega un combo validando sus datos
     *
     * @param comboDTO Datos del combo a agregar
     * @return Combo agregado
     * @throws PersistenciaException si hay error de validación o persistencia
     */
    public Combo agregarCombo(ComboDTO comboDTO) throws PersistenciaException;

    /**
     * Crea un combo con sus productos asociados
     *
     * @param dto Datos del combo
     * @param idProductos Lista de IDs de productos
     * @param cantidades Lista de cantidades por producto
     * @return Combo creado
     * @throws PersistenciaException si hay error de validación o persistencia
     */
    public Combo crearComboConProductos(ComboDTO dto, List<Long> idProductos, List<Integer> cantidades) throws PersistenciaException;

    /**
     * Actualiza un combo existente por su ID
     *
     * @param id ID del combo a actualizar
     * @param comboDTO Nuevos datos del combo
     * @return Combo actualizado
     * @throws PersistenciaException si hay error de validación o persistencia
     */
    public Combo actualizarComboPorId(Long id, ComboDTO comboDTO) throws PersistenciaException;

    /**
     * Obtiene todos los combos registrados
     *
     * @return Lista de ComboDTO
     * @throws PersistenciaException si ocurre error
     */
    public List<ComboDTO> obtenerTodosCombos() throws PersistenciaException;

    /**
     * Busca combos por nombre. Si está vacío, devuelve todos.
     *
     * @param nombre Nombre a buscar
     * @return Lista de ComboDTO que coinciden
     * @throws PersistenciaException si ocurre error
     */
    public List<ComboDTO> buscarCombosPorNombre(String nombre) throws PersistenciaException;

    /**
     * Busca un combo por su ID
     *
     * @param id ID del combo a buscar
     * @return ComboDTO encontrado
     * @throws PersistenciaException si no se encuentra o hay error
     */
    public ComboDTO buscarComboPorId(Long id) throws PersistenciaException;

    /**
     * Busca combos que contengan un producto específico
     *
     * @param idProducto ID del producto
     * @return Lista de ComboDTO
     * @throws PersistenciaException si ocurre error
     */
    public List<ComboDTO> buscarCombosPorProducto(Long idProducto) throws PersistenciaException;

    /**
     * Cambia el estado activo/inactivo de un combo (baja o alta lógica).
     *
     * @param id ID del combo a modificar
     * @param activo nuevo estado (true = activo, false = inactivo)
     * @return ComboDTO actualizado
     * @throws PersistenciaException si el ID no es válido, el combo no existe o hay
     * error de persistencia
     */
    public ComboDTO cambiarEstado(Long id, Boolean activo) throws PersistenciaException;

    /**
     * Verifica si un combo puede ser vendido (todos sus productos tienen stock
     * suficiente).
     *
     * @param idCombo ID del combo a verificar
     * @return true si el combo está activo y todos sus ingredientes tienen
     * stock
     * @throws PersistenciaException si ocurre un error al consultar
     */
    public boolean puedeVenderse(Long idCombo) throws PersistenciaException;

}
