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

    
}