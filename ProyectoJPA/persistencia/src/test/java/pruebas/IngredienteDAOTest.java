/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pruebas;

import DAOs.IngredienteDAO;
import entidades.Ingrediente;
import enums.UnidadDeMedida;
import excepciones.PersistenciaException;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author joser
 */
public class IngredienteDAOTest {
    
    private IngredienteDAO ingredienteDAO;

    @BeforeEach
    void setUp() {
        ingredienteDAO = new IngredienteDAO();
    }

    @Test
    void testAgregarIngrediente_Real() throws PersistenciaException {
        Ingrediente nuevoIngrediente = new Ingrediente(null, "Pimienta Negra", UnidadDeMedida.GRAMO, 50.0, "url_pimienta");

        Ingrediente resultado = ingredienteDAO.agregarIngrediente(nuevoIngrediente);

        assertNotNull(resultado, "El ingrediente guardado no debería ser nulo");
        assertNotNull(resultado.getId(), "La base de datos debería haber asignado un ID (autoincremental)");
        assertEquals("Pimienta Negra", resultado.getNombre());
        assertEquals(50.0, resultado.getStock());
    }

    @Test
    void testObtenerIngredientePorId_Real() throws PersistenciaException {
        Ingrediente ingredienteAux = new Ingrediente(null, "Orégano", UnidadDeMedida.GRAMO, 30.0, "url_oregano");
        Ingrediente ingredienteGuardado = ingredienteDAO.agregarIngrediente(ingredienteAux);
        Long idBuscado = ingredienteGuardado.getId();

        Ingrediente resultado = ingredienteDAO.obtenerIngredientePorId(idBuscado);

        assertNotNull(resultado);
        assertEquals(idBuscado, resultado.getId());
        assertEquals("Orégano", resultado.getNombre());
    }

    @Test
    void testActualizarStock_Real() throws PersistenciaException {
        Ingrediente ingrediente = new Ingrediente(null, "Leche", UnidadDeMedida.LITRO, 2.0, "url_leche");
        Ingrediente ingredienteGuardado = ingredienteDAO.agregarIngrediente(ingrediente);
        
        Long idAActualizar = ingredienteGuardado.getId();
        Double nuevoStock = 15.5;

        Ingrediente resultado = ingredienteDAO.actualizarStock(idAActualizar, nuevoStock);

        assertNotNull(resultado);
        assertEquals(nuevoStock, resultado.getStock(), "El stock debería haberse actualizado en la base de datos");
    }

    @Test
    void testEliminarIngrediente_Real() throws PersistenciaException {
        Ingrediente ingrediente = new Ingrediente(null, "Zanahoria", UnidadDeMedida.KILOGRAMO, 5.0, "url_zanahoria");
        Ingrediente ingredienteGuardado = ingredienteDAO.agregarIngrediente(ingrediente);
        Long idAEliminar = ingredienteGuardado.getId();

        Ingrediente eliminado = ingredienteDAO.eliminarIngrediente(idAEliminar);

        assertNotNull(eliminado);
        assertEquals(idAEliminar, eliminado.getId());

        PersistenciaException exception = assertThrows(PersistenciaException.class, () -> {
            ingredienteDAO.obtenerIngredientePorId(idAEliminar);
        });
        assertTrue(exception.getMessage().contains("Ha habido un error al buscar el ingrediente"));
    }

    @Test
    void testObtenerIngredientePorFiltro_Real() throws PersistenciaException {
        String nombreUnico = "IngredienteFiltroTest123";
        Ingrediente ingrediente = new Ingrediente(null, nombreUnico, UnidadDeMedida.GRAMO, 100.0, "url_test");
        ingredienteDAO.agregarIngrediente(ingrediente);

        List<Ingrediente> resultados = ingredienteDAO.obtenerIngredientePorFiltro("FiltroTest");

        assertNotNull(resultados);
        assertFalse(resultados.isEmpty(), "Debería encontrar al menos un resultado con el filtro");

        boolean encontrado = resultados.stream().anyMatch(i -> i.getNombre().equals(nombreUnico));
        assertTrue(encontrado, "El ingrediente con el nombre específico debería estar en la lista devuelta");
    }

    @Test
    void testObtenerIngredienteTodos_Real() throws PersistenciaException {
        Ingrediente ingrediente = new Ingrediente(null, "Manzana", UnidadDeMedida.KILOGRAMO, 10.0, "url_manzana");
        ingredienteDAO.agregarIngrediente(ingrediente);

        List<Ingrediente> resultados = ingredienteDAO.obtenerIngredienteTodos();

        assertNotNull(resultados);
        assertFalse(resultados.isEmpty(), "La lista completa no debería estar vacía porque acabamos de insertar al menos uno");
    }
    
}
