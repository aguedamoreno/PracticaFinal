package Juego; // Indica el paquete al que pertenece esta clase

import Juego.listas.ListaSimplementeEnlazada; // Importa la lista enlazada usada para el inventario

/** Clase que representa al jugador del juego. Gestiona estadísticas, inventario, posición y acciones del jugador
 */
public class Jugador {

    private int vidaMax;    // vida máxima del jugador
    private int vidaActual; // vida actual del jugador
    private int ataqueBase; // ataque base del jugador
    private int defensaBase;    // defensa base del jugador
    private int velocidad;  // número de casillas que puede moverse por turno
    private int ataqueEquipamiento;    // bonus de ataque por equipamiento
    private int defensaEquipamiento;    // bonus de defensa por equipamiento
    private ListaSimplementeEnlazada<Objeto> inventario;    // inventario del jugador
    private String habitacionActualId;  // ID de la habitación actual
    private int posicionX;  // posición X del jugador
    private int posicionY;  // posición Y del jugador
    private boolean vivo;   // estado de vida del jugador

    /** Constructor del jugador. Inicializa estadísticas y estructuras necesarias
     */
    public Jugador(int vidaMax, int ataque, int defensa, int velocidad) {

        this.vidaMax = vidaMax; // inicializa la vida máxima
        this.vidaActual = vidaMax;  // la vida actual empieza al máximo
        this.ataqueBase = ataque;   // guarda el ataque base
        this.defensaBase = defensa; // guarda la defensa base
        this.velocidad = velocidad; // guarda la velocidad
        this.ataqueEquipamiento = 0;    // inicializa el bonus de ataque
        this.defensaEquipamiento = 0;   // inicializa el bonus de defensa
        this.inventario = new ListaSimplementeEnlazada<>(); // crea el inventario vacío
        this.vivo = true;   // el jugador empieza vivo
    }

    /** Devuelve la vida máxima del jugador
     */
    public int getVidaMax() {
        return vidaMax;
    }

    /** Devuelve la vida actual del jugador
     */
    public int getVidaActual() {
        return vidaActual;
    }

    /** Modifica la vida actual del jugador. Nunca puede bajar de 0 ni superar la vida máxima
     */
    public void setVidaActual(int vidaActual) {
        this.vidaActual = Math.max(0, Math.min(vidaMax, vidaActual));
    }

    /** Devuelve el ataque base
     */
    public int getAtaqueBase() {
        return ataqueBase;
    }

    /** Devuelve la defensa base
     */
    public int getDefensaBase() {
        return defensaBase;
    }

    /** Devuelve la velocidad del jugador
     */
    public int getVelocidad() {
        return velocidad;
    }

    /** Devuelve el ataque extra por equipamiento
     */
    public int getAtaqueEquipamiento() {
        return ataqueEquipamiento;
    }

    /** Modifica el ataque extra por equipamiento
     */
    public void setAtaqueEquipamiento(int ataqueEquipamiento) {
        this.ataqueEquipamiento = ataqueEquipamiento;
    }

    /** Devuelve la defensa extra por equipamiento
     */
    public int getDefensaEquipamiento() {
        return defensaEquipamiento;
    }

    /** Modifica la defensa extra por equipamiento
     */
    public void setDefensaEquipamiento(int defensaEquipamiento) {
        this.defensaEquipamiento = defensaEquipamiento;
    }

    /** Devuelve el ataque total del jugador
     */
    public int getAtaqueTotal() {
        return ataqueBase + ataqueEquipamiento;
    }

    /** Devuelve la defensa total del jugador
     */
    public int getDefensaTotal() {
        return defensaBase + defensaEquipamiento;
    }

    /** Devuelve el inventario del jugador
     */
    public ListaSimplementeEnlazada<Objeto> getInventario() {
        return inventario;
    }

    /** Devuelve el ID de la habitación actual
     */
    public String getHabitacionActualId() {
        return habitacionActualId;
    }

    /** Modifica el ID de la habitación actual
     */
    public void setHabitacionActualId(String habitacionActualId) {
        this.habitacionActualId = habitacionActualId;
    }

    /** Devuelve la posición X del jugador
     */
    public int getPosicionX() {
        return posicionX;
    }

    /** Modifica la posición X del jugador
     */
    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
    }

    /** Devuelve la posición Y del jugador
     */
    public int getPosicionY() {
        return posicionY;
    }

    /** Modifica la posición Y del jugador
     */
    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
    }

    /** Devuelve si el jugador sigue vivo
     */
    public boolean isVivo() {
        return vivo;
    }

    /** Modifica el estado de vida del jugador
     */
    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    /** Añade un objeto al inventario
     */
    public void agregarObjetoInventario(Objeto objeto) {

        // inserta el objeto al final del inventario
        inventario.insertarUltimo(objeto);
    }

    /** Elimina un objeto del inventario según su índice
     */
    public Objeto eliminarObjetoInventario(int indice) {

        // comprueba si el índice es válido
        if (indice < 0 || indice >= inventario.tamaño()) {
            return null;
        }

        // elimina y devuelve el objeto
        return inventario.eliminar(indice);
    }

    /** Usa un objeto del inventario
     */
    public boolean usarObjetoInventario(int indice) {

        // comprueba si el índice es válido
        if (indice < 0 || indice >= inventario.tamaño()) {
            return false;
        }

        // obtiene el objeto
        Objeto objeto = inventario.obtener(indice);

        // comprueba si puede usarse
        if (objeto.puedeUsarse()) {

            // usa el objeto
            boolean usado = objeto.usar();

            // si es fungible y no tiene usos restantes se elimina
            if (usado && objeto.isFungible() && objeto.getUsosRestantes() == 0) {
                inventario.eliminar(indice);
            }

            return true;
        }

        return false;
    }

    /** Equipa un objeto del inventario
     */
    public boolean equipoObjetoInventario(int indice) {

        // comprueba si el índice es válido
        if (indice < 0 || indice >= inventario.tamaño()) {
            return false;
        }

        // obtiene el objeto
        Objeto objeto = inventario.obtener(indice);

        // comprueba si es equipable
        if (!objeto.isEquipable()) {
            return false;
        }

        // aplica los efectos según el tipo
        switch (objeto.getTipo()) {

            case ARMA:

                // aumenta el ataque
                setAtaqueEquipamiento(
                        getAtaqueEquipamiento() + objeto.getValor()
                );
                break;

            case ESCUDO:

                // aumenta la defensa
                setDefensaEquipamiento(
                        getDefensaEquipamiento() + objeto.getValor()
                );
                break;

            default:
                return false;
        }

        return true;
    }

    /** Desequipa un objeto del inventario
     */
    public boolean desequipoObjetoInventario(int indice) {

        // comprueba si el índice es válido
        if (indice < 0 || indice >= inventario.tamaño()) {
            return false;
        }

        // obtiene el objeto
        Objeto objeto = inventario.obtener(indice);

        // comprueba si es equipable
        if (!objeto.isEquipable()) {
            return false;
        }

        // elimina efectos según el tipo
        switch (objeto.getTipo()) {

            case ARMA:

                // reduce el ataque
                setAtaqueEquipamiento(
                        getAtaqueEquipamiento() - objeto.getValor()
                );
                break;

            case ESCUDO:

                // reduce la defensa
                setDefensaEquipamiento(
                        getDefensaEquipamiento() - objeto.getValor()
                );
                break;

            default:
                return false;
        }

        return true;
    }

    /** Comprueba si unas coordenadas son válidas
     */
    public boolean esPosicionValida(int x, int y) {

        // las posiciones deben ser positivas
        return x >= 0 && y >= 0;
    }

    /** Aplica daño al jugador teniendo en cuenta la defensa
     */
    public int recibirDano(int ataqueEnemigo) {

        // calcula el daño real
        int danoReal = Math.max(
                0,
                ataqueEnemigo - getDefensaTotal()
        );

        // reduce la vida actual
        this.vidaActual = Math.max(
                0,
                this.vidaActual - danoReal
        );

        // comprueba si el jugador murió
        if (this.vidaActual == 0) {
            this.vivo = false;
        }

        // devuelve el daño aplicado
        return danoReal;
    }

    /** Devuelve una representación en texto del jugador
     */
    @Override
    public String toString() {

        return "Jugador [Vida: " +
                vidaActual + "/" + vidaMax +
                ", Ataque: " + getAtaqueTotal() +
                ", Defensa: " + getDefensaTotal() +
                ", Velocidad: " + velocidad +
                ", Posición: (" +
                posicionX + "," + posicionY + ")" +
                ", Inventario: " +
                inventario.tamaño() + " objetos]";
    }
}