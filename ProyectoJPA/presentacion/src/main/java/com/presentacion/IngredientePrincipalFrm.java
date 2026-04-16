/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.presentacion;

import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author joser
 */
public class IngredientePrincipalFrm extends BorderPane{
    private final ControllerIngrediente controller;
    private VBox sidebar;
    private StackPane contentArea;

    public IngredientePrincipalFrm(ControllerIngrediente controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        sidebar = new VBox(15);
        sidebar.setPadding(new Insets(30, 15, 30, 15));
        sidebar.setPrefWidth(250);
        sidebar.getStyleClass().add("card");
        sidebar.setStyle(sidebar.getStyle() + "-fx-background-color: #2c3e50;");

        Label menuTitulo = new Label("MÓDULO INGREDIENTES");
        menuTitulo.setTextFill(Color.WHITE);
        menuTitulo.setFont(Font.font("System", FontWeight.BOLD, 16));
        menuTitulo.setPadding(new Insets(0, 0, 20, 0));

        Button btnAgregar = crearBotonMenu("Agregar Ingrediente");
        btnAgregar.setOnAction(e -> controller.mostrarRegistro());

        Button btnStock = crearBotonMenu("Control de Stock");
        btnStock.setOnAction(e -> controller.mostrarGestionStockEnPrincipal(this));

        Button btnBuscador = crearBotonMenu("Buscador Ingredientes");
        btnBuscador.setOnAction(e -> {
            System.out.println("Buscador próximamente...");
        });

        sidebar.getChildren().addAll(menuTitulo, btnAgregar, btnStock, btnBuscador);

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));

        Label lblBienvenida = new Label("Seleccione una opción del menú lateral para comenzar");
        lblBienvenida.getStyleClass().add("titulo");
        contentArea.getChildren().add(lblBienvenida);

        setLeft(sidebar);
        setCenter(contentArea);
    }

    private Button crearBotonMenu(String texto) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10, 15, 10, 15));
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 14px;");

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 14px;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 14px;"));

        return btn;
    }


    public void setView(Region view) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(view);
    }
}
