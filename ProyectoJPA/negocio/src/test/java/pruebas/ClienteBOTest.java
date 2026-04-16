package pruebas;

import BOs.ClienteBO;
import conexion.ConexionBD;
import entidades.Comanda;
import entidades.Mesa;
import enums.EstadoComanda;
import javax.persistence.EntityManager;
import com.dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

public class ClienteBOTest {

    public ClienteBOTest() {
    }

    @Test
    public void testAgregarClienteFrecuenteExito() {
        ClienteBO bo = new ClienteBO();
        ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
        dto.setNombre("Cliente exitoo");
        dto.setTelefono("5555555555");
        dto.setCorreo("manExitoso@homai.com");
        dto.setFechaRegistro(new Date());

        assertDoesNotThrow(() -> bo.agregarClienteFrecuente(dto));
    }

    @Test
    public void testAgregarClienteFrecuenteTelefonoInvalido() {
        ClienteBO bo = new ClienteBO();
        ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
        dto.setNombre("Cliente que no le sabe al telefono");
        dto.setTelefono("111");
        dto.setCorreo("nosabo@homai.com");
        dto.setFechaRegistro(new Date());

        assertThrows(NegocioException.class, () -> bo.agregarClienteFrecuente(dto));
    }

    @Test
    public void testCalcularPuntosConComandasEntregadas() {
        ClienteBO bo = new ClienteBO();
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();

            ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
            dto.setNombre("Panfilo");
            dto.setTelefono("6666535666");
            dto.setCorreo("panf@homai.com");
            dto.setFechaRegistro(new Date());
            bo.agregarClienteFrecuente(dto);

            List<entidades.ClienteFrecuente> clientes = em.createQuery(
                "SELECT c FROM ClienteFrecuente c WHERE c.nombre = 'Panfilo'",
                entidades.ClienteFrecuente.class
            ).getResultList();
            Long idCliente = clientes.get(0).getId();

            Mesa mesa = new Mesa(7001);
            em.persist(mesa);
            entidades.ClienteFrecuente clienteRef = em.find(entidades.ClienteFrecuente.class, idCliente);

            Comanda c1 = new Comanda("C1", java.time.LocalDateTime.now(), mesa, clienteRef);
            c1.setEstado(EstadoComanda.ENTREGADA);
            c1.setTotal(100.0);
            em.persist(c1);

            Comanda c2 = new Comanda("C2", java.time.LocalDateTime.now(), mesa, clienteRef);
            c2.setEstado(EstadoComanda.ENTREGADA);
            c2.setTotal(450.0);
            em.persist(c2);

            em.getTransaction().commit();

            Integer puntos = bo.calcularPuntos(idCliente);
            assertTrue(puntos >= 27);
        } catch (Exception e) {
            fail("Error: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Test
    public void testCalcularTotalGastadoSinComandas() {
        ClienteBO bo = new ClienteBO();
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();

            ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
            dto.setNombre("Cliente sin dinero");
            dto.setTelefono("1111111111");
            dto.setCorreo("ruino@homai.com");
            dto.setFechaRegistro(new Date());
            bo.agregarClienteFrecuente(dto);

            List<entidades.ClienteFrecuente> clientes = em.createQuery(
                "SELECT c FROM ClienteFrecuente c WHERE c.nombre = 'Cliente sin dinero'",
                entidades.ClienteFrecuente.class
            ).getResultList();
            Long idCliente = clientes.get(0).getId();

            em.getTransaction().commit();

            Double total = bo.calcularTotalGastado(idCliente);
            assertEquals(0.0, total);
        } catch (Exception e) {
            fail("Error: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}