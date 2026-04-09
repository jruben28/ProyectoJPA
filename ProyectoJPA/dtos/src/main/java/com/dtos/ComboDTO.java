/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dtos;

/**
 *
 * @author keppler
 */
public class ComboDTO {
    private String nombre; 
    private String descripcion; 
    private Double precioCombo; 
    private Double precioOriginal;
    private Integer porcentajeDescuento; 
    private Boolean activo; 

    public ComboDTO() {
    }
     
    public ComboDTO(String nombre, String descripcion, Double precioCombo, Double precioOriginal, Integer porcentajeDescuento){
        this.nombre=nombre;
        this.descripcion=descripcion;
        this.precioCombo=precioCombo;
        this.precioOriginal=precioOriginal;
        this.porcentajeDescuento=porcentajeDescuento;
        this.activo=true;
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

    public Double getPrecioCombo() {
        return precioCombo;
    }

    public void setPrecioCombo(Double precioCombo) {
        this.precioCombo = precioCombo;
    }

    public Double getPrecioOriginal() {
        return precioOriginal;
    }

    public void setPrecioOriginal(Double precioOriginal) {
        this.precioOriginal = precioOriginal;
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
    
}
