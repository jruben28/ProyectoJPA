package pruebas;

import DAOs.ClienteDAO;
import DAOs.MesaDAO;
import conexion.ConexionBD;
import entidades.ClienteFrecuente;
import entidades.ClienteGeneral;
import entidades.Comanda;
import entidades.Mesa;
import enums.EstadoComanda;
import excepciones.PersistenciaException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para ClienteDAO siguiendo la metodología de ComboProducto
 * @author keppler
 */
public class ClienteDAOTest {

    public ClienteDAOTest() {
    }

    @Test
    public void testAgregarClienteFrecuenteExito() {
        ClienteDAO dao = new ClienteDAO();
        ClienteFrecuente nuevo = new ClienteFrecuente("Sofia Lopez", "6441231234", "sofia@homai.com");
        ClienteFrecuente resultado = dao.agregarClienteFrecuente(nuevo);
        
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Sofia Lopez", resultado.getNombre());
    }

    @Test
    public void testBuscarPorIdInexistente() {
        ClienteDAO dao = new ClienteDAO();
        Object resultado = dao.buscarPorId(99999L);
        
        
        assertNull(resultado);
    }

    @Test
    public void testActualizarClienteFrecuenteExito() {
        ClienteDAO dao = new ClienteDAO();
        ClienteFrecuente cliente = new ClienteFrecuente("Carlos Mendez", "6449876543", "carlos@test.com");
        dao.agregarClienteFrecuente(cliente);
        cliente.setNombre("Carlos Actualizado");
        dao.actualizarClienteFrecuente(cliente);
        ClienteFrecuente resultado = (ClienteFrecuente) dao.buscarPorId(cliente.getId());
       
        
        assertEquals("Carlos Actualizado", resultado.getNombre());
    }

    @Test
    public void testBuscarFrecuentesPorCampoExito() {
        ClienteDAO dao = new ClienteDAO();
        String telCodificado = Base64.getEncoder().encodeToString("6449999888".getBytes());
        ClienteFrecuente cliente = new ClienteFrecuente("Robert Silvador", telCodificado, "roberto@homai.com");
        dao.agregarClienteFrecuente(cliente);
        
        List<ClienteFrecuente> resultados = dao.buscarFrecuentesPorCampo("Robert", "nombre");
        
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
    }

    @Test
    public void testObtenerClienteGeneral() {
        ClienteDAO dao = new ClienteDAO();
        ClienteGeneral clienteG = dao.obtenerClienteGeneral();
        if (clienteG == null) {
            dao.agregar(new ClienteGeneral("Cliente General"));
            clienteG = dao.obtenerClienteGeneral();
        }
        
        assertNotNull(clienteG);
        assertEquals("Cliente General", clienteG.getNombre());
    }

    @Test
    public void testBuscarComandasPorClienteExito() throws PersistenciaException {
        MesaDAO mesaDao = new MesaDAO();
        Mesa mesa = new Mesa(555);
        mesaDao.agregar(mesa);
        ClienteDAO dao = new ClienteDAO();
        ClienteFrecuente cliente = new ClienteFrecuente("Diego Perez", "6445556666", "diego@homai.com");
        dao.agregarClienteFrecuente(cliente);
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        
        Comanda c1 = new Comanda();
        c1.setFolio("comanda1");
        c1.setTotal(200.0);
        c1.setEstado(EstadoComanda.ENTREGADA);
        c1.setCliente(cliente);
        c1.setMesa(mesa);
        c1.setFechaHora(LocalDateTime.now());
        //esta no va a salir
        Comanda c2 = new Comanda();
        c2.setFolio("comanda2");
        c2.setTotal(100.0);
        c2.setEstado(EstadoComanda.ABIERTA);
        c2.setCliente(cliente);
        c2.setMesa(mesa);
        c2.setFechaHora(LocalDateTime.now());
        em.persist(c1);
        em.persist(c2);
        em.getTransaction().commit();
        em.close();
        List<Comanda> entregadas = dao.buscarComandasPorCliente(cliente.getId());


        assertNotNull(entregadas);
        assertEquals(1, entregadas.size());
        assertEquals(EstadoComanda.ENTREGADA, entregadas.get(0).getEstado());
    }
}