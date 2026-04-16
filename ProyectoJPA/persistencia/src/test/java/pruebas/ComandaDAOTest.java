/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pruebas;

import DAOs.ComandaDAO;
import conexion.ConexionBD;
import entidades.ClienteFrecuente;
import entidades.Comanda;
import entidades.Mesa;
import enums.EstadoComanda;
import excepciones.PersistenciaException;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author keppler
 */

public class ComandaDAOTest {

    public ComandaDAOTest() {
    }

    @Test
    public void testAgregarComandaExito() throws PersistenciaException {
        ComandaDAO dao = new ComandaDAO();
        
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        Mesa mesa = new Mesa(7001);
        em.persist(mesa);
        ClienteFrecuente panfilo = new ClienteFrecuente("Panfilo", "6666535666", "panf@homai.com");
        em.persist(panfilo);
        em.getTransaction().commit();
        em.close();

        Comanda comanda = new Comanda("COM-001", LocalDateTime.now(), mesa, panfilo);
        comanda.setEstado(EstadoComanda.ABIERTA);
        comanda.setTotal(0.0);
        
        Comanda resultado = dao.agregarComanda(comanda);
        
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("COM-001", resultado.getFolio());
        assertEquals(EstadoComanda.ABIERTA, resultado.getEstado());
        assertEquals("Panfilo", resultado.getCliente().getNombre());
    }

    @Test
    public void testAgregarComandaFolioDuplicado() throws PersistenciaException {
        ComandaDAO dao = new ComandaDAO();
        
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        Mesa mesa = new Mesa(7002);
        em.persist(mesa);
        ClienteFrecuente cliente = new ClienteFrecuente("Cliente Repetido", "6449876543", "repe@homai.com");
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();

        Comanda c1 = new Comanda("COM-002", LocalDateTime.now(), mesa, cliente);
        c1.setEstado(EstadoComanda.ABIERTA);
        c1.setTotal(0.0);
        dao.agregarComanda(c1);

        Comanda c2 = new Comanda("COM-002", LocalDateTime.now(), mesa, cliente);
        c2.setEstado(EstadoComanda.ABIERTA);
        c2.setTotal(0.0);
        
        assertThrows(PersistenciaException.class, () -> dao.agregarComanda(c2));
    }

    @Test
    public void testActualizarComandaExito() throws PersistenciaException {
        ComandaDAO dao = new ComandaDAO();
        
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        Mesa mesa = new Mesa(7003);
        em.persist(mesa);
        ClienteFrecuente cliente = new ClienteFrecuente("Cliente Actualizar", "6621112233", "actualiza@homai.com");
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();

        Comanda comanda = new Comanda("COM-003", LocalDateTime.now(), mesa, cliente);
        comanda.setEstado(EstadoComanda.ABIERTA);
        comanda.setTotal(0.0);
        Comanda agregada = dao.agregarComanda(comanda);
        
        agregada.setEstado(EstadoComanda.ENTREGADA);
        agregada.setTotal(350.0);
        Comanda actualizada = dao.actualizar(agregada);
        
        assertNotNull(actualizada);
        assertEquals(EstadoComanda.ENTREGADA, actualizada.getEstado());
        assertEquals(350.0, actualizada.getTotal());
    }

    @Test
    public void testBuscarPorIdExito() throws PersistenciaException {
        ComandaDAO dao = new ComandaDAO();
        
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        Mesa mesa = new Mesa(7004);
        em.persist(mesa);
        ClienteFrecuente cliente = new ClienteFrecuente("Cliente Buscar", "5554443322", "buscar@homai.com");
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();

        Comanda comanda = new Comanda("COM-004", LocalDateTime.now(), mesa, cliente);
        comanda.setEstado(EstadoComanda.ABIERTA);
        comanda.setTotal(0.0);
        Comanda agregada = dao.agregarComanda(comanda);
        
        Comanda resultado = dao.buscarPorId(agregada.getId());
        
        assertNotNull(resultado);
        assertEquals(agregada.getId(), resultado.getId());
        assertEquals("COM-004", resultado.getFolio());
    }

    @Test
    public void testBuscarPorIdInexistente() throws PersistenciaException {
        ComandaDAO dao = new ComandaDAO();
        Comanda resultado = dao.buscarPorId(999999L);
        assertNull(resultado);
    }

    @Test
    public void testBuscarPorFolioExito() throws PersistenciaException {
        ComandaDAO dao = new ComandaDAO();
        
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        Mesa mesa = new Mesa(7005);
        em.persist(mesa);
        ClienteFrecuente cliente = new ClienteFrecuente("Cliente Folio", "8115556677", "folio@homai.com");
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();

        Comanda comanda = new Comanda("COM-005", LocalDateTime.now(), mesa, cliente);
        comanda.setEstado(EstadoComanda.ABIERTA);
        comanda.setTotal(0.0);
        dao.agregarComanda(comanda);
        
        Comanda resultado = dao.buscarPorFolio("COM-005");
        
        assertNotNull(resultado);
        assertEquals("COM-005", resultado.getFolio());
    }

    @Test
    public void testBuscarPorFolioInexistente() throws PersistenciaException {
        ComandaDAO dao = new ComandaDAO();
        Comanda resultado = dao.buscarPorFolio("NO-EXISTE");
        assertNull(resultado);
    }

    @Test
    public void testContarComandasDelDia() throws PersistenciaException {
        ComandaDAO dao = new ComandaDAO();
        
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        Mesa mesa = new Mesa(7006);
        em.persist(mesa);
        ClienteFrecuente cliente = new ClienteFrecuente("Cliente Conteo", "3337778899", "conteo@homai.com");
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();

        int antes = dao.contarComandasDelDia(LocalDateTime.now());
        
        Comanda comanda = new Comanda("COM-006", LocalDateTime.now(), mesa, cliente);
        comanda.setEstado(EstadoComanda.ABIERTA);
        comanda.setTotal(0.0);
        dao.agregarComanda(comanda);
        
        int despues = dao.contarComandasDelDia(LocalDateTime.now());
        
        assertEquals(antes + 1, despues);
    }

    @Test
    public void testBuscarPorRangoFechasExito() throws PersistenciaException {
        ComandaDAO dao = new ComandaDAO();
        
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        Mesa mesa = new Mesa(7007);
        em.persist(mesa);
        ClienteFrecuente cliente = new ClienteFrecuente("Cliente Rango", "4429990011", "rango@homai.com");
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();

        Comanda comanda = new Comanda("COM-007", LocalDateTime.now(), mesa, cliente);
        comanda.setEstado(EstadoComanda.ABIERTA);
        comanda.setTotal(0.0);
        dao.agregarComanda(comanda);
        
        LocalDateTime desde = LocalDateTime.now().minusDays(1);
        LocalDateTime hasta = LocalDateTime.now().plusDays(1);
        List<Comanda> resultado = dao.buscarPorRangoFechas(desde, hasta);
        
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorRangoFechasSinResultados() throws PersistenciaException {
        ComandaDAO dao = new ComandaDAO();
        
        LocalDateTime desde = LocalDateTime.now().minusYears(10);
        LocalDateTime hasta = LocalDateTime.now().minusYears(9);
        List<Comanda> resultado = dao.buscarPorRangoFechas(desde, hasta);
        
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testMesaTieneComandaAbiertaTrue() throws PersistenciaException {
        ComandaDAO dao = new ComandaDAO();
        
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        Mesa mesa = new Mesa(7008);
        em.persist(mesa);
        ClienteFrecuente cliente = new ClienteFrecuente("Cliente Ocupa", "2223334455", "ocupa@homai.com");
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();

        Comanda comanda = new Comanda("COM-008", LocalDateTime.now(), mesa, cliente);
        comanda.setEstado(EstadoComanda.ABIERTA);
        comanda.setTotal(0.0);
        dao.agregarComanda(comanda);
        
        boolean resultado = dao.mesaTieneComandaAbierta(mesa.getId());
        
        assertTrue(resultado);
    }

    @Test
    public void testMesaTieneComandaAbiertaFalse() throws PersistenciaException {
        ComandaDAO dao = new ComandaDAO();
        
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        Mesa mesa = new Mesa(7009);
        em.persist(mesa);
        ClienteFrecuente cliente = new ClienteFrecuente("Cliente Libre", "6145554433", "libre@homai.com");
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();

        Comanda comanda = new Comanda("COM-009", LocalDateTime.now(), mesa, cliente);
        comanda.setEstado(EstadoComanda.ENTREGADA);
        comanda.setTotal(100.0);
        dao.agregarComanda(comanda);
        
        boolean resultado = dao.mesaTieneComandaAbierta(mesa.getId());
        
        assertFalse(resultado);
    }
}