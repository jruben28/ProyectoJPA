/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pruebas;

import DAOs.ComboDAO;
import excepciones.PersistenciaException;
import entidades.Combo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de ComboDAO 
 * @author Adrian Mendoza
 */
public class ComboDAOTest {
    
    public ComboDAOTest() {
    }
 
    @Test
    public void testAgregarComboExito()throws PersistenciaException{
        ComboDAO dao= new ComboDAO();
        Combo combo= new Combo("Combo de prueba","Descripcion",99.0,79.0,19);
        
        Combo resultado= dao.agregarCombo(combo);
        
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(combo.getId(), resultado.getId());
        assertEquals(combo.getNombre(),resultado.getNombre());
        assertEquals(combo.getDescripcion(),resultado.getDescripcion());
        assertEquals(combo.getPrecioOriginal(), resultado.getPrecioOriginal());
        
    }
    

}
