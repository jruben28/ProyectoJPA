/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pruebas;

import BOs.ComboBO;
import com.dtos.ComboDTO;
import conexion.ConexionBD;
import entidades.Combo;
import entidades.Ingrediente;
import entidades.Producto;
import entidades.ProductoIngrediente;
import enums.TipoProducto;
import enums.UnidadDeMedida;
import excepciones.NegocioException;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para ComboBO
 *
 * @author Adrian Mendoza
 */
public class ComboBOTest {

    public ComboBOTest() {
    }

    @Test
    public void testAgregarComboExito() throws NegocioException {
        ComboBO bo = new ComboBO();
        ComboDTO dto = new ComboDTO("Combo de prueba", "Descripcion", 100.0, 80.00, 20);
        Combo resultado = bo.agregarCombo(dto);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(dto.getNombre(), resultado.getNombre());
        assertEquals(dto.getDescripcion(), resultado.getDescripcion());
        assertEquals(dto.getPorcentajeDescuento(), resultado.getPorcentajeDescuento());
        assertEquals(dto.getPrecioOriginal(), resultado.getPrecioOriginal());
        assertEquals(dto.getPrecioCombo(), resultado.getPrecioCombo());
        assertTrue(resultado.getActivo());
    }

    @Test
    public void testAgregarComboDtoNulo() throws NegocioException {
        ComboBO bo = new ComboBO();

        assertThrows(NegocioException.class, () -> bo.agregarCombo(null));
    }

    @Test
    public void testAgregarComboNombreVacio() throws NegocioException {
        ComboBO bo = new ComboBO();
        ComboDTO dto = new ComboDTO("", "Descripcion", 100.0, 80.0, 20);

        assertThrows(NegocioException.class, () -> bo.agregarCombo(dto));

    }

    @Test
    public void testAgregarComboNombreNulo() throws NegocioException {
        ComboBO bo = new ComboBO();
        ComboDTO dto = new ComboDTO(null, "Descripcion", 100.0, 80.0, 20);

        assertThrows(NegocioException.class, () -> bo.agregarCombo(dto));
    }

    @Test
    public void testAgregarComboPrecioComboNegativo() throws NegocioException {
        ComboBO bo = new ComboBO();
        ComboDTO dto = new ComboDTO("d", "Descripcion", 100.0, -80.0, 20);

        assertThrows(NegocioException.class, () -> bo.agregarCombo(dto));
    }

    @Test
    public void testAgregarComboPrecioOriginalNegativo() throws NegocioException {
        ComboBO bo = new ComboBO();
        ComboDTO dto = new ComboDTO("d", "Descripcion", -100.0, 80.0, 20);

        assertThrows(NegocioException.class, () -> bo.agregarCombo(dto));
    }

    @Test
    public void testAgregarComboDescuentoFueraLimites() throws NegocioException {
        ComboBO bo = new ComboBO();
        ComboDTO dto = new ComboDTO("d", "Descripcion", 100.0, 80.0, 101);
        ComboDTO dto2 = new ComboDTO("d", "Descripcion", 100.0, 80.0, -1);

        assertThrows(NegocioException.class, () -> bo.agregarCombo(dto));
        assertThrows(NegocioException.class, () -> bo.agregarCombo(dto2));
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
    public void testCrearComboConProductosExito() throws NegocioException {
        ComboBO bo = new ComboBO();

        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        Producto p1 = new Producto("Prod1-" + System.nanoTime(), "desc", 50.0, TipoProducto.PLATILLO);
        Producto p2 = new Producto("Prod2-" + System.nanoTime(), "desc", 60.0, TipoProducto.PLATILLO);
        em.persist(p1);
        em.persist(p2);
        em.getTransaction().commit();
        em.close();

        ComboDTO dto = new ComboDTO("El mejor combo del mundo " + System.nanoTime(),
                "Descripcion chila", 100.0, 80.0, 20);
        List<Long> idProductos = Arrays.asList(p1.getId(), p2.getId());
        List<Integer> cantidades = Arrays.asList(1, 2);

        Combo resultado = bo.crearComboConProductos(dto, idProductos, cantidades);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
    }

    @Test
    public void testCrearComboConMenosDosProductos() throws NegocioException {
        ComboBO bo = new ComboBO();
        ComboDTO dto = new ComboDTO("combo", "descripcion", 100.0, 80.0, 20);
        List<Long> idProductos = Arrays.asList(1L);
        List<Integer> cantidades = Arrays.asList(1);

        assertThrows(NegocioException.class,
                () -> bo.crearComboConProductos(dto, idProductos, cantidades));

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
    public void testActualizarComboIdNulo() throws NegocioException {
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

    @Test
    public void testObtenerTodosCombos() throws NegocioException {
        ComboBO bo = new ComboBO();
        List<ComboDTO> resultado = bo.obtenerTodosCombos();

        assertNotNull(resultado);
    }

    @Test
    public void testBuscarCombosPorNombreConResultados() throws NegocioException {
        ComboBO bo = new ComboBO();
        ComboDTO dto = new ComboDTO("ComboTest123", "Desc", 100.0, 80.0, 20);
        bo.agregarCombo(dto);

        List<ComboDTO> resultado = bo.buscarCombosPorNombre("Test");
        assertNotNull(resultado);
    }

    @Test
    public void testBuscarCombosPorNombreVacio() throws NegocioException {
        ComboBO bo = new ComboBO();
        List<ComboDTO> resultado = bo.buscarCombosPorNombre("");

        assertNotNull(resultado);
    }

    @Test
    public void testBuscarCombosPorNombreNull() throws NegocioException {
        ComboBO bo = new ComboBO();
        List<ComboDTO> resultado = bo.buscarCombosPorNombre(null);

        assertNotNull(resultado);
    }

    @Test
    public void testBuscarComboPorIdExito() throws NegocioException {
        ComboBO bo = new ComboBO();
        ComboDTO dto = new ComboDTO("ComboParaBuscar", "Desc", 100.0, 80.0, 20);
        Combo agregado = bo.agregarCombo(dto);
        ComboDTO resultado = bo.buscarComboPorId(agregado.getId());

        assertNotNull(resultado);
        assertEquals(agregado.getId(), resultado.getId());
        assertEquals("ComboParaBuscar", resultado.getNombre());
    }

    @Test
    public void testBuscarComboPorIdNulo() {
        ComboBO bo = new ComboBO();

        assertThrows(NegocioException.class, () -> bo.buscarComboPorId(null));
    }

    @Test
    public void testBuscarComboPorIdInvalido() {
        ComboBO bo = new ComboBO();

        assertThrows(NegocioException.class, () -> bo.buscarComboPorId(-1L));
    }

    @Test
    public void testBuscarCombosPorProductoExito() throws NegocioException {
        ComboBO bo = new ComboBO();
        List<ComboDTO> resultado = bo.buscarCombosPorProducto(9999L);

        assertNotNull(resultado);
    }

    @Test
    public void testBuscarCombosPorProductoIdNulo() {
        ComboBO bo = new ComboBO();

        assertThrows(NegocioException.class, () -> bo.buscarCombosPorProducto(null));
    }

    @Test
    public void testBuscarCombosPorProductoIdInvalido() {
        ComboBO bo = new ComboBO();

        assertThrows(NegocioException.class, () -> bo.buscarCombosPorProducto(-1L));
    }

    @Test
    public void testCambiarEstadoExito() throws NegocioException {
        ComboBO bo = new ComboBO();
        ComboDTO dto = new ComboDTO("Combo Estado", "Desc", 100.0, 80.0, 20);
        Combo agregado = bo.agregarCombo(dto);

        assertTrue(agregado.getActivo());

        ComboDTO resultado = bo.cambiarEstado(agregado.getId(), false);
        assertNotNull(resultado);
        assertFalse(resultado.getActivo());

        resultado = bo.cambiarEstado(agregado.getId(), true);
        assertTrue(resultado.getActivo());
    }

    @Test
    public void testCambiarEstadoIdNulo() {
        ComboBO bo = new ComboBO();
        assertThrows(NegocioException.class, () -> bo.cambiarEstado(null, true));
    }

    @Test
    public void testCambiarEstadoIdInvalido() {
        ComboBO bo = new ComboBO();
        assertThrows(NegocioException.class, () -> bo.cambiarEstado(-5L, false));
    }

    @Test
    public void testCambiarEstadoEstadoNulo() {
        ComboBO bo = new ComboBO();
        ComboDTO dto = new ComboDTO("Combo Estado2", "Desc", 100.0, 80.0, 20);
        Combo agregado = bo.agregarCombo(dto);
        assertThrows(NegocioException.class, () -> bo.cambiarEstado(agregado.getId(), null));
    }

    @Test
    public void testCambiarEstadoComboInexistente() {
        ComboBO bo = new ComboBO();

        assertThrows(NegocioException.class, () -> bo.cambiarEstado(99999L, true));
    }

    @Test
    public void testPuedeVenderseComboActivoConStock() throws NegocioException {
        ComboBO bo = new ComboBO();

        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();

        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNombre("Harina");
        ingrediente.setUnidadDeMedida(UnidadDeMedida.GRAMO);
        ingrediente.setStock(5000.0);
        em.persist(ingrediente);

        Producto producto1 = new Producto("Pizza Margarita", "Pizza clasica", 120.0, TipoProducto.PLATILLO);
        em.persist(producto1);
        ProductoIngrediente pi1 = new ProductoIngrediente(200.0, producto1, ingrediente);
        em.persist(pi1);
        producto1.getIngredientes().add(pi1);

        Producto producto2 = new Producto("Refresco", "Refresco de cola", 35.0, TipoProducto.BEBIDA);
        em.persist(producto2);
        ProductoIngrediente pi2 = new ProductoIngrediente(10.0, producto2, ingrediente);
        em.persist(pi2);
        producto2.getIngredientes().add(pi2);

        em.getTransaction().commit();
        em.close();

        ComboDTO dto = new ComboDTO("Combo Familiar", "Incluye pizza y refresco", 140.0, 155.0, 10);
        List<Long> idsProductos = Arrays.asList(producto1.getId(), producto2.getId());
        List<Integer> cantidades = Arrays.asList(1, 2);

        Combo combo = bo.crearComboConProductos(dto, idsProductos, cantidades);

        boolean puedeVenderse = bo.puedeVenderse(combo.getId());
        assertTrue(puedeVenderse, "El combo con stock suficiente debe poder venderse");
    }

    @Test
    public void testPuedeVenderseComboInactivo() throws NegocioException {
        ComboBO bo = new ComboBO();
        ComboDTO dto = new ComboDTO("Combo Inactivo Test", "Descripcion generica", 100.0, 80.0, 20);
        Combo agregado = bo.agregarCombo(dto);

        bo.cambiarEstado(agregado.getId(), false);

        boolean puedeVenderse = bo.puedeVenderse(agregado.getId());
        assertFalse(puedeVenderse, "Un combo inactivo no debe poder venderse");
    }

}
