package Juego;

public class Objeto {
    public enum Tipo {
        POCION_VIDA,      // Restaura vida
        POCION_FUERZA,    // Aumenta ataque temporalmente
        POCION_DEFENSA,   // Aumenta defensa temporalmente
        ARMA,             // Aumenta ataque cuando se equipo
        ESCUDO,           // Aumenta defensa cuando se equipo
        LLAVE,            // Necesaria para abrir ciertas puertas
        OBJETO_ESPECIAL   // Otros efectos especiales
    }

    private Tipo tipo;
    private String nombre;
    private String descripcion;
    private int valor; // Valor de la mejora (ej: +10 vida, +5 ataque)
    private boolean fungible; // Si desaparece después de usarlo
    private boolean equipable; // Si se puede equipo
    private boolean mejoraPasiva; // Si la mejora aplica solo por llevarlo en inventario
    private int usosMaximos; // Número máximo de usos (-1 para ilimitado)
    private int usosRestantes; // Usos restantes
    private int duracionTurnos; // Duración en turnos de la mejora (-1 para permanente)

    public Objeto(Tipo tipo, String nombre, String descripcion, int valor) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valor = valor;
        this.fungible = true;
        this.equipable = false;
        this.mejoraPasiva = false;
        this.usosMaximos = -1; // Ilimitado por defecto
        this.usosRestantes = -1;
        this.duracionTurnos = -1; // Permanente por defecto

        // Configurar valores por defecto según el tipo
        switch (tipo) {
            case POCION_VIDA:
                this.fungible = true;
                this.equipable = false;
                this.mejoraPasiva = false;
                this.usosMaximos = 1;
                this.duracionTurnos = -1; // Efecto instantáneo
                break;
            case POCION_FUERZA:
            case POCION_DEFENSA:
                this.fungible = true;
                this.equipable = false;
                this.mejoraPasiva = false;
                this.usosMaximos = 1;
                this.duracionTurnos = 3; // Ejemplo: 3 turnos de duración
                break;
            case ARMA:
            case ESCUDO:
                this.fungible = false;
                this.equipable = true;
                this.mejoraPasiva = false;
                this.usosMaximos = -1; // Ilimitado
                this.duracionTurnos = -1; // Permanente mientras se equipo
                break;
            case LLAVE:
                this.fungible = true; // Se consume al usar
                this.equipable = false;
                this.mejoraPasiva = false;
                this.usosMaximos = 1;
                this.duracionTurnos = -1;
                break;
            case OBJETO_ESPECIAL:
                // Valores por defecto, pueden overridearse
                break;
        }

        // Inicializar usos restantes
        if (this.usosMaximos != -1) {
            this.usosRestantes = this.usosMaximos;
        }
    }

    // Getters y Setters
    public Tipo getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getValor() {
        return valor;
    }

    public boolean isFungible() {
        return fungible;
    }

    public boolean isEquipable() {
        return equipable;
    }

    public boolean isMejoraPasiva() {
        return mejoraPasiva;
    }

    public int getUsosMaximos() {
        return usosMaximos;
    }

    public int getUsosRestantes() {
        return usosRestantes;
    }

    public void setUsosRestantes(int usosRestantes) {
        this.usosRestantes = usosRestantes;
    }

    public int getDuracionTurnos() {
        return duracionTurnos;
    }

    /**
     * Usa el objeto, decrementando los usos si es fungible
     * @return true si se pudo usar, false si no tiene usos restantes
     */
    public boolean usar() {
        if (usosMaximos == -1) {
            // Usos ilimitados, siempre se puede usar
            return true;
        }

        if (usosRestantes > 0) {
            usosRestantes--;
            return true;
        }
        return false;
    }

    /**
     * Verifica si el objeto aún puede ser usado
     * @return true si tiene usos disponibles
     */
    public boolean puedeUsarse() {
        if (usosMaximos == -1) {
            return true; // Ilimitado
        }
        return usosRestantes > 0;
    }

    @Override
    public String toString() {
        return nombre + " (" + descripcion + ")";
    }
}
