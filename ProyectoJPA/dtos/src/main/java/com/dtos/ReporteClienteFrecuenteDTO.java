package com.dtos;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO para las filas del Reporte de Clientes Frecuentes.
 *
 * @author Devora
 */
public class ReporteClienteFrecuenteDTO implements Serializable {

    private String nombre;
    private Integer numVisitas;
    private Double totalGastado;
    private Date fechaUltimaComanda;

    public ReporteClienteFrecuenteDTO() {
    }

    public ReporteClienteFrecuenteDTO(String nombre, Integer numVisitas,
                                      Double totalGastado, Date fechaUltimaComanda) {
        this.nombre = nombre;
        this.numVisitas = numVisitas;
        this.totalGastado = totalGastado;
        this.fechaUltimaComanda = fechaUltimaComanda;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getNumVisitas() { return numVisitas; }
    public void setNumVisitas(Integer numVisitas) { this.numVisitas = numVisitas; }

    public Double getTotalGastado() { return totalGastado; }
    public void setTotalGastado(Double totalGastado) { this.totalGastado = totalGastado; }

    public Date getFechaUltimaComanda() { return fechaUltimaComanda; }
    public void setFechaUltimaComanda(Date fechaUltimaComanda) { this.fechaUltimaComanda = fechaUltimaComanda; }
}
