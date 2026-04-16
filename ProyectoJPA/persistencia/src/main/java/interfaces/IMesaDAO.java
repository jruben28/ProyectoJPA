/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.Mesa;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Interfaz del DAO para Mesa.
 * @author Adrian Mendoza
 */
public interface IMesaDAO {

   public Mesa agregar(Mesa mesa) throws PersistenciaException;

   public List<Mesa> obtenerTodas() throws PersistenciaException;

   public List<Mesa> obtenerDisponibles() throws PersistenciaException;

   public Mesa buscarPorNumero(Integer numero) throws PersistenciaException;
   
   public  void cargaMasiva(int cantidad) throws PersistenciaException;


  
}
