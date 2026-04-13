/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package BOs;

import com.dtos.IngredienteDTO;
import enums.UnidadDeMedida;
import excepciones.NegocioException;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author joser
 */
public class IngredienteBOTest {
    
    private IngredienteBO ingredienteBO;

    @BeforeEach
    void setUp() {
        ingredienteBO = new IngredienteBO(); 
    }

    // --- PRUEBAS DE AGREGAR ---

    @Test
    void testAgregarIngrediente_DatosValidos_GuardaEnBD() {
        try {
            IngredienteDTO nuevoIngrediente = new IngredienteDTO();
            nuevoIngrediente.setNombre("Cebolla de Prueba");
            nuevoIngrediente.setStock(15.5);
            nuevoIngrediente.setUnidadDeMedida(UnidadDeMedida.KILOGRAMO); 

            IngredienteDTO resultado = ingredienteBO.agregarIngrediente(nuevoIngrediente);

            assertNotNull(resultado, "El resultado no debería ser nulo");

            assertNotNull(resultado.getIdIngrediente(), "El ingrediente debería tener un ID generado por la BD");
            assertEquals("Cebolla de Prueba", resultado.getNombre());
            
        } catch (NegocioException e) {
            fail("No debió lanzar excepción: " + e.getMessage());
        }
    }

    @Test
    void testAgregarIngrediente_NombreNulo_LanzaExcepcion() {
        IngredienteDTO dtoInvalido = new IngredienteDTO();
        dtoInvalido.setNombre(""); 
        dtoInvalido.setStock(10.0);
        dtoInvalido.setUnidadDeMedida(UnidadDeMedida.KILOGRAMO);

        NegocioException excepcion = assertThrows(NegocioException.class, () -> {
            ingredienteBO.agregarIngrediente(dtoInvalido);
        });
        
        assertTrue(excepcion.getMessage().contains("El nombre del ingrediente es obligatorio"));
    }

    // --- PRUEBAS DE ACTUALIZAR ---

    @Test
    void testActualizarStock_DatosValidos_ActualizaBD() throws NegocioException {
        IngredienteDTO dtoInicial = new IngredienteDTO();
        dtoInicial.setNombre("Ajo de Prueba");
        dtoInicial.setStock(5.0);
        dtoInicial.setUnidadDeMedida(UnidadDeMedida.PIEZA);
        IngredienteDTO guardado = ingredienteBO.agregarIngrediente(dtoInicial);
        
        Long idGenerado = guardado.getIdIngrediente();

        IngredienteDTO actualizado = ingredienteBO.actualizarStock(idGenerado, 20.0);

        assertNotNull(actualizado);
        assertEquals(20.0, actualizado.getStock(), "El stock debió actualizarse a 20.0");
    }

    @Test
    void testActualizarStock_StockNegativo_LanzaExcepcion() {
        NegocioException excepcion = assertThrows(NegocioException.class, () -> {
            ingredienteBO.actualizarStock(1L, -5.0);
        });
        
        assertTrue(excepcion.getMessage().contains("no son validos"));
    }

    // --- PRUEBAS DE ELIMINAR ---

    @Test
    void testEliminarIngrediente_IdValido_EliminaDeBD() throws NegocioException {
        IngredienteDTO dtoParaBorrar = new IngredienteDTO();
        dtoParaBorrar.setNombre("Ingrediente a Borrar");
        dtoParaBorrar.setStock(1.0);
        dtoParaBorrar.setUnidadDeMedida(UnidadDeMedida.GRAMO);
        IngredienteDTO guardado = ingredienteBO.agregarIngrediente(dtoParaBorrar);
        
        Long idGenerado = guardado.getIdIngrediente();

        IngredienteDTO eliminado = ingredienteBO.eliminarIngrediente(idGenerado);

        assertNotNull(eliminado, "Debe retornar los datos del ingrediente eliminado");
        assertEquals("Ingrediente a Borrar", eliminado.getNombre());
    }

    // --- PRUEBAS DE LECTURA ---

    @Test
    void testObtenerIngredienteTodos_RetornaLista() {
        try {
            List<IngredienteDTO> lista = ingredienteBO.obtenerIngredienteTodos();

            assertNotNull(lista, "La lista no debe ser nula");
            assertFalse(lista.isEmpty(), "La lista debería contener al menos un elemento");
        } catch (NegocioException e) {
            fail("Falló al consultar los ingredientes: " + e.getMessage());
        }
    }
}
