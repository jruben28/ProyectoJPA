/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 *
 * @author keppler
 */
public class NegocioException extends RuntimeException{

    public NegocioException() {
    super();
    }
    
    public NegocioException(String mensaje) {
    super(mensaje);
    }
    
    
    public NegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
}
