package com.presentacion;

import com.dtos.ComboDTO;
import excepciones.PersistenciaException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class ComboDisponibilidadFrm {

    private final ControllerCombo controller;
    private VBox root;

    public ComboDisponibilidadFrm(ControllerCombo controller) {
        this.controller = controller;
        construirPantalla();
    }

    public VBox getRoot() {
        return root;
    }

    private void construirPantalla() {
        root = new VBox(15);
        root.setPadding(new Insets(25, 30, 25, 30));
        root.getStyleClass().add("root-pane");

        // Encabezado
        HBox encabezado = new HBox(10);
        encabezado.setAlignment(Pos.CENTER_LEFT);

        Button btnVolver = new Button("← Volver a Combos");
        btnVolver.getStyleClass().add("btn-cancelar");
        btnVolver.setOnAction(e -> controller.mostrarGestionCombos());

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        Label lblTitulo = new Label("Disponibilidad de Combos");
        lblTitulo.getStyleClass().add("titulo");

        encabezado.getChildren().addAll(btnVolver, espaciador, lblTitulo);

        // Tarjetas en grid de 2 columnas
        FlowPane grillaTarjetas = new FlowPane();
        grillaTarjetas.setHgap(15);
        grillaTarjetas.setVgap(15);

        try {
            List<ComboDTO> combos = controller.obtenerTodosCombos();

            for (ComboDTO combo : combos) {
                boolean disponible = false;
                try {
                    disponible = controller.puedeVenderse(combo.getId());
                } catch (PersistenciaException e) {
                    // si falla la consulta, se muestra como no disponible
                }
                VBox tarjeta = construirTarjeta(combo, disponible);
                tarjeta.setPrefWidth(380);
                grillaTarjetas.getChildren().add(tarjeta);
            }

            if (combos.isEmpty()) {
                Label lblVacio = new Label("No hay combos registrados.");
                lblVacio.getStyleClass().add("resultados-label");
                grillaTarjetas.getChildren().add(lblVacio);
            }

        } catch (PersistenciaException e) {
            Label lblError = new Label("Error al cargar combos: " + e.getMessage());
            grillaTarjetas.getChildren().add(lblError);
        }

        ScrollPane scroll = new ScrollPane(grillaTarjetas);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        root.getChildren().addAll(lblTitulo, encabezado, new Separator(), scroll);
    }

    private VBox construirTarjeta(ComboDTO combo, boolean disponible) {
        VBox tarjeta = new VBox(8);
        tarjeta.setPadding(new Insets(15));

        if (disponible) {
            tarjeta.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; -fx-border-radius: 8; -fx-background-radius: 8;");
        } else {
            tarjeta.setStyle("-fx-background-color: #FEF2F2; -fx-border-color: #FECACA; -fx-border-radius: 8; -fx-background-radius: 8;");
        }

        // Fila superior: nombre + badge
        HBox filaNombre = new HBox(10);
        filaNombre.setAlignment(Pos.CENTER_LEFT);

        Label lblNombre = new Label(combo.getNombre());
        lblNombre.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        Label badge = new Label(disponible ? "Disponible" : "Bloqueado");
        if (disponible) {
            badge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #16a34a; -fx-font-weight: bold; -fx-padding: 3 10; -fx-background-radius: 10;");
        } else {
            badge.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-padding: 3 10; -fx-background-radius: 10;");
        }

        filaNombre.getChildren().addAll(lblNombre, espaciador, badge);

        // Precio
        Label lblPrecio = new Label(String.format("$%.2f", combo.getPrecioCombo()));
        lblPrecio.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #16a34a;");

        // Descuento y ahorro
        double ahorro = combo.getPrecioOriginal() - combo.getPrecioCombo();
        Label lblAhorro = new Label(String.format("Ahorro: $%.2f  (%d%% descuento)",
                ahorro, combo.getPorcentajeDescuento()));
        lblAhorro.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12px;");

        tarjeta.getChildren().addAll(filaNombre, lblPrecio, lblAhorro);
        return tarjeta;
    }
}