package Juego;

/**
 * Clase que representa un objeto del juego.
 *
 * Los objetos pueden:
 * - curar vida,
 * - aumentar ataque o defensa,
 * - equiparse,
 * - abrir puertas,
 * - o tener efectos especiales.
 */
public class Objeto {

    /**
     * Tipos posibles de objetos.
     */
    public enum Tipo {

        // Cura vida al jugador.
        POCION_VIDA,

        // Aumenta temporalmente el ataque.
        POCION_FUERZA,

        // Aumenta temporalmente la defensa.
        POCION_DEFENSA,

        // Objeto equipable que mejora el ataque.
        ARMA,

        // Objeto equipable que mejora la defensa.
        ESCUDO,

        // Objeto necesario para abrir puertas especiales.
        LLAVE,

        // Tipo genérico para objetos especiales personalizados.
        OBJETO_ESPECIAL
    }

    // Tipo concreto de este objeto.
    private Tipo tipo;

    // Nombre visible del objeto.
    // Ejemplo: "Espada de hierro", "Poción roja", etc.
    private String nombre;

    // Explicación corta de lo que hace el objeto.
    private String descripcion;

    // Intensidad o potencia del objeto.
    // Ejemplos:
    // +10 vida
    // +5 ataque
    // +3 defensa
    private int valor;

    // Indica si el objeto desaparece después de usarse.
    // true = se consume.
    // false = permanece.
    private boolean fungible;

    // Indica si el objeto puede equiparse.
    // Ejemplo: armas o escudos.
    private boolean equipable;

    // Indica si el objeto da la mejora solo por llevarlo encima,
    // sin necesidad de equiparlo manualmente.
    private boolean mejoraPasiva;

    // Número máximo de usos del objeto.
    // -1 significa usos infinitos.
    private int usosMaximos;

    // Número de usos que quedan actualmente.
    private int usosRestantes;

    // Número de turnos que dura el efecto.
    // -1 significa efecto permanente.
    private int duracionTurnos;

    /**
     * Constructor principal del objeto.
     *
     * Según el tipo recibido, se configuran automáticamente
     * sus propiedades básicas.
     *
     * @param tipo tipo de objeto
     * @param nombre nombre visible
     * @param descripcion explicación del objeto
     * @param valor intensidad del efecto
     */
    public Objeto(Tipo tipo, String nombre, String descripcion, int valor) {

        // Guardamos el tipo del objeto.
        this.tipo = tipo;

        // Guardamos el nombre.
        this.nombre = nombre;

        // Guardamos la descripción.
        this.descripcion = descripcion;

        // Guardamos el valor numérico del efecto.
        this.valor = valor;

        // Por defecto asumimos que los objetos son consumibles.
        this.fungible = true;

        // Por defecto asumimos que no se equipan.
        this.equipable = false;

        // Por defecto asumimos que no tienen mejora pasiva.
        this.mejoraPasiva = false;

        // Por defecto los usos son ilimitados.
        this.usosMaximos = -1;

        // También dejamos los usos restantes como ilimitados.
        this.usosRestantes = -1;

        // Por defecto el efecto es permanente.
        this.duracionTurnos = -1;

        /**
         * Según el tipo del objeto,
         * configuramos automáticamente sus propiedades.
         */
        switch (tipo) {

            case POCION_VIDA:

                // La poción desaparece al usarla.
                this.fungible = true;

                // No se equipa.
                this.equipable = false;

                // No funciona pasivamente.
                this.mejoraPasiva = false;

                // Solo se puede usar una vez.
                this.usosMaximos = 1;

                // Su efecto es instantáneo.
                this.duracionTurnos = -1;
                break;

            case POCION_FUERZA:
            case POCION_DEFENSA:

                // Estas pociones también desaparecen.
                this.fungible = true;

                // No se equipan.
                this.equipable = false;

                // No son mejoras pasivas.
                this.mejoraPasiva = false;

                // Solo tienen un uso.
                this.usosMaximos = 1;

                // Su efecto dura unos turnos concretos.
                this.duracionTurnos = 3;
                break;

            case ARMA:
            case ESCUDO:

                // Armas y escudos no se consumen.
                this.fungible = false;

                // Sí pueden equiparse.
                this.equipable = true;

                // No aplican automáticamente solo por llevarlos.
                this.mejoraPasiva = false;

                // Tienen usos infinitos.
                this.usosMaximos = -1;

                // Su efecto dura mientras estén equipados.
                this.duracionTurnos = -1;
                break;

            case LLAVE:

                // La llave se consume al abrir una puerta.
                this.fungible = true;

                // No se equipa.
                this.equipable = false;

                // No tiene efecto pasivo.
                this.mejoraPasiva = false;

                // Solo sirve una vez.
                this.usosMaximos = 1;

                // No tiene duración temporal.
                this.duracionTurnos = -1;
                break;

            case OBJETO_ESPECIAL:

                // Los objetos especiales usan los valores por defecto,
                // salvo que luego se cambien manualmente.
                break;
        }

        /**
         * Si el objeto tiene usos limitados,
         * inicializamos los usos restantes con el máximo.
         */
        if (this.usosMaximos != -1) {

            // Ejemplo:
            // si usosMaximos = 1,
            // entonces usosRestantes empieza en 1.
            this.usosRestantes = this.usosMaximos;
        }
    }

    /**
     * Devuelve el tipo del objeto.
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * Devuelve el nombre del objeto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve la descripción del objeto.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Devuelve el valor numérico del efecto.
     */
    public int getValor() {
        return valor;
    }

    /**
     * Indica si el objeto desaparece tras usarlo.
     */
    public boolean isFungible() {
        return fungible;
    }

    /**
     * Indica si el objeto puede equiparse.
     */
    public boolean isEquipable() {
        return equipable;
    }

    /**
     * Indica si el objeto da mejoras pasivas.
     */
    public boolean isMejoraPasiva() {
        return mejoraPasiva;
    }

    /**
     * Devuelve el número máximo de usos.
     */
    public int getUsosMaximos() {
        return usosMaximos;
    }

    /**
     * Devuelve los usos restantes.
     */
    public int getUsosRestantes() {
        return usosRestantes;
    }

    /**
     * Cambia los usos restantes.
     *
     * @param usosRestantes nuevo número de usos
     */
    public void setUsosRestantes(int usosRestantes) {

        // Actualizamos los usos restantes.
        this.usosRestantes = usosRestantes;
    }

    /**
     * Devuelve cuántos turnos dura el efecto.
     */
    public int getDuracionTurnos() {
        return duracionTurnos;
    }

    /**
     * Usa el objeto.
     *
     * Si el objeto tiene usos limitados,
     * reduce en 1 los usos restantes.
     *
     * @return true si se pudo usar, false si ya no quedan usos
     */
    public boolean usar() {

        // Si usosMaximos es -1, significa que el objeto es infinito.
        if (usosMaximos == -1) {

            // Siempre se puede usar.
            return true;
        }

        // Si todavía quedan usos disponibles.
        if (usosRestantes > 0) {

            // Consumimos un uso.
            usosRestantes--;

            // Indicamos que el uso fue correcto.
            return true;
        }

        // Si no quedaban usos, devolvemos false.
        return false;
    }

    /**
     * Comprueba si el objeto todavía puede usarse.
     *
     * @return true si aún tiene usos disponibles
     */
    public boolean puedeUsarse() {

        // Si tiene usos infinitos.
        if (usosMaximos == -1) {

            // Siempre puede utilizarse.
            return true;
        }

        // Si tiene usos limitados,
        // comprobamos que quede al menos uno.
        return usosRestantes > 0;
    }

    /**
     * Convierte el objeto a texto.
     *
     * Se usa por ejemplo para mostrarlo en listas o inventarios.
     */
    @Override
    public String toString() {

        // Devolvemos nombre y descripción juntos.
        return nombre + " (" + descripcion + ")";
    }
}
