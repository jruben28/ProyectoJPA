package pruebas;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import conexion.ConexionBD;
import DAOs.ClienteDAO;
import entidades.ClienteFrecuente;
import entidades.ClienteGeneral;
import entidades.Comanda;

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
    @Test
public void testAgregarClienteFrecuente_FlujoBase() {
    System.out.println("Prueba: Agregar ClienteFrecuente (Flujo Base)");
    
    ClienteFrecuente nuevoCliente = new ClienteFrecuente("Sofia Lopez", "6441231234", "sofia@test.com");
    
    ClienteFrecuente clienteAgregado = dao.agregarClienteFrecuente(nuevoCliente);
    
    assertNotNull(clienteAgregado.getId(), "El ID no debe ser nulo después de persistir");
    
    ClienteFrecuente clienteRecuperado = (ClienteFrecuente) dao.buscarPorId(clienteAgregado.getId());
    
    assertNotNull(clienteRecuperado, "El cliente recuperado no debe ser nulo");
    assertEquals("Sofia Lopez", clienteRecuperado.getNombre(), "El nombre debe coincidir");
    assertEquals("6441231234", clienteRecuperado.getTelefono(), "El teléfono debe coincidir");
    
    System.out.println("✓ Test pasado: Cliente agregado correctamente");
}

@Test
public void testActualizarClienteFrecuente_FlujoBase() {
    System.out.println("Prueba: Actualizar ClienteFrecuente (Flujo Base)");
    
    ClienteFrecuente cliente = new ClienteFrecuente("Carlos Mendez", "6449876543", "carlos@test.com");
    dao.agregarClienteFrecuente(cliente);
    
    Long idOriginal = cliente.getId();
    
    cliente.setNombre("Carlos Mendez Actualizado");
    cliente.setTelefono("6440001234");
    
    dao.actualizarClienteFrecuente(cliente);
    
    ClienteFrecuente actualizado = (ClienteFrecuente) dao.buscarPorId(idOriginal);
    
    assertEquals("Carlos Mendez Actualizado", actualizado.getNombre(), "El nombre debe estar actualizado");
    assertEquals("6440001234", actualizado.getTelefono(), "El teléfono debe estar actualizado");
    
    System.out.println("✓ Test pasado: Cliente actualizado correctamente");
}

@Test
public void testBuscarFrecuentesPorCampo_FlujoBase() {
    System.out.println("Prueba: Buscar por Campo (Flujo Base)");
    
    // 1. Simulamos el trabajo del BO codificando el teléfono a Base64 antes de guardar
    String telefonoOriginal = "6449999888";
    String telefonoBase64 = java.util.Base64.getEncoder().encodeToString(telefonoOriginal.getBytes());
    
    // 2. Guardamos el cliente con el teléfono codificado
    ClienteFrecuente cliente = new ClienteFrecuente("Roberto Silva", telefonoBase64, "roberto@test.com");
    dao.agregarClienteFrecuente(cliente);
    
    // Búsqueda por nombre
    List<ClienteFrecuente> resultadosNombre = dao.buscarFrecuentesPorCampo("Roberto", "nombre");
    assertFalse(resultadosNombre.isEmpty(), "Debería encontrar clientes por nombre");
    
    // Búsqueda por teléfono (Buscamos un pedazo del número normal, la BD lo decodifica y ¡bum!, lo encuentra)
    List<ClienteFrecuente> resultadosTel = dao.buscarFrecuentesPorCampo("9999888", "telefono");
    assertFalse(resultadosTel.isEmpty(), "Debería encontrar clientes por teléfono");
    
    // Búsqueda por correo
    List<ClienteFrecuente> resultadosEmail = dao.buscarFrecuentesPorCampo("roberto", "correo");
    assertFalse(resultadosEmail.isEmpty(), "Debería encontrar clientes por correo");
    
    System.out.println(" Test pasado: Búsquedas por campo funcionan correctamente");
}

@Test
public void testBuscarFrecuentesPorCampo_SinResultados() {
    System.out.println("Prueba: Buscar con filtro que NO existe (Flujo Alternativo)");
    
    List<ClienteFrecuente> resultados = dao.buscarFrecuentesPorCampo("XXXXXXXX_NO_EXISTE", "nombre");
    
    assertTrue(resultados.isEmpty(), "La lista debe estar vacía porque el filtro no existe");
    
    System.out.println("Test pasado: Búsqueda sin resultados retorna lista vacía");
}

@Test
public void testBuscarComandasPorCliente_FlujoBase() {
    System.out.println("Prueba: Buscar Comandas por Cliente (Flujo Base)");
    
    // Creamos un cliente
    ClienteFrecuente cliente = new ClienteFrecuente("Diego Perez", "6445556666", "diego@test.com");
    EntityManager em = ConexionBD.crearConexion();
    
    em.getTransaction().begin();
    em.persist(cliente);
    
    em.flush(); 
   Long idCliente = cliente.getId();
    assertNotNull(idCliente, "El cliente debe tener ID antes de crear comandas");

    // Creamos 2 comandas ENTREGADAS y 1 ABIERTA
    Comanda c1 = new Comanda(200.0, "ENTREGADA", idCliente);
    Comanda c2 = new Comanda(350.0, "ENTREGADA", idCliente);
    Comanda c3 = new Comanda(100.0, "ABIERTA", idCliente);
    
    em.persist(c1);
    em.persist(c2);
    em.persist(c3);
    em.getTransaction().commit();
    em.close();
    
    // Buscamos las comandas del cliente
    List<Comanda> comandasEntregadas = dao.buscarComandasPorCliente(idCliente);
    
    // Validaciones
    assertEquals(2, comandasEntregadas.size(), "Debería retornar SOLO 2 comandas entregadas (sin la ABIERTA)");
    
    for (Comanda c : comandasEntregadas) {
       assertEquals("ENTREGADA", c.getEstado(), "Todas las comandas deben estar ENTREGADAS");
    }
    
    System.out.println(" Test pasado: Busca correcta de comandas por cliente (filtra por estado ENTREGADA)");
}
}