/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOs;

import Entidades.Comanda;
import excepciones.PersistenciaException;

/**
 *
 * @author joser
 */
public interface IComandaDAO {
    
    public Comanda agregarComanda(Comanda comanda) throws PersistenciaException;
}
