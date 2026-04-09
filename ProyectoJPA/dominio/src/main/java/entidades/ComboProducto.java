/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entidad ComboProducto, es la relación entre Combo y Producto, aqui se define cuales productos tendra un combo 
 * @author keppler
 */
@Entity
@Table(name="combo_productos")
public class ComboProducto implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="id_combo", nullable=false)
    private Long idCombo;   
    
    @Column(name="id_producto",nullable=false)
    private Long idProducto;
    
    @Column(nullable=false)
    private Integer cantidad;
    
    public ComboProducto(){
        
    }

    public ComboProducto(Long idCombo, Long idProducto, Integer cantidad) {
    this.idCombo = idCombo;
    this.idProducto = idProducto;
    this.cantidad = cantidad;
}

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Long getIdCombo() {
        return idCombo;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public Long getId() {
        return id;
    }

    
    
    
    
    
    
    
}
