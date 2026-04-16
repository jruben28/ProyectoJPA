/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import com.dtos.ComboDTO;
import entidades.Combo;
import excepciones.NegocioException;
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
     * @throws NegocioException si hay error de validación o persistencia
     */
    public Combo agregarCombo(ComboDTO comboDTO) throws NegocioException;

    /**
     * Crea un combo con sus productos asociados
     *
     * @param dto Datos del combo
     * @param idProductos Lista de IDs de productos
     * @param cantidades Lista de cantidades por producto
     * @return Combo creado
     * @throws NegocioException si hay error de validación o persistencia
     */
    public Combo crearComboConProductos(ComboDTO dto, List<Long> idProductos, List<Integer> cantidades) throws NegocioException;

    /**
     * Actualiza un combo existente por su ID
     *
     * @param id ID del combo a actualizar
     * @param comboDTO Nuevos datos del combo
     * @return Combo actualizado
     * @throws NegocioException si hay error de validación o persistencia
     */
    public Combo actualizarComboPorId(Long id, ComboDTO comboDTO) throws NegocioException;

    /**
     * Obtiene todos los combos registrados
     *
     * @return Lista de ComboDTO
     * @throws NegocioException si ocurre error
     */
    public List<ComboDTO> obtenerTodosCombos() throws NegocioException;

    /**
     * Busca combos por nombre. Si está vacío, devuelve todos.
     *
     * @param nombre Nombre a buscar
     * @return Lista de ComboDTO que coinciden
     * @throws NegocioException si ocurre error
     */
    public List<ComboDTO> buscarCombosPorNombre(String nombre) throws NegocioException;

    /**
     * Busca un combo por su ID
     *
     * @param id ID del combo a buscar
     * @return ComboDTO encontrado
     * @throws NegocioException si no se encuentra o hay error
     */
    public ComboDTO buscarComboPorId(Long id) throws NegocioException;

    /**
     * Busca combos que contengan un producto específico
     *
     * @param idProducto ID del producto
     * @return Lista de ComboDTO
     * @throws NegocioException si ocurre error
     */
    public List<ComboDTO> buscarCombosPorProducto(Long idProducto) throws NegocioException;

    /**
     * Cambia el estado activo/inactivo de un combo (baja o alta lógica).
     *
     * @param id ID del combo a modificar
     * @param activo nuevo estado (true = activo, false = inactivo)
     * @return ComboDTO actualizado
     * @throws NegocioException si el ID no es válido, el combo no existe o hay
     * error de persistencia
     */
    public ComboDTO cambiarEstado(Long id, Boolean activo) throws NegocioException;

    /**
     * Verifica si un combo puede ser vendido (todos sus productos tienen stock
     * suficiente).
     *
     * @param idCombo ID del combo a verificar
     * @return true si el combo está activo y todos sus ingredientes tienen
     * stock
     * @throws NegocioException si ocurre un error al consultar
     */
    public boolean puedeVenderse(Long idCombo) throws NegocioException;

}
