/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author icoro
 */
@Entity
@Table(name = "clientes_frecuentes")
public class ClienteFrecuente extends Cliente implements Serializable{
    
    @Column(nullable = false)
    private String telefono;

    @Column(nullable = true)
    private String correo; 

    @Temporal(TemporalType.DATE)
    private Date fechaRegistro; 

    public ClienteFrecuente() {
    super();
    }

     public ClienteFrecuente(String nombre, String telefono, String correo) {
        super(nombre);
        this.telefono = telefono;
        this.correo = correo;
        this.fechaRegistro = new Date();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
}