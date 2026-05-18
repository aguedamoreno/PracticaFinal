package Juego;

public class Enemigo {
    public enum Tipo {
        GOBLIN,      // Bajo vida, bajo ataque
        ORCO,        // Medio vida, medio ataque
        ESQUELETO,   // Bajo vida, alto ataque (frágil pero peligroso)
        DRAGON,      // Alto vida, alto ataque (jefe)
        LADRON       // Medio vida, roba objetos
    }

    private Tipo tipo;
    private String nombre;
    private int vidaMax;
    private int vidaActual;
    private int ataque;
    private int defensa;
    private int velocidad; // Cuántas casillas puede moverse por turno
    private boolean equipado; // Si tiene arma/equipamiento
    private int danoArma; // Daño adicional si tiene arma
    private int defensaArmadura; // Defensa adicional si tiene armadura

    public Enemigo(Tipo tipo) {
        this.tipo = tipo;

        // Configurar estadísticas según el tipo
        switch (tipo) {
            case GOBLIN:
                this.nombre = "Goblin";
                this.vidaMax = 20;
                this.ataque = 5;
                this.defensa = 2;
                this.velocidad = 2;
                break;
            case ORCO:
                this.nombre = "Orco";
                this.vidaMax = 35;
                this.ataque = 8;
                this.defensa = 4;
                this.velocidad = 1;
                break;
            case ESQUELETO:
                this.nombre = "Esqueleto";
                this.vidaMax = 15;
                this.ataque = 10;
                this.defensa = 1;
                this.velocidad = 3;
                break;
            case DRAGON:
                this.nombre = "Dragon";
                this.vidaMax = 100;
                this.ataque = 15;
                this.defensa = 8;
                this.velocidad = 2;
                break;
            case LADRON:
                this.nombre = "Ladron";
                this.vidaMax = 25;
                this.ataque = 6;
                this.defensa = 3;
                this.velocidad = 3;
                break;
        }

        this.vidaActual = this.vidaMax;
        this.equipado = false;
        this.danoArma = 0;
        this.defensaArmadura = 0;
    }

    // Getters y Setters
    public Tipo getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getVidaMax() {
        return vidaMax;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public void setVidaActual(int vidaActual) {
        this.vidaActual = Math.max(0, Math.min(vidaMax, vidaActual));
    }

    public int getAtaque() {
        return ataque + (equipado ? danoArma : 0);
    }

    public int getDefensa() {
        return defensa + (equipado ? defensaArmadura : 0);
    }

    public int getVelocidad() {
        return velocidad;
    }

    public boolean isEquipado() {
        return equipado;
    }

    public void setEquipado(boolean equipado) {
        this.equipado = equipado;
    }

    public int getDanoArma() {
        return danoArma;
    }

    public void setDanoArma(int danoArma) {
        this.danoArma = danoArma;
    }

    public int getDefensaArmadura() {
        return defensaArmadura;
    }

    public void setDefensaArmadura(int defensaArmadura) {
        this.defensaArmadura = defensaArmadura;
    }

    /**
     * Verifica si el enemigo está vivo
     * @return true si tiene vida mayor a 0
     */
    public boolean estaVivo() {
        return vidaActual > 0;
    }

    /**
     * Aplica daño al enemigo
     * @param dano Cantidad de daño a aplicar
     */
    public void recibirDano(int dano) {
        int danoReal = Math.max(0, dano - getDefensa());
        vidaActual = Math.max(0, vidaActual - danoReal);
    }

    /**
     * Cura al enemigo (para objetos especiales)
     * @param cura Cantidad de vida a recuperar
     */
    public void curar(int cura) {
        vidaActual = Math.min(vidaMax, vidaActual + cura);
    }

    @Override
    public String toString() {
        return nombre + " [Vida: " + vidaActual + "/" + vidaMax +
                ", Ataque: " + getAtaque() + ", Defensa: " + getDefensa() +
                ", Velocidad: " + velocidad + "]";
    }
}
