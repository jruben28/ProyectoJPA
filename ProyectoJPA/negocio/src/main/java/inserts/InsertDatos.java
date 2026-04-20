package inserts;

import BOs.ClienteBO;
import DAOs.MesaDAO;
import com.dtos.ClienteDTO;
import com.dtos.ClienteFrecuenteDTO;
import conexion.ConexionBD;
import entidades.Combo;
import entidades.ComboProducto;
import entidades.Ingrediente;
import entidades.Producto;
import entidades.ProductoIngrediente;
import enums.TipoProducto;
import enums.UnidadDeMedida;
import excepciones.PersistenciaException;
import java.util.Date;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class InsertDatos {

    private static final Logger LOG = Logger.getLogger(InsertDatos.class.getName());

    public static void main(String[] args) {
        try {
            new MesaDAO().cargaMasiva(20);
            LOG.info("20 mesas cargadas.");

            ClienteBO clienteBO = new ClienteBO();
            ClienteDTO clienteGeneral = clienteBO.obtenerClienteGeneral();
            if (clienteGeneral == null) {
                clienteGeneral = clienteBO.crearClienteGeneral();
                LOG.info("Cliente General creado con ID: " + clienteGeneral.getId());
            } else {
                LOG.info("Cliente General ya existe con ID: " + clienteGeneral.getId());
            }

            Date hoy = new Date();

            ClienteFrecuenteDTO c1 = new ClienteFrecuenteDTO();
            c1.setNombre("Juan Perez");
            c1.setTelefono("6441234567");
            c1.setCorreo("juan.perez@mail.com");
            c1.setFechaRegistro(hoy);
            clienteBO.agregarClienteFrecuente(c1);

            ClienteFrecuenteDTO c2 = new ClienteFrecuenteDTO();
            c2.setNombre("Maria Garcia");
            c2.setTelefono("6449876543");
            c2.setCorreo("m.garcia@outlook.com");
            c2.setFechaRegistro(hoy);
            clienteBO.agregarClienteFrecuente(c2);

            ClienteFrecuenteDTO c3 = new ClienteFrecuenteDTO();
            c3.setNombre("Carlos Mendoza");
            c3.setTelefono("6621112233");
            c3.setCorreo("carlos.m@empresa.mx");
            c3.setFechaRegistro(hoy);
            clienteBO.agregarClienteFrecuente(c3);

            ClienteFrecuenteDTO c4 = new ClienteFrecuenteDTO();
            c4.setNombre("Ana Lucia Torres");
            c4.setTelefono("5554443322");
            c4.setCorreo("ana.torres@gmail.com");
            c4.setFechaRegistro(hoy);
            clienteBO.agregarClienteFrecuente(c4);

            ClienteFrecuenteDTO c5 = new ClienteFrecuenteDTO();
            c5.setNombre("Roberto Jimenez");
            c5.setTelefono("8115556677");
            c5.setCorreo("roberto.j@servicios.com");
            c5.setFechaRegistro(hoy);
            clienteBO.agregarClienteFrecuente(c5);

            ClienteFrecuenteDTO c6 = new ClienteFrecuenteDTO();
            c6.setNombre("Sofia Castro");
            c6.setTelefono("3337778899");
            c6.setCorreo("sofi.castro@web.com");
            c6.setFechaRegistro(hoy);
            clienteBO.agregarClienteFrecuente(c6);

            ClienteFrecuenteDTO c7 = new ClienteFrecuenteDTO();
            c7.setNombre("Diego Solis");
            c7.setTelefono("4429990011");
            c7.setCorreo("diego.solis@proyectos.net");
            c7.setFechaRegistro(hoy);
            clienteBO.agregarClienteFrecuente(c7);

            ClienteFrecuenteDTO c8 = new ClienteFrecuenteDTO();
            c8.setNombre("Lucia Mendez");
            c8.setTelefono("2223334455");
            c8.setCorreo("lucia.mendez@it.com");
            c8.setFechaRegistro(hoy);
            clienteBO.agregarClienteFrecuente(c8);

            ClienteFrecuenteDTO c9 = new ClienteFrecuenteDTO();
            c9.setNombre("Fernando Ruiz");
            c9.setTelefono("6145554433");
            c9.setCorreo("fruiz@ingenieria.com");
            c9.setFechaRegistro(hoy);
            clienteBO.agregarClienteFrecuente(c9);

            ClienteFrecuenteDTO c10 = new ClienteFrecuenteDTO();
            c10.setNombre("Elena Villalobos");
            c10.setTelefono("9991112233");
            c10.setCorreo("elena.villa@academia.edu");
            c10.setFechaRegistro(hoy);
            clienteBO.agregarClienteFrecuente(c10);

            LOG.info("Clientes cargados.");

            EntityManager em = ConexionBD.crearConexion();
            em.getTransaction().begin();

            Ingrediente tortilla  = new Ingrediente(null, "Tortilla de maiz",    UnidadDeMedida.PIEZA,     200.0,   null);
            Ingrediente carne     = new Ingrediente(null, "Carne de res",         UnidadDeMedida.GRAMO,     5000.0,  null);
            Ingrediente queso     = new Ingrediente(null, "Queso rallado",        UnidadDeMedida.GRAMO,     2000.0,  null);
            Ingrediente pan       = new Ingrediente(null, "Pan de hamburguesa",   UnidadDeMedida.PIEZA,     100.0,   null);
            Ingrediente refresco  = new Ingrediente(null, "Refresco 355ml",       UnidadDeMedida.MILILITRO, 10000.0, null);
            Ingrediente helado    = new Ingrediente(null, "Helado de vainilla",   UnidadDeMedida.GRAMO,     3000.0,  null);
            Ingrediente panHot    = new Ingrediente(null, "Pan para hotdog",      UnidadDeMedida.PIEZA,     80.0,    null);
            Ingrediente salchicha = new Ingrediente(null, "Salchicha",            UnidadDeMedida.PIEZA,     150.0,   null);

            em.persist(tortilla);
            em.persist(carne);
            em.persist(queso);
            em.persist(pan);
            em.persist(refresco);
            em.persist(helado);
            em.persist(panHot);
            em.persist(salchicha);

            Producto taco = new Producto("Taco de res", "Taco con carne asada y queso", 30.0, TipoProducto.PLATILLO);
            em.persist(taco);
            em.persist(new ProductoIngrediente(2.0,  taco, tortilla));
            em.persist(new ProductoIngrediente(80.0, taco, carne));
            em.persist(new ProductoIngrediente(15.0, taco, queso));

            Producto hamburguesa = new Producto("Hamburguesa clasica", "Hamburguesa con carne y queso", 75.0, TipoProducto.PLATILLO);
            em.persist(hamburguesa);
            em.persist(new ProductoIngrediente(1.0,   hamburguesa, pan));
            em.persist(new ProductoIngrediente(150.0, hamburguesa, carne));
            em.persist(new ProductoIngrediente(20.0,  hamburguesa, queso));

            Producto hotdog = new Producto("Hotdog", "Hotdog con salchicha", 40.0, TipoProducto.PLATILLO);
            em.persist(hotdog);
            em.persist(new ProductoIngrediente(1.0, hotdog, panHot));
            em.persist(new ProductoIngrediente(1.0, hotdog, salchicha));

            Producto refrescoP = new Producto("Refresco", "Refresco de 355ml", 25.0, TipoProducto.BEBIDA);
            em.persist(refrescoP);
            em.persist(new ProductoIngrediente(355.0, refrescoP, refresco));

            Producto postre = new Producto("Copa de helado", "Copa de helado de vainilla", 45.0, TipoProducto.POSTRE);
            em.persist(postre);
            em.persist(new ProductoIngrediente(150.0, postre, helado));

            Combo combo1 = new Combo("Combo Taquero", "Dos tacos de res con refresco", 85.0, 70.0, 17);
            em.persist(combo1);
            em.persist(new ComboProducto(combo1, taco, 2));
            em.persist(new ComboProducto(combo1, refrescoP, 1));

            Combo combo2 = new Combo("Combo Hamburguesa", "Hamburguesa clasica con refresco", 100.0, 85.0, 15);
            em.persist(combo2);
            em.persist(new ComboProducto(combo2, hamburguesa, 1));
            em.persist(new ComboProducto(combo2, refrescoP, 1));

            Combo combo3 = new Combo("Combo Hotdog", "Hotdog con refresco", 65.0, 55.0, 15);
            em.persist(combo3);
            em.persist(new ComboProducto(combo3, hotdog, 1));
            em.persist(new ComboProducto(combo3, refrescoP, 1));

            Combo combo4 = new Combo("Combo Familiar", "Dos hamburguesas con descuento", 150.0, 125.0, 16);
            em.persist(combo4);
            em.persist(new ComboProducto(combo4, hamburguesa, 2));

            Combo combo5 = new Combo("Combo Postre", "Taco con copa de helado", 75.0, 60.0, 20);
            combo5.setActivo(false);
            em.persist(combo5);
            em.persist(new ComboProducto(combo5, taco, 1));
            em.persist(new ComboProducto(combo5, postre, 1));

            em.getTransaction().commit();
            em.close();

            LOG.info("InsertDatos completado.");
        } catch (PersistenciaException e) {
            LOG.warning("Error en InsertDatos: " + e.getMessage());
        }
    }
}