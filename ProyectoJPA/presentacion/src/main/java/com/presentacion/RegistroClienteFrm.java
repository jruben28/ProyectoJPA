package com.presentacion;

import com.dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class RegistroClienteFrm extends Stage {

    private final ControllerClienteFrecuente controller;
    private ClienteFrecuenteDTO clienteEditando;
    private TextField txtNombre;
    private TextField txtTelefono;
    private TextField txtCorreo;
    private DatePicker dpFechaRegistro;
    private Label titulo;
    private Label subtitulo;
    private Button btnGuardar;

    public RegistroClienteFrm(ControllerClienteFrecuente controller) {
        this.controller = controller;
        this.clienteEditando = null;
        initComponents();
    }

    public RegistroClienteFrm(ControllerClienteFrecuente controller, ClienteFrecuenteDTO cliente) {
        this.controller = controller;
        this.clienteEditando = cliente;
        initComponents();
        precargarDatos(cliente);
    }

    private void initComponents() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25, 35, 25, 35));
        root.getStyleClass().add("root-pane");

        boolean esEdicion = clienteEditando != null;
        titulo = new Label(esEdicion ? "Editar Cliente Frecuente" : "Registrar Cliente Frecuente");
        titulo.getStyleClass().add("titulo");

        subtitulo = new Label(esEdicion ? "Actualiza los datos del cliente" : "Completa los datos del nuevo cliente frecuente");
        subtitulo.getStyleClass().add("resultados-label");

        VBox formulario = crearFormulario();
        HBox botones = crearBotones();

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        root.getChildren().addAll(titulo, subtitulo, formulario, spacer, botones);

        Scene scene = new Scene(root, 620, 580);
        setTitle(esEdicion ? "Editar Cliente Frecuente" : "Registrar Cliente Frecuente");
        setScene(scene);
    }

    private VBox crearFormulario() {
        Label lblNombre = new Label("Nombre completo *");
        lblNombre.getStyleClass().add("form-label");
        txtNombre = new TextField();
        txtNombre.setPromptText("Ej: Maria Lopez");
        txtNombre.getStyleClass().add("form-field");

        Label lblTelefono = new Label("Numero de telefono *");
        lblTelefono.getStyleClass().add("form-label");
        txtTelefono = new TextField();
        txtTelefono.setPromptText("10 digitos, ej: 6441234567");
        txtTelefono.getStyleClass().add("form-field");

        Label lblCorreo = new Label("Correo electronico (opcional)");
        lblCorreo.getStyleClass().add("form-label");
        txtCorreo = new TextField();
        txtCorreo.setPromptText("Ej: maria@correo.com");
        txtCorreo.getStyleClass().add("form-field");

        Label lblFecha = new Label("Fecha de registro *");
        lblFecha.getStyleClass().add("form-label");
        dpFechaRegistro = new DatePicker(LocalDate.now());
        dpFechaRegistro.getStyleClass().add("form-field");
        dpFechaRegistro.setMaxWidth(Double.MAX_VALUE);

        VBox form = new VBox(10,
                lblNombre, txtNombre,
                lblTelefono, txtTelefono,
                lblCorreo, txtCorreo,
                lblFecha, dpFechaRegistro
        );
        form.getStyleClass().add("card");
        form.setPadding(new Insets(25));

        return form;
    }

    private HBox crearBotones() {
        boolean esEdicion = clienteEditando != null;

        btnGuardar = new Button(esEdicion ? "Actualizar Cliente" : "Guardar Cliente");
        btnGuardar.getStyleClass().add("btn-buscar");
        btnGuardar.setOnAction(e -> guardarOActualizarCliente());

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("btn-cancelar");
        btnCancelar.setOnAction(e -> close());

        HBox hbox = new HBox(12, btnCancelar, btnGuardar);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        return hbox;
    }

    private void guardarOActualizarCliente() {
        ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();

        if (clienteEditando != null) {
            dto.setId(clienteEditando.getId());
        }

        dto.setNombre(txtNombre.getText().trim());
        dto.setTelefono(txtTelefono.getText().trim());

        String correo = txtCorreo.getText().trim();
        dto.setCorreo(correo.isEmpty() ? null : correo);

        LocalDate fecha = dpFechaRegistro.getValue();
        if (fecha != null) {
            dto.setFechaRegistro(
                    Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        dto.setTipoCliente("FRECUENTE");

        try {
            if (clienteEditando != null) {
                controller.actualizarCliente(dto);
                Alert exito = new Alert(Alert.AlertType.INFORMATION,
                        "Cliente '" + dto.getNombre() + "' actualizado exitosamente.");
                exito.setHeaderText(null);
                exito.setTitle("Cliente Actualizado");
                exito.showAndWait();
            } else {
                controller.registrarCliente(dto);
                Alert exito = new Alert(Alert.AlertType.INFORMATION,
                        "Cliente '" + dto.getNombre() + "' registrado exitosamente.");
                exito.setHeaderText(null);
                exito.setTitle("Cliente Registrado");
                exito.showAndWait();
                limpiarFormulario();
            }

            close();
        } catch (NegocioException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            error.setHeaderText("Error al guardar cliente");
            error.setTitle("Error");
            error.showAndWait();
        }
    }

    private void precargarDatos(ClienteFrecuenteDTO cliente) {
        txtNombre.setText(cliente.getNombre() != null ? cliente.getNombre() : "");
        txtTelefono.setText(cliente.getTelefono() != null ? cliente.getTelefono() : "");
        txtCorreo.setText(cliente.getCorreo() != null ? cliente.getCorreo() : "");

        if (cliente.getFechaRegistro() != null) {
            dpFechaRegistro.setValue(
                    cliente.getFechaRegistro().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate());
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        dpFechaRegistro.setValue(LocalDate.now());
    }
}
