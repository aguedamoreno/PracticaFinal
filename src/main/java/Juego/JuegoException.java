package Juego;

/** Excepción propia del juego para avisar de errores controlados, como movimientos inválidos o acciones no permitidas
 */
public class JuegoException extends Exception { // declara una clase que agrupa datos y métodos relacionados

    /** Constructor que inicializa los atributos principales del objeto
     */
    public JuegoException(String mensaje) {
        super(mensaje); // ejecuta una llamada a un metodo para realizar una acción concreta
    }
}