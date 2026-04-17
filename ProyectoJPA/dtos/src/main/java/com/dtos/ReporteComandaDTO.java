package com.dtos;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO para las filas del Reporte de Comandas.
 *
 * @author Devora
 */
public class ReporteComandaDTO implements Serializable {

    private String folio;
    private Date fechaHora;
    private Integer numMesa;
    private Double total;
    private String estado;
    private String nombreCliente;

    public ReporteComandaDTO() {
    }

    public ReporteComandaDTO(String folio, Date fechaHora, Integer numMesa,
                             Double total, String estado, String nombreCliente) {
        this.folio = folio;
        this.fechaHora = fechaHora;
        this.numMesa = numMesa;
        this.total = total;
        this.estado = estado;
        this.nombreCliente = nombreCliente;
    }

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }

    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }

    public Integer getNumMesa() { return numMesa; }
    public void setNumMesa(Integer numMesa) { this.numMesa = numMesa; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
}
