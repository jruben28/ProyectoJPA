/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dtos;

/**
 *
 * @author icoro
 */
public class ProductoIngredienteDTO {
    private Long idIngrediente;
    private String nombreIngrediente; //  para mostrar en tablas de la interfaz
    private Double cantidad;

    public ProductoIngredienteDTO() {}

    public ProductoIngredienteDTO(Long idIngrediente, String nombreIngrediente, Double cantidad) {
        this.idIngrediente = idIngrediente;
        this.nombreIngrediente = nombreIngrediente;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public Long getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(Long idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getNombreIngrediente() {
        return nombreIngrediente;
    }

    public void setNombreIngrediente(String nombreIngrediente) {
        this.nombreIngrediente = nombreIngrediente;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
}