/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dtos;

import enums.UnidadDeMedida;


/**
 *
 * @author joser
 */
public class IngredienteDTO {
    private Long idIngrediente;
    private String nombre;
    private UnidadDeMedida unidadDeMedida;
    private Double stock;
    private String urlImagen;

    public IngredienteDTO() {
    }

    public IngredienteDTO(Long idIngrediente, String nombre, UnidadDeMedida unidadDeMedida, Double stock, String urlImagen) {
        this.idIngrediente = idIngrediente;
        this.nombre = nombre;
        this.unidadDeMedida = unidadDeMedida;
        this.stock = stock;
        this.urlImagen = urlImagen;
    }

    public Long getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(Long idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public UnidadDeMedida getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(UnidadDeMedida unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }


    
    
    
}
