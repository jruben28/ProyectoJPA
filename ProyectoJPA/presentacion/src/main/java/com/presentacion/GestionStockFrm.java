/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.presentacion;
import com.dtos.IngredienteDTO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.converter.DoubleStringConverter;

/**
 *
 * @author joser
 */
public class GestionStockFrm extends VBox {

    private final ControllerIngrediente controller;
    private TableView<MovimientoStockFila> tableView;
    private Label titulo;
    private Button btnGuardar;

    public GestionStockFrm(ControllerIngrediente controller) {
        this.controller = controller;
        initComponents();
        cargarDatos(); 
    }

    private void initComponents() {
        setSpacing(20);
        setPadding(new Insets(25, 35, 25, 35));
        getStyleClass().add("root-pane");

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        titulo = new Label("Gestión de Inventario (Movimientos)");
        titulo.getStyleClass().add("titulo");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        btnGuardar = new Button("Guardar Movimientos");
        btnGuardar.getStyleClass().add("btn-buscar");
        btnGuardar.setOnAction(e -> guardarCambios());

        header.getChildren().addAll(titulo, spacer, btnGuardar);

        tableView = new TableView<>();
        tableView.setEditable(true);
        tableView.getStyleClass().add("card");
        buildTableColumns();

        getChildren().addAll(header, tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
    }

    private void buildTableColumns() {
        TableColumn<MovimientoStockFila, String> colNombre = new TableColumn<>("Ingrediente");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(250);

        TableColumn<MovimientoStockFila, String> colUnidad = new TableColumn<>("Unidad");
        colUnidad.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        colUnidad.setPrefWidth(100);

        TableColumn<MovimientoStockFila, Double> colStockActual = new TableColumn<>("Stock Actual");
        colStockActual.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colStockActual.setPrefWidth(120);

        TableColumn<MovimientoStockFila, Double> colEntrada = new TableColumn<>("Entrada (Compra)");
        colEntrada.setCellValueFactory(new PropertyValueFactory<>("entrada"));
        colEntrada.setPrefWidth(200);

        colEntrada.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        colEntrada.setStyle("-fx-background-color: #f0fff4;");

        TableColumn<MovimientoStockFila, Double> colStockFinal = new TableColumn<>("Stock Final");
        colStockFinal.setCellValueFactory(new PropertyValueFactory<>("stockFinal"));
        colStockFinal.setPrefWidth(200);

        colStockFinal.setCellFactory(column -> new TableCell<MovimientoStockFila, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.1f", item));
                    setStyle("-fx-text-fill: #000000; -fx-font-weight: bold;");
                }
            }
        });

        tableView.getColumns().addAll(colNombre, colUnidad, colStockActual, colEntrada, colStockFinal);
    }

    private void cargarDatos() {
        tableView.setItems(controller.cargarMovimientosInventario());
    }

    private void guardarCambios() {
        var rows = tableView.getItems();

        try {
            controller.guardarMovimientos(rows);

            Alert exito = new Alert(Alert.AlertType.INFORMATION, "Movimientos guardados exitosamente.");
            exito.setTitle("Éxito");
            exito.setHeaderText(null);
            exito.showAndWait();
            
            cargarDatos();

        } catch (Exception ex) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Error al guardar: " + ex.getMessage());
            error.setTitle("Error");
            error.setHeaderText(null);
            error.showAndWait();
        }
    }

    public VBox getRoot() {
        return this;
    }


    
    
    
    public static class MovimientoStockFila {

        private final IngredienteDTO dto;
        
        private final StringProperty nombre;
        private final StringProperty unidad;
        private final DoubleProperty stockActual;
        
        private final DoubleProperty entrada = new SimpleDoubleProperty(0.0);
        
        private final DoubleProperty stockFinal;


        public MovimientoStockFila(IngredienteDTO dto) {
            this.dto = dto;
            this.nombre = new SimpleStringProperty(dto.getNombre());
            this.unidad = new SimpleStringProperty(dto.getUnidadDeMedida().name());
            this.stockActual = new SimpleDoubleProperty(dto.getStock());

            this.stockFinal = new SimpleDoubleProperty();
            this.stockFinal.bind(this.stockActual.add(this.entrada));
        }

        public IngredienteDTO getDto() {
            return dto;
        }

        public String getNombre() {
            return nombre.get();
        }

        public StringProperty nombreProperty() {
            return nombre;
        }

        public String getUnidad() {
            return unidad.get();
        }

        public StringProperty unidadProperty() {
            return unidad;
        }

        public double getStockActual() {
            return stockActual.get();
        }

        public DoubleProperty stockActualProperty() {
            return stockActual;
        }

        public double getEntrada() {
            return entrada.get();
        }

        public DoubleProperty entradaProperty() {
            return entrada;
        }

        public void setEntrada(double value) {
            entrada.set(value);
        }

        public double getStockFinal() {
            return stockFinal.get();
        }

        public DoubleProperty stockFinalProperty() {
            return stockFinal;
        }
    }
}
