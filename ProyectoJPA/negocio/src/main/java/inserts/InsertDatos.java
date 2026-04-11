/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package inserts;

import BOs.ClienteBO;
import BOs.ComandaBO;
import interfaces.IClienteBO;
import interfaces.IComandaBO;
import com.dtos.ClienteFrecuenteDTO;
import com.dtos.ComandaDTO;
import java.util.Date;

/**
 *
 * @author joser
 */
public class InsertDatos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Date fechaHoy = new Date();

        IClienteBO clienteBO = new ClienteBO();
        IComandaBO comandaBO = new ComandaBO();
        
        // 1
        ClienteFrecuenteDTO cliente1 = new ClienteFrecuenteDTO();
        cliente1.setNombre("Juan Pérez");
        cliente1.setTelefono("6441234567");
        cliente1.setCorreo("juan.perez@mail.com");
        cliente1.setFechaRegistro(fechaHoy);
        

        // 2
        ClienteFrecuenteDTO cliente2 = new ClienteFrecuenteDTO();
        cliente2.setNombre("María García");
        cliente2.setTelefono("6449876543");
        cliente2.setCorreo("m.garcia@outlook.com");
        cliente2.setFechaRegistro(fechaHoy);

        // 3
        ClienteFrecuenteDTO cliente3 = new ClienteFrecuenteDTO();
        cliente3.setNombre("Carlos Mendoza");
        cliente3.setTelefono("6621112233");
        cliente3.setCorreo("carlos.m@empresa.mx");
        cliente3.setFechaRegistro(fechaHoy);

        // 4
        ClienteFrecuenteDTO cliente4 = new ClienteFrecuenteDTO();
        cliente4.setNombre("Ana Lucía Torres");
        cliente4.setTelefono("5554443322");
        cliente4.setCorreo("ana.torres@gmail.com");
        cliente4.setFechaRegistro(fechaHoy);

        // 5
        ClienteFrecuenteDTO cliente5 = new ClienteFrecuenteDTO();
        cliente5.setNombre("Roberto Jiménez");
        cliente5.setTelefono("8115556677");
        cliente5.setCorreo("roberto.j@servicios.com");
        cliente5.setFechaRegistro(fechaHoy);

        // 6
        ClienteFrecuenteDTO cliente6 = new ClienteFrecuenteDTO();
        cliente6.setNombre("Sofía Castro");
        cliente6.setTelefono("3337778899");
        cliente6.setCorreo("sofi.castro@web.com");
        cliente6.setFechaRegistro(fechaHoy);

        // 7
        ClienteFrecuenteDTO cliente7 = new ClienteFrecuenteDTO();
        cliente7.setNombre("Diego Armando Solís");
        cliente7.setTelefono("4429990011");
        cliente7.setCorreo("diego.solis@proyectos.net");
        cliente7.setFechaRegistro(fechaHoy);

        // 8
        ClienteFrecuenteDTO cliente8 = new ClienteFrecuenteDTO();
        cliente8.setNombre("Lucía Méndez");
        cliente8.setTelefono("2223334455");
        cliente8.setCorreo("lucia.mendez@it.com");
        cliente8.setFechaRegistro(fechaHoy);

        // 9
        ClienteFrecuenteDTO cliente9 = new ClienteFrecuenteDTO();
        cliente9.setNombre("Fernando Ruiz");
        cliente9.setTelefono("6145554433");
        cliente9.setCorreo("fruiz@ingenieria.com");
        cliente9.setFechaRegistro(fechaHoy);

        // 10
        ClienteFrecuenteDTO cliente10 = new ClienteFrecuenteDTO();
        cliente10.setNombre("Elena Villalobos");
        cliente10.setTelefono("9991112233");
        cliente10.setCorreo("elena.villa@academia.edu");
        cliente10.setFechaRegistro(fechaHoy);

        // 11
        ClienteFrecuenteDTO cliente11 = new ClienteFrecuenteDTO();
        cliente11.setNombre("Ricardo Salinas");
        cliente11.setTelefono("6678889900");
        cliente11.setCorreo("rsalinas@negocios.com");
        cliente11.setFechaRegistro(fechaHoy);

        // 12
        ClienteFrecuenteDTO cliente12 = new ClienteFrecuenteDTO();
        cliente12.setNombre("Gabriela Ortiz");
        cliente12.setTelefono("7774445566");
        cliente12.setCorreo("gaby.ortiz@diseno.com");
        cliente12.setFechaRegistro(fechaHoy);

        // 13
        ClienteFrecuenteDTO cliente13 = new ClienteFrecuenteDTO();
        cliente13.setNombre("Hugo Sánchez");
        cliente13.setTelefono("5510203040");
        cliente13.setCorreo("hugo.s@deportes.mx");
        cliente13.setFechaRegistro(fechaHoy);

        // 14
        ClienteFrecuenteDTO cliente14 = new ClienteFrecuenteDTO();
        cliente14.setNombre("Patricia Luna");
        cliente14.setTelefono("8182838485");
        cliente14.setCorreo("paty.luna@legal.com");
        cliente14.setFechaRegistro(fechaHoy);

        // 15
        ClienteFrecuenteDTO cliente15 = new ClienteFrecuenteDTO();
        cliente15.setNombre("Oscar Wilde");
        cliente15.setTelefono("6445550011");
        cliente15.setCorreo("oscar.w@literatura.com");
        cliente15.setFechaRegistro(fechaHoy);

        // 16
        ClienteFrecuenteDTO cliente16 = new ClienteFrecuenteDTO();
        cliente16.setNombre("Mónica Naranjo");
        cliente16.setTelefono("3312345678");
        cliente16.setCorreo("monica.n@musica.es");
        cliente16.setFechaRegistro(fechaHoy);

        // 17
        ClienteFrecuenteDTO cliente17 = new ClienteFrecuenteDTO();
        cliente17.setNombre("Alberto Instain");
        cliente17.setTelefono("6629871234");
        cliente17.setCorreo("alberto.i@ciencia.org");
        cliente17.setFechaRegistro(fechaHoy);

        // 18
        ClienteFrecuenteDTO cliente18 = new ClienteFrecuenteDTO();
        cliente18.setNombre("Carmen Aristegui");
        cliente18.setTelefono("5544332211");
        cliente18.setCorreo("carmen.a@noticias.mx");
        cliente18.setFechaRegistro(fechaHoy);

        // 19
        ClienteFrecuenteDTO cliente19 = new ClienteFrecuenteDTO();
        cliente19.setNombre("Jorge Ramos");
        cliente19.setTelefono("3056667788");
        cliente19.setCorreo("jorge.r@prensa.com");
        cliente19.setFechaRegistro(fechaHoy);

        // 20
        ClienteFrecuenteDTO cliente20 = new ClienteFrecuenteDTO();
        cliente20.setNombre("Natalia Lafourcade");
        cliente20.setTelefono("5521213232");
        cliente20.setCorreo("natalia.l@artistas.com");
        cliente20.setFechaRegistro(fechaHoy);

        try {
            clienteBO.agregarClienteFrecuente(cliente1);
            clienteBO.agregarClienteFrecuente(cliente2);
            clienteBO.agregarClienteFrecuente(cliente3);
            clienteBO.agregarClienteFrecuente(cliente4);
            clienteBO.agregarClienteFrecuente(cliente5);
            clienteBO.agregarClienteFrecuente(cliente6);
            clienteBO.agregarClienteFrecuente(cliente7);
            clienteBO.agregarClienteFrecuente(cliente8);
            clienteBO.agregarClienteFrecuente(cliente9);
            clienteBO.agregarClienteFrecuente(cliente10);
            clienteBO.agregarClienteFrecuente(cliente11);
            clienteBO.agregarClienteFrecuente(cliente12);
            clienteBO.agregarClienteFrecuente(cliente13);
            clienteBO.agregarClienteFrecuente(cliente14);
            clienteBO.agregarClienteFrecuente(cliente15);
            clienteBO.agregarClienteFrecuente(cliente16);
            clienteBO.agregarClienteFrecuente(cliente17);
            clienteBO.agregarClienteFrecuente(cliente18);
            clienteBO.agregarClienteFrecuente(cliente19);
            clienteBO.agregarClienteFrecuente(cliente20);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        

        ComandaDTO comanda1 = new ComandaDTO("ENTREGADA", 450.50, 4L);
        ComandaDTO comanda2 = new ComandaDTO("PENDIENTE", 120.00, 4L);
        ComandaDTO comanda3 = new ComandaDTO("ENTREGADA", 890.99, 4L);
        ComandaDTO comanda4 = new ComandaDTO("PENDIENTE", 55.50, 4L);
        ComandaDTO comanda5 = new ComandaDTO("ENTREGADA", 320.00, 4L);

        ComandaDTO comanda6 = new ComandaDTO("PENDIENTE", 210.75, 6L);
        ComandaDTO comanda7 = new ComandaDTO("ENTREGADA", 1500.00, 7L);
        ComandaDTO comanda8 = new ComandaDTO("PENDIENTE", 45.00, 8L);
        ComandaDTO comanda9 = new ComandaDTO("ENTREGADA", 675.25, 9L);
        ComandaDTO comanda10 = new ComandaDTO("PENDIENTE", 12.99, 10L);
        ComandaDTO comanda11 = new ComandaDTO("ENTREGADA", 99.90, 11L);
        ComandaDTO comanda12 = new ComandaDTO("PENDIENTE", 430.40, 12L);
        ComandaDTO comanda13 = new ComandaDTO("ENTREGADA", 115.00, 13L);
        ComandaDTO comanda14 = new ComandaDTO("PENDIENTE", 89.00, 14L);
        ComandaDTO comanda15 = new ComandaDTO("ENTREGADA", 2200.50, 15L);
        ComandaDTO comanda16 = new ComandaDTO("PENDIENTE", 315.20, 16L);
        ComandaDTO comanda17 = new ComandaDTO("ENTREGADA", 60.00, 17L);
        ComandaDTO comanda18 = new ComandaDTO("PENDIENTE", 185.30, 18L);
        ComandaDTO comanda19 = new ComandaDTO("ENTREGADA", 740.00, 19L);
        ComandaDTO comanda20 = new ComandaDTO("PENDIENTE", 510.15, 20L);
        
        try{
            comandaBO.agregarComanda(comanda1);
            comandaBO.agregarComanda(comanda2);
            comandaBO.agregarComanda(comanda3);
            comandaBO.agregarComanda(comanda4);
            comandaBO.agregarComanda(comanda5);
            comandaBO.agregarComanda(comanda6);
            comandaBO.agregarComanda(comanda7);
            comandaBO.agregarComanda(comanda8);
            comandaBO.agregarComanda(comanda9);
            comandaBO.agregarComanda(comanda10);
            comandaBO.agregarComanda(comanda11);
            comandaBO.agregarComanda(comanda12);
            comandaBO.agregarComanda(comanda13);
            comandaBO.agregarComanda(comanda14);
            comandaBO.agregarComanda(comanda15);
            comandaBO.agregarComanda(comanda16);
            comandaBO.agregarComanda(comanda17);
            comandaBO.agregarComanda(comanda18);
            comandaBO.agregarComanda(comanda19);
            comandaBO.agregarComanda(comanda20);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
    }
    
}
