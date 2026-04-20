package com.presentacion;

import com.dtos.ComboDTO;
import com.dtos.ProductoDTO;
import excepciones.PersistenciaException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class ComboCreacionFrm {

    private final ControllerCombo controller;

    private TextField txtNombre;
    private TextField txtDescripcion;
    private TextField txtPorcentajeDescuento;
    private Label lblPrecioOriginal;
    private Label lblPrecioCombo;
    private VBox vboxProductosSeleccionados;
    private Label lblContadorProductos;

    private TextField txtBuscarProducto;
    private VBox vboxCatalogo;

    private List<FilaSeleccionada> seleccionados;
    private List<ProductoDTO> todosLosProductos;

    private VBox root;

    public ComboCreacionFrm(ControllerCombo controller) {
        this.controller = controller;
        this.seleccionados = new ArrayList<>();
    }

    public VBox getRoot() {
        return root;
    }

    private static class FilaSeleccionada {
        ProductoDTO producto;
        int cantidad;

        FilaSeleccionada(ProductoDTO producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }
    }

    public void construirPantalla() {
        try {
            todosLosProductos = controller.obtenerTodosProductos();
        } catch (PersistenciaException e) {
            todosLosProductos = new ArrayList<>();
        }

        root = new VBox();
        root.getStyleClass().add("root-pane");

        HBox encabezado = new HBox(10);
        encabezado.setPadding(new Insets(15, 20, 10, 20));
        encabezado.setAlignment(Pos.CENTER_LEFT);

        Button btnVolver = new Button("← Volver a Combos");
        btnVolver.getStyleClass().add("btn-cancelar");
        btnVolver.setOnAction(e -> controller.mostrarGestionCombos());

        Label lblTitulo = new Label("Nuevo Combo");
        lblTitulo.getStyleClass().add("titulo");

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);
        encabezado.getChildren().addAll(btnVolver, espaciador, lblTitulo);

        HBox paneles = new HBox();
        VBox.setVgrow(paneles, Priority.ALWAYS);

        VBox panelIzquierdo = construirPanelIzquierdo();
        panelIzquierdo.setPrefWidth(500);

        Separator separador = new Separator(javafx.geometry.Orientation.VERTICAL);

        VBox panelDerecho = construirPanelDerecho();
        HBox.setHgrow(panelDerecho, Priority.ALWAYS);

        paneles.getChildren().addAll(panelIzquierdo, separador, panelDerecho);

        root.getChildren().addAll(encabezado, new Separator(), paneles);
    }

    private VBox construirPanelIzquierdo() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(20));

        Label lblNombreLbl = new Label("Nombre del Combo");
        lblNombreLbl.getStyleClass().add("form-label");
        txtNombre = new TextField();
        txtNombre.setPromptText("Ej: Combo Familiar");
        txtNombre.getStyleClass().add("search-field");

        Label lblDescripcionLbl = new Label("Descripción (opcional)");
        lblDescripcionLbl.getStyleClass().add("form-label");
        txtDescripcion = new TextField();
        txtDescripcion.setPromptText("Descripción del combo");
        txtDescripcion.getStyleClass().add("search-field");

        Label lblProductosLbl = new Label("Productos Seleccionados");
        lblProductosLbl.getStyleClass().add("form-label");

        vboxProductosSeleccionados = new VBox(8);
        ScrollPane scroll = new ScrollPane(vboxProductosSeleccionados);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(200);
        scroll.setStyle("-fx-background-color: transparent;");

        lblContadorProductos = new Label("0 productos seleccionados (mínimo 2)");
        lblContadorProductos.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12px;");

        // Precio original calculado automáticamente
        lblPrecioOriginal = new Label("Precio original: $0.00");
        lblPrecioOriginal.setStyle("-fx-font-size: 13px; -fx-text-fill: #374151;");

        // Porcentaje de descuento
        Label lblDescuentoLbl = new Label("Porcentaje de descuento (%)");
        lblDescuentoLbl.getStyleClass().add("form-label");
        txtPorcentajeDescuento = new TextField("0");
        txtPorcentajeDescuento.setPromptText("Ej: 15");
        txtPorcentajeDescuento.getStyleClass().add("search-field");
        txtPorcentajeDescuento.textProperty().addListener((obs, anterior, nuevo) -> recalcularPrecios());

        // Precio combo calculado automáticamente
        lblPrecioCombo = new Label("Precio combo: $0.00");
        lblPrecioCombo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #16a34a;");

        Button btnGuardar = new Button("Guardar Combo");
        btnGuardar.getStyleClass().add("btn-buscar");
        btnGuardar.setPrefWidth(Double.MAX_VALUE);
        btnGuardar.setOnAction(e -> guardarCombo());

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("btn-cancelar");
        btnCancelar.setPrefWidth(Double.MAX_VALUE);
        btnCancelar.setOnAction(e -> controller.mostrarGestionCombos());

        HBox botones = new HBox(10, btnCancelar, btnGuardar);
        HBox.setHgrow(btnGuardar, Priority.ALWAYS);
        HBox.setHgrow(btnCancelar, Priority.ALWAYS);

        panel.getChildren().addAll(
                lblNombreLbl, txtNombre,
                lblDescripcionLbl, txtDescripcion,
                lblProductosLbl, scroll,
                lblContadorProductos,
                lblPrecioOriginal,
                lblDescuentoLbl, txtPorcentajeDescuento,
                lblPrecioCombo,
                botones
        );

        return panel;
    }

    private VBox construirPanelDerecho() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(20));

        Label lblCatalogo = new Label("Catálogo de Productos");
        lblCatalogo.getStyleClass().add("titulo");

        txtBuscarProducto = new TextField();
        txtBuscarProducto.setPromptText("Buscar producto...");
        txtBuscarProducto.getStyleClass().add("search-field");
        txtBuscarProducto.textProperty().addListener((obs, anterior, nuevo) -> filtrarCatalogo(nuevo));

        vboxCatalogo = new VBox(8);
        ScrollPane scrollCatalogo = new ScrollPane(vboxCatalogo);
        scrollCatalogo.setFitToWidth(true);
        VBox.setVgrow(scrollCatalogo, Priority.ALWAYS);
        scrollCatalogo.setStyle("-fx-background-color: transparent;");

        mostrarProductosEnCatalogo(todosLosProductos);

        panel.getChildren().addAll(lblCatalogo, txtBuscarProducto, scrollCatalogo);
        return panel;
    }

    private void mostrarProductosEnCatalogo(List<ProductoDTO> productos) {
        vboxCatalogo.getChildren().clear();

        for (ProductoDTO producto : productos) {
            HBox fila = new HBox(10);
            fila.setAlignment(Pos.CENTER_LEFT);
            fila.setPadding(new Insets(8, 10, 8, 10));
            fila.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; -fx-border-radius: 6; -fx-background-radius: 6;");

            VBox info = new VBox(2);
            Label lblNombreProducto = new Label(producto.getNombre());
            lblNombreProducto.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
            Label lblPrecioProducto = new Label(String.format("$%.2f", producto.getPrecio()));
            lblPrecioProducto.setStyle("-fx-text-fill: #16a34a; -fx-font-size: 12px;");
            info.getChildren().addAll(lblNombreProducto, lblPrecioProducto);

            Region espaciador = new Region();
            HBox.setHgrow(espaciador, Priority.ALWAYS);

            Button btnAgregar = new Button("Agregar");
            btnAgregar.getStyleClass().add("btn-buscar");
            btnAgregar.setOnAction(e -> agregarProducto(producto));

            fila.getChildren().addAll(info, espaciador, btnAgregar);
            vboxCatalogo.getChildren().add(fila);
        }
    }

    private void filtrarCatalogo(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            mostrarProductosEnCatalogo(todosLosProductos);
            return;
        }
        List<ProductoDTO> filtrados = new ArrayList<>();
        for (ProductoDTO p : todosLosProductos) {
            if (p.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                filtrados.add(p);
            }
        }
        mostrarProductosEnCatalogo(filtrados);
    }

    private void agregarProducto(ProductoDTO producto) {
        for (FilaSeleccionada fila : seleccionados) {
            if (fila.producto.getId().equals(producto.getId())) {
                fila.cantidad++;
                refrescarProductosSeleccionados();
                return;
            }
        }
        seleccionados.add(new FilaSeleccionada(producto, 1));
        refrescarProductosSeleccionados();
    }

    private void refrescarProductosSeleccionados() {
        vboxProductosSeleccionados.getChildren().clear();

        for (FilaSeleccionada fila : seleccionados) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(6, 10, 6, 10));
            row.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 6; -fx-background-radius: 6;");

            VBox info = new VBox(2);
            Label lblNombre = new Label(fila.producto.getNombre());
            lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
            Label lblPrecio = new Label(String.format("$%.2f", fila.producto.getPrecio()));
            lblPrecio.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 11px;");
            info.getChildren().addAll(lblNombre, lblPrecio);

            Region espaciador = new Region();
            HBox.setHgrow(espaciador, Priority.ALWAYS);

            Button btnMenos = new Button("-");
            btnMenos.setStyle("-fx-background-color: #E5E7EB; -fx-min-width: 28px;");

            Label lblCantidad = new Label(String.valueOf(fila.cantidad));
            lblCantidad.setStyle("-fx-font-size: 13px; -fx-min-width: 20px; -fx-alignment: center;");

            Button btnMas = new Button("+");
            btnMas.setStyle("-fx-background-color: #E5E7EB; -fx-min-width: 28px;");

            Button btnQuitar = new Button("✕");
            btnQuitar.setStyle("-fx-text-fill: #dc2626; -fx-background-color: transparent;");

            FilaSeleccionada filaRef = fila;

            btnMenos.setOnAction(e -> {
                if (filaRef.cantidad > 1) {
                    filaRef.cantidad--;
                    refrescarProductosSeleccionados();
                }
            });
            btnMas.setOnAction(e -> {
                filaRef.cantidad++;
                refrescarProductosSeleccionados();
            });
            btnQuitar.setOnAction(e -> {
                seleccionados.remove(filaRef);
                refrescarProductosSeleccionados();
            });

            row.getChildren().addAll(info, espaciador, btnMenos, lblCantidad, btnMas, btnQuitar);
            vboxProductosSeleccionados.getChildren().add(row);
        }

        // Actualizar contador y precios
        int totalUnidades = 0;
        for (FilaSeleccionada f : seleccionados) {
            totalUnidades += f.cantidad;
        }

        if (totalUnidades >= 2) {
            lblContadorProductos.setText("✔ " + totalUnidades + " productos seleccionados (mínimo 2)");
            lblContadorProductos.setStyle("-fx-text-fill: #16a34a; -fx-font-size: 12px;");
        } else {
            lblContadorProductos.setText(totalUnidades + " productos seleccionados (mínimo 2)");
            lblContadorProductos.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12px;");
        }

        recalcularPrecios();
    }

    private void recalcularPrecios() {
        double precioOriginal = 0.0;
        for (FilaSeleccionada f : seleccionados) {
            precioOriginal += f.producto.getPrecio() * f.cantidad;
        }

        lblPrecioOriginal.setText(String.format("Precio original: $%.2f", precioOriginal));

        try {
            int descuento = Integer.parseInt(txtPorcentajeDescuento.getText().trim());
            if (descuento >= 0 && descuento <= 100) {
                double precioCombo = precioOriginal * (1 - descuento / 100.0);
                lblPrecioCombo.setText(String.format("Precio combo: $%.2f", precioCombo));
            } else {
                lblPrecioCombo.setText("Precio combo: descuento inválido");
            }
        } catch (NumberFormatException e) {
            lblPrecioCombo.setText("Precio combo: -");
        }
    }

    private void guardarCombo() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            mostrarAlerta("El nombre del combo es obligatorio.");
            return;
        }

        if (seleccionados.isEmpty()) {
            mostrarAlerta("Debes agregar al menos un producto.");
            return;
        }

        int totalUnidades = 0;
        for (FilaSeleccionada f : seleccionados) {
            totalUnidades += f.cantidad;
        }
        if (totalUnidades < 2) {
            mostrarAlerta("El combo debe tener al menos 2 unidades de producto en total.");
            return;
        }

        int porcentajeDescuento;
        try {
            porcentajeDescuento = Integer.parseInt(txtPorcentajeDescuento.getText().trim());
            if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
                mostrarAlerta("El descuento debe estar entre 0 y 100.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("El porcentaje de descuento debe ser un número entero.");
            return;
        }

        double precioOriginal = 0.0;
        for (FilaSeleccionada f : seleccionados) {
            precioOriginal += f.producto.getPrecio() * f.cantidad;
        }
        double precioCombo = precioOriginal * (1 - porcentajeDescuento / 100.0);

        ComboDTO dto = new ComboDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(txtDescripcion.getText().trim());
        dto.setPrecioOriginal(precioOriginal);
        dto.setPrecioCombo(precioCombo);
        dto.setPorcentajeDescuento(porcentajeDescuento);
        dto.setActivo(true);

        List<Long> idProductos = new ArrayList<>();
        List<Integer> cantidades = new ArrayList<>();
        for (FilaSeleccionada f : seleccionados) {
            idProductos.add(f.producto.getId());
            cantidades.add(f.cantidad);
        }

        try {
            controller.crearComboConProductos(dto, idProductos, cantidades);
            mostrarExito("Combo guardado correctamente.");
            controller.mostrarGestionCombos();
        } catch (PersistenciaException e) {
            mostrarAlerta("Error al guardar: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR, mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }

    private void mostrarExito(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}