package inserts;

import BOs.ClienteBO;
import DAOs.MesaDAO;
import com.dtos.ClienteDTO;
import com.dtos.ClienteFrecuenteDTO;
import java.util.Date;
import java.util.logging.Logger;

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

            LOG.info("InsertDatos completado.");
        } catch (Exception e) {
            LOG.warning("Error en InsertDatos: " + e.getMessage());
        }
    }
}