//package pruebas;
//
//import DAOs.ComboDAO;
//import DAOs.ComboProductoDAO;
//import conexion.ConexionBD;
//import entidades.Combo;
//import entidades.ComboProducto;
//import entidades.Producto;
//import enums.TipoProducto;
//import excepciones.PersistenciaException;
//import javax.persistence.EntityManager;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.List;
//
//public class ComboProductoDAOTest {
//
//    public ComboProductoDAOTest() {
//    }
//
//    // Método auxiliar para crear un producto real en la BD
//    private Producto crearProducto(String nombre) {
//        EntityManager em = ConexionBD.crearConexion();
//        try {
//            em.getTransaction().begin();
//            Producto p = new Producto(nombre, "desc", 50.0, TipoProducto.PLATILLO);
//            em.persist(p);
//            em.getTransaction().commit();
//            return p;
//        } finally {
//            em.close();
//        }
//    }
//
//    @Test
//    public void testAgregarComboProductoExito() throws PersistenciaException {
//        ComboDAO comboDao = new ComboDAO();
//        Combo combo = new Combo("Combo prueba", "c", 100.0, 80.0, 20);
//        Combo comboAgregado = comboDao.agregarCombo(combo);
//
//        Producto p = crearProducto("prod-test-" + System.nanoTime());
//        ComboProductoDAO dao = new ComboProductoDAO();
//
//        ComboProducto resultado = dao.agregar(comboAgregado.getId(), p.getId(), 2);
//
//        assertNotNull(resultado);
//        assertNotNull(resultado.getId());
//        assertNotNull(resultado.getCombo());
//        assertNotNull(resultado.getProducto());
//        assertEquals(comboAgregado.getId(), resultado.getCombo().getId());
//        assertEquals(p.getId(), resultado.getProducto().getId());
//        assertEquals(2, resultado.getCantidad());
//    }
//
//    @Test
//    public void testAgregarComboProductoComboInexistente() throws PersistenciaException {
//        Producto p = crearProducto("prod-test-" + System.nanoTime());
//        ComboProductoDAO dao = new ComboProductoDAO();
//
//        assertThrows(PersistenciaException.class, () -> dao.agregar(999999L, p.getId(), 2));
//    }
//
//    @Test
//    public void testAgregarComboProductoProductoInexistente() throws PersistenciaException {
//        ComboDAO comboDao = new ComboDAO();
//        Combo combo = comboDao.agregarCombo(new Combo("Combo prueba", "c", 100.0, 80.0, 20));
//        ComboProductoDAO dao = new ComboProductoDAO();
//
//        assertThrows(PersistenciaException.class, () -> dao.agregar(combo.getId(), 999999L, 2));
//    }
//
//    @Test
//    public void testObtenerPorComboExito() throws PersistenciaException {
//        ComboDAO comboDao = new ComboDAO();
//        Combo combo = comboDao.agregarCombo(new Combo("Combo prueba", "c", 100.0, 80.0, 20));
//
//        Producto p1 = crearProducto("prod-test1-" + System.nanoTime());
//        Producto p2 = crearProducto("prod-test2-" + System.nanoTime());
//
//        ComboProductoDAO dao = new ComboProductoDAO();
//        dao.agregar(combo.getId(), p1.getId(), 1);
//        dao.agregar(combo.getId(), p2.getId(), 2);
//
//        List<ComboProducto> resultado = dao.obtenerPorCombo(combo.getId());
//
//        assertNotNull(resultado);
//        assertEquals(2, resultado.size());
//    }
//
//    @Test
//    public void testObtenerPorComboIdInexistente() throws PersistenciaException {
//        ComboProductoDAO dao = new ComboProductoDAO();
//        List<ComboProducto> resultado = dao.obtenerPorCombo(852L);
//
//        assertNotNull(resultado);
//        assertTrue(resultado.isEmpty());
//    }
//}