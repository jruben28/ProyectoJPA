/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.ComboProducto;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Interfaz para la relacion entre combo y producto
 * @author Adrian Mendoza
 */
public interface IComboProductoDAO {
    public ComboProducto agregar(ComboProducto comboProducto) throws PersistenciaException;
    
    public List<ComboProducto> obtenerPorCombo(Long idCombo) throws PersistenciaException;
}
