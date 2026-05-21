package Juego;

public class Celda {
    public enum Tipo {
        VACIA,
        ENEMIGO,
        OBJETO,
        PUERTA,
        TRAMPA,
        SALIDA
    }

    private Tipo tipo;
    private Object contenido; // Puede ser Enemigo, Objeto, null, etc.
    private boolean accesible; // Controla si es un muro/obstáculo infranqueable

    public Celda() {
        this.tipo = Tipo.VACIA;
        this.contenido = null;
        this.accesible = true;
    }

    public Celda(Tipo tipo) {
        this.tipo = tipo;
        this.contenido = null;
        this.accesible = true; // Por defecto todas permiten interactuar/entrar (salvo si implementas PARED)
    }

    public Celda(Tipo tipo, Object contenido) {
        this.tipo = tipo;
        this.contenido = contenido;
        this.accesible = true;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Object getContenido() {
        return contenido;
    }

    public void setContenido(Object contenido) {
        this.contenido = contenido;
    }

    public boolean isAccesible() {
        return accesible;
    }

    public void setAccesible(boolean accesible) {
        this.accesible = accesible;
    }

    /**
     * CORREGIDO: Ahora el jugador puede transitar o entrar a casillas
     * con enemigos, objetos y trampas para interactuar con ellos.
     */
    public boolean estaLibreParaMovimiento() {
        return accesible && (
                tipo == Tipo.VACIA ||
                        tipo == Tipo.PUERTA ||
                        tipo == Tipo.SALIDA ||
                        tipo == Tipo.TRAMPA ||
                        tipo == Tipo.OBJETO ||
                        tipo == Tipo.ENEMIGO
        );
    }

    public void limpiar() {
        this.tipo = Tipo.VACIA;
        this.contenido = null;
        this.accesible = true;
    }

    @Override
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
