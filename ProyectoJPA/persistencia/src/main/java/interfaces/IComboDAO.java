/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import excepciones.PersistenciaException;
import entidades.Combo;
/**
 * Interfaz para las operaciones de Combo
 * @author Adrian Mendoza
 */
public interface IComboDAO {
    
    public Combo agregarCombo(Combo combo) throws PersistenciaException;
}
