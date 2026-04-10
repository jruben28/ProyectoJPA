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
 * @author Adrian Mendoza
 */
public interface IComboBO {
    public Combo agregarCombo(ComboDTO comboDTO) throws NegocioException;
    
    public Combo crearComboConProductos(ComboDTO dto, List<Long>idProductos,List<Integer> cantidades)throws NegocioException;
    
    public Combo actualizarComboPorId(Long id,ComboDTO comboDTO) throws NegocioException;
}
