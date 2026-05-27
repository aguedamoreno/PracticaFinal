package Juego;

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

    /** Metodp que indica qué tipo de casilla es actualmente
    */
    private Tipo tipo;

    /**
     * Guarda el contenido real que hay dentro de la celda.
     *
     * Es de tipo Object porque puede guardar distintos tipos de objetos:
     * - Un Enemigo, si la celda es de tipo ENEMIGO.
     * - Un Objeto, si la celda es de tipo OBJETO.
     * - null, si la celda está vacía o no necesita guardar nada concreto.
     *
     * El tipo de la celda dice "qué clase de cosa hay",
     * y contenido guarda "la cosa concreta".
     *
     * Ejemplo:
     * tipo = Tipo.ENEMIGO
     * contenido = un enemigo concreto con vida, ataque, defensa, etc.
     */
    private Object contenido;

    /**
     * Indica si el jugador puede entrar o interactuar con esta casilla.
     *
     * Si vale true, la celda se considera accesible.
     * Si vale false, se comportaría como un obstáculo o muro.
     *
     * En este código, por defecto las celdas se crean accesibles,
     * porque incluso las celdas con enemigos, objetos o trampas pueden
     * necesitar ser seleccionadas o visitadas para que ocurra la acción.
     */
    private boolean accesible;

    /**
     * Constructor por defecto.
     *
     * Crea una celda vacía, sin contenido y accesible.
     *
     * Este constructor sirve para crear casillas normales del mapa
     * antes de colocar enemigos, objetos, trampas, puertas o salidas.
     */
    public Celda() {
        // Al principio la casilla no tiene nada especial.
        this.tipo = Tipo.VACIA;

        // Como está vacía, no guarda ningún enemigo ni objeto.
        this.contenido = null;

        // Se marca como accesible para que el jugador pueda pasar por ella.
        this.accesible = true;
    }

    /**
     * Constructor que crea una celda indicando solo su tipo.
     *
     * Se usa cuando queremos decir qué representa la casilla
     * pero todavía no necesitamos guardar un contenido concreto.
     *
     * Por ejemplo:
     * new Celda(Tipo.TRAMPA)
     * new Celda(Tipo.SALIDA)
     *
     * @param tipo tipo inicial de la casilla.
     */
    public Celda(Tipo tipo) {
        // Guarda el tipo recibido como tipo actual de la celda.
        this.tipo = tipo;

        // No se asigna contenido porque este constructor solo recibe el tipo.
        this.contenido = null;

        // Por defecto se permite acceder a la celda.
        this.accesible = true;
    }

    /**
     * Constructor que crea una celda indicando su tipo y su contenido.
     *
     * Se usa cuando la casilla necesita guardar algo real dentro.
     *
     * Por ejemplo:
     * - Una celda de tipo ENEMIGO con un objeto Enemigo dentro.
     * - Una celda de tipo OBJETO con un objeto del inventario dentro.
     *
     * @param tipo tipo inicial de la casilla.
     * @param contenido objeto concreto guardado en la casilla.
     */
    public Celda(Tipo tipo, Object contenido) {
        // Guarda el tipo de casilla que se ha indicado.
        this.tipo = tipo;

        // Guarda el objeto real que hay dentro de la celda.
        this.contenido = contenido;

        // La celda se crea accesible salvo que después se cambie con setAccesible(false).
        this.accesible = true;
    }

    /**
     * Devuelve el tipo actual de la celda.
     *
     * Este método se usa desde otras clases para saber qué hay en una posición
     * del mapa sin tocar directamente el atributo privado tipo.
     *
     * @return tipo actual de la celda.
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * Cambia el tipo de la celda.
     *
     * Sirve, por ejemplo, para convertir una celda en VACIA después de recoger
     * un objeto, o para marcar una posición como ENEMIGO, TRAMPA, PUERTA, etc.
     *
     * @param tipo nuevo tipo que tendrá la celda.
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    /**
     * Devuelve el contenido guardado dentro de la celda.
     *
     * Como devuelve Object, la clase que lo use tendrá que saber qué espera
     * encontrar según el tipo de la celda.
     *
     * Ejemplo:
     * si getTipo() devuelve ENEMIGO, normalmente el contenido será un Enemigo.
     *
     * @return objeto guardado en la celda, o null si no hay contenido.
     */
    public Object getContenido() {
        return contenido;
    }

    /**
     * Cambia el contenido guardado en la celda.
     *
     * Se usa cuando se coloca un enemigo, un objeto, o cuando se elimina
     * el contenido de una casilla.
     *
     * @param contenido nuevo objeto que quedará guardado en la celda.
     */
    public void setContenido(Object contenido) {
        this.contenido = contenido;
    }

    /**
     * Indica si la celda es accesible.
     *
     * El nombre empieza por "is" porque es un getter de un boolean.
     *
     * @return true si se puede acceder a la celda, false si está bloqueada.
     */
    public boolean isAccesible() {
        return accesible;
    }

    /**
     * Cambia si la celda es accesible o no.
     *
     * Esto permite bloquear una casilla si en algún momento se quiere
     * representar una pared, obstáculo o zona no transitable.
     *
     * @param accesible true si se puede entrar, false si no.
     */
    public void setAccesible(boolean accesible) {
        this.accesible = accesible;
    }

    /**
     * Comprueba si el jugador puede moverse hacia esta celda.
     *
     * Primero exige que la celda sea accesible. Si accesible es false,
     * no se puede pasar aunque el tipo sea VACIA, OBJETO, ENEMIGO, etc.
     *
     * Después comprueba que el tipo de celda sea uno de los permitidos
     * para el movimiento dentro de este juego.
     *
     * En este proyecto se permite moverse también a celdas con objetos,
     * enemigos o trampas porque entrar o seleccionar esas casillas puede
     * activar acciones del juego: recoger, combatir o sufrir una trampa.
     *
     * @return true si el jugador puede intentar moverse a esta celda.
     */
    public boolean estaLibreParaMovimiento() {
        return accesible && (
                // Casilla normal sin nada.
                tipo == Tipo.VACIA ||

                        // Puerta por la que se puede pasar o interactuar.
                        tipo == Tipo.PUERTA ||

                        // Salida de la habitación o de la partida.
                        tipo == Tipo.SALIDA ||

                        // Trampa: se permite entrar para que pueda activarse su efecto.
                        tipo == Tipo.TRAMPA ||

                        // Objeto: se permite entrar/interactuar para poder recogerlo.
                        tipo == Tipo.OBJETO ||

                        // Enemigo: se permite seleccionar o entrar según la lógica del motor.
                        tipo == Tipo.ENEMIGO
        );
    }

    /**
     * Limpia completamente la celda.
     *
     * Después de llamar a este método, la celda queda como una casilla normal:
     * vacía, sin contenido y accesible.
     *
     * Se puede usar, por ejemplo, después de:
     * - recoger un objeto,
     * - eliminar un enemigo,
     * - desactivar una trampa,
     * - reiniciar una posición del mapa.
     */
    public void limpiar() {
        // La casilla deja de tener objeto, enemigo, trampa, puerta, etc.
        this.tipo = Tipo.VACIA;

        // Se elimina cualquier objeto real que estuviera guardado.
        this.contenido = null;

        // Se asegura que la casilla vuelva a ser transitable.
        this.accesible = true;
    }

    /**
     * Convierte la celda en texto para poder verla de forma sencilla.
     *
     * Este método es útil para depurar o imprimir el mapa en consola,
     * porque cada tipo de celda se representa con una letra entre corchetes:
     *
     * [ ] celda vacía
     * [E] enemigo
     * [O] objeto
     * [P] puerta
     * [T] trampa
     * [S] salida
     *
     * @return representación textual de la celda.
     */
    @Override
    public String toString() {
        switch (tipo) {
            case VACIA:
                // Casilla sin contenido visible.
                return "[ ]";

            case ENEMIGO:
                // Casilla ocupada por un enemigo.
                return "[E]";

            case OBJETO:
                // Casilla con un objeto recogible.
                return "[O]";

            case PUERTA:
                // Casilla que representa una puerta.
                return "[P]";

            case TRAMPA:
                // Casilla que representa una trampa.
                return "[T]";

            case SALIDA:
                // Casilla que representa la salida.
                return "[S]";

            default:
                // Caso de seguridad por si aparece un tipo no contemplado.
                return "[?]";
        }
    }
}




