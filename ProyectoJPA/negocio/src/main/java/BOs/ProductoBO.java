/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import DAOs.ProductoDAO;
import entidades.Producto;
import entidades.ProductoIngrediente;
import com.dtos.ProductoDTO;
import com.dtos.ProductoIngredienteDTO;
import entidades.Ingrediente;
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
        Producto productoExistente = productoDAO.buscarPorNombre(productoDTO.getNombre());
        if (productoExistente != null) {
            throw new Exception("No se puede registrar: Ya existe un producto con el nombre '" + productoDTO.getNombre() + "'.");
        }

        TipoProducto tipoEnum = TipoProducto.valueOf(productoDTO.getTipo().toUpperCase());
        Producto productoEntidad = new Producto(
                productoDTO.getNombre(), 
                productoDTO.getDescripcion(), 
                productoDTO.getPrecio(), 
                tipoEnum
        );
        
        // Asignamos la URL a la entidad antes de guardar
        productoEntidad.setUrlImagen(productoDTO.getUrlImagen());

        List<ProductoIngrediente> listaIngredientesEntidad = new ArrayList<>();
        for (ProductoIngredienteDTO dtoIngrediente : productoDTO.getIngredientes()) {
            Ingrediente ingredienteRef = new Ingrediente();
            ingredienteRef.setId(dtoIngrediente.getIdIngrediente());
            
            ProductoIngrediente pi = new ProductoIngrediente(
                    dtoIngrediente.getCantidad(), 
                    productoEntidad, 
                    ingredienteRef
            );
            listaIngredientesEntidad.add(pi);
        }

        productoEntidad.setIngredientes(listaIngredientesEntidad);
        productoDAO.guardar(productoEntidad);
    }

    @Override
    public void actualizarProducto(ProductoDTO productoDTO) throws Exception {
        Producto productoExistente = productoDAO.buscarPorId(productoDTO.getId());
        if (productoExistente == null) {
            throw new Exception("El producto que intentas actualizar no existe.");
        }

        Producto productoMismoNombre = productoDAO.buscarPorNombre(productoDTO.getNombre());
        if (productoMismoNombre != null && !productoMismoNombre.getId().equals(productoDTO.getId())) {
             throw new Exception("No se puede actualizar: Ya existe otro producto llamado '" + productoDTO.getNombre() + "'.");
        }

        productoExistente.setNombre(productoDTO.getNombre());
        productoExistente.setDescripcion(productoDTO.getDescripcion());
        productoExistente.setPrecio(productoDTO.getPrecio());
        productoExistente.setTipo(TipoProducto.valueOf(productoDTO.getTipo().toUpperCase()));
        
        // Actualizamos la URL
        productoExistente.setUrlImagen(productoDTO.getUrlImagen());

        productoExistente.getIngredientes().clear(); 
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

        productoDAO.actualizar(productoExistente);
    }

    @Override
    public void cambiarEstado(Long idProducto) throws Exception {
        Producto producto = productoDAO.buscarPorId(idProducto);
        if (producto == null) {
            throw new Exception("El producto no existe.");
        }
        producto.setActivo(!producto.getActivo()); 
        productoDAO.actualizar(producto);
    }

    @Override
    public List<ProductoDTO> buscarProductos(String textoBusqueda, String categoria) throws Exception {
        TipoProducto tipoEnum = null;
        if (categoria != null && !categoria.trim().isEmpty() && !categoria.equalsIgnoreCase("TODOS")) {
            tipoEnum = TipoProducto.valueOf(categoria.toUpperCase());
        }
        
        List<Producto> productosBD = productoDAO.buscarPorFiltros(textoBusqueda, tipoEnum);
        List<ProductoDTO> listaDTO = new ArrayList<>();
        
        for (Producto p : productosBD) {
            ProductoDTO dto = new ProductoDTO();
            dto.setId(p.getId());
            dto.setNombre(p.getNombre());
            dto.setDescripcion(p.getDescripcion());
            dto.setPrecio(p.getPrecio());
            dto.setTipo(p.getTipo().name());
            
            // Extraemos la URL de la base de datos hacia la pantalla
            dto.setUrlImagen(p.getUrlImagen());
            
            listaDTO.add(dto);
        }
        
        return listaDTO;
    }
}