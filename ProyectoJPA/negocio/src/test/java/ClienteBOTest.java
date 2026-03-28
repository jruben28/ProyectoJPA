/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import BOs.ClienteBO;
import com.dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

/**
 *
 * @author icoro
 */
public class ClienteBOTest {

    private static ClienteBO bo;

    @BeforeAll
    public static void setUpClass() {
        bo = new ClienteBO();
        System.out.println("Iniciando pruebas unitarias para ClienteBO...");
    }

    @Test
    public void testActualizarClienteSinId_DebeLanzarExcepcion() {
        System.out.println("Prueba: Intentar actualizar sin ID (Debe fallar a propósito)");
        
        // Creamos un DTO de prueba pero NO le ponemos ID (null)
        ClienteFrecuenteDTO dtoSinId = new ClienteFrecuenteDTO(null, "Fantasma", "FRECUENTE", "6441112233", "fan@test.com", new Date(), 0);
        
        // AssertThrows verifica que el BO se defienda y lance la NegocioException
        Exception exception = assertThrows(NegocioException.class, () -> {
            bo.actualizarClienteFrecuente(dtoSinId);
        });
        
        // Verificamos que el mensaje de error hable sobre el ID
        assertTrue(exception.getMessage().contains("ID"), "El mensaje de error debería mencionar la falta de ID");
        System.out.println("Éxito: El BO se defendió correctamente. Mensaje: " + exception.getMessage());
    }

    @Test
    public void testActualizarCliente_FlujoBase() {
        System.out.println("Prueba: Actualizar un cliente real (Flujo Base)");
        
        try {
            // 1. Creamos y agregamos un cliente nuevo para tener a quién editar
            ClienteFrecuenteDTO clienteNuevo = new ClienteFrecuenteDTO(null, "Juan Editar", "FRECUENTE", "6440001111", "juan@test.com", new Date(), 0);
            bo.agregarClienteFrecuente(clienteNuevo);
            
            // 2. Lo buscamos en la base de datos para obtener el DTO ya con su ID real generado
            // (Usamos tu método buscarFrecuentesPorFiltro)
            List<ClienteFrecuenteDTO> resultados = bo.buscarFrecuentesPorFiltro("Juan Editar", "nombre");
            assertFalse(resultados.isEmpty(), "Debería haber encontrado al cliente recién agregado");
            
            ClienteFrecuenteDTO clienteAEditar = resultados.get(0); // Tomamos el primero
            
            // 3. Le modificamos los datos (Simulando lo que haría el usuario en la pantalla)
            clienteAEditar.setNombre("Juan Editado Confirmado");
            clienteAEditar.setTelefono("6449998888"); // Cambiamos el teléfono
            
            // 4. Llamamos al método 
            bo.actualizarClienteFrecuente(clienteAEditar);
            
            // 5. Lo volvemos a buscar para ver si la base de datos realmente guardó los cambios
            List<ClienteFrecuenteDTO> verificacion = bo.buscarFrecuentesPorFiltro("Juan Editado", "nombre");
            assertFalse(verificacion.isEmpty(), "Debería encontrar al cliente con su nuevo nombre");
            
            ClienteFrecuenteDTO clienteFinal = verificacion.get(0);
            
            // 6. Verificamos que los datos coincidan. 
            // Si el teléfono coincide, significa que se encriptó bien al guardar y se desencriptó bien al buscar.
            assertEquals("Juan Editado Confirmado", clienteFinal.getNombre(), "El nombre no se actualizó");
            assertEquals("6449998888", clienteFinal.getTelefono(), "El teléfono no se actualizó/encriptó correctamente");
            
            System.out.println("¡Éxito! El cliente se actualizó correctamente y el teléfono pasó la prueba de encriptación.");
            
        } catch (Exception e) {
            fail("La prueba falló porque lanzó un error inesperado: " + e.getMessage());
        }
    }
}