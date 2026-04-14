/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import enums.EstadoComanda;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author keppler
 */

    @Entity
    @Table(name = "comandas")
    public class Comanda implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true)
        private String folio;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "fecha_hora")
        private Date fechaHora;

        @Column(name = "num_mesa")
        private Integer numMesa;

        @Column(nullable = false)
        private Double total;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private EstadoComanda estado;

        @ManyToOne
        @JoinColumn(name = "id_cliente", nullable = true)
        private Cliente cliente;

        public Comanda() {
        }

        public Comanda(String folio, Date fechaHora, Integer numMesa, Double total, EstadoComanda estado, Cliente cliente) {
            this.folio = folio;
            this.fechaHora = fechaHora;
            this.numMesa = numMesa;
            this.total = total;
            this.estado = estado;
            this.cliente = cliente;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFolio() {
            return folio;
        }

        public void setFolio(String folio) {
            this.folio = folio;
        }

        public Date getFechaHora() {
            return fechaHora;
        }

        public void setFechaHora(Date fechaHora) {
            this.fechaHora = fechaHora;
        }

        public Integer getNumMesa() {
            return numMesa;
        }

        public void setNumMesa(Integer numMesa) {
            this.numMesa = numMesa;
        }

        public Double getTotal() {
            return total;
        }

        public void setTotal(Double total) {
            this.total = total;
        }

        public EstadoComanda getEstado() {
            return estado;
        }

        public void setEstado(EstadoComanda estado) {
            this.estado = estado;
        }

        public Cliente getCliente() {
            return cliente;
        }

        public void setCliente(Cliente cliente) {
            this.cliente = cliente;
        }
    }

