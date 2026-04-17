/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import entidades.Producto;
import enums.TipoProducto;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author icoro
 */
public class ProductoDAOTest {

    private ProductoDAO productoDAO;

    // Este método se ejecuta ANTES de cada prueba
    @BeforeEach
    public void setUp() {
        productoDAO = new ProductoDAO();
    }

    // ==========================================
    // PRUEBAS PARA: guardar()
    // ==========================================

    @Test
    public void testGuardar_FlujoBase() {
        // 1. Preparar (Arrange)
        Producto nuevoProducto = new Producto("Tacos de Asada Test", "Con todo", 100.0, TipoProducto.PLATILLO);
        
        // 2. Ejecutar (Act)
        // Intentamos guardar el producto en la BD generada por JPA
        assertDoesNotThrow(() -> {
            productoDAO.guardar(nuevoProducto);
        }, "No debería lanzar ninguna excepción al guardar un producto válido.");

        // 3. Comprobar (Assert)
        Producto productoGuardado = productoDAO.buscarPorNombre("Tacos de Asada Test");
        assertNotNull(productoGuardado, "El producto debería existir en la BD");
        assertEquals("Tacos de Asada Test", productoGuardado.getNombre());
    }

    // ==========================================
    // PRUEBAS PARA: buscarPorNombre()
    // ==========================================

    @Test
    public void testBuscarPorNombre_FlujoBase() {
        // 1. Preparar (Arrange)
        // Asumimos que "Tacos al Pastor" ya existe o lo creamos antes
        
        // 2. Ejecutar (Act)
        Producto resultado = productoDAO.buscarPorNombre("Tacos al Pastor"); 
        
        // 3. Comprobar (Assert)
        assertNotNull(resultado, "Debe retornar un producto cuando el nombre sí existe en la BD.");
        assertEquals("Tacos al Pastor", resultado.getNombre());
    }

    @Test
    public void testBuscarPorNombre_FlujoAlternativo_NoExiste() {
        // 1. Preparar (Arrange)
        String nombreInexistente = "Pizza Hawaiana Radiactiva";
        
        // 2. Ejecutar (Act)
        Producto resultado = productoDAO.buscarPorNombre(nombreInexistente);
        
        // 3. Comprobar (Assert)
        // devuelve null si no encuentra nada gracias al NoResultException
        assertNull(resultado, "Debe retornar null cuando se busca un producto que NO existe.");
    }

    // ==========================================
    // PRUEBAS PARA: buscarPorFiltros()
    // ==========================================

    @Test
    public void testBuscarPorFiltros_FlujoBase() {
        // Ejecutar (Buscamos productos que lleven la palabra "Taco" y sean PLATILLO)
        var lista = productoDAO.buscarPorFiltros("Taco", TipoProducto.PLATILLO);
        
        // Comprobar
        assertNotNull(lista, "La lista no debe ser nula");
        assertFalse(lista.isEmpty(), "La lista debería contener al menos un resultado (si tienes datos de prueba)");
    }

    @Test
    public void testBuscarPorFiltros_FlujoAlternativo_SinResultados() {
        // Ejecutar (Buscamos algo que no tiene sentido)
        var lista = productoDAO.buscarPorFiltros("Xyz123", TipoProducto.BEBIDA);
        
        // Comprobar
        assertNotNull(lista, "La lista no debe ser nula, sino una lista vacía");
        assertTrue(lista.isEmpty(), "La lista debe estar completamente vacía porque no hay coincidencias");
    }
}