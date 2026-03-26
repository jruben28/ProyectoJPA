/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dtos;

import java.io.Serializable;

/**
 *
 * @author icoro
 */
public class ClienteDTO implements Serializable {
    private Long id;
    private String nombre;
    private String tipoCliente; // "FRECUENTE" o "GENERAL" (para mostrar en la tabla)

    public ClienteDTO() {
    }

    public ClienteDTO(Long id, String nombre, String tipoCliente) {
        this.id = id;
        this.nombre = nombre;
        this.tipoCliente = tipoCliente;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
}
