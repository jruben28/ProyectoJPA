/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import excepciones.PersistenciaException;
import entidades.Combo;
import java.util.List;

/**
 * Interfaz para las operaciones de persistencia de Combo.
 *
 * @author Adrian Mendoza
 */
public interface IComboDAO {

    /**
     * Agrega un combo a la base de datos.
     *
     * @param combo Combo a agregar
     * @return Combo agregado con su ID generado
     * @throws PersistenciaException si ocurre error en la base de datos
     */
    Combo agregarCombo(Combo combo) throws PersistenciaException;

    /**
     * Actualiza un combo existente en la base de datos.
     *
     * @param combo Combo con los datos actualizados
     * @return Combo actualizado
     * @throws PersistenciaException si ocurre error en la base de datos
     */
    Combo actualizarCombo(Combo combo) throws PersistenciaException;

    /**
     * Obtiene todos los combos registrados.
     *
     * @return Lista de todos los combos
     * @throws PersistenciaException si ocurre error en la base de datos
     */
    List<Combo> obtenerTodosCombos() throws PersistenciaException;
    
     /**
     * Obtiene todos los combos con su lista de productos.
     * Se usa para validar duplicados por composición.
     *
     * @return Lista de combos con productos inicializados
     * @throws PersistenciaException si ocurre error en la base de datos
     */
    List<Combo> obtenerTodosCombosConProductos() throws PersistenciaException;

    /**
     * Busca un combo por su id, solo datos básicos.
     *
     * @param id ID del combo a buscar
     * @return Combo encontrado
     * @throws PersistenciaException si no se encuentra o hay error
     */
    Combo buscarComboPorId(Long id) throws PersistenciaException;

    /**
     * Busca un combo por su id con datos para relaciones.
     * @param id ID del combo
     * @return Combo con productos e ingredientes cargados
     * @throws PersistenciaException si no se encuentra o hay error
     */
    Combo buscarComboPorIdConDetalles(Long id) throws PersistenciaException;

    /**
     * Busca combos cuyo nombre contenga el texto dado.
     *
     * @param nombre Texto a buscar en el nombre
     * @return Lista de combos que coinciden
     * @throws PersistenciaException si ocurre error en la base de datos
     */
    List<Combo> buscarCombosPorNombre(String nombre) throws PersistenciaException;

    /**
     * Busca combos que contengan un producto específico.
     *
     * @param idProducto ID del producto a buscar
     * @return Lista de combos que contienen ese producto
     * @throws PersistenciaException si ocurre error
     */
    List<Combo> buscarCombosPorProducto(Long idProducto) throws PersistenciaException;

    /**
     * Cambia el estado activo de un combo (baja o alta lógica).
     *
     * @param id     ID del combo
     * @param activo nuevo estado 
     * @return Combo actualizado
     * @throws PersistenciaException si el combo no existe o hay error
     */
    Combo cambiarEstado(Long id, Boolean activo) throws PersistenciaException;
}