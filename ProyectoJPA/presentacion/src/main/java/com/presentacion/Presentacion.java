package com.presentacion;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicacion JavaFX.
 * Crea el controlador y le delega el flujo.
 *
 * @author joser
 */
public class Presentacion extends Application {

    @Override
    public void start(Stage primaryStage) {
        ControllerClienteFrecuente controller = new ControllerClienteFrecuente(primaryStage);
        controller.mostrarBuscador();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
