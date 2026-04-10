/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import com.dtos.ComboDTO;
import excepciones.NegocioException;

/**
 * Interfaz para las operaciones de negocio de Combo
 * @author Adrian Mendoza
 */
public interface IComboBO {
    public void agregarCombo(ComboDTO comboDTO) throws NegocioException;
}
