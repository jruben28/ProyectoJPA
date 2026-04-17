/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.presentacion;

import BOs.IngredienteBO;
import com.dtos.IngredienteDTO;
import com.dtos.ProductoDTO;
import com.dtos.ProductoIngredienteRDTO;
import excepciones.NegocioException;
import interfaces.IIngredienteBO;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author joser
 */
public class ControllerIngrediente {
    private final IIngredienteBO ingredienteBO;
    private IngredienteDTO ingredienteDTO;
    private static final String CSS_PATH = "/styles/buscador-clientes.css";
    
    private Stage primaryStage;
    private IngredientePrincipalFrm principalView;

    /**
     * Constructor clase de control
     * @param primaryStage 
     */
    public ControllerIngrediente(Stage primaryStage) {
        this.ingredienteBO = new IngredienteBO();
        this.primaryStage = primaryStage;
    }
    
    /**
     * Muestra el menú principal de ingredientes, temporal en lo que se conecta al menú final
     */
    public void mostrarPrincipal() {
        principalView = new IngredientePrincipalFrm(this);
        Scene scene = new Scene(principalView, 1200, 800);
        
        java.net.URL cssUrl = getClass().getResource(CSS_PATH);
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

        primaryStage.setTitle("Restaurante - Módulo de Ingredientes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Muestra el registro de un ingrediente nuevo
     */
    public void mostrarRegistro() {
        AgregarIngredienteFrm registro = new AgregarIngredienteFrm(this);

        java.net.URL cssUrl = getClass().getResource(CSS_PATH);
        if (cssUrl != null) registro.getScene().getStylesheets().add(cssUrl.toExternalForm());
        registro.show();
    }
    
 
    /**
     * Muestra el gestor de stock de ingredientes.
     * @param principal 
     */
    public void mostrarGestionStockEnPrincipal(IngredientePrincipalFrm principal) {
        GestionStockFrm gestionStock = new GestionStockFrm(this);
        principal.setView(gestionStock);
    }
  
    
    /**
     * Metodo para persistir un ingrediente
     * @param ingredienteDTO 
     */
    public void registrarIngrediente(IngredienteDTO ingredienteDTO){
        ingredienteBO.agregarIngrediente(ingredienteDTO);
    }
    
//    /**
//     * Muestra el gestor del stock de ingredientes
//     */
//    public void mostrarGestionStock() {
//        GestionStockFrm gestionStock = new GestionStockFrm(this);
//        Scene scene = new Scene(gestionStock.getRoot(), 1100, 700);
//        
//        java.net.URL cssUrl = getClass().getResource(CSS_PATH);
//        if (cssUrl != null) {
//            scene.getStylesheets().add(cssUrl.toExternalForm());
//        } else {
//            System.out.println("️ ADVERTENCIA: No se encontró CSS.");
//        }
//        
//        primaryStage.setTitle("Gestión de Stock");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
    
    
    /**
     * Metodo que ayuda a persistir los cambios al inventario de ingredientes.
     * @return 
     */
    public ObservableList<GestionStockFrm.MovimientoStockFila> cargarMovimientosInventario() {
        ObservableList<GestionStockFrm.MovimientoStockFila> filas = FXCollections.observableArrayList();
        try {
            List<IngredienteDTO> ingredientes = ingredienteBO.obtenerIngredienteTodos();
            for (IngredienteDTO dto : ingredientes) {
                filas.add(new GestionStockFrm.MovimientoStockFila(dto));
            }
        } catch (NegocioException ex) {
            System.err.println("Error al cargar ingredientes: " + ex.getMessage());
        }
        return filas;
    }
    
    /**
     * Metodo auxiliar, guarda los cambios hechos al stock
     * @param rows
     * @throws NegocioException 
     */
    public void guardarMovimientos(List<GestionStockFrm.MovimientoStockFila> rows) throws NegocioException {
        boolean huboCambios = false;
        
        for (GestionStockFrm.MovimientoStockFila fila : rows) {
            if (fila.getEntrada() > 0) {
                
                IngredienteDTO dto = fila.getDto();
                
                dto.setStock(fila.getStockFinal());
                
                ingredienteBO.actualizarStock(dto.getIdIngrediente(), dto.getStock()); 
                
                huboCambios = true;
            }
        }
        
        if (!huboCambios) {
            throw new NegocioException("No se registró ninguna entrada nueva para guardar.");
        }
        
    }
    
    /**
     * Muestra el menú para asignar ingredientes a un producto.
     * @param principal 
     */
    public void mostrarAsignarIngredientesEnPrincipal(IngredientePrincipalFrm principal) {
        AsignarIngredientesFrm asignarView = new AsignarIngredientesFrm(this);
        principal.setView(asignarView.getRoot());
    }
    
    /**
     * Obtiene la lista de los productos disponibles, de momento mockeado al no existir el metodo
     * @return 
     */
    public List<ProductoDTO> obtenerProductosDisponibles() {
        try {
            //Mock de productos dado que no existe el metodo en ProductosDAO        
            List<ProductoDTO> listaProductos = new ArrayList<>();
            
            ProductoDTO producto1 = new ProductoDTO();
            producto1.setId(1L);
            producto1.setNombre("Ensalada");
            producto1.setDescripcion("Verduras y topings");
            producto1.setPrecio(100.00);
            producto1.setTipo("COMIDA");

            
//            ProductoDTO producto2 = new ProductoDTO();
//            producto1.setId(2L);
//            producto1.setNombre("Hamburgesa");
//            producto1.setDescripcion("Pan con carne y verduras");
//            producto1.setPrecio(100.00);
//            producto1.setTipo("COMIDA");
//            
//            ProductoDTO producto3 = new ProductoDTO();
//            producto1.setId(3L);
//            producto1.setNombre("Pizza");
//            producto1.setDescripcion("Masa con salsa de tomate");
//            producto1.setPrecio(100.00);
//            producto1.setTipo("COMIDA");
//            
//            ProductoDTO producto4 = new ProductoDTO();
//            producto1.setId(4L);
//            producto1.setNombre("Malteada");
//            producto1.setDescripcion("Leche con helado");
//            producto1.setPrecio(100.00);
//            producto1.setTipo("BEBIDA");
//            
//            ProductoDTO producto5 = new ProductoDTO();
//            producto1.setId(5L);
//            producto1.setNombre("Helado");
//            producto1.setDescripcion("Producto lacteo");
//            producto1.setPrecio(100.00);
//            producto1.setTipo("POSTRE");
            
            listaProductos.add(producto1);
            //listaProductos.add(producto2);
            //listaProductos.add(producto3);
            //listaProductos.add(producto4);
            //listaProductos.add(producto5);

            return listaProductos;
        } catch (Exception ex) {
            System.err.println("Error al cargar productos: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene la lista de ingredientes en el stock acutalizado.
     * @return 
     */
    public List<IngredienteDTO> obtenerIngredientesStockActual() {
        try {
            return ingredienteBO.obtenerIngredienteTodos();
        } catch (Exception ex) {
            System.err.println("Error al cargar stock para vinculación: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public List<IngredienteDTO> obtenerIngredientesFiltro(String textoBusqueda){
    
        try{
            return ingredienteBO.obtenerIngredientePorFiltro(textoBusqueda);
        }
        catch(Exception ex){
            System.out.println("Error al cargar ingredientes por filtro.");
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene la relacion de ProductoIngredientes, mockeado de momento al no poder manejar productos correctamente.
     * @param producto
     * @return 
     */
    public List<ProductoIngredienteRDTO> obtenerRecetaPorProducto(ProductoDTO producto) {
        try {
            
            return obtenerRecetaPorProductoMock(producto);
        } catch (Exception ex) {
            System.err.println("Error al cargar la receta: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Guarda los datos de los nuevos ingredientes que agreguemos a un producto.
     * @param producto
     * @param nuevaReceta
     * @throws Exception 
     */
    public void guardarRecetaDeProducto(ProductoDTO producto, List<ProductoIngredienteRDTO> nuevaReceta) throws Exception {
        //cuando pueda manejar productos correctamente ya podré persistir los nuevos ingredientes que se quieran asociar aquí.
        System.out.println("Guardando receta de " + nuevaReceta.size() + " ingredientes para: " + producto.getNombre());
    }
    
    /**
     * Metodo provisional que genera una receta.
     * @param producto
     * @return 
     */
    private List<ProductoIngredienteRDTO> obtenerRecetaPorProductoMock(ProductoDTO producto){
            //Mock al no poder acoplar lista de productos en la BDD
            List<ProductoIngredienteRDTO> listaRecetas = new ArrayList<>();
            List<IngredienteDTO> listaIngredientes = ingredienteBO.obtenerIngredienteTodos();
            
            ProductoDTO producto1 = new ProductoDTO();
            producto1.setId(1L);
            producto1.setNombre("Ensalada");
            producto1.setDescripcion("Verduras y topings");
            producto1.setPrecio(100.00);
            producto1.setTipo("COMIDA");
            
            ProductoIngredienteRDTO receta1 = new ProductoIngredienteRDTO();
            receta1.setCantidad(5.0);
            receta1.setProducto(producto1);
            receta1.setIngrediente(listaIngredientes.get(0));
            
            ProductoIngredienteRDTO receta2 = new ProductoIngredienteRDTO();
            receta2.setCantidad(2.0);
            receta2.setProducto(producto1);
            receta2.setIngrediente(listaIngredientes.get(1));
            
            listaRecetas.add(receta1);
            listaRecetas.add(receta2);
            
            return listaRecetas;
            
            
    }
}
