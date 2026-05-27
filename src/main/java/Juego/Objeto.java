package Juego;

/**
 * Clase que representa un objeto del juego.
 * Los objetos pueden: curar vida, aumentar ataque o defensa, equiparse, abrir puertas o tener efectos especiales
 */
public class Objeto {

    /** Tipos posibles de objetos.
     */
    public enum Tipo {

        POCION_VIDA,        // cura vida al jugador
        POCION_FUERZA,      // Aumenta temporalmente el ataque
        POCION_DEFENSA,     // Aumenta temporalmente la defensa
        ARMA,       // objeto equipable que mejora el ataque
        ESCUDO,     // objeto equipable que mejora la defensa
        LLAVE,      // objeto necesario para abrir puertas especiales
        OBJETO_ESPECIAL     // tipo genérico para objetos especiales personalizados
    }

    // tipo concreto de este objeto
    private Tipo tipo;

    // nombre visible del objeto
    private String nombre;

    // explicación corta de lo que hace el objeto
    private String descripcion;

    // intensidad o potencia del objeto
    private int valor;

    // indica si el objeto desaparece después de usarse
    // true = se consume
    // false = permanece
    private boolean fungible;

    // indica si el objeto puede equiparse
    private boolean equipable;

    // indica si el objeto da la mejora solo por llevarlo encima, sin necesidad de equiparlo manualmente
    private boolean mejoraPasiva;

    // número máximo de usos del objeto, -1 significa usos infinitos
    private int usosMaximos;

    // número de usos que quedan actualmente
    private int usosRestantes;

    // número de turnos que dura el efecto, -1 significa efecto permanente
    private int duracionTurnos;

    /**
     * Constructor principal del objeto. Según el tipo recibido, se configuran automáticamente sus propiedades básicas
     */
    public Objeto(Tipo tipo, String nombre, String descripcion, int valor) {

        // Guardamos el tipo del objeto, el nombre, la descripción y el valor numérico del efecto
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valor = valor;

        // por defecto asumimos que los objetos son consumibles
        this.fungible = true;

        // por defecto asumimos que no se equipan, que no tienen mejora pasiva y que los usos son ilimitados
        this.equipable = false;
        this.mejoraPasiva = false;
        this.usosMaximos = -1;

        // también dejamos los usos restantes como ilimitados
        this.usosRestantes = -1;

        // por defecto el efecto es permanente
        this.duracionTurnos = -1;

        /** Según el tipo del objeto, configuramos automáticamente sus propiedades
         */
        switch (tipo) {

            case POCION_VIDA:

                // la poción desaparece al usarla, no equipa, no funciona pasivamente, solo se puede usar una vez y su efecto es instantáneo
                this.fungible = true;
                this.equipable = false;
                this.mejoraPasiva = false;
                this.usosMaximos = 1;
                this.duracionTurnos = -1;
                break;

            case POCION_FUERZA:
            case POCION_DEFENSA:

                // estas pociones también desaparecen, no se equipan, no son mejoras pasivas, solo tienen un uso y su efecto dura unos turnos concretos
                this.fungible = true;
                this.equipable = false;
                this.mejoraPasiva = false;
                this.usosMaximos = 1;
                this.duracionTurnos = 3;
                break;

            case ARMA:
            case ESCUDO:


                this.fungible = false;  // armas y escudos no se consumen
                this.equipable = true;  // si pueden equiparse
                this.mejoraPasiva = false;  // no aplican automáticamente solo por llevarse
                this.usosMaximos = -1;  // tienen usos infinitos
                this.duracionTurnos = -1;   // su efecto dura mientras estén equipados
                break;

            case LLAVE:


                this.fungible = true;   // La llave se consume al abrir una puerta
                this.equipable = false; // no se equipa
                this.mejoraPasiva = false;  // no tiene efecto pasivo
                this.usosMaximos = 1;   // solo sirve una vez
                this.duracionTurnos = -1;   // no tiene duración temporal
                break;

            case OBJETO_ESPECIAL:

                // los objetos especiales usan los valores por defecto, salvo que luego se cambien manualmente
                break;
        }

        /** Si el objeto tiene usos limitados, inicializamos los usos restantes con el máximo
         */
        if (this.usosMaximos != -1) {
            this.usosRestantes = this.usosMaximos;
        }
    }

    /** Devuelve el tipo del objeto
     */
    public Tipo getTipo() {
        return tipo;
    }

    /** Devuelve el nombre del objeto
     */
    public String getNombre() {
        return nombre;
    }

    /** Devuelve la descripción del objeto
     */
    public String getDescripcion() {
        return descripcion;
    }

    /** Devuelve el valor numérico del efecto
     */
    public int getValor() {
        return valor;
    }

    /** Indica si el objeto desaparece tras usarlo
     */
    public boolean isFungible() {
        return fungible;
    }

    /** Indica si el objeto puede equiparse
     */
    public boolean isEquipable() {
        return equipable;
    }

    /** Indica si el objeto da mejoras pasivas
     */
    public boolean isMejoraPasiva() {
        return mejoraPasiva;
    }

    /** Devuelve el número máximo de usos
     */
    public int getUsosMaximos() {
        return usosMaximos;
    }

    /** Devuelve los usos restantes
     */
    public int getUsosRestantes() {
        return usosRestantes;
    }

    /** Cambia los usos restantes
     */
    public void setUsosRestantes(int usosRestantes) {

        // actualizamos los usos restantes
        this.usosRestantes = usosRestantes;
    }

    /** Devuelve cuántos turnos dura el efecto
     */
    public int getDuracionTurnos() {
        return duracionTurnos;
    }

    /** Usa el objeto; si el objeto tiene usos limitados, reduce en 1 los usos restantes
     */
    public boolean usar() {

        // si usosMaximos es -1, significa que el objeto es infinito
        if (usosMaximos == -1) {

            // siempre se puede usar
            return true;
        }

        // si todavía quedan usos disponibles
        if (usosRestantes > 0) {

            // consumimos un uso
            usosRestantes--;

            // indicamos que el uso fue correcto
            return true;
        }

        // si no quedaban usos, devolvemos false
        return false;
    }

    /** Comprueba si el objeto todavía puede usarse
     */
    public boolean puedeUsarse() {

        // si tiene usos infinitos, siempre puede utilizarse
        if (usosMaximos == -1) {
            return true;
        }

        // si tiene usos limitados, comprobamos que quede al menos uno
        return usosRestantes > 0;
    }

    /** Convierte el objeto a texto. Se usa por ejemplo para mostrarlo en listas o inventarios
     */
    @Override
    public String toString() {

        // devolvemos nombre y descripción juntos
        return nombre + " (" + descripcion + ")";
    }
}
