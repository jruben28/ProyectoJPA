
import DAOs.ClienteDAO;
import Entidades.ClienteFrecuente;
import Entidades.ClienteGeneral;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ClienteDAOTest {

    private static ClienteDAO dao;

    @BeforeAll
    public static void setUpClass() {
        dao = new ClienteDAO();
        System.out.println("Iniciando pruebas unitarias para ClienteDAO...");
    }

    @Test
    public void testAgregarYBuscarPorId_FlujoBase() {
        System.out.println("Prueba: Agregar y Buscar por ID (Flujo Base)");
        
        // Usamos tu constructor exacto: (nombre, telefono, correo)
        ClienteFrecuente nuevoCliente = new ClienteFrecuente("Juan Perez", "6441234567", "juan@test.com");
        
        dao.agregar(nuevoCliente);
        
        assertNotNull(nuevoCliente.getId(), "El ID no debería ser nulo después de persistir");

        ClienteFrecuente clienteRecuperado = (ClienteFrecuente) dao.buscarPorId(nuevoCliente.getId());
        
        assertNotNull(clienteRecuperado, "El cliente recuperado no debe ser nulo");
        assertEquals("Juan Perez", clienteRecuperado.getNombre(), "Los nombres deben coincidir");
    }

    @Test
    public void testBuscarPorId_FlujoAlternativo() {
        System.out.println("Prueba: Buscar por ID que NO existe (Flujo Alternativo)");
        
        Object resultado = dao.buscarPorId(99999L);
        
        assertNull(resultado, "El resultado debe ser nulo porque el cliente no existe");
    }

    @Test
    public void testActualizar_FlujoBase() {
        System.out.println("Prueba: Actualizar Cliente (Flujo Base)");
        
        // Usamos tu constructor exacto
        ClienteFrecuente c = new ClienteFrecuente("Maria Gonzalez", "6441112233", "maria@test.com");
        dao.agregar(c);
        
        // Modificamos sus datos
        c.setNombre("Maria Gonzalez Modificada");
        c.setTelefono("6440000000"); // Cambiamos el teléfono
        dao.actualizar(c);
        
        // Lo volvemos a buscar
        ClienteFrecuente actualizado = (ClienteFrecuente) dao.buscarPorId(c.getId());
        
        // Verificamos que los cambios se hayan guardado en la BD
        assertEquals("Maria Gonzalez Modificada", actualizado.getNombre());
        assertEquals("6440000000", actualizado.getTelefono());
    }

    @Test
    public void testBuscarFrecuentesPorFiltro_FlujoBase() {
        System.out.println("Prueba: Buscar por Filtro (Flujo Base)");
        
        // Usamos tu constructor exacto
        ClienteFrecuente c = new ClienteFrecuente("Roberto Filtro", "6449998877", "filtro@test.com");
        dao.agregar(c);
        
        // Buscamos por coincidencia de nombre
        List<ClienteFrecuente> resultadosNombre = dao.buscarFrecuentesPorFiltro("Roberto");
        assertFalse(resultadosNombre.isEmpty(), "La lista no debe estar vacía al buscar por nombre");
        
        // Buscamos por coincidencia de teléfono
        List<ClienteFrecuente> resultadosTel = dao.buscarFrecuentesPorFiltro("9998877");
        assertFalse(resultadosTel.isEmpty(), "La lista no debe estar vacía al buscar por teléfono");
    }

    @Test
    public void testObtenerClienteGeneral_Flujos() {
        System.out.println("Prueba: Obtener Cliente General");
        
        ClienteGeneral cg = dao.obtenerClienteGeneral();
        
        if (cg == null) {
            ClienteGeneral nuevoCg = new ClienteGeneral("Cliente General");
            dao.agregar(nuevoCg);
            cg = dao.obtenerClienteGeneral();
        }
        
        assertNotNull(cg, "El cliente general no debe ser nulo");
        assertEquals("Cliente General", cg.getNombre(), "El nombre debe ser 'Cliente General'");
    }
}