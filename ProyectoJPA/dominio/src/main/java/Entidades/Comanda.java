/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import enums.EstadoComanda;
import java.io.Serializable;
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

        public Comanda(Double total, EstadoComanda estado, Cliente cliente) {
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

