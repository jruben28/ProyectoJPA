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
        assertEquals(combo.getPorcentajeDescuento(),resultado.getPorcentajeDescuento());
        assertTrue(resultado.getActivo());
    }
    
   @Test
   public void testAgregarComboConDescripcionVacia()throws PersistenciaException{
       ComboDAO dao=new ComboDAO();
       Combo combo= new Combo("Combo descripcion vacia","",50.0,40.0,20);
       
       Combo resultado= dao.agregarCombo(combo);
       assertNotNull(resultado);
       assertNotNull(resultado.getId());
       assertEquals(combo.getNombre(),resultado.getNombre());
       assertEquals(combo.getDescripcion(),resultado.getDescripcion());
       assertEquals(combo.getPrecioOriginal(), resultado.getPrecioOriginal());
       assertEquals(combo.getPorcentajeDescuento(),resultado.getPorcentajeDescuento());
       assertTrue(resultado.getActivo());
       
   }
   
   @Test 
   public void testAgregarComboConDescripcionNula()throws PersistenciaException{
       ComboDAO dao=new ComboDAO();
       Combo combo= new Combo("Combo descripcion nula",null,50.0,40.0,20);
       
       Combo resultado = dao.agregarCombo(combo);
       assertNotNull(resultado);
       assertNotNull(resultado.getId());
       assertEquals(combo.getNombre(), resultado.getNombre());
       assertEquals(combo.getDescripcion(), resultado.getDescripcion());
       assertEquals(combo.getPrecioOriginal(), resultado.getPrecioOriginal());
       assertEquals(combo.getPorcentajeDescuento(), resultado.getPorcentajeDescuento());
       assertTrue(resultado.getActivo());
       
   }
  @Test 
  public void testAgregarComboConNombreNulo()throws PersistenciaException{
    ComboDAO dao=new ComboDAO();
    Combo combo= new Combo(null,"Combo con nombre nulo",50.0,40.0,20);
    
    assertThrows(PersistenciaException.class,()->dao.agregarCombo(combo));
}
  
 @Test 
 public void testAgregarComboConPreciosCero()throws PersistenciaException{
      ComboDAO dao=new ComboDAO();
      Combo combo= new Combo("Combo con precios 0",null,0.0,0.0,20);
      
      Combo resultado = dao.agregarCombo(combo);
      assertNotNull(resultado);
      assertEquals(combo.getPrecioOriginal(), resultado.getPrecioOriginal());
      assertEquals(combo.getPrecioCombo(),resultado.getPrecioCombo());
 } 

 @Test
 public void testActualizarComboExito() throws PersistenciaException {
    ComboDAO dao = new ComboDAO();
    Combo combo = new Combo("combo", "descripcion ", 100.0, 80.0, 20);
    Combo agregado = dao.agregarCombo(combo);
    agregado.setNombre("actualizado");
    agregado.setDescripcion("actualizada");
    agregado.setPrecioOriginal(150.0);
    agregado.setActivo(false);
    agregado.setPorcentajeDescuento(99);
    agregado.setPrecioCombo(100.0);
    Combo resultado=dao.actualizarCombo(agregado);
    
    assertNotNull(resultado);
    assertEquals(agregado.getNombre(), resultado.getNombre());
    assertEquals(agregado.getDescripcion(), resultado.getDescripcion());
    assertEquals(agregado.getPrecioOriginal(), resultado.getPrecioOriginal());
    assertEquals(agregado.getPrecioCombo(),resultado.getPrecioCombo());
    assertEquals(agregado.getActivo(),resultado.getActivo());
    assertEquals(agregado.getId(), resultado.getId());
    
    }
 
}
