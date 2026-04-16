package adaptadores;

import entidades.Comanda;
import com.dtos.ComandaDTO;
import enums.EstadoComanda;

public class ComandaAdapter {

    public static Comanda dtoAEntidad(ComandaDTO dto) {
        if (dto == null) return null;
        
        Comanda comanda = new Comanda();
        if (dto.getId() != null) comanda.setId(dto.getId());
        
        comanda.setFolio(dto.getFolio());
        comanda.setFechaHora(dto.getFechaHora());
        
        if (dto.getEstado() != null) {
            try {
                String estado = dto.getEstado().trim().toUpperCase();
                comanda.setEstado(EstadoComanda.valueOf(estado));
            } catch (IllegalArgumentException e) {
                comanda.setEstado(EstadoComanda.ABIERTA);
            }
        }
        
        comanda.setTotal(dto.getTotal() != null ? dto.getTotal() : 0.0);
        return comanda;
    }

    public static ComandaDTO entidadADTO(Comanda comanda) {
        if (comanda == null) return null;
        ComandaDTO dto = new ComandaDTO();
        dto.setId(comanda.getId());
        dto.setFolio(comanda.getFolio());
        dto.setFechaHora(comanda.getFechaHora());
        dto.setTotal(comanda.getTotal());
        if (comanda.getEstado() != null) dto.setEstado(comanda.getEstado().name());
        return dto;
    }
}