package com.presentacion;

import BOs.ClienteBO;
import com.dtos.ClienteDTO;
import interfaces.IClienteBO;
import com.dtos.ClienteFrecuenteDTO;
import com.dtos.ComandaDTO;
import excepciones.PersistenciaException;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador del modulo de Clientes Frecuentes. Las pantallas NO se comunican
 * entre si, todo pasa por aqui.
 */
public class ControllerClienteFrecuente {

   private final IClienteBO clienteBO;
    private final Stage primaryStage;
    private ClienteFrecuenteDTO clienteVinculado;
    private static final String CSS_PATH = "/styles/buscador-clientes.css";

    public ControllerClienteFrecuente(Stage primaryStage) {
        this.clienteBO = new ClienteBO();
        this.primaryStage = primaryStage;
    }

    // ==================== Navegación ====================
    public void mostrarBuscador() {
        BuscadorClientesFrm buscador = new BuscadorClientesFrm(this);
        Scene scene = new Scene(buscador.getRoot(), 1100, 700);
        scene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
        primaryStage.setTitle("Buscar Clientes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void mostrarSistemaPuntos(ClienteFrecuenteDTO cliente) {
        List<SistemaPuntosFrm.FilaTransaccion> transacciones = cargarTransacciones(cliente);

        SistemaPuntosFrm ventana = new SistemaPuntosFrm(cliente, transacciones);
        ventana.getScene().getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
        ventana.show();
    }

    public void mostrarRegistro() {
        RegistroClienteFrm ventana = new RegistroClienteFrm(this);
        ventana.getScene().getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
        ventana.show();
    }

    // ==================== Lógica ====================
    public List<ClienteFrecuenteDTO> buscarClientes(String filtro, String campo) throws PersistenciaException {
        return clienteBO.buscarFrecuentesPorFiltro(filtro, campo);
    }

    public void registrarCliente(ClienteFrecuenteDTO dto) throws PersistenciaException {
        clienteBO.agregarClienteFrecuente(dto);
    }

    public void actualizarCliente(ClienteFrecuenteDTO dto) throws PersistenciaException {
        clienteBO.actualizarClienteFrecuente(dto);
    }

    public void vincularCliente(ClienteFrecuenteDTO cliente) {
        this.clienteVinculado = cliente;
    }

    public ClienteDTO crearClienteGeneral() throws PersistenciaException {
        return clienteBO.crearClienteGeneral();
    }

    /**
     * Convierte las ComandaDTO (del BO) al modelo FilaTransaccion que usa la tabla
     */
    private List<SistemaPuntosFrm.FilaTransaccion> cargarTransacciones(ClienteFrecuenteDTO cliente) {
        List<SistemaPuntosFrm.FilaTransaccion> filas = new ArrayList<>();
        try {
            List<ComandaDTO> comandas = clienteBO.buscarComandasPorCliente(cliente.getId());

            int acumulado = 0;
            for (ComandaDTO c : comandas) {
                int puntosGanados = (int) (c.getTotal() / 20.0);
                acumulado += puntosGanados;

                String folio = c.getFolio() != null ? c.getFolio() : "SIN-FOLIO";

                filas.add(new SistemaPuntosFrm.FilaTransaccion(
                        "-",                    // fecha (puedes mejorarlo después)
                        folio,
                        c.getTotal(),
                        puntosGanados,
                        acumulado
                ));
            }
        } catch (PersistenciaException e) {
            System.err.println("Error al cargar transacciones: " + e.getMessage());
        }
        return filas;
    }

    public ClienteFrecuenteDTO obtenerClienteVinculado() {
        return clienteVinculado;
    }
}