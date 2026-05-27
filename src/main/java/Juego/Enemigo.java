package Juego;

/** La clase Enemigo representa a los enemigos que aparecen dentro del juego, gestiona sus combates e interacciones
 * Cada enemigo tiene:
 * - Un tipo (goblin, orco, dragón...)
 * - Estadísticas de combate
 * - Posición en el mapa
 * - Posible equipamiento
 */
public class Enemigo {

    /** Enumeración que define los tipos posibles de enemigos
     */
    public enum Tipo {
        GOBLIN,      // Enemigo débil y rápido
        ORCO,        // Más resistente y fuerte
        ESQUELETO,   // Mucho daño pero poca vida
        DRAGON,      // Jefe muy poderoso
        LADRON       // Enemigo rápido que puede robar
    }

    /** Metodo que indica el tipo concreto del enemigo, sirve para saber qué estadísticas darle
     */
    private final Tipo tipo;

    /** Metodo que indica el nombre del enemigo
     */
    private String nombre;

    /** Metodo que indica la vida máxima que puede tener el enemigo
     */
    private int vidaMax;

    /** Metodo que indica la vida actual del enemigo durante la partida
     */
    private int vidaActual;

    /** Metodo que indica el daño base que hace el enemigo
     */
    private int ataque;

    /** Metodo que indica la defensa base del enemigo
     */
    private int defensa;

    /** Metodo que indica el número de casillas que puede moverse por turno
     */
    private int velocidad;

    /** Metodo que indica si el enemigo tiene equipamiento puesto
     */
    private boolean equipado;

    /** Metodo que indica el daño extra proporcionado por un arma equipada
     */
    private int danoArma;

    /** Metodo que indica la defensa extra proporcionada por armadura
     */
    private int defensaArmadura;

    /** Metodo que indica la coordenada X del enemigo dentro del mapa
     */
    private int posicionX;

    /** Metodo que indica la coordenada Y del enemigo dentro del mapa
     */
    private int posicionY;

    /** Constructor del enemigo
     */
    public Enemigo(Tipo tipo) {
        // Guardamos el tipo recibido
        this.tipo = tipo;
         // Dependiendo del tipo de enemigo se le asignan estadísticas distintas
        switch (tipo) {
            case GOBLIN:
                // Nombre que se mostrará
                this.nombre = "Goblin";
                // Tiene poca vida
                this.vidaMax = 20;
                // Hace poco daño
                this.ataque = 5;
                // Tiene poca defensa
                this.defensa = 2;
                // Puede moverse bastante
                this.velocidad = 2;
                break;
            case ORCO:
                this.nombre = "Orco";
                // Más resistente
                this.vidaMax = 35;
                // Más daño
                this.ataque = 8;
                // Más defensa
                this.defensa = 4;
                // Más lento
                this.velocidad = 1;
                break;
            case ESQUELETO:
                this.nombre = "Esqueleto";
                // Muy poca vida
                this.vidaMax = 15;
                // Mucho ataque
                this.ataque = 10;
                // Muy frágil
                this.defensa = 1;
                // Muy rápido
                this.velocidad = 3;
                break;
            case DRAGON:
                this.nombre = "Dragon";
                // Muchísima vida
                this.vidaMax = 100;
                // Mucho daño
                this.ataque = 15;
                // Mucha defensa
                this.defensa = 8;
                this.velocidad = 2;
                break;
            case LADRON:
                this.nombre = "Ladron";
                this.vidaMax = 25;
                this.ataque = 6;
                this.defensa = 3;
                // Muy rápido
                this.velocidad = 3;
                break;
        }

        // Al crear el enemigo: empieza con la vida llena, no tiene equipo, y no tiene bonificaciones
        this.vidaActual = this.vidaMax;
        this.equipado = false;
        this.danoArma = 0;
        this.defensaArmadura = 0;
        // La posición empieza en -1 porque todavía no se ha colocado en el mapa
        this.posicionX = -1;
        this.posicionY = -1;
    }

    /** Metodo que devuelve el tipo del enemigo
     */
    public Tipo getTipo() {
        return tipo;
    }

    /** Metodo que devuelve el nombre del enemigo
     */
    public String getNombre() {
        return nombre;
    }

    /** Metodo que devuelve la vida máxima
     */
    public int getVidaMax() {
        return vidaMax;
    }

    /** Metodo que devuelve la vida actual
     */
    public int getVidaActual() {
        return vidaActual;
    }

    /** Metodo que modifica la vida actual
     */
    public void setVidaActual(int vidaActual) {
        this.vidaActual = Math.max(0, Math.min(vidaMax, vidaActual));   // así evitamos que supere la vida máxima
    }

    /** Metodo que devuelve el ataque total del enemigo
    */
    public int getAtaque() {
        // Si equipado == true, devuelve danoArma
        // Si equipado == false, devuelve 0
        return ataque + // si lleva arma equipada se suma daño extra
                (equipado ? danoArma : 0);
    }

    /** Metodo que dvuelve la defensa total
     */
    public int getDefensa() {

        return defensa +    //Si tiene armadura equipada, añade defensa extra
                (equipado ? defensaArmadura : 0);
    }

    /** Metodo que devuelve la velocidad del enemigo.
     */
    public int getVelocidad() {
        return velocidad;
    }

    /** Metodo que devuelve la posición X
     */
    public int getPosicionX() {
        return posicionX;
    }

    /** Metodo que devuelve la posición Y
     */
    public int getPosicionY() {
        return posicionY;
    }

    /** Metodo que cambia la posición del enemigo en el mapa
     * Se usa cuando el enemigo se mueve
     */
    public void setPosicion(int posicionX, int posicionY) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    /** Metodo que indica si el enemigo tiene equipamiento
     */
    public boolean isEquipado() {
        return equipado;
    }

    /** Metodo que activa o desactiva el equipamiento
     */
    public void setEquipado(boolean equipado) {
        this.equipado = equipado;
    }

    /** Metodo que devuelve el daño extra del arma
     */
    public int getDanoArma() {
        return danoArma;
    }

    /** Metodo que cambia el daño extra del arma
     */
    public void setDanoArma(int danoArma) {
        this.danoArma = danoArma;
    }

    /** Metodo que devuelve la defensa extra de la armadura
     */
    public int getDefensaArmadura() {
        return defensaArmadura;
    }

    /** Metodo que cambia la defensa extra de la armadura
     */
    public void setDefensaArmadura(int defensaArmadura) {
        this.defensaArmadura = defensaArmadura;
    }

    /** Metodo que comprueba si el enemigo sigue vivo
     */
    public boolean estaVivo() {
        return vidaActual > 0;  //Si la vida es mayor que 0, el enemigo sigue vivo
    }

    /** Metodo que hace que el enemigo reciba daño
     */
    public void recibirDano(int dano) {

        // Calculamos el daño real, si el ataque es menor que la defensa, el daño nunca será negativo
        int danoReal = Math.max(0, dano - getDefensa());

        // Restamos la vida, que nunca baja de 0
        vidaActual = Math.max(0, vidaActual - danoReal);
    }

    /** Metodo que le cura vida al enemigo
     */
    public void curar(int cura) {
        vidaActual = Math.min(vidaMax, vidaActual + cura);  // Nunca puede superar la vida máxima
    }

    /** Metodo que convierte el enemigo en texto
     */
    @Override
    public String toString() {
        return nombre +
                " [Vida: " + vidaActual + "/" + vidaMax +
                ", Ataque: " + getAtaque() +
                ", Defensa: " + getDefensa() +
                ", Velocidad: " + velocidad +
                ", Posición: (" + posicionX + "," + posicionY + ")]";
    }
}



