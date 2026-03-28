/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package BOs;

import com.dtos.ComandaDTO;
import excepciones.NegocioException;

/**
 *
 * @author joser
 */
public interface IComandaBO {
    public ComandaDTO agregarComanda() throws NegocioException;
    
}
