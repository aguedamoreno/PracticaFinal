package Juego;

/** Representa una casilla individual dentro de una habitación del juego, en este proyecto el mapa se construye como una matriz de celdas
 * (cada posición del mapa es un objeto Celda). Por eso esta clase guarda toda la información necesaria para saber qué hay en esa posición
 */
public class Celda {

    /** Enumeración que define todos los tipos posibles de casilla dentro del mapa
     * Usar un enum evita tener que escribir textos como "VACIA" o "ENEMIGO" a mano, reduciendo errores
     */
    public enum Tipo {
        VACIA,      // Casilla sin nada especial, normalmente el jugador puede pasar por ella
        ENEMIGO,   // Casilla que contiene un enemigo con el que se puede combatir
        OBJETO,    // Casilla que contiene un objeto que el jugador puede recoger
        PUERTA,    // Casilla que representa una puerta entre habitaciones
        TRAMPA,    // Casilla peligrosa que puede afectar al jugador al pasar por ella
        SALIDA     // Casilla que representa el final de una partida
    }

    /** Metodo que indica qué tipo de casilla es actualmente
    */
    private Tipo tipo;

    /** Metodo que guarda el contenido real que hay dentro de la celda
     * Es de tipo Object porque puede guardar distintos tipos de objetos:
     * - Un Enemigo, si la celda es de tipo ENEMIGO.
     * - Un Objeto, si la celda es de tipo OBJETO.
     * - null, si la celda está vacía o no necesita guardar nada concreto
     * Ejemplo:
     * tipo = Tipo.ENEMIGO
     * contenido = un enemigo concreto con vida, ataque, defensa, etc
     */
    private Object contenido;

    /** Metodo que indica si el jugador puede entrar o interactuar con esta casilla
     * Si vale true, la celda es accesible
     * Si vale false, sería un obstáculo o muro
     * En este código, por defecto las celdas se crean accesibles, porque incluso las celdas con enemigos, objetos o trampas pueden
     * necesitar ser seleccionadas o visitadas para que ocurra la acción
     */
    private boolean accesible;

    /** Constructor que construye una celda por defecto, vacía, sin contenido y accesible
     * Sirve para crear casillas normales del mapa antes de colocar enemigos, objetos, trampas, puertas o salidas.
     */
    public Celda() {
        // Al principio la casilla no tiene nada especial
        this.tipo = Tipo.VACIA;
        // Como está vacía, no guarda ningún enemigo ni objeto
        this.contenido = null;
        // Se marca como accesible para que el jugador pueda pasar por ella
        this.accesible = true;
    }

    /** Constructor que crea una celda indicando solo su tipo
     * Se usa cuando queremos decir qué representa la casilla pero todavía no necesitamos guardar un contenido concreto
    */
    public Celda(Tipo tipo) {
        // Guarda el tipo recibido como tipo actual de la celda
        this.tipo = tipo;
        // No se asigna contenido porque este constructor solo recibe el tipo
        this.contenido = null;
        // Por defecto se permite acceder a la celda
        this.accesible = true;
    }

    /** Constructor que crea una celda indicando su tipo y su contenido.
     * Se usa cuando la casilla necesita guardar algo real dentro
     */
    public Celda(Tipo tipo, Object contenido) {
        // Guarda el tipo de casilla que se ha indicado
        this.tipo = tipo;
        // Guarda el objeto real que hay dentro de la celda
        this.contenido = contenido;
        // La celda se crea accesible salvo que después se cambie
        this.accesible = true;
    }

    /** Metodo que devuelve el tipo actual de la celda
     * Este metodo se usa desde otras clases para saber qué hay en una posición del mapa sin tocar directamente el atributo tipo
     */
    public Tipo getTipo() {
        return tipo;
    }

    /** Metodo que cambia el tipo de la celda
     * Sirve, por ejemplo, para convertir una celda en vacía después de recoger un objeto
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    /** Metodo que devuelve el contenido guardado dentro de la celda
     * Como devuelve Object, la clase que lo use tendrá que saber qué espera encontrar según el tipo de la celda
     */
    public Object getContenido() {
        return contenido;
    }

    /** Metodo que cambia el contenido guardado en la celda
    */
    public void setContenido(Object contenido) {
        this.contenido = contenido;
    }

    /** Metodo que indica si la celda es accesible
    */
    public boolean isAccesible() {
        return accesible;
    }

    /** Metodo que cambia si la celda es accesible o no
     * Esto permite bloquear una casilla si en algún momento se quiere representar una pared, obstáculo o zona no transitable
     */
    public void setAccesible(boolean accesible) {
        this.accesible = accesible;
    }

    /** Metodo que comprueba si el jugador puede moverse hacia esta celda
     * Primero exige que la celda sea accesible. Si accesible es false, no se puede pasar
     * Después comprueba que el tipo de celda sea uno de los permitidos
     */
    public boolean estaLibreParaMovimiento() {
        return accesible && (
                // Casilla normal sin nada
                tipo == Tipo.VACIA ||
                        // Puerta por la que se puede pasar o interactuar
                        tipo == Tipo.PUERTA ||
                        // Salida de la partida
                        tipo == Tipo.SALIDA ||
                        // Trampa, se permite entrar para que pueda activarse su efecto
                        tipo == Tipo.TRAMPA ||
                        // Objeto, se permite entrar e interactuar para poder recogerlo
                        tipo == Tipo.OBJETO ||
                        // Enemigo, se permite seleccionar o entrar
                        tipo == Tipo.ENEMIGO
        );
    }

    /** Metodo que limpia completamente la celda
     * Después de llamar a este metodo, la celda queda como una casilla normal: vacía, sin contenido y accesible
    */
    public void limpiar() {
        // La casilla deja de tener objeto, enemigo, trampa, puerta, etc...
        this.tipo = Tipo.VACIA;
        // Se elimina cualquier objeto real que estuviera guardado
        this.contenido = null;
        // Se asegura que la casilla vuelva a ser transitable
        this.accesible = true;
    }

    /** Metodo que convierte la celda en texto para poder verla de forma sencilla
     */
    @Override
    public String toString() {
        return switch (tipo) {  // dependiendo de los tipos, devuelve un texto distinto
            case VACIA ->   // Casilla sin contenido
                    "[ ]";
            case ENEMIGO -> // Casilla ocupada por un enemigo
                    "[E]";
            case OBJETO ->  // Casilla con un objeto recogible
                    "[O]";
            case PUERTA ->  // Casilla que representa una puerta
                    "[P]";
            case TRAMPA ->  // Casilla que representa una trampa
                    "[T]";
            case SALIDA ->   // Casilla que representa la salida
                    "[S]";
            default ->  // Caso de seguridad por si aparece un tipo no contemplado
                    "[?]";
        };
    }
}




