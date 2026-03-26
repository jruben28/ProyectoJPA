/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pruebas;

import Conexion.ConexionBD;
import DAOs.ClienteDAO;
import Entidades.ClienteFrecuente;
import Entidades.Comanda;
import enums.EstadoComanda;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author keppler
 */
public class PruebaClienteDAO {

    public static void main(String[] args) {
        EntityManager em = ConexionBD.crearConexion();
        ClienteDAO dao = new ClienteDAO();

        ClienteFrecuente cliente = new ClienteFrecuente("Juan Pérez", "6441234567", "juan@mail.com");

        Comanda c1 = new Comanda(200.0, EstadoComanda.ENTREGADA, cliente);
        Comanda c2 = new Comanda(350.0, EstadoComanda.ENTREGADA, cliente);
        Comanda c3 = new Comanda(100.0, EstadoComanda.ABIERTA, cliente);

        em.getTransaction().begin();
        em.persist(cliente);
        em.persist(c1);
        em.persist(c2);
        em.persist(c3);
        em.getTransaction().commit();
        em.close();

        List<Comanda> resultado = dao.buscarComandasPorCliente(cliente.getId());
        
        double totalGastado = resultado.stream().mapToDouble(Comanda::getTotal).sum();
        int puntos = (int) (totalGastado / 20);

        System.out.println("Comandas entregadas: " + resultado.size());
        System.out.println("Total gastado: $" + totalGastado);
        System.out.println("Puntos calculados: " + puntos);
    }
    
}
