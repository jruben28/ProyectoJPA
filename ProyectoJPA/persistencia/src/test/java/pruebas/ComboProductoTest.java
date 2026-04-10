/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pruebas;

import DAOs.ComboProductoDAO;
import entidades.ComboProducto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para ComboProductoDAO
 * @author keppler
 */
public class ComboProductoTest {
    
    public ComboProductoTest() {
    }
    
    @Test
    public void testAgregarComboProductoExito(){
        ComboProductoDAO dao= new ComboProductoDAO();
        ComboProducto comboP= new ComboProducto(1L,1L,2);
        ComboProducto resultado = dao.agregar(comboP);
        
        
        
        
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(1L, resultado.getIdCombo());
        assertEquals(1L, resultado.getIdProducto());
        assertEquals(2, resultado.getCantidad());
        
    }
    
}
