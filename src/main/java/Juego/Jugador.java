package Juego; // indica el paquete al que pertenece esta clase.
import Juego.listas.ListaSimplementeEnlazada; // importa una clase externa necesaria para este archivo.

import Juego.listas.ListaSimplementeEnlazada; // importa una clase externa necesaria para este archivo.

/** Clase que representa al jugador: vida, ataque, defensa, inventario, posición y operaciones para usar/equipar objetos y recibir daño
 */
public class Jugador { // declara una clase que agrupa datos y métodos relacionados.
    // Estadísticas básicas
    private int vidaMax; // declara un atributo/campo de la clase donde se guarda estado.
    private int vidaActual; // declara un atributo/campo de la clase donde se guarda estado.
    private int ataqueBase; // declara un atributo/campo de la clase donde se guarda estado.
    private int defensaBase; // declara un atributo/campo de la clase donde se guarda estado.
    private int velocidad; // Casillas que puede moverse por turno

    // Mejoras por equipamiento
    private int ataqueEquipamiento; // Bonus de armas equipadas
    private int defensaEquipamiento; // Bonus de escudos/armadura equipada

    // Inventario de objetos
    private ListaSimplementeEnlazada<Objeto> inventario; // declara un atributo/campo de la clase donde se guarda estado.

    // Posición actual (habitación + coordenadas dentro de la habitación)
    private String habitacionActualId; // declara un atributo/campo de la clase donde se guarda estado.
    private int posicionX; // declara un atributo/campo de la clase donde se guarda estado.
    private int posicionY; // declara un atributo/campo de la clase donde se guarda estado.

    // Estado
    private boolean vivo; // declara un atributo/campo de la clase donde se guarda estado.

    /**
     * Constructor que inicializa los atributos principales del objeto.
     */
    public Jugador(int vidaMax, int ataque, int defensa, int velocidad) {
        this.vidaMax = vidaMax; // guarda el valor recibido dentro del atributo del objeto actual.
        this.vidaActual = vidaMax; // guarda el valor recibido dentro del atributo del objeto actual.
        this.ataqueBase = ataque; // guarda el valor recibido dentro del atributo del objeto actual.
        this.defensaBase = defensa; // guarda el valor recibido dentro del atributo del objeto actual.
        this.velocidad = velocidad; // guarda el valor recibido dentro del atributo del objeto actual.
        this.ataqueEquipamiento = 0; // guarda el valor recibido dentro del atributo del objeto actual.
        this.defensaEquipamiento = 0; // guarda el valor recibido dentro del atributo del objeto actual.
        this.inventario = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.
        this.vivo = true; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    // Getters y Setters
    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getVidaMax() {
        return vidaMax; // devuelve el resultado calculado por el método.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getVidaActual() {
        return vidaActual; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setVidaActual(int vidaActual) {
        this.vidaActual = Math.max(0, Math.min(vidaMax, vidaActual)); // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getAtaqueBase() {
        return ataqueBase; // devuelve el resultado calculado por el método.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getDefensaBase() {
        return defensaBase; // devuelve el resultado calculado por el método.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getVelocidad() {
        return velocidad; // devuelve el resultado calculado por el método.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getAtaqueEquipamiento() {
        return ataqueEquipamiento; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setAtaqueEquipamiento(int ataqueEquipamiento) {
        this.ataqueEquipamiento = ataqueEquipamiento; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getDefensaEquipamiento() {
        return defensaEquipamiento; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setDefensaEquipamiento(int defensaEquipamiento) {
        this.defensaEquipamiento = defensaEquipamiento; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getAtaqueTotal() {
        return ataqueBase + ataqueEquipamiento; // devuelve el resultado calculado por el método.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getDefensaTotal() {
        return defensaBase + defensaEquipamiento; // devuelve el resultado calculado por el método.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public ListaSimplementeEnlazada<Objeto> getInventario() {
        return inventario; // devuelve el resultado calculado por el método.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public String getHabitacionActualId() {
        return habitacionActualId; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setHabitacionActualId(String habitacionActualId) {
        this.habitacionActualId = habitacionActualId; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getPosicionX() {
        return posicionX; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getPosicionY() {
        return posicionY; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Devuelve verdadero o falso según el estado actual del objeto.
     */
    public boolean isVivo() {
        return vivo; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setVivo(boolean vivo) {
        this.vivo = vivo; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Añade un objeto al inventario
     * @param objeto Objeto a añadir
     */
    public void agregarObjetoInventario(Objeto objeto) {
        inventario.insertarUltimo(objeto); // ejecuta una llamada a un método para realizar una acción concreta.
    }

    /**
     * Elimina un objeto del inventario por índice
     * @param indice Índice del objeto a eliminar
     * @return Objeto eliminado o null si índice inválido
     */
    public Objeto eliminarObjetoInventario(int indice) {
        if (indice < 0 || indice >= inventario.tamaño()) { // comprueba una condición para decidir qué camino sigue el programa.
            return null; // devuelve el resultado calculado por el método.
        }
        return inventario.eliminar(indice); // devuelve el resultado calculado por el método.
    }

    /**
     * Usa un objeto del inventario por índice
     * @param indice Índice del objeto a usar
     * @return true si se pudo usar, false en caso contrario
     */
    public boolean usarObjetoInventario(int indice) {
        if (indice < 0 || indice >= inventario.tamaño()) { // comprueba una condición para decidir qué camino sigue el programa.
            return false; // devuelve el resultado calculado por el método.
        }
        Objeto objeto = inventario.obtener(indice); // asigna o actualiza un valor necesario para el estado del programa.
        if (objeto.puedeUsarse()) { // comprueba una condición para decidir qué camino sigue el programa.
            boolean usado = objeto.usar(); // asigna o actualiza un valor necesario para el estado del programa.
            if (usado && objeto.isFungible() && objeto.getUsosRestantes() == 0) { // comprueba una condición para decidir qué camino sigue el programa.
                // Si es fungible y se agotó, lo removemos del inventario
                inventario.eliminar(indice); // ejecuta una llamada a un método para realizar una acción concreta.
            }
            return true; // devuelve el resultado calculado por el método.
        }
        return false; // devuelve el resultado calculado por el método.
    }

    /**
     * Equipo un objeto del inventario (si es equipable)
     * @param indice Índice del objeto a equipo
     * @return true si se pudo equipo, false en caso contrario
     */
    public boolean equipoObjetoInventario(int indice) {
        if (indice < 0 || indice >= inventario.tamaño()) { // comprueba una condición para decidir qué camino sigue el programa.
            return false; // devuelve el resultado calculado por el método.
        }
        Objeto objeto = inventario.obtener(indice); // asigna o actualiza un valor necesario para el estado del programa.
        if (!objeto.isEquipable()) { // comprueba una condición para decidir qué camino sigue el programa.
            return false; // devuelve el resultado calculado por el método.
        }

        // Aplicar efectos de equipamiento según tipo
        switch (objeto.getTipo()) {
            case ARMA:
                setAtaqueEquipamiento(getAtaqueEquipamiento() + objeto.getValor()); // ejecuta una llamada a un método para realizar una acción concreta.
                break; // controla el flujo del bucle actual.
            case ESCUDO:
                setDefensaEquipamiento(getDefensaEquipamiento() + objeto.getValor()); // ejecuta una llamada a un método para realizar una acción concreta.
                break; // controla el flujo del bucle actual.
            // Otros tipos de equipamiento pueden añadirse aquí
            default:
                // Si no es un tipo de equipamiento reconocido, no se equipo
                return false; // devuelve el resultado calculado por el método.
        }
        // Nota: No removemos el objeto del inventario al equipoarlo
        return true; // devuelve el resultado calculado por el método.
    }

    /**
     * Des equipo un objeto del inventario (si estaba equipo)
     * @param indice Índice del objeto a des equipo
     * @return true si se pudo des equipo, false en caso contrario
     */
    public boolean desequipoObjetoInventario(int indice) {
        if (indice < 0 || indice >= inventario.tamaño()) { // comprueba una condición para decidir qué camino sigue el programa.
            return false; // devuelve el resultado calculado por el método.
        }
        Objeto objeto = inventario.obtener(indice); // asigna o actualiza un valor necesario para el estado del programa.
        if (!objeto.isEquipable()) { // comprueba una condición para decidir qué camino sigue el programa.
            return false; // devuelve el resultado calculado por el método.
        }

        // Remover efectos de equipamiento según tipo
        switch (objeto.getTipo()) {
            case ARMA:
                setAtaqueEquipamiento(getAtaqueEquipamiento() - objeto.getValor()); // ejecuta una llamada a un método para realizar una acción concreta.
                break; // controla el flujo del bucle actual.
            case ESCUDO:
                setDefensaEquipamiento(getDefensaEquipamiento() - objeto.getValor()); // ejecuta una llamada a un método para realizar una acción concreta.
                break; // controla el flujo del bucle actual.
            default:
                return false; // devuelve el resultado calculado por el método.
        }
        return true; // devuelve el resultado calculado por el método.
    }

    /**
     * Verifica si el jugador puede moverse a una posición dentro de la habitación actual
     * (se verificará desde MotorJuego usando el mapa de la habitación)
     * Este método solo valida que las coordenadas estén dentro de rangos razonables
     * (la validación específica de accesibilidad la hace la habitación)
     * @param x Coordenada X
     * @param y Coordenada Y
     * @return true si las coordenadas son válidas (rango no negativo)
     */
    public boolean esPosicionValida(int x, int y) {
        return x >= 0 && y >= 0; // devuelve el resultado calculado por el método.
    }

    /**
     * Método de apoyo usado por la clase para completar la lógica del juego.
     */
    public int recibirDano(int ataqueEnemigo) {
        // 1. Calculamos la mitigación: el daño real no puede ser menor que 0
        int danoReal = Math.max(0, ataqueEnemigo - getDefensaTotal()); // asigna o actualiza un valor necesario para el estado del programa.

        // 2. Restamos el daño real a la vida actual, asegurando que no baje de 0
        this.vidaActual = Math.max(0, this.vidaActual - danoReal); // guarda el valor recibido dentro del atributo del objeto actual.

        // 3. Si la vida llega a 0, actualizamos el estado del jugador a muerto
        if (this.vidaActual == 0) { // comprueba una condición para decidir qué camino sigue el programa.
            this.vivo = false; // guarda el valor recibido dentro del atributo del objeto actual.
        }

        // Devolvemos el daño real para poder pintarlo en los registros de la interfaz
        return danoReal; // devuelve el resultado calculado por el método.
    }

    @Override
    /**
     * Devuelve una representación en texto del objeto para mostrarlo fácilmente.
     */
    public String toString() {
        return "Jugador [Vida: " + vidaActual + "/" + vidaMax + // devuelve el resultado calculado por el método.
                ", Ataque: " + getAtaqueTotal() +
                ", Defensa: " + getDefensaTotal() +
                ", Velocidad: " + velocidad +
                ", Posición: (" + posicionX + "," + posicionY + ")" +
                ", Inventario: " + inventario.tamaño() + " objetos]";
    }
}

