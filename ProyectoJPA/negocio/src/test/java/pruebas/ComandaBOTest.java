/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pruebas;

import BOs.ComandaBO;
import DAOs.MesaDAO;
import com.dtos.ComandaDTO;
import conexion.ConexionBD;
import entidades.ClienteFrecuente;
import entidades.Comanda;
import entidades.Ingrediente;
import entidades.Mesa;
import entidades.Producto;
import entidades.ProductoIngrediente;
import enums.EstadoComanda;
import enums.TipoProducto;
import enums.UnidadDeMedida;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para ComandaBO
 * @author joser
 */
public class ComandaBOTest {

    public ComandaBOTest() {
    }

    @Test
    public void testAbrirComandaExito() throws NegocioException {
        ComandaBO bo = new ComandaBO();
        Mesa mesa = new MesaDAO().agregar(new Mesa(40001));
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        ClienteFrecuente cliente = new ClienteFrecuente("cliente-ab", "6441112222", "ab@test.com");
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();
        ComandaDTO dto = new ComandaDTO();
        dto.setIdMesa(mesa.getId());
        dto.setIdCliente(cliente.getId());
        Comanda resultado = bo.abrirComanda(dto);
        assertNotNull(resultado);
        assertNotNull(resultado.getFolio());
        assertTrue(resultado.getFolio().startsWith("OB-"));
        assertEquals(EstadoComanda.ABIERTA, resultado.getEstado());
        assertEquals(0.0, resultado.getTotal());
    }

    @Test
    public void testAbrirComandaDtoNulo() {
        ComandaBO bo = new ComandaBO();
        assertThrows(NegocioException.class, () -> bo.abrirComanda(null));
    }

    @Test
    public void testAbrirComandaSinMesa() {
        ComandaBO bo = new ComandaBO();
        ComandaDTO dto = new ComandaDTO();
        assertThrows(NegocioException.class, () -> bo.abrirComanda(dto));
    }

    @Test
    public void testAbrirComandaMesaOcupada() throws NegocioException {
        ComandaBO bo = new ComandaBO();
        Mesa mesa = new MesaDAO().agregar(new Mesa(40003));
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        ClienteFrecuente cliente = new ClienteFrecuente("cliente-oc", "6441112222", "oc@test.com");
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();
        ComandaDTO dto = new ComandaDTO();
        dto.setIdMesa(mesa.getId());
        dto.setIdCliente(cliente.getId());
        bo.abrirComanda(dto);
        ComandaDTO dto2 = new ComandaDTO();
        dto2.setIdMesa(mesa.getId());
        assertThrows(NegocioException.class, () -> bo.abrirComanda(dto2));
    }

    @Test
    public void testAgregarDetalleProductoExito() throws NegocioException {
        ComandaBO bo = new ComandaBO();
        Mesa mesa = new MesaDAO().agregar(new Mesa(40004));
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        ClienteFrecuente cliente = new ClienteFrecuente("cliente-adp", "6441112222", "adp@test.com");
        em.persist(cliente);
        Ingrediente ing = new Ingrediente(null, "ing-adp", UnidadDeMedida.GRAMO, 100.0, null);
        em.persist(ing);
        Producto p = new Producto("prod-adp", "desc", 50.0, TipoProducto.PLATILLO);
        em.persist(p);
        ProductoIngrediente pi = new ProductoIngrediente(1.0, p, ing);
        em.persist(pi);
        em.getTransaction().commit();
        em.close();
        ComandaDTO dto = new ComandaDTO();
        dto.setIdMesa(mesa.getId());
        dto.setIdCliente(cliente.getId());
        Comanda comanda = bo.abrirComanda(dto);
        assertDoesNotThrow(() -> bo.agregarDetalleProducto(comanda.getId(), p.getId(), 2, "sin cebolla"));
    }

    @Test
public void testAgregarDetalleProductoSinStock() throws NegocioException {
    ComandaBO bo = new ComandaBO();
    Mesa mesa = new MesaDAO().agregar(new Mesa(40006));
    EntityManager em = ConexionBD.crearConexion();
    em.getTransaction().begin();
    ClienteFrecuente cliente = new ClienteFrecuente("cliente-ss", "6441112222", "ss@test.com");
    em.persist(cliente);
    Ingrediente ing = new Ingrediente(null, "ing-ss", UnidadDeMedida.GRAMO, 0.0, null);
    em.persist(ing);
    Producto p = new Producto("prod-ss", "desc", 50.0, TipoProducto.PLATILLO);
    em.persist(p);
    ProductoIngrediente pi = new ProductoIngrediente(1.0, p, ing);
    em.persist(pi);
    p.getIngredientes().add(pi);   
    em.getTransaction().commit();
    em.close();
    
    ComandaDTO dto = new ComandaDTO();
    dto.setIdMesa(mesa.getId());
    dto.setIdCliente(cliente.getId());
    Comanda comanda = bo.abrirComanda(dto);
    
    assertThrows(NegocioException.class,
            () -> bo.agregarDetalleProducto(comanda.getId(), p.getId(), 1, null));
}

    @Test
    public void testEntregarComandaSinDetallesFalla() throws NegocioException {
        ComandaBO bo = new ComandaBO();
        Mesa mesa = new MesaDAO().agregar(new Mesa(40007));
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        ClienteFrecuente cliente = new ClienteFrecuente("cliente-esd", "6441112222", "esd@test.com");
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();
        ComandaDTO dto = new ComandaDTO();
        dto.setIdMesa(mesa.getId());
        dto.setIdCliente(cliente.getId());
        Comanda comanda = bo.abrirComanda(dto);
        assertThrows(NegocioException.class, () -> bo.entregarComanda(comanda.getId()));
    }

    @Test
    public void testEntregarComandaExito() throws NegocioException {
        ComandaBO bo = new ComandaBO();
        Mesa mesa = new MesaDAO().agregar(new Mesa(40008));
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        ClienteFrecuente cliente = new ClienteFrecuente("cliente-ex", "6441112222", "ex@test.com");
        em.persist(cliente);
        Ingrediente ing = new Ingrediente(null, "ing-ex", UnidadDeMedida.GRAMO, 100.0, null);
        em.persist(ing);
        Producto p = new Producto("prod-ex", "desc", 50.0, TipoProducto.PLATILLO);
        em.persist(p);
        ProductoIngrediente pi = new ProductoIngrediente(1.0, p, ing);
        em.persist(pi);
        em.getTransaction().commit();
        em.close();
        ComandaDTO dto = new ComandaDTO();
        dto.setIdMesa(mesa.getId());
        dto.setIdCliente(cliente.getId());
        Comanda comanda = bo.abrirComanda(dto);
        bo.agregarDetalleProducto(comanda.getId(), p.getId(), 2, null);
        assertDoesNotThrow(() -> bo.entregarComanda(comanda.getId()));
        ComandaDTO actualizada = bo.buscarPorId(comanda.getId());
        assertEquals("ENTREGADA", actualizada.getEstado());
        assertTrue(actualizada.getTotal() > 0);
    }

    @Test
    public void testCancelarComandaExito() throws NegocioException {
        ComandaBO bo = new ComandaBO();
        Mesa mesa = new MesaDAO().agregar(new Mesa(40010));
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        ClienteFrecuente cliente = new ClienteFrecuente("cliente-can", "6441112222", "can@test.com");
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();
        ComandaDTO dto = new ComandaDTO();
        dto.setIdMesa(mesa.getId());
        dto.setIdCliente(cliente.getId());
        Comanda comanda = bo.abrirComanda(dto);
        assertDoesNotThrow(() -> bo.cancelarComanda(comanda.getId()));
        ComandaDTO actualizada = bo.buscarPorId(comanda.getId());
        assertEquals("CANCELADA", actualizada.getEstado());
    }

    @Test
    public void testBuscarPorRangoFechasExito() throws NegocioException {
        ComandaBO bo = new ComandaBO();
        Mesa mesa = new MesaDAO().agregar(new Mesa(40011));
        EntityManager em = ConexionBD.crearConexion();
        em.getTransaction().begin();
        ClienteFrecuente cliente = new ClienteFrecuente("cliente-rg", "6441112222", "rg@test.com");
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();
        ComandaDTO dto = new ComandaDTO();
        dto.setIdMesa(mesa.getId());
        dto.setIdCliente(cliente.getId());
        bo.abrirComanda(dto);
        List<ComandaDTO> resultado = bo.buscarPorRangoFechas(
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }
}
