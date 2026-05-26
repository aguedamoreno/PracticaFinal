package Juego; // indica el paquete al que pertenece esta clase.

/**
 * Clase que representa cada casilla de una habitación del mapa. Una celda tiene un tipo, puede contener objetos/enemigos/puertas y puede ser accesible o no.
 *
 * Comentarios añadidos para explicar la función de la clase, sus variables
 * y los bloques principales de código sin cambiar la lógica original.
 */
public class Celda { // declara una clase que agrupa datos y métodos relacionados.
    public enum Tipo { // declara un conjunto cerrado de valores posibles.
        VACIA,
        ENEMIGO,
        OBJETO,
        PUERTA,
        TRAMPA,
        SALIDA
    }

    private Tipo tipo; // declara un atributo/campo de la clase donde se guarda estado.
    private Object contenido; // Puede ser Enemigo, Objeto, null, etc.
    private boolean accesible; // Controla si es un muro/obstáculo infranqueable

    /**
     * Constructor que inicializa los atributos principales del objeto.
     */
    public Celda() {
        this.tipo = Tipo.VACIA; // guarda el valor recibido dentro del atributo del objeto actual.
        this.contenido = null; // guarda el valor recibido dentro del atributo del objeto actual.
        this.accesible = true; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Constructor que inicializa los atributos principales del objeto.
     */
    public Celda(Tipo tipo) {
        this.tipo = tipo; // guarda el valor recibido dentro del atributo del objeto actual.
        this.contenido = null; // guarda el valor recibido dentro del atributo del objeto actual.
        this.accesible = true; // Por defecto todas permiten interactuar/entrar (salvo si implementas PARED)
    }

    /**
     * Constructor que inicializa los atributos principales del objeto.
     */
    public Celda(Tipo tipo, Object contenido) {
        this.tipo = tipo; // guarda el valor recibido dentro del atributo del objeto actual.
        this.contenido = contenido; // guarda el valor recibido dentro del atributo del objeto actual.
        this.accesible = true; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public Tipo getTipo() {
        return tipo; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public Object getContenido() {
        return contenido; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setContenido(Object contenido) {
        this.contenido = contenido; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Devuelve verdadero o falso según el estado actual del objeto.
     */
    public boolean isAccesible() {
        return accesible; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setAccesible(boolean accesible) {
        this.accesible = accesible; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * CORREGIDO: Ahora el jugador puede transitar o entrar a casillas
     * con enemigos, objetos y trampas para interactuar con ellos.
     */
    public boolean estaLibreParaMovimiento() {
        return accesible && ( // devuelve el resultado calculado por el método.
                tipo == Tipo.VACIA ||
                        tipo == Tipo.PUERTA ||
                        tipo == Tipo.SALIDA ||
                        tipo == Tipo.TRAMPA ||
                        tipo == Tipo.OBJETO ||
                        tipo == Tipo.ENEMIGO
        ); // ejecuta una llamada a un método para realizar una acción concreta.
    }

    /**
     * Método de apoyo usado por la clase para completar la lógica del juego.
     */
    public void limpiar() {
        this.tipo = Tipo.VACIA; // guarda el valor recibido dentro del atributo del objeto actual.
        this.contenido = null; // guarda el valor recibido dentro del atributo del objeto actual.
        this.accesible = true; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    @Override
    /**
     * Devuelve una representación en texto del objeto para mostrarlo fácilmente.
     */
    public String toString() {
        switch (tipo) {
            case VACIA: return "[ ]";
            case ENEMIGO: return "[E]";
            case OBJETO: return "[O]";
            case PUERTA: return "[P]";
            case TRAMPA: return "[T]";
            case SALIDA: return "[S]";
            default: return "[?]";
        }
    }
}



