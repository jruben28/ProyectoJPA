package com.presentacion;

import com.dtos.ClienteFrecuenteDTO;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Pantalla de Sistema de Puntos / Perfil del cliente.
 * No conoce otras pantallas ni el BO. Recibe datos ya procesados.
 */
public class SistemaPuntosFrm extends Stage {

    private final ClienteFrecuenteDTO cliente;
    private final List<FilaTransaccion> transacciones;

    public SistemaPuntosFrm(ClienteFrecuenteDTO cliente, List<FilaTransaccion> transacciones) {
        this.cliente = cliente;
        this.transacciones = transacciones;
        initComponents();
    }

    private void initComponents() {
        VBox root = new VBox(18);
        root.setPadding(new Insets(25, 30, 25, 30));
        root.getStyleClass().add("root-pane");

        Label titulo = new Label("Sistema de Puntos");
        titulo.getStyleClass().add("titulo");

        HBox perfilCard = crearTarjetaPerfil();
        HBox reglaCard = crearTarjetaRegla();

        Label lblHistorial = new Label("Historial de Transacciones");
        lblHistorial.getStyleClass().add("seccion-titulo");

        TableView<FilaTransaccion> tablaHistorial = crearTablaHistorial();
        VBox.setVgrow(tablaHistorial, Priority.ALWAYS);

        root.getChildren().addAll(titulo, perfilCard, reglaCard, lblHistorial, tablaHistorial);

        Scene scene = new Scene(root, 1100, 700);
        setTitle("Sistema de Puntos - " + cliente.getNombre());
        setScene(scene);
    }

    // ==================== Tarjeta de perfil ====================

    private HBox crearTarjetaPerfil() {
        String iniciales = obtenerIniciales(cliente.getNombre());
        Label lblIniciales = new Label(iniciales);
        lblIniciales.getStyleClass().add("avatar-text");

        StackPane avatar = new StackPane(lblIniciales);
        avatar.getStyleClass().add("avatar-circle");
        avatar.setMinSize(64, 64);
        avatar.setMaxSize(64, 64);
        Circle clip = new Circle(32, 32, 32);
        avatar.setClip(clip);

        Label lblNombre = new Label(cliente.getNombre());
        lblNombre.getStyleClass().add("perfil-nombre");

        String contacto = enmascararTelefono(cliente.getTelefono());
        if (cliente.getCorreo() != null && !cliente.getCorreo().isEmpty()) {
            contacto += "  |  " + cliente.getCorreo();
        }
        Label lblContacto = new Label(contacto);
        lblContacto.getStyleClass().add("perfil-contacto");

        String fechaStr = "Cliente desde: ";
        if (cliente.getFechaRegistro() != null) {
            fechaStr += new SimpleDateFormat("dd/MM/yyyy").format(cliente.getFechaRegistro());
        } else {
            fechaStr += "-";
        }
        Label lblFecha = new Label(fechaStr);
        lblFecha.getStyleClass().add("perfil-fecha");

        VBox infoBox = new VBox(3, lblNombre, lblContacto, lblFecha);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        int puntos = cliente.getPuntosAcumulados() != null ? cliente.getPuntosAcumulados() : 0;
        if (transacciones != null && !transacciones.isEmpty()) {
            puntos = transacciones.stream()
                .mapToInt(FilaTransaccion::getPuntosGanados)
                .sum();
        }
        Label lblPuntosNum = new Label(String.valueOf(puntos));
        lblPuntosNum.getStyleClass().add("puntos-numero");
        Label lblPuntosTxt = new Label("puntos");
        lblPuntosTxt.getStyleClass().add("puntos-texto");

        VBox puntosBox = new VBox(0, lblPuntosNum, lblPuntosTxt);
        puntosBox.setAlignment(Pos.CENTER);
        puntosBox.getStyleClass().add("puntos-badge");
        puntosBox.setMinSize(110, 110);
        puntosBox.setMaxSize(110, 110);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox card = new HBox(16, avatar, infoBox, spacer, puntosBox);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(22, 28, 22, 28));

        return card;
    }

    // ==================== Tarjeta de regla ====================

    private HBox crearTarjetaRegla() {
        Label iconoEstrella = new Label("\u2B50");
        iconoEstrella.setStyle("-fx-font-size: 26px;");

        Label lblRegla = new Label("Regla de acumulacion: 1 punto por cada $20.00 gastados");
        lblRegla.getStyleClass().add("regla-titulo");

        Label lblDetalle = new Label("Los puntos se calculan automaticamente al finalizar cada comanda");
        lblDetalle.getStyleClass().add("regla-detalle");

        VBox textoBox = new VBox(2, lblRegla, lblDetalle);

        HBox card = new HBox(16, iconoEstrella, textoBox);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(16, 28, 16, 28));

        return card;
    }

    // ==================== Tabla de historial ====================

    @SuppressWarnings("unchecked")
    private TableView<FilaTransaccion> crearTablaHistorial() {
        TableView<FilaTransaccion> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("No hay transacciones registradas"));
        tabla.setItems(FXCollections.observableArrayList(transacciones));

        TableColumn<FilaTransaccion, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFecha()));
        colFecha.setPrefWidth(150);

        TableColumn<FilaTransaccion, String> colComanda = new TableColumn<>("Comanda");
        colComanda.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getComanda()));
        colComanda.setPrefWidth(200);

        TableColumn<FilaTransaccion, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("$%,.2f", c.getValue().getTotal())));
        colTotal.setPrefWidth(130);

        TableColumn<FilaTransaccion, Number> colPuntosGanados = new TableColumn<>("Puntos Ganados");
        colPuntosGanados.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getPuntosGanados()));
        colPuntosGanados.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label lbl = new Label("+" + item.intValue());
                    lbl.getStyleClass().add("puntos-ganados-cell");
                    setGraphic(lbl);
                }
            }
        });
        colPuntosGanados.setPrefWidth(150);

        TableColumn<FilaTransaccion, Number> colAcumulado = new TableColumn<>("Acumulado");
        colAcumulado.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getAcumulado()));
        colAcumulado.setPrefWidth(120);

        tabla.getColumns().addAll(colFecha, colComanda, colTotal, colPuntosGanados, colAcumulado);
        return tabla;
    }

    // ==================== Utilidades ====================

    private String enmascararTelefono(String telefono) {
        if (telefono == null || telefono.length() < 4) return "****";
        return "****" + telefono.substring(telefono.length() - 4);
    }

    private String obtenerIniciales(String nombre) {
        if (nombre == null || nombre.isEmpty()) return "?";
        String[] partes = nombre.trim().split("\\s+");
        if (partes.length >= 2) {
            return ("" + partes[0].charAt(0) + partes[partes.length - 1].charAt(0)).toUpperCase();
        }
        return ("" + partes[0].charAt(0)).toUpperCase();
    }

    // ==================== Modelo de fila ====================

    public static class FilaTransaccion {
        private final String fecha;
        private final String comanda;
        private final double total;
        private final int puntosGanados;
        private final int acumulado;

        public FilaTransaccion(String fecha, String comanda, double total, int puntosGanados, int acumulado) {
            this.fecha = fecha;
            this.comanda = comanda;
            this.total = total;
            this.puntosGanados = puntosGanados;
            this.acumulado = acumulado;
        }

        public String getFecha() { return fecha; }
        public String getComanda() { return comanda; }
        public double getTotal() { return total; }
        public int getPuntosGanados() { return puntosGanados; }
        public int getAcumulado() { return acumulado; }
    }
}
