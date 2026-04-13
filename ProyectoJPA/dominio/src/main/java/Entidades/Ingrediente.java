package dominio;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ingredientes")
public class Ingrediente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "stock")
    private Double stock; // Solo ocupas esto para tu lógica de Productos

    public Ingrediente() {}

    // Getters y Setters mínimos
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Double getStock() { return stock; }
    public void setStock(Double stock) { this.stock = stock; }
}