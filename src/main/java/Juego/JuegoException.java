package Juego; // indica el paquete al que pertenece esta clase.

/**
 * Excepción propia del juego para avisar de errores controlados, como movimientos inválidos o acciones no permitidas.
 *
 * Comentarios añadidos para explicar la función de la clase, sus variables
 * y los bloques principales de código sin cambiar la lógica original.
 */
public class JuegoException extends Exception { // declara una clase que agrupa datos y métodos relacionados.
    /**
     * Constructor que inicializa los atributos principales del objeto.
     */
    public JuegoException(String mensaje) {
        super(mensaje); // ejecuta una llamada a un método para realizar una acción concreta.
    }
}