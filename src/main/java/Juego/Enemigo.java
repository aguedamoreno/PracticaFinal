package Juego; // indica el paquete al que pertenece esta clase.

/**
 * Clase que representa a los enemigos del juego, guardando sus estadísticas, posición, equipo y operaciones de combate.
 *
 * Comentarios añadidos para explicar la función de la clase, sus variables
 * y los bloques principales de código sin cambiar la lógica original.
 */
public class Enemigo { // declara una clase que agrupa datos y métodos relacionados.
    public enum Tipo { // declara un conjunto cerrado de valores posibles.
        GOBLIN,      // Bajo vida, bajo ataque
        ORCO,        // Medio vida, medio ataque
        ESQUELETO,   // Bajo vida, alto ataque (frágil pero peligroso)
        DRAGON,      // Alto vida, alto ataque (jefe)
        LADRON       // Medio vida, roba objetos
    }

    private Tipo tipo; // declara un atributo/campo de la clase donde se guarda estado.
    private String nombre; // declara un atributo/campo de la clase donde se guarda estado.
    private int vidaMax; // declara un atributo/campo de la clase donde se guarda estado.
    private int vidaActual; // declara un atributo/campo de la clase donde se guarda estado.
    private int ataque; // declara un atributo/campo de la clase donde se guarda estado.
    private int defensa; // declara un atributo/campo de la clase donde se guarda estado.
    private int velocidad; // Cuántas casillas puede moverse por turno
    private boolean equipado; // Si tiene arma/equipamiento
    private int danoArma; // Daño adicional si tiene arma
    private int defensaArmadura; // Defensa adicional si tiene armadura

    // Posición del enemigo dentro de la habitación
    private int posicionX; // declara un atributo/campo de la clase donde se guarda estado.
    private int posicionY; // declara un atributo/campo de la clase donde se guarda estado.

    /**
     * Constructor que inicializa los atributos principales del objeto.
     */
    public Enemigo(Tipo tipo) {
        this.tipo = tipo; // guarda el valor recibido dentro del atributo del objeto actual.

        // Configurar estadísticas según el tipo
        switch (tipo) {
            case GOBLIN:
                this.nombre = "Goblin"; // guarda el valor recibido dentro del atributo del objeto actual.
                this.vidaMax = 20; // guarda el valor recibido dentro del atributo del objeto actual.
                this.ataque = 5; // guarda el valor recibido dentro del atributo del objeto actual.
                this.defensa = 2; // guarda el valor recibido dentro del atributo del objeto actual.
                this.velocidad = 2; // guarda el valor recibido dentro del atributo del objeto actual.
                break; // controla el flujo del bucle actual.
            case ORCO:
                this.nombre = "Orco"; // guarda el valor recibido dentro del atributo del objeto actual.
                this.vidaMax = 35; // guarda el valor recibido dentro del atributo del objeto actual.
                this.ataque = 8; // guarda el valor recibido dentro del atributo del objeto actual.
                this.defensa = 4; // guarda el valor recibido dentro del atributo del objeto actual.
                this.velocidad = 1; // guarda el valor recibido dentro del atributo del objeto actual.
                break; // controla el flujo del bucle actual.
            case ESQUELETO:
                this.nombre = "Esqueleto"; // guarda el valor recibido dentro del atributo del objeto actual.
                this.vidaMax = 15; // guarda el valor recibido dentro del atributo del objeto actual.
                this.ataque = 10; // guarda el valor recibido dentro del atributo del objeto actual.
                this.defensa = 1; // guarda el valor recibido dentro del atributo del objeto actual.
                this.velocidad = 3; // guarda el valor recibido dentro del atributo del objeto actual.
                break; // controla el flujo del bucle actual.
            case DRAGON:
                this.nombre = "Dragon"; // guarda el valor recibido dentro del atributo del objeto actual.
                this.vidaMax = 100; // guarda el valor recibido dentro del atributo del objeto actual.
                this.ataque = 15; // guarda el valor recibido dentro del atributo del objeto actual.
                this.defensa = 8; // guarda el valor recibido dentro del atributo del objeto actual.
                this.velocidad = 2; // guarda el valor recibido dentro del atributo del objeto actual.
                break; // controla el flujo del bucle actual.
            case LADRON:
                this.nombre = "Ladron"; // guarda el valor recibido dentro del atributo del objeto actual.
                this.vidaMax = 25; // guarda el valor recibido dentro del atributo del objeto actual.
                this.ataque = 6; // guarda el valor recibido dentro del atributo del objeto actual.
                this.defensa = 3; // guarda el valor recibido dentro del atributo del objeto actual.
                this.velocidad = 3; // guarda el valor recibido dentro del atributo del objeto actual.
                break; // controla el flujo del bucle actual.
        }

        this.vidaActual = this.vidaMax; // guarda el valor recibido dentro del atributo del objeto actual.
        this.equipado = false; // guarda el valor recibido dentro del atributo del objeto actual.
        this.danoArma = 0; // guarda el valor recibido dentro del atributo del objeto actual.
        this.defensaArmadura = 0; // guarda el valor recibido dentro del atributo del objeto actual.
        this.posicionX = -1; // guarda el valor recibido dentro del atributo del objeto actual.
        this.posicionY = -1; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    // Getters y Setters
    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public Tipo getTipo() {
        return tipo; // devuelve el resultado calculado por el método.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public String getNombre() {
        return nombre; // devuelve el resultado calculado por el método.
    }

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
    public int getAtaque() {
        return ataque + (equipado ? danoArma : 0); // devuelve el resultado calculado por el método.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getDefensa() {
        return defensa + (equipado ? defensaArmadura : 0); // devuelve el resultado calculado por el método.
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
    public int getPosicionX() {
        return posicionX; // devuelve el resultado calculado por el método.
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
    public void setPosicion(int posicionX, int posicionY) {
        this.posicionX = posicionX; // guarda el valor recibido dentro del atributo del objeto actual.
        this.posicionY = posicionY; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Devuelve verdadero o falso según el estado actual del objeto.
     */
    public boolean isEquipado() {
        return equipado; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setEquipado(boolean equipado) {
        this.equipado = equipado; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getDanoArma() {
        return danoArma; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setDanoArma(int danoArma) {
        this.danoArma = danoArma; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getDefensaArmadura() {
        return defensaArmadura; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setDefensaArmadura(int defensaArmadura) {
        this.defensaArmadura = defensaArmadura; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Verifica si el enemigo está vivo
     * @return true si tiene vida mayor a 0
     */
    public boolean estaVivo() {
        return vidaActual > 0; // devuelve el resultado calculado por el método.
    }

    /**
     * Aplica daño al enemigo
     * @param dano Cantidad de daño a aplicar
     */
    public void recibirDano(int dano) {
        int danoReal = Math.max(0, dano - getDefensa()); // asigna o actualiza un valor necesario para el estado del programa.
        vidaActual = Math.max(0, vidaActual - danoReal); // asigna o actualiza un valor necesario para el estado del programa.
    }

    /**
     * Cura al enemigo (para objetos especiales)
     * @param cura Cantidad de vida a recuperar
     */
    public void curar(int cura) {
        vidaActual = Math.min(vidaMax, vidaActual + cura); // asigna o actualiza un valor necesario para el estado del programa.
    }

    @Override
    /**
     * Devuelve una representación en texto del objeto para mostrarlo fácilmente.
     */
    public String toString() {
        return nombre + " [Vida: " + vidaActual + "/" + vidaMax + // devuelve el resultado calculado por el método.
                ", Ataque: " + getAtaque() + ", Defensa: " + getDefensa() +
                ", Velocidad: " + velocidad +
                ", Posición: (" + posicionX + "," + posicionY + ")]";
    }
}



