package com.presentacion;

import BOs.ClienteBO;
import BOs.IClienteBO;
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

    private TextField txtNombre;
    private TextField txtTelefono;
    private TextField txtCorreo;
    private DatePicker dpFechaRegistro;
    private final IClienteBO clienteBO;

    public RegistroClienteFrm() {
        this.clienteBO = new ClienteBO();
        initComponents();
    }

    private void initComponents() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25, 35, 25, 35));
        root.getStyleClass().add("root-pane");

        Label titulo = new Label("Registrar Cliente Frecuente");
        titulo.getStyleClass().add("titulo");

        Label subtitulo = new Label("Completa los datos del nuevo cliente frecuente");
        subtitulo.getStyleClass().add("resultados-label");

        VBox formulario = crearFormulario();
        HBox botones = crearBotones();

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        root.getChildren().addAll(titulo, subtitulo, formulario, spacer, botones);

        Scene scene = new Scene(root, 620, 580);
        scene.getStylesheets().add(
                getClass().getResource("/styles/buscador-clientes.css").toExternalForm()
        );

        setTitle("Registrar Cliente Frecuente");
        setScene(scene);
    }

    // ==================== Formulario ====================

    private VBox crearFormulario() {
        // Nombre
        Label lblNombre = new Label("Nombre completo *");
        lblNombre.getStyleClass().add("form-label");
        txtNombre = new TextField();
        txtNombre.setPromptText("Ej: Maria Lopez");
        txtNombre.getStyleClass().add("form-field");

        // Telefono
        Label lblTelefono = new Label("Numero de telefono *");
        lblTelefono.getStyleClass().add("form-label");
        txtTelefono = new TextField();
        txtTelefono.setPromptText("10 digitos, ej: 6441234567");
        txtTelefono.getStyleClass().add("form-field");

        // Correo
        Label lblCorreo = new Label("Correo electronico (opcional)");
        lblCorreo.getStyleClass().add("form-label");
        txtCorreo = new TextField();
        txtCorreo.setPromptText("Ej: maria@correo.com");
        txtCorreo.getStyleClass().add("form-field");

        // Fecha de registro
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

    // ==================== Botones ====================

    private HBox crearBotones() {
        Button btnGuardar = new Button("Guardar Cliente");
        btnGuardar.getStyleClass().add("btn-buscar");
        btnGuardar.setOnAction(e -> guardarCliente());

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("btn-cancelar");
        btnCancelar.setOnAction(e -> close());

        HBox hbox = new HBox(12, btnCancelar, btnGuardar);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        return hbox;
    }

    // ==================== Guardar ====================

    private void guardarCliente() {
        ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
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
            clienteBO.agregarClienteFrecuente(dto);

            Alert exito = new Alert(Alert.AlertType.INFORMATION,
                    "Cliente '" + dto.getNombre() + "' registrado exitosamente.");
            exito.setHeaderText(null);
            exito.setTitle("Cliente Registrado");
            exito.showAndWait();
            limpiarFormulario();
        } catch (NegocioException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR,
                    ex.getMessage());
            error.setHeaderText("Error al registrar cliente");
            error.setTitle("Error");
            error.showAndWait();
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        dpFechaRegistro.setValue(LocalDate.now());
    }
}
