/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import com.dtos.ComandaDTO;
import excepciones.NegocioException;

/**
 *
 * @author joser
 */
public interface IComandaBO {
    public void agregarComanda(ComandaDTO comandaDTO) throws NegocioException;
    
}
