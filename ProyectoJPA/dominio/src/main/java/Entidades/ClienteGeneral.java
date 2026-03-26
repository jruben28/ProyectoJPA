/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author icoro
 */
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Representa a un comensal no registrado formalmente en el sistema.
 * Hereda de la clase Cliente.
 */
@Entity
@Table(name = "clientes_generales")
public class ClienteGeneral extends Cliente implements Serializable {


    // Constructor vacío requerido por JPA
    public ClienteGeneral() {
        super();
    }

    // Constructor para inicializar con un nombre (ej. "Cliente General")
    public ClienteGeneral(String nombre) {
        super(nombre);
    }

    
}
