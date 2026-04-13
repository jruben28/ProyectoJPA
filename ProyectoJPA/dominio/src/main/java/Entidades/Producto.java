/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import enums.TipoProducto;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author icoro
 */
@Entity
@Table(name = "productos")
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre; 

    @Column(name = "descripcion")
    private String descripcion; 

    @Column(name = "precio", nullable = false)
    private Double precio; 

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoProducto tipo;

    @Lob
    @Column(name = "imagen")
    private byte[] imagen; 

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    // Relación con la tabla intermedia (Un producto tiene muchos ingredientes en su receta)
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoIngrediente> ingredientes;

   
    public Producto() {
    }

    public Producto(String nombre, String descripcion, Double precio, TipoProducto tipo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipo = tipo;
        this.activo = true; // Por defecto activo al crearse
    }

    
    public Boolean estaDisponible() {
        if (!this.activo) return false;
        
      
        for (ProductoIngrediente pi : ingredientes) {
            if (pi.getIngrediente().getStock() < pi.getCantidad()) {
                return false;
            }
        }
        return true;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public TipoProducto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<ProductoIngrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<ProductoIngrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

   
}