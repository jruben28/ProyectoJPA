/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dtos;

import java.util.Date;

/**
 *
 * @author icoro
 */
// Hereda de ClienteDTO para reutilizar id y nombre
public class ClienteFrecuenteDTO extends ClienteDTO {
    private String telefono;
    private String correo;
    private Date fechaRegistro;
    private Integer puntosAcumulados;
    private Double totalGastado;
    private Integer numVisitas;

    public ClienteFrecuenteDTO() {
        super();
    }

    // Constructor completo para usar en la capa de Negocio al convertir Entidad -> DTO
    public ClienteFrecuenteDTO(Long id, String nombre, String tipoCliente, 
                               String telefono, String correo, Date fechaRegistro, 
                               Integer puntosAcumulados) {
        // Llamamos al constructor de la clase madre (ClienteDTO)
        super(id, nombre, tipoCliente); 
        this.telefono = telefono;
        this.correo = correo;
        this.fechaRegistro = fechaRegistro;
        this.puntosAcumulados = puntosAcumulados;
    }

    // Getters y Setters específicos
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Integer getPuntosAcumulados() { return puntosAcumulados; }
    public void setPuntosAcumulados(Integer puntosAcumulados) { this.puntosAcumulados = puntosAcumulados; }

    public Double getTotalGastado() { return totalGastado; }
    public void setTotalGastado(Double totalGastado) { this.totalGastado = totalGastado; }

    public Integer getNumVisitas() { return numVisitas; }
    public void setNumVisitas(Integer numVisitas) { this.numVisitas = numVisitas; }
}
