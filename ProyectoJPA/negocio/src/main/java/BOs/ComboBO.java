/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import DAOs.ComboDAO;
import com.dtos.ComboDTO;
import excepciones.NegocioException;
import interfaces.IComboBO;
import interfaces.IComboDAO;
import java.util.logging.Logger;

/**
 * Implementacion de la interfaz IComboBO
 * @author Adrian Mendoza
 */
public class ComboBO implements IComboBO{
  private IComboDAO comboDAO;
  private static final Logger LOG= Logger.getLogger(ComboBO.class.getName());
  
  public ComboBO(){
      this.comboDAO=new ComboDAO();
  }

    @Override
    public void agregarCombo(ComboDTO comboDTO) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
  
}
