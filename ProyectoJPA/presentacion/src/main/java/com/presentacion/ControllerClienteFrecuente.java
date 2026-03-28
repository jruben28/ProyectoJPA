package com.presentacion;

import BOs.ClienteBO;
import BOs.IClienteBO;
import Entidades.Comanda;
import com.dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;

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

    // ==================== Navegacion ====================
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
        ventana.getScene().getStylesheets().add(
                getClass().getResource(CSS_PATH).toExternalForm());
        ventana.show();
    }

    public void mostrarRegistro() {
        RegistroClienteFrm ventana = new RegistroClienteFrm(this);
        ventana.getScene().getStylesheets().add(
                getClass().getResource(CSS_PATH).toExternalForm());
        ventana.show();
    }

    // ==================== Logica de negocio ====================
    public List<ClienteFrecuenteDTO> buscarClientes(String filtro, String campo) throws NegocioException {
        return clienteBO.buscarFrecuentesPorFiltro(filtro, campo);
    }

    public void registrarCliente(ClienteFrecuenteDTO dto) throws NegocioException {
        clienteBO.agregarClienteFrecuente(dto);
    }

    public void vincularCliente(ClienteFrecuenteDTO cliente) {
        this.clienteVinculado = cliente;
        System.out.println("Cliente vinculado: " + cliente.getNombre());
    }

    // ==================== Carga de datos ====================
    private List<SistemaPuntosFrm.FilaTransaccion> cargarTransacciones(ClienteFrecuenteDTO cliente) {
        List<SistemaPuntosFrm.FilaTransaccion> filas = new ArrayList<>();
        try {
            List<Comanda> comandas = clienteBO.buscarComandasPorCliente(cliente.getId());
            int acumulado = 0;

            for (Comanda c : comandas) {
                int puntosGanados = (int) (c.getTotal() / 20);
                String folio = "OB-" + String.format("%06d", c.getId());
                acumulado += puntosGanados;

                filas.add(new SistemaPuntosFrm.FilaTransaccion(
                        "-", folio, c.getTotal(), puntosGanados, acumulado));
            }
        } catch (Exception ex) {
            // buscarComandasPorCliente aun no implementado en BO, lista vacia
        }
        return filas;
    }

    public ClienteFrecuenteDTO obtenerClienteVinculado() {
        return clienteVinculado;
    }

    /**
     * Actualiza un cliente frecuente existente
     */
    public void actualizarCliente(ClienteFrecuenteDTO dto) throws NegocioException {
        clienteBO.actualizarClienteFrecuente(dto);
    }

    /**
     * Crea un nuevo cliente general
     */
    public String crearClienteGeneral() throws NegocioException {
        return clienteBO.obtenerOCrearClienteGeneral();
    }

    /**
     * Abre la ventana de registro en modo edición
     */
    public void mostrarRegistroEdicion(ClienteFrecuenteDTO cliente) {
        RegistroClienteFrm ventana = new RegistroClienteFrm(this, cliente);
        ventana.getScene().getStylesheets().add(
                getClass().getResource(CSS_PATH).toExternalForm());
        ventana.show();
    }

}
