/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;


import Entidades.Producto;
import enums.TipoProducto;
import java.util.List;

/**
 *
 * @author icoro
 */
public interface IProductoDAO {
    void guardar(Producto producto);
    void actualizar(Producto producto);
    Producto buscarPorNombre(String nombre); // Para validar que no haya duplicados
    List<Producto> buscarPorFiltros(String textoBusqueda, TipoProducto categoria); // Para el buscador de comandas
    Producto buscarPorId(Long id);
}
