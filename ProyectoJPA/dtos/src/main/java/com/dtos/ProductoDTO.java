package com.dtos;

import com.dtos.ProductoIngredienteDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author icoro
 */
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String tipo;
    private Boolean activo; 
    private String urlImagen; // NUEVO: Atributo para la imagen
    private List<ProductoIngredienteDTO> ingredientes;

    public ProductoDTO() {
        this.ingredientes = new ArrayList<>();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    // NUEVO: Getter y setter de la URL
    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
    
    public List<ProductoIngredienteDTO> getIngredientes() { return ingredientes; }
    public void setIngredientes(List<ProductoIngredienteDTO> ingredientes) { this.ingredientes = ingredientes; }
}