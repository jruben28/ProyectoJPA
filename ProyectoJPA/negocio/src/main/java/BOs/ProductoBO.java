/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import DAOs.ProductoDAO;
import dominio.Ingrediente;
import Entidades.Producto;
import Entidades.ProductoIngrediente;
import com.dtos.ProductoDTO;
import com.dtos.ProductoIngredienteDTO;
import enums.TipoProducto;
import interfaces.IProductoBO;
import interfaces.IProductoDAO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author icoro
 */
public class ProductoBO implements IProductoBO {

    private IProductoDAO productoDAO;

    public ProductoBO() {
        this.productoDAO = new ProductoDAO();
    }

    @Override
    public void guardarProducto(ProductoDTO productoDTO) throws Exception {
        // 1. VALIDACIÓN: Regla de negocio (Nombres no duplicados)
        Producto productoExistente = productoDAO.buscarPorNombre(productoDTO.getNombre());
        if (productoExistente != null) {
            throw new Exception("No se puede registrar: Ya existe un producto con el nombre '" + productoDTO.getNombre() + "'.");
        }

        // 2. CONVERSIÓN: Convertimos el ProductoDTO a la Entidad Producto (Dominio)
        TipoProducto tipoEnum = TipoProducto.valueOf(productoDTO.getTipo().toUpperCase());
        Producto productoEntidad = new Producto(
                productoDTO.getNombre(), 
                productoDTO.getDescripcion(), 
                productoDTO.getPrecio(), 
                tipoEnum
        );

        // 3. CONVERSIÓN DE INGREDIENTES: Llenamos la lista de la tabla intermedia
        List<ProductoIngrediente> listaIngredientesEntidad = new ArrayList<>();
        
        for (ProductoIngredienteDTO dtoIngrediente : productoDTO.getIngredientes()) {
            Ingrediente ingredienteRef = new Ingrediente();
            ingredienteRef.setId(dtoIngrediente.getIdIngrediente());
            
            // Creamos la entidad intermedia
            ProductoIngrediente pi = new ProductoIngrediente(
                    dtoIngrediente.getCantidad(), 
                    productoEntidad, 
                    ingredienteRef
            );
            
            listaIngredientesEntidad.add(pi);
        }

        // Asignamos la lista al producto principal
        productoEntidad.setIngredientes(listaIngredientesEntidad);

        // 4. GUARDADO: Mandamos al DAO
        productoDAO.guardar(productoEntidad);
    }
    @Override
    public void actualizarProducto(ProductoDTO productoDTO) throws Exception {
        // 1. Buscamos el producto original en la base de datos
        Producto productoExistente = productoDAO.buscarPorId(productoDTO.getId());
        if (productoExistente == null) {
            throw new Exception("El producto que intentas actualizar no existe.");
        }

        // 2. Validamos que si el usuario le cambió el nombre, no choque con otro ya existente
        Producto productoMismoNombre = productoDAO.buscarPorNombre(productoDTO.getNombre());
        if (productoMismoNombre != null && !productoMismoNombre.getId().equals(productoDTO.getId())) {
             throw new Exception("No se puede actualizar: Ya existe otro producto llamado '" + productoDTO.getNombre() + "'.");
        }

        // 3. Actualizamos los datos básicos
        productoExistente.setNombre(productoDTO.getNombre());
        productoExistente.setDescripcion(productoDTO.getDescripcion());
        productoExistente.setPrecio(productoDTO.getPrecio());
        productoExistente.setTipo(TipoProducto.valueOf(productoDTO.getTipo().toUpperCase()));

        // 4. Actualizamos los ingredientes (Limpiamos los viejos y metemos los nuevos)
        productoExistente.getIngredientes().clear(); // Borra los anteriores
        for (ProductoIngredienteDTO dtoIngrediente : productoDTO.getIngredientes()) {
            Ingrediente ingredienteRef = new Ingrediente();
            ingredienteRef.setId(dtoIngrediente.getIdIngrediente());
            
            ProductoIngrediente pi = new ProductoIngrediente(
                    dtoIngrediente.getCantidad(), 
                    productoExistente, 
                    ingredienteRef
            );
            productoExistente.getIngredientes().add(pi);
        }

        // 5. Mandamos a guardar los cambios
        productoDAO.actualizar(productoExistente);
    }

    @Override
    public void cambiarEstado(Long idProducto) throws Exception {
        Producto producto = productoDAO.buscarPorId(idProducto);
        if (producto == null) {
            throw new Exception("El producto no existe.");
        }
        
        // Invertimos el estado (Si era true pasa a false, y viceversa)
        producto.setActivo(!producto.getActivo()); 
        
        productoDAO.actualizar(producto);
    }

    @Override
    public List<ProductoDTO> buscarProductos(String textoBusqueda, String categoria) throws Exception {
        TipoProducto tipoEnum = null;
        
        // Convertimos el texto del ComboBox a Enum (ignoramos si dice "TODOS" o viene vacío)
        if (categoria != null && !categoria.trim().isEmpty() && !categoria.equalsIgnoreCase("TODOS")) {
            tipoEnum = TipoProducto.valueOf(categoria.toUpperCase());
        }
        
        // Buscamos en la base de datos usando tu DAO
        List<Producto> productosBD = productoDAO.buscarPorFiltros(textoBusqueda, tipoEnum);
        List<ProductoDTO> listaDTO = new ArrayList<>();
        
        // Convertimos las Entidades a DTOs para mandarlas a la Pantalla
        for (Producto p : productosBD) {
            ProductoDTO dto = new ProductoDTO();
            dto.setId(p.getId());
            dto.setNombre(p.getNombre());
            dto.setDescripcion(p.getDescripcion());
            dto.setPrecio(p.getPrecio());
            dto.setTipo(p.getTipo().name());
            
          
            listaDTO.add(dto);
        }
        
        return listaDTO;
    }
}
