/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import com.dtos.ProductoDTO;
import java.util.List;

/**
 *
 * @author icoro
 */
public interface IProductoBO {
  void guardarProducto(ProductoDTO productoDTO) throws Exception;
    void actualizarProducto(ProductoDTO productoDTO) throws Exception;
    void cambiarEstado(Long idProducto) throws Exception;
    List<ProductoDTO> buscarProductos(String textoBusqueda, String categoria) throws Exception;
}
