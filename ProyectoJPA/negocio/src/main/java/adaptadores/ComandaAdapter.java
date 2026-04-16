/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import entidades.Comanda;
import com.dtos.ComandaDTO;
import enums.EstadoComanda;

/**
 *
 * @author joser
 */
public class ComandaAdapter {

    public static Comanda dtoAEntidad(ComandaDTO dto) {
        if (dto == null) {
            return null;
        }
        Comanda comanda = new Comanda();
        if (dto.getId() != null) {
            comanda.setId(dto.getId());
        }
        comanda.setFolio(dto.getFolio());
        comanda.setFechaHora(dto.getFechaHora());
        if (dto.getEstado() != null) {
            comanda.setEstado(EstadoComanda.valueOf(dto.getEstado()));
        }
        Double total = dto.getTotal();
        if (total == null) {
            total = 0.0;
        }
        comanda.setTotal(total);
        return comanda;
    }

    public static ComandaDTO entidadADTO(Comanda comanda) {
        if (comanda == null) {
            return null;
        }

        ComandaDTO dto = new ComandaDTO();

        dto.setId(comanda.getId());
        dto.setFolio(comanda.getFolio());
        dto.setFechaHora(comanda.getFechaHora());
        dto.setTotal(comanda.getTotal());

        if (comanda.getEstado() != null) {
            dto.setEstado(comanda.getEstado().name());
        }

        if (comanda.getMesa() != null) {
            dto.setIdMesa(comanda.getMesa().getId());
            dto.setNumeroMesa(comanda.getMesa().getNumero());
        }

        if (comanda.getCliente() != null) {
            dto.setIdCliente(comanda.getCliente().getId());
            dto.setNombreCliente(comanda.getCliente().getNombre());
        }

        return dto;
    }
}
