package com.presentacion;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Presentacion extends Application {

    private static final String CSS_PATH = "/styles/buscador-clientes.css";

    @Override
    public void start(Stage primaryStage) {
        mostrarMenuPrincipal(primaryStage);
    }

    public static void mostrarMenuPrincipal(Stage primaryStage) {
        Label titulo = new Label("Sistema de Comandas");
        titulo.getStyleClass().add("titulo");

        Label subtitulo = new Label("Selecciona un módulo para continuar");
        subtitulo.getStyleClass().add("resultados-label");

        Button btnCombos = new Button("Gestión de Combos");
        btnCombos.getStyleClass().add("btn-buscar");
        btnCombos.setPrefWidth(260);
        btnCombos.setOnAction(e -> new ControllerCombo(primaryStage).mostrarGestionCombos());

        Button btnClientes = new Button("Clientes Frecuentes");
        btnClientes.getStyleClass().add("btn-buscar");
        btnClientes.setPrefWidth(260);
        btnClientes.setOnAction(e -> new ControllerClienteFrecuente(primaryStage).mostrarBuscador());

        Button btnSalir = new Button("Salir");
        btnSalir.getStyleClass().add("btn-cancelar");
        btnSalir.setPrefWidth(260);
        btnSalir.setOnAction(e -> primaryStage.close());

        VBox root = new VBox(18, titulo, subtitulo, btnCombos, btnClientes, btnSalir);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.getStyleClass().add("root-pane");

        Scene scene = new Scene(root, 520, 420);
        scene.getStylesheets().add(Presentacion.class.getResource(CSS_PATH).toExternalForm());
        primaryStage.setTitle("Sistema de Comandas - Menú Principal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}