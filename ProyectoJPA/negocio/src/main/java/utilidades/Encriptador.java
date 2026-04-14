/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;

import java.util.Base64;

/**
 * Encriptador para numeros de telefono, usa base 64 de la clase util para esta
 * labor.
 *
 * @author keppler
 */
public class Encriptador {

    /**
     * Encripta un string
     *
     * @param nTelefono
     * @return String cifrado
     */
    public static String encriptar(String nTelefono) {
        if (nTelefono == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(nTelefono.getBytes());
    }

    /**
     * Desencripta un string
     *
     * @param nTelefonoCifrado
     * @return String descifrado
     */
    public static String desencriptar(String nTelefonoCifrado) {
        if (nTelefonoCifrado == null) {
            return null;
        }
        return new String(Base64.getDecoder().decode(nTelefonoCifrado));
    }

    /**
     *
     * @param telefonoDesencriptado
     * @return
     */
    public static String mostrarUltimosNumeros(String telefonoDesencriptado) {
        if (telefonoDesencriptado == null || telefonoDesencriptado.length() < 4) {
            return "****";
        }
        return "******" + telefonoDesencriptado.substring(telefonoDesencriptado.length() - 4);
    }
}
