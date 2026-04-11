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
}