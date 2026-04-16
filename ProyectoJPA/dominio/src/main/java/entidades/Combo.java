/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entidad Combo, representa un paquete de productos con promocion 
 * @author Adrian Mendoza
 */
@Entity
@Table(name = "combos")
public class Combo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String nombre;
    
    @Column(nullable = true, length = 650)
    private String descripcion;
    
    @Column(nullable = false)
    private Double precioCombo; 
    
    @Column(nullable = false)
    private Double precioOriginal;
    
    @Column(nullable = false)
    private Integer porcentajeDescuento;
    
    @Column(nullable = false)
    private Boolean activo;
    
    @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComboProducto> productos;

    public Combo() {
        this.productos = new ArrayList<>();
    }
    
    public Combo(String nombre, String descripcion, Double precioOriginal, Double precioCombo, Integer porcentajeDescuento) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioCombo = precioCombo;
        this.precioOriginal = precioOriginal;
        this.porcentajeDescuento = porcentajeDescuento;
        this.activo = true;
        this.productos = new ArrayList<>();
    }
    
    public Long getId() {
        return id;
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

    public Double getPrecioOriginal() {
        return precioOriginal;
    }

    public void setPrecioOriginal(Double precioOriginal) {
        this.precioOriginal = precioOriginal;
    }

    public Double getPrecioCombo() {
        return precioCombo;
    }

    public void setPrecioCombo(Double precioCombo) {
        this.precioCombo = precioCombo;
    }

    public Integer getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(Integer porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    public List<ComboProducto> getProductos() {
        return productos;
    }

    public void setProductos(List<ComboProducto> productos) {
        this.productos = productos;
    }
    
    
}
