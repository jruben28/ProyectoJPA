///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
// */
//package pruebas;
//
//import BOs.ClienteBO;
//import Conexion.ConexionBD;
//import Entidades.ClienteFrecuente;
//import Entidades.Comanda;
//import enums.EstadoComanda;
//import javax.persistence.EntityManager;
//
//public class PruebaClienteBO {
//
//    public static void main(String[] args) {
//        EntityManager em = ConexionBD.crearConexion();
//
//        ClienteFrecuente cliente = new ClienteFrecuente("Kevin Mendoza", "6441673132", "kevinKKevinKKKKevin@gmail.com");
//        Comanda comanda1 = new Comanda(10000.0, EstadoComanda.ENTREGADA, cliente);
//        Comanda comanda2 = new Comanda(-2.0, EstadoComanda.ENTREGADA, cliente);
//        Comanda comanda3 = new Comanda(1.0, EstadoComanda.ABIERTA, cliente);
//
//        ClienteFrecuente clienteMalo = new ClienteFrecuente("Adrian Moreno", "6441673137", "AAAdrian@gmail.com");
//        Comanda comanda4 = new Comanda(-999.0, EstadoComanda.ENTREGADA, clienteMalo);
//
//        em.getTransaction().begin();
//        em.persist(cliente);
//        em.persist(comanda1);
//        em.persist(comanda2);
//        em.persist(comanda3);
//
//        em.persist(clienteMalo);
//        em.persist(comanda4);
//
//        em.getTransaction().commit();
//        em.close();
//
//        ClienteBO clienteBO = new ClienteBO();
//        Double totalBien = clienteBO.calcularTotalGastado(cliente.getId());
//        Integer puntosBien = clienteBO.calcularPuntos(cliente.getId());
//
//        ClienteBO clienteBOMalo = new ClienteBO();
//        Double totalMalo = clienteBOMalo.calcularTotalGastado(clienteMalo.getId());
//        Integer puntosMalo = clienteBOMalo.calcularPuntos(clienteMalo.getId());
//
//        System.out.println("Total gastado: " + totalBien);
//        System.out.println("Puntos acumulados: " + puntosBien);
//
//        System.out.println("Total gastado: " + totalMalo);
//        System.out.println("Puntos acumulados: " + puntosMalo);
//    }
//}
