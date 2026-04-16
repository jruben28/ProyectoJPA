/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.Comanda;
import excepciones.PersistenciaException;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author joser
 */
public interface IComandaDAO {
    
    public Comanda agregarComanda(Comanda comanda) throws PersistenciaException;
   
    Integer contarComandasDelDia(LocalDateTime dia) throws PersistenciaException;

    List<Comanda> buscarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta) throws PersistenciaException;
}
