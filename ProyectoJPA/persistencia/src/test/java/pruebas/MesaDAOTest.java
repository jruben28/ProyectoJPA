/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pruebas;

import DAOs.MesaDAO;
import entidades.Mesa;
import enums.EstadoMesa;
import excepciones.PersistenciaException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class MesaDAOTest {

    private final MesaDAO dao = new MesaDAO();

    @Test
    void agregarMesa_Exito() throws PersistenciaException {
        Mesa mesa = new Mesa(101);
        Mesa guardada = dao.agregar(mesa);

        assertNotNull(guardada.getId());
        assertEquals(101, guardada.getNumero());
        assertEquals(EstadoMesa.DISPONIBLE, guardada.getEstado());
    }

    @Test
    void obtenerTodas_Exito() throws PersistenciaException {
        dao.agregar(new Mesa(201));
        dao.agregar(new Mesa(202));

        List<Mesa> mesas = dao.obtenerTodas();

        assertTrue(mesas.size() >= 2);
    }

    @Test
    void obtenerDisponibles_Exito() throws PersistenciaException {
        Mesa ocupada = new Mesa(301);
        ocupada.setEstado(EstadoMesa.OCUPADA);
        dao.agregar(ocupada);

        dao.agregar(new Mesa(302)); 
        dao.agregar(new Mesa(303)); 

        List<Mesa> disponibles = dao.obtenerDisponibles();

        assertTrue(disponibles.size() >= 2);
        assertTrue(disponibles.stream().allMatch(m -> m.getEstado() == EstadoMesa.DISPONIBLE));
    }

    @Test
    void cargaMasiva_Exito() throws PersistenciaException {
        dao.cargaMasiva(10);

        List<Mesa> todas = dao.obtenerTodas();

        assertTrue(todas.size() >= 10);
    }
    
        @Test
    void buscarPorId_Exito() throws PersistenciaException {

        Mesa mesa = new Mesa(401);
        Mesa guardada = dao.agregar(mesa);
        Long idGenerado = guardada.getId();
        Mesa encontrada = dao.buscarPorId(idGenerado);

        assertNotNull(encontrada);
        assertEquals(idGenerado, encontrada.getId());
        assertEquals(401, encontrada.getNumero());
        assertEquals(EstadoMesa.DISPONIBLE, encontrada.getEstado());
    }

    @Test
    void buscarPorId_Inexistente() {

        assertThrows(PersistenciaException.class, () -> {
            dao.buscarPorId(99999L);
        });
    }

    @Test
    void actualizar_Exito() throws PersistenciaException {
        Mesa mesa = new Mesa(402);
        Mesa guardada = dao.agregar(mesa);
        Long id = guardada.getId();
        guardada.setEstado(EstadoMesa.OCUPADA);
        Mesa actualizada = dao.actualizar(guardada);

      
        assertNotNull(actualizada);
        assertEquals(id, actualizada.getId());
        assertEquals(EstadoMesa.OCUPADA, actualizada.getEstado());

        Mesa resultadoBuscada = dao.buscarPorId(id);
        assertEquals(EstadoMesa.OCUPADA, resultadoBuscada.getEstado());
    }
}