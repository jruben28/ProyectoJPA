/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 *
 * @author keppler
 */
public class DAOException extends RuntimeException{

    public DAOException() {
    }

    public DAOException(String mensaje) {
        super(mensaje);
    }
    
    
}
