/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebas;
import DAOs.ClienteDAO;
import Entidades.ClienteFrecuente;

/**
 *
 * @author keppler
 */



public class PruebaBuscadorClientes {

    public static void main(String[] args) throws Exception {
        ClienteDAO dao = new ClienteDAO();

        dao.agregar(new ClienteFrecuente("Kevin Mendoza", "6441111111", "kevin@gmail.com"));
        dao.agregar(new ClienteFrecuente("Ana Martínez", "6442222222", "ana@hotmail.com"));
        dao.agregar(new ClienteFrecuente("Luis Kevin Ruiz", "6443333333", "luis@gmail.com"));

        dao.buscarFrecuentesPorCampo("kevin", "nombre").forEach(c -> System.out.println(c.getNombre()));
        dao.buscarFrecuentesPorCampo("gmail", "correo").forEach(c -> System.out.println(c.getCorreo()));
        dao.buscarFrecuentesPorCampo("ana", "nombre,correo").forEach(c -> System.out.println(c.getNombre()));
        dao.buscarFrecuentesPorCampo("luis", "").forEach(c -> System.out.println(c.getNombre()));
    }
}
