package pruebas;

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

public class ClienteBOTest {

    private static ClienteBO clienteBO;

    @BeforeAll
    public static void setUp() {
        clienteBO = new ClienteBO();
    }

    @Test
    public void testAgregarClienteFrecuente_FlujoBase() {
        System.out.println("Prueba: Agregar ClienteFrecuente en BO (Flujo Base)");
        ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
        dto.setNombre("Test Cliente");
        dto.setTelefono("5551234567");
        dto.setCorreo("test@mail.com");
        dto.setFechaRegistro(new Date());

        assertDoesNotThrow(() -> clienteBO.agregarClienteFrecuente(dto));
        System.out.println("✓ Test pasado: ClienteFrecuente agregado correctamente");
    }

    @Test
    public void testAgregarClienteFrecuente_TelefonoInvalido() {
        System.out.println("Prueba: Agregar con teléfono inválido (Debe fallar)");
        ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
        dto.setNombre("Test Cliente");
        dto.setTelefono("123"); // Teléfono inválido
        dto.setCorreo("test@mail.com");
        dto.setFechaRegistro(new Date());

        assertThrows(NegocioException.class, () -> clienteBO.agregarClienteFrecuente(dto));
        System.out.println("Test pasado: Teléfono inválido rechazado correctamente");
    }

    @Test
    public void testCalcularPuntos_ConComandasEntregadas() {
        System.out.println("Prueba: Calcular puntos con comandas entregadas");
        
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();

            // Crear cliente de prueba
            ClienteFrecuenteDTO dtoCliente = new ClienteFrecuenteDTO();
            dtoCliente.setNombre("Cliente Prueba Puntos");
            dtoCliente.setTelefono("6661234567");
            dtoCliente.setCorreo("puntos@mail.com");
            dtoCliente.setFechaRegistro(new Date());
            
            clienteBO.agregarClienteFrecuente(dtoCliente);
            
            // Obtener el cliente creado
            List<entidades.ClienteFrecuente> clientes = em.createQuery(
                "SELECT c FROM ClienteFrecuente c WHERE c.nombre = 'Cliente Prueba Puntos'",
                entidades.ClienteFrecuente.class
            ).getResultList();
            
            if (clientes.isEmpty()) {
                fail("No se creó el cliente");
            }
            
            Long idCliente = clientes.get(0).getId();

            // Crear comandas con CONSTRUCTOR ACTUAL: (Double total, String estado, Long idCliente)
            Comanda c1 = new Comanda(100.0, "ENTREGADA", idCliente);
            em.persist(c1);

            Comanda c2 = new Comanda(450.0, "ENTREGADA", idCliente);
            em.persist(c2);

            Comanda c3 = new Comanda(999.0, "ABIERTA", idCliente);
            em.persist(c3);

            em.getTransaction().commit();

            // Calcular puntos (1 punto cada 20 pesos)
            Integer puntos = clienteBO.calcularPuntos(idCliente);
            // 100 + 450 = 550 (ENTREGADAS) / 20 = 27.5 → 27 puntos
            assertTrue(puntos >= 27, "Puntos incorrectos: " + puntos);

            System.out.println("Test pasado: puntos calculados correctamente: " + puntos);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error: " + ex.getMessage());
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    @Test
    public void testCalcularTotalGastado_SinComandasDevuelve0() {
        System.out.println("Prueba: Cliente sin comandas debe retornar 0.0");
        
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();

            ClienteFrecuenteDTO dtoCliente = new ClienteFrecuenteDTO();
            dtoCliente.setNombre("Cliente Sin Compras");
            dtoCliente.setTelefono("6662224567");
            dtoCliente.setCorreo("sincompras@mail.com");
            dtoCliente.setFechaRegistro(new Date());
            
            clienteBO.agregarClienteFrecuente(dtoCliente);

            List<entidades.ClienteFrecuente> clientes = em.createQuery(
                "SELECT c FROM ClienteFrecuente c WHERE c.nombre = 'Cliente Sin Compras'",
                entidades.ClienteFrecuente.class
            ).getResultList();

            em.getTransaction().commit();

            Long idCliente = clientes.get(0).getId();
            Double total = clienteBO.calcularTotalGastado(idCliente);

            assertEquals(0.0, total, "Total debería ser 0.0");
            System.out.println("Test pasado: Cliente sin compras retorna 0.0");
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error: " + ex.getMessage());
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    @Test
    public void testBuscarFrecuentesPorFiltro_FlujoBase() {
        System.out.println("Prueba: Buscar Clientes Frecuentes por Filtro");
        
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();

            ClienteFrecuenteDTO dtoCliente = new ClienteFrecuenteDTO();
            dtoCliente.setNombre("Cliente Filtro Test");
            dtoCliente.setTelefono("6663334567");
            dtoCliente.setCorreo("filtro@mail.com");
            dtoCliente.setFechaRegistro(new Date());
            
            clienteBO.agregarClienteFrecuente(dtoCliente);
            em.getTransaction().commit();

            // Buscar por nombre
            var resultados = assertDoesNotThrow(() -> 
                clienteBO.buscarFrecuentesPorCampo("nombre", "Cliente Filtro Test")
            );
            
            assertFalse(resultados.isEmpty(), "No se encontraron clientes");
            System.out.println("✓ Test pasado: Búsqueda por filtro funciona");
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error: " + ex.getMessage());
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
}