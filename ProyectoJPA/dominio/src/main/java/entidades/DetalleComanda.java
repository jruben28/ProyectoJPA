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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author keppler
 */
@Entity
@Table(name = "detalles_comanda")
public class DetalleComanda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(length = 300)
    private String comentario;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @ManyToOne
    @JoinColumn(name = "id_comanda", nullable = false)
    private Comanda comanda;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = true)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_combo", nullable = true)
    private Combo combo;

    public DetalleComanda() {
    }

    public DetalleComanda(Integer cantidad, String comentario, Producto producto) {
        this.cantidad = cantidad;
        this.comentario = comentario;
        this.producto = producto;
        this.precioUnitario = producto.getPrecio();
    }

    public DetalleComanda(Integer cantidad, String comentario, Combo combo) {
        this.cantidad = cantidad;
        this.comentario = comentario;
        this.combo = combo;
        this.precioUnitario = combo.getPrecioCombo();
    }

    public Double calcularSubtotal() {
        return precioUnitario * cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Comanda getComanda() {
        return comanda;
    }

    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }
}
