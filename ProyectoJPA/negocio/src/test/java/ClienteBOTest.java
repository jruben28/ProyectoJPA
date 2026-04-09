/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import BOs.ClienteBO;
import conexion.ConexionBD;
import entidades.Comanda;
import javax.persistence.EntityManager;
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
    @Test
public void testAgregarClienteFrecuente_FlujoBase() {
    System.out.println("Prueba: Agregar ClienteFrecuente en BO (Flujo Base)");
    
    try {
        ClienteFrecuenteDTO clienteNuevo = new ClienteFrecuenteDTO(
            null, "Marcela Rojas", "FRECUENTE", "6447778888", "marcela@test.com", new Date(), 0);
        
        bo.agregarClienteFrecuente(clienteNuevo);
        
        // Verificar que fue agregado buscándolo
        List<ClienteFrecuenteDTO> resultados = bo.buscarFrecuentesPorFiltro("Marcela", "nombre");
        
        assertFalse(resultados.isEmpty(), "El cliente recién agregado debería encontrarse");
        assertEquals("Marcela Rojas", resultados.get(0).getNombre(), "El nombre debe coincidir");
        
        System.out.println(" Test pasado: ClienteFrecuente agregado correctamente");
    } catch (Exception e) {
        fail("No debería lanzar excepción: " + e.getMessage());
    }
}

@Test
public void testAgregarClienteFrecuente_TelefonoInvalido() {
    System.out.println("Prueba: Agregar con teléfono inválido (Debe fallar)");
    
    ClienteFrecuenteDTO clienteInvalido = new ClienteFrecuenteDTO(
        null, "Miguel Torres", "FRECUENTE", "123", "miguel@test.com", new Date(), 0);
    
    Exception exception = assertThrows(NegocioException.class, () -> {
        bo.agregarClienteFrecuente(clienteInvalido);
    });
    
    assertTrue(exception.getMessage().toLowerCase().contains("telefono"), 
        "El mensaje debe mencionar que el teléfono es inválido");
    
    System.out.println("Test pasado: Teléfono inválido rechazado correctamente");
}

@Test
public void testBuscarFrecuentesPorFiltro_FlujoBase() {
    System.out.println("Prueba: Buscar Frecuentes por Filtro (Flujo Base)");
    
    try {
        // Primero agregamos un cliente
        ClienteFrecuenteDTO clienteNuevo = new ClienteFrecuenteDTO(
            null, "Valentina Cruz", "FRECUENTE", "6441112233", "valentina@test.com", new Date(), 0);
        bo.agregarClienteFrecuente(clienteNuevo);
        
        // Buscamos por nombre
        List<ClienteFrecuenteDTO> resultados = bo.buscarFrecuentesPorFiltro("Valentina", "nombre");
        
        assertFalse(resultados.isEmpty(), "Debería encontrar al cliente");
        
        ClienteFrecuenteDTO encontrado = resultados.get(0);
        
        assertNotNull(encontrado.getId(), "El DTO debe tener ID");
        assertEquals("Valentina Cruz", encontrado.getNombre(), "El nombre debe coincidir");
        assertNotNull(encontrado.getPuntosAcumulados(), "Los puntos deben estar inicializados");
        assertNotNull(encontrado.getTotalGastado(), "El total gastado debe estar inicializado");
        
        System.out.println("✓ Test pasado: Búsqueda retorna DTOs con datos completos (puntos, visitas, total)");
    } catch (Exception e) {
        fail("No debería lanzar excepción: " + e.getMessage());
    }
}

@Test
public void testBuscarFrecuentesPorFiltro_FiltroVacio() {
    System.out.println("Prueba: Buscar con filtro vacío (Debe fallar)");
    
    Exception exception = assertThrows(NegocioException.class, () -> {
        bo.buscarFrecuentesPorFiltro("", "nombre");
    });
    
    assertTrue(exception.getMessage().toLowerCase().contains("filtro"), 
        "El mensaje debe mencionar que el filtro está vacío");
    
    System.out.println(" Test pasado: Filtro vacío rechazado correctamente");
}

@Test
public void testCalcularPuntos_ConComandasEntregadas() {
    System.out.println("Prueba: Calcular puntos con comandas entregadas");

    try {
        String sufijo = String.valueOf(System.currentTimeMillis());
        ClienteFrecuenteDTO clienteNuevo = new ClienteFrecuenteDTO(
                null,
                "ClientePuntos_" + sufijo,
                "FRECUENTE",
                "6441234567",
                "puntos_" + sufijo + "@test.com",
                new Date(),
                0
        );

        bo.agregarClienteFrecuente(clienteNuevo);

        List<ClienteFrecuenteDTO> resultados = bo.buscarFrecuentesPorFiltro("ClientePuntos_" + sufijo, "nombre");
        assertFalse(resultados.isEmpty(), "Debe encontrar el cliente creado");

        Long idCliente = resultados.get(0).getId();
        assertNotNull(idCliente, "El ID del cliente no debe ser nulo");

        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        em.persist(new Comanda(100.0, "ENTREGADA", idCliente));
        em.persist(new Comanda(450.0, "ENTREGADA", idCliente));
        em.persist(new Comanda(999.0, "ABIERTA", idCliente)); // No debe contar
        em.getTransaction().commit();
        em.close();

        Integer puntos = bo.calcularPuntos(idCliente);

        // 100 + 450 = 550; 550/20 = 27
        assertEquals(27, puntos, "El cálculo de puntos debe considerar solo ENTREGADAS");
        System.out.println("Test pasado: puntos calculados correctamente");
    } catch (Exception e) {
        fail("No debería lanzar excepción: " + e.getMessage());
    }
}

@Test
public void testCalcularTotalGastado_SinComandasDevuelve0() {
    System.out.println("Prueba: Cliente sin comandas debe retornar 0.0");
    
    // Crear un cliente nuevo sin comandas
    ClienteFrecuenteDTO clienteSinGastos = new ClienteFrecuenteDTO(
        null, "Cliente Sin Compras", "FRECUENTE", "6449999999", "nocompra@test.com", new Date(), 0);
    
    try {
        bo.agregarClienteFrecuente(clienteSinGastos);
        
        // Buscarlo para obtener el ID
        List<ClienteFrecuenteDTO> resultados = bo.buscarFrecuentesPorFiltro("Cliente Sin Compras", "nombre");
        ClienteFrecuenteDTO cliente = resultados.get(0);
        
        // Calcular total
        Double total = bo.calcularTotalGastado(cliente.getId());
        
        assertEquals(0.0, total, "Un cliente sin comandas debe tener total = 0.0");
        
        System.out.println(" Test pasado: Cliente sin compras retorna 0.0");
    } catch (Exception e) {
        fail("No debería lanzar excepción: " + e.getMessage());
    }
}
}