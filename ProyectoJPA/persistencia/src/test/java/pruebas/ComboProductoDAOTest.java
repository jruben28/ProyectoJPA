/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pruebas;

import DAOs.ComboDAO;
import DAOs.ComboProductoDAO;
import entidades.Combo;
import entidades.ComboProducto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Clase de prueba para ComboProductoDAO
 * @author keppler
 */
public class ComboProductoDAOTest {
    
    public ComboProductoDAOTest() {
    }
    
    @Test
    public void testAgregarComboProductoExito() throws PersistenciaException {
        ComboDAO comboDao = new ComboDAO();
        Combo combo = new Combo("Combo prueba ", "c", 100.0, 80.0, 20);
        Combo comboAgregado = comboDao.agregarCombo(combo); 
        ComboProductoDAO dao = new ComboProductoDAO();
        ComboProducto comboP = new ComboProducto(comboAgregado, 1L, 2); 
        ComboProducto resultado = dao.agregar(comboP);
        
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertNotNull(resultado.getCombo()); 
        assertEquals(comboAgregado.getId(), resultado.getCombo().getId());
        assertEquals(1L, resultado.getIdProducto());
        assertEquals(2, resultado.getCantidad());
    }
    
    @Test
    public void testObtenerPorComboExito()throws PersistenciaException{
         ComboDAO comboDao = new ComboDAO();
        Combo combo = new Combo("Combo prueba ", "c", 100.0, 80.0, 20);
        Combo comboAgregado = comboDao.agregarCombo(combo); 
        ComboProductoDAO dao = new ComboProductoDAO();
        ComboProducto cProducto = new ComboProducto(comboAgregado, 1L, 2);
        ComboProducto cProducto2 = new ComboProducto(comboAgregado, 2L, 1);
        dao.agregar(cProducto);
        dao.agregar(cProducto2);
        List<ComboProducto> resultado = dao.obtenerPorCombo(comboAgregado.getId());
        
        assertNotNull(resultado);
        assertEquals(2,resultado.size());
       
    }
    
    @Test
    public void testObtenerPorComboIdInexistente()throws PersistenciaException{
      ComboProductoDAO dao= new ComboProductoDAO();
      List<ComboProducto> resultado= dao.obtenerPorCombo(852L);
      
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
    
    @Test
    public void testAgregarComboProducto_comboNulo_lanzaExcepcion() {
        ComboProductoDAO dao = new ComboProductoDAO();
        ComboProducto cp = new ComboProducto(null, 1L, 2);

        assertThrows(PersistenciaException.class, () -> dao.agregar(cp));
    }
}