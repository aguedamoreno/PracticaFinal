package Juego;
import Juego.listas.ListaSimplementeEnlazada;

import Juego.listas.ListaSimplementeEnlazada;

public class Jugador {
    // Estadísticas básicas
    private int vidaMax;
    private int vidaActual;
    private int ataqueBase;
    private int defensaBase;
    private int velocidad; // Casillas que puede moverse por turno

    // Mejoras por equipamiento
    private int ataqueEquipamiento; // Bonus de armas equipadas
    private int defensaEquipamiento; // Bonus de escudos/armadura equipada

    // Inventario de objetos
    private ListaSimplementeEnlazada<Objeto> inventario;

    // Posición actual (habitación + coordenadas dentro de la habitación)
    private String habitacionActualId;
    private int posicionX;
    private int posicionY;

    // Estado
    private boolean vivo;

    public Jugador(int vidaMax, int ataque, int defensa, int velocidad) {
        this.vidaMax = vidaMax;
        this.vidaActual = vidaMax;
        this.ataqueBase = ataque;
        this.defensaBase = defensa;
        this.velocidad = velocidad;
        this.ataqueEquipamiento = 0;
        this.defensaEquipamiento = 0;
        this.inventario = new ListaSimplementeEnlazada<>();
        this.vivo = true;
    }

    // Getters y Setters
    public int getVidaMax() {
        return vidaMax;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public void setVidaActual(int vidaActual) {
        this.vidaActual = Math.max(0, Math.min(vidaMax, vidaActual));
    }

    public int getAtaqueBase() {
        return ataqueBase;
    }

    public int getDefensaBase() {
        return defensaBase;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public int getAtaqueEquipamiento() {
        return ataqueEquipamiento;
    }

    public void setAtaqueEquipamiento(int ataqueEquipamiento) {
        this.ataqueEquipamiento = ataqueEquipamiento;
    }

    public int getDefensaEquipamiento() {
        return defensaEquipamiento;
    }

    public void setDefensaEquipamiento(int defensaEquipamiento) {
        this.defensaEquipamiento = defensaEquipamiento;
    }

    public int getAtaqueTotal() {
        return ataqueBase + ataqueEquipamiento;
    }

    public int getDefensaTotal() {
        return defensaBase + defensaEquipamiento;
    }

    public ListaSimplementeEnlazada<Objeto> getInventario() {
        return inventario;
    }

    public String getHabitacionActualId() {
        return habitacionActualId;
    }

    public void setHabitacionActualId(String habitacionActualId) {
        this.habitacionActualId = habitacionActualId;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
    }

    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    /**
     * Añade un objeto al inventario
     * @param objeto Objeto a añadir
     */
    public void agregarObjetoInventario(Objeto objeto) {
        inventario.insertarUltimo(objeto);
    }

    /**
     * Elimina un objeto del inventario por índice
     * @param indice Índice del objeto a eliminar
     * @return Objeto eliminado o null si índice inválido
     */
    public Objeto eliminarObjetoInventario(int indice) {
        if (indice < 0 || indice >= inventario.tamaño()) {
            return null;
        }
        return inventario.eliminar(indice);
    }

    /**
     * Usa un objeto del inventario por índice
     * @param indice Índice del objeto a usar
     * @return true si se pudo usar, false en caso contrario
     */
    public boolean usarObjetoInventario(int indice) {
        if (indice < 0 || indice >= inventario.tamaño()) {
            return false;
        }
        Objeto objeto = inventario.obtener(indice);
        if (objeto.puedeUsarse()) {
            boolean usado = objeto.usar();
            if (usado && objeto.isFungible() && objeto.getUsosRestantes() == 0) {
                // Si es fungible y se agotó, lo removemos del inventario
                inventario.eliminar(indice);
            }
            return true;
        }
        return false;
    }

    /**
     * Equipo un objeto del inventario (si es equipable)
     * @param indice Índice del objeto a equipo
     * @return true si se pudo equipo, false en caso contrario
     */
    public boolean equipoObjetoInventario(int indice) {
        if (indice < 0 || indice >= inventario.tamaño()) {
            return false;
        }
        Objeto objeto = inventario.obtener(indice);
        if (!objeto.isEquipable()) {
            return false;
        }

        // Aplicar efectos de equipamiento según tipo
        switch (objeto.getTipo()) {
            case ARMA:
                setAtaqueEquipamiento(getAtaqueEquipamiento() + objeto.getValor());
                break;
            case ESCUDO:
                setDefensaEquipamiento(getDefensaEquipamiento() + objeto.getValor());
                break;
            // Otros tipos de equipamiento pueden añadirse aquí
            default:
                // Si no es un tipo de equipamiento reconocido, no se equipo
                return false;
        }
        // Nota: No removemos el objeto del inventario al equipoarlo
        return true;
    }

    /**
     * Des equipo un objeto del inventario (si estaba equipo)
     * @param indice Índice del objeto a des equipo
     * @return true si se pudo des equipo, false en caso contrario
     */
    public boolean desequipoObjetoInventario(int indice) {
        if (indice < 0 || indice >= inventario.tamaño()) {
            return false;
        }
        Objeto objeto = inventario.obtener(indice);
        if (!objeto.isEquipable()) {
            return false;
        }

        // Remover efectos de equipamiento según tipo
        switch (objeto.getTipo()) {
            case ARMA:
                setAtaqueEquipamiento(getAtaqueEquipamiento() - objeto.getValor());
                break;
            case ESCUDO:
                setDefensaEquipamiento(getDefensaEquipamiento() - objeto.getValor());
                break;
            default:
                return false;
        }
        return true;
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
        return x >= 0 && y >= 0;
    }

    public int recibirDano(int ataqueEnemigo) {
        // 1. Calculamos la mitigación: el daño real no puede ser menor que 0
        int danoReal = Math.max(0, ataqueEnemigo - getDefensaTotal());

        // 2. Restamos el daño real a la vida actual, asegurando que no baje de 0
        this.vidaActual = Math.max(0, this.vidaActual - danoReal);

        // 3. Si la vida llega a 0, actualizamos el estado del jugador a muerto
        if (this.vidaActual == 0) {
            this.vivo = false;
        }

        // Devolvemos el daño real para poder pintarlo en los registros de la interfaz
        return danoReal;
    }

    @Override
    public String toString() {
        return "Jugador [Vida: " + vidaActual + "/" + vidaMax +
                ", Ataque: " + getAtaqueTotal() +
                ", Defensa: " + getDefensaTotal() +
                ", Velocidad: " + velocidad +
                ", Posición: (" + posicionX + "," + posicionY + ")" +
                ", Inventario: " + inventario.tamaño() + " objetos]";
    }
}
