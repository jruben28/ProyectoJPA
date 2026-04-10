/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pruebas;

import BOs.ComboBO;
import com.dtos.ComboDTO;
import entidades.Combo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para ComboBO
 * @author Adrian Mendoza
 */
public class ComboBOTest {
    
    public ComboBOTest() {
    }
 @Test
 public void testAgregarComboExito(){
     ComboBO bo= new ComboBO();
     ComboDTO dto= new ComboDTO("Combo de prueba","Descripcccion",100.0,80.00,20);
     Combo resultado=bo.agregarCombo(dto);
     
     assertNotNull(resultado);
     assertNotNull(resultado.getId());
     assertEquals(dto.getNombre(),resultado.getNombre());
     assertEquals(dto.getDescripcion(),resultado.getDescripcion());
     assertEquals(dto.getPorcentajeDescuento(),resultado.getPorcentajeDescuento());
     assertEquals(dto.getPrecioOriginal(),resultado.getPrecioOriginal());
     assertEquals(dto.getPrecioCombo(),resultado.getPrecioCombo());
     assertTrue(resultado.getActivo());
 }
}
