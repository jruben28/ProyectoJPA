/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import com.dtos.ComandaDTO;
import entidades.Comanda;
import excepciones.NegocioException;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author adrian mendoza
 */
public interface IComandaBO {

    /**
     * Abre una comanda nueva.
     * @param dto debe tener idMesa; idCliente es opcional.
     * @return la comanda persistida.
     * @throws NegocioException si la mesa está ocupada, no existe, etc.
     */
    Comanda abrirComanda(ComandaDTO dto) throws NegocioException;

    /**
     * Agrega un detalle de Producto a una comanda ABIERTA.
     * Valida disponibilidad (activo + stock suficiente) y recalcula el total.
     */
    void agregarDetalleProducto(Long idComanda, Long idProducto, Integer cantidad, String comentario) throws NegocioException;

    /**
     * Agrega un detalle de Combo a una comanda ABIERTA.
     * Valida disponibilidad (activo + todos los ingredientes con stock) y recalcula el total.
     */
    void agregarDetalleCombo(Long idComanda, Long idCombo, Integer cantidad, String comentario) throws NegocioException;

    /**
     * Marca la comanda como ENTREGADA. Exige al menos un detalle.
     * Descuenta el stock de los ingredientes y libera la mesa.
     */
    void entregarComanda(Long idComanda) throws NegocioException;

    /**
     * Marca la comanda como CANCELADA. No descuenta stock, libera la mesa.
     */
    void cancelarComanda(Long idComanda) throws NegocioException;

    /**
     * Busca una comanda por ID y la devuelve como DTO.
     */
    ComandaDTO buscarPorId(Long idComanda) throws NegocioException;

    /**
     * Busca comandas dentro de un rango de fechas. Para el Reporte de Comandas.
     */
    List<ComandaDTO> buscarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta) throws NegocioException;
    
    
    
}
