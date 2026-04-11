/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pruebas;

import BOs.ComboBO;
import com.dtos.ComboDTO;
import entidades.Combo;
import excepciones.NegocioException;
import java.util.Arrays;
import java.util.List;
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
 public void testAgregarComboExito()throws NegocioException{
     ComboBO bo= new ComboBO();
     ComboDTO dto= new ComboDTO("Combo de prueba","Descripcion",100.0,80.00,20);
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
 
 @Test
 public void testAgregarComboDtoNulo()throws NegocioException{
     ComboBO bo=new ComboBO();
     
     assertThrows(NegocioException.class,()->bo.agregarCombo(null));
 }
 
 @Test
 public void testAgregarComboNombreVacio()throws NegocioException{
     ComboBO bo=new ComboBO();
     ComboDTO dto= new ComboDTO("","Descripcion",100.0,80.0,20);
     
     assertThrows(NegocioException.class,()->bo.agregarCombo(dto));
     
}
 
 @Test
 public void testAgregarComboNombreNulo()throws NegocioException{
     ComboBO bo=new ComboBO();
     ComboDTO dto= new ComboDTO(null,"Descripcion",100.0,80.0,20);
     
     assertThrows(NegocioException.class,()->bo.agregarCombo(dto));
 }
 
 @Test
 public void testAgregarComboPrecioComboNegativo()throws NegocioException{
     ComboBO bo=new ComboBO();
     ComboDTO dto= new ComboDTO("d","Descripcion",100.0,-80.0,20);
     
     assertThrows(NegocioException.class,()->bo.agregarCombo(dto));
 }
 
 @Test
 public void testAgregarComboPrecioOriginalNegativo()throws NegocioException{
     ComboBO bo=new ComboBO();
     ComboDTO dto= new ComboDTO("d","Descripcion",-100.0,80.0,20);
     
     assertThrows(NegocioException.class,()->bo.agregarCombo(dto));
 }

 @Test 
 public void testAgregarComboDescuentoFueraLimites()throws NegocioException{
     ComboBO bo=new ComboBO();
     ComboDTO dto= new ComboDTO("d","Descripcion",100.0,80.0,101);
     ComboDTO dto2= new ComboDTO("d","Descripcion",100.0,80.0,-1);
     
     assertThrows(NegocioException.class,()->bo.agregarCombo(dto));
     assertThrows(NegocioException.class,()->bo.agregarCombo(dto2));
 }
 
    @Test
    public void testAgregarComboConActivoNulo() throws NegocioException {
        ComboBO bo = new ComboBO();
        ComboDTO dto = new ComboDTO("d", "Descripcion", 100.0, 80.0, 20);
        dto.setActivo(null);
        Combo resultado = bo.agregarCombo(dto);

        assertNotNull(resultado);
        assertTrue(resultado.getActivo());
    }
    
 @Test
 public void testCrearComboConProductosExito()throws NegocioException{
     ComboBO bo=new ComboBO();
     ComboDTO dto= new ComboDTO("El mejor combo del mundo","Descripcion chila",100.0,80.0,20);
     List<Long> idProductos = Arrays.asList(1L, 2L);
     List<Integer> cantidades = Arrays.asList(1, 2);
     Combo resultado = bo.crearComboConProductos(dto, idProductos, cantidades);
    
    assertNotNull(resultado);
    assertNotNull(resultado.getId());
 }
 
  @Test
  public void testCrearComboConMenosDosProductos()throws NegocioException{
    ComboBO bo = new ComboBO();
    ComboDTO dto = new ComboDTO("combo", "descripcion", 100.0, 80.0, 20);
    List<Long> idProductos = Arrays.asList(1L);  
    List<Integer> cantidades = Arrays.asList(1);
    
    
      assertThrows(NegocioException.class, 
       ()->bo.crearComboConProductos(dto, idProductos, cantidades));
    
  }
  
 @Test
 public void testActualizarComboExito() throws NegocioException {
   ComboBO bo = new ComboBO();
   ComboDTO dtoOriginal = new ComboDTO("Viejo", "Viejon", 100.0, 80.0, 20);
   Combo agregado = bo.agregarCombo(dtoOriginal);
   ComboDTO dtoActualizar = new ComboDTO("Nuevo", "Nuevon", 111.11, 88.88, 21);
   Combo resultado = bo.actualizarComboPorId(agregado.getId(), dtoActualizar);
    
   assertNotNull(resultado);
   assertNotNull(resultado.getId());
   assertEquals(agregado.getId(), resultado.getId());
   assertEquals(dtoActualizar.getNombre(), resultado.getNombre());
   assertEquals(dtoActualizar.getDescripcion(), resultado.getDescripcion());
   assertEquals(dtoActualizar.getPrecioOriginal(), resultado.getPrecioOriginal());
   assertEquals(dtoActualizar.getPrecioCombo(), resultado.getPrecioCombo());
   assertEquals(dtoActualizar.getPorcentajeDescuento(), resultado.getPorcentajeDescuento());
 }
 
  @Test
  public void testActualizarComboIdNulo()throws NegocioException{
    ComboBO bo = new ComboBO();
    ComboDTO dto = new ComboDTO("Combo", "Desc", 100.0, 80.0, 20);
    
    assertThrows(NegocioException.class, () -> bo.actualizarComboPorId(null, dto));
  }
  
  @Test
  public void testActualizarComboDtoNulo() throws NegocioException {
    ComboBO bo = new ComboBO();
    
    assertThrows(NegocioException.class, () -> bo.actualizarComboPorId(1L, null));
    }
  
  @Test
  public void testActualizarComboNombreVacio() throws NegocioException {
    ComboBO bo = new ComboBO();
    ComboDTO dtoOriginal = new ComboDTO("combo", "descripcion", 100.0, 80.0, 20);
    Combo agregado = bo.agregarCombo(dtoOriginal);
    ComboDTO dtoConNombreVacio = new ComboDTO("", "Nuevo", 100.0, 80.0, 20);
  
    assertThrows(NegocioException.class, () -> bo.actualizarComboPorId(agregado.getId(), dtoConNombreVacio));
}
  @Test
  public void testActualizarComboPrecioOriginalNegativo() throws NegocioException {
   ComboBO bo = new ComboBO();
   ComboDTO dtoOriginal = new ComboDTO("Original", "Desc", 100.0, 80.0, 20);
   Combo agregado = bo.agregarCombo(dtoOriginal);
   ComboDTO dtoConPrecioNegativo = new ComboDTO("Nuevo", "Desc", 100.0, -80.0, 20);

    assertThrows(NegocioException.class, () -> bo.actualizarComboPorId(agregado.getId(), dtoConPrecioNegativo));
}
   @Test
  public void testActualizarComboPrecioComboNegativo() throws NegocioException {
   ComboBO bo = new ComboBO();
   ComboDTO dtoOriginal = new ComboDTO("Original", "Desc", 100.0, 80.0, 20);
   Combo agregado = bo.agregarCombo(dtoOriginal);
   ComboDTO dtoConPrecioNegativo = new ComboDTO("Nuevo", "Desc", -100.0, 80.0, 20);

    assertThrows(NegocioException.class, () -> bo.actualizarComboPorId(agregado.getId(), dtoConPrecioNegativo));
}
  @Test
  public void testActualizarComboDescuentoFueraLimites() throws NegocioException {
    ComboBO bo = new ComboBO();
    ComboDTO dtoOriginal = new ComboDTO("Original", "Desc", 100.0, 80.0, 20);
    Combo agregado = bo.agregarCombo(dtoOriginal);
    ComboDTO dtoConDescuentoAlto = new ComboDTO("Nuevo", "Desc", 100.0, 80.0, 101);
    ComboDTO dtoConDescuentoNulo = new ComboDTO("Nuevo", "Desc", 100.0, 80.0, null);
    ComboDTO dtoConDescuentoNegativo = new ComboDTO("Nuevo", "Desc", 100.0, 80.0, -1);
  
    assertThrows(NegocioException.class, () -> bo.actualizarComboPorId(agregado.getId(), dtoConDescuentoAlto));
    assertThrows(NegocioException.class, () -> bo.actualizarComboPorId(agregado.getId(), dtoConDescuentoNulo));
    assertThrows(NegocioException.class, () -> bo.actualizarComboPorId(agregado.getId(), dtoConDescuentoNegativo));
}
}
