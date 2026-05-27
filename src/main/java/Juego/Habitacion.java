package Juego; // indica el paquete al que pertenece esta clase.

import Juego.listas.ListaSimplementeEnlazada; // importa la lista enlazada utilizada en la clase.

/** Clase que representa una habitación del juego, cada habitación está formada por una matriz de celdas donde pueden existir enemigos, objetos, trampas, puertas y salidas.
 */
public class Habitacion {

    // matriz que almacena todas las celdas de la habitación
    private Celda[][] matriz;

    // número de filas y columnas de la habitación
    private int filas;
    private int columnas;

    // identificador único de la habitación
    private String id;

    /** Constructor de la habitación. Inicializa la matriz y crea todas las celdas vacías.
     */
    public Habitacion(String id, int filas, int columnas) {


        this.id = id;   // guarda el identificador
        this.filas = filas; // guarda el número de filas
        this.columnas = columnas;   // guarda el número de columnas
        this.matriz = new Celda[filas][columnas];   // crea la matriz de celdas

        // inicializa todas las celdas como vacías
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

                // crea una celda vacía en cada posición
                matriz[i][j] = new Celda(Celda.Tipo.VACIA);
            }
        }
    }

    /** Devuelve el identificador de la habitación
     */
    public String getId() {
        return id;
    }

    /** Devuelve el número de filas
     */
    public int getFilas() {
        return filas;
    }

    /** Devuelve el número de columnas
     */
    public int getColumnas() {
        return columnas;
    }

    /** Devuelve la celda de una posición concreta
     */
    public Celda getCelda(int fila, int columna) {

        // comprueba si la posición es válida
        if (esPosicionValida(fila, columna)) {

            // devuelve la celda correspondiente
            return matriz[fila][columna];
        }

        // si no es válida devuelve null
        return null;
    }

    /** Modifica una celda concreta de la matriz
     */
    public void setCelda(int fila, int columna, Celda celda) {

        // comprueba si la posición existe
        if (esPosicionValida(fila, columna)) {

            // sustituye la celda
            matriz[fila][columna] = celda;
        }
    }

    /** Comprueba si una posición está dentro de la habitación
     */
    public boolean esPosicionValida(int fila, int columna) {

        return fila >= 0 &&
                fila < filas &&
                columna >= 0 &&
                columna < columnas;
    }

    /** Coloca un objeto en una celda específica
     */
    public void colocarObjeto(int fila, int columna, Object objeto) {

        // comprueba si la posición es válida
        if (esPosicionValida(fila, columna)) {
            Celda celda = getCelda(fila, columna);  // obtiene la celda
            celda.setTipo(Celda.Tipo.OBJETO);   // cambia el tipo de la celda
            celda.setContenido(objeto); // guarda el objeto en la celda
        }
    }

    /** Coloca un enemigo en una celda específica
     */
    public void colocarEnemigo(int fila, int columna, Object enemigo) {

        // comprueba si la posición es válida
        if (esPosicionValida(fila, columna)) {
            Celda celda = getCelda(fila, columna);  // obtiene la celda
            celda.setTipo(Celda.Tipo.ENEMIGO);  // marca la celda como enemigo
            celda.setContenido(enemigo);    // guarda el enemigo en la celda

            // si realmente es un enemigo, también actualiza su posición interna
            if (enemigo instanceof Enemigo) {

                ((Enemigo) enemigo).setPosicion(fila, columna);
            }
        }
    }

    /** Coloca una puerta en la habitación
     */
    public void colocarPuerta(int fila, int columna, String destino) {

        // comprueba si la posición existe
        if (esPosicionValida(fila, columna)) {
            Celda celda = getCelda(fila, columna);  // obtiene la celda
            celda.setTipo(Celda.Tipo.PUERTA);   // marca la celda como puerta
            celda.setContenido(destino);    // guarda el identificador de la habitación destino
        }
    }

    /** Coloca una trampa en una celda
     */
    public void colocarTrampa(int fila, int columna) {

        // comprueba si la posición es válida
        if (esPosicionValida(fila, columna)) {
            Celda celda = getCelda(fila, columna);  // obtiene la celda
            celda.setTipo(Celda.Tipo.TRAMPA);   // marca la celda como trampa

            celda.setAccesible(true);   // las trampas se pueden pisar
        }
    }

    /** Coloca una salida en la habitación
     */
    public void colocarSalida(int fila, int columna) {

        // comprueba si la posición existe
        if (esPosicionValida(fila, columna)) {
            Celda celda = getCelda(fila, columna);  // obtiene la celda
            celda.setTipo(Celda.Tipo.SALIDA);   // marca la celda como salida
        }
    }

    /** Devuelve todas las posiciones vacías y accesibles
     */
    public ListaSimplementeEnlazada<Posicion> obtenerPosicionesVacias() {

        // lista donde se guardarán las posiciones
        ListaSimplementeEnlazada<Posicion> posiciones =
                new ListaSimplementeEnlazada<>();

        // recorre toda la matriz
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

                // comprueba si la celda está vacía y es accesible
                if (matriz[i][j].getTipo() == Celda.Tipo.VACIA
                        && matriz[i][j].isAccesible()) {

                    // guarda la posición
                    posiciones.insertarUltimo(new Posicion(i, j));
                }
            }
        }

        return posiciones;
    }

    /** Devuelve todas las posiciones con objetos
     */
    public ListaSimplementeEnlazada<Posicion> obtenerPosicionesConObjetos() {

        ListaSimplementeEnlazada<Posicion> posiciones =
                new ListaSimplementeEnlazada<>();

        // recorre toda la habitación
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

                // comprueba si la celda tiene un objeto
                if (matriz[i][j].getTipo() == Celda.Tipo.OBJETO) {

                    posiciones.insertarUltimo(new Posicion(i, j));
                }
            }
        }

        return posiciones;
    }

    /** Devuelve todas las posiciones donde hay enemigos
     */
    public ListaSimplementeEnlazada<Posicion> obtenerPosicionesConEnemigos() {

        ListaSimplementeEnlazada<Posicion> posiciones =
                new ListaSimplementeEnlazada<>();

        // recorre toda la matriz
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

                // comprueba si la celda contiene un enemigo
                if (matriz[i][j].getTipo() == Celda.Tipo.ENEMIGO) {

                    posiciones.insertarUltimo(new Posicion(i, j));
                }
            }
        }

        return posiciones;
    }

    /** Devuelve todas las posiciones con puertas
     */
    public ListaSimplementeEnlazada<Posicion> obtenerPosicionesConPuertas() {

        ListaSimplementeEnlazada<Posicion> posiciones =
                new ListaSimplementeEnlazada<>();

        // recorre la matriz completa
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

                // comprueba si hay una puerta
                if (matriz[i][j].getTipo() == Celda.Tipo.PUERTA) {

                    posiciones.insertarUltimo(new Posicion(i, j));
                }
            }
        }

        return posiciones;
    }

    /** Calcula las posiciones alcanzables por el jugador en línea recta hasta una distancia máxima.
     */
    public ListaSimplementeEnlazada<Posicion> calcularPosicionesAlcanzables(
            Posicion origen,
            int distanciaMaxima
    ) {

        // lista de posiciones alcanzables
        ListaSimplementeEnlazada<Posicion> alcanzables =
                new ListaSimplementeEnlazada<>();

        // si el origen es nulo devuelve lista vacía
        if (origen == null) {
            return alcanzables;
        }


        int filaOrig = origen.getFila();    // guarda la fila de origen


        int colOrig = origen.getColumna();  // guarda la columna de origen


        alcanzables.insertarUltimo(origen); // la propia posición también cuenta

        // direcciones posibles: arriba, abajo, izquierda y derecha
        int[][] direcciones = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        // recorre todas las direcciones
        for (int[] dir : direcciones) {

            // avanza hasta la distancia máxima
            for (int d = 1; d <= distanciaMaxima; d++) {


                int nuevaFila = filaOrig + (dir[0] * d);    // calcula la nueva fila


                int nuevaCol = colOrig + (dir[1] * d);  // calcula la nueva columna

                // comprueba si la posición existe
                if (esPosicionValida(nuevaFila, nuevaCol)) {


                    Celda celda = getCelda(nuevaFila, nuevaCol);    // obtiene la celda destino

                    // si está libre se añade a las alcanzables
                    if (celda != null &&
                            celda.estaLibreParaMovimiento()) {

                        alcanzables.insertarUltimo(
                                new Posicion(nuevaFila, nuevaCol)
                        );

                    } else {

                        // si encuentra un obstáculo deja de avanzar
                        break;
                    }
                }
            }
        }

        return alcanzables;
    }

    /** Devuelve una representación en texto de la habitación
     */
    @Override
    public String toString() {

        // crea un StringBuilder para construir el texto
        StringBuilder sb = new StringBuilder();

        // añade información básica
        sb.append("Habitación ")
                .append(id)
                .append(" (")
                .append(filas)
                .append("x")
                .append(columnas)
                .append("):\n");

        // recorre la matriz completa
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

                // añade la representación de cada celda
                sb.append(matriz[i][j]).append(" ");
            }

            // salto de línea por fila
            sb.append("\n");
        }

        return sb.toString();
    }
}

/** Clase auxiliar que representa una posición dentro de la habitación
 */
class Posicion {


    private int fila;   // fila de la posición


    private int columna;    // columna de la posición

    /** Constructor de la posición
     */
    public Posicion(int fila, int columna) {


        this.fila = fila;   // guarda la fila


        this.columna = columna; // guarda la columna
    }

    /** Devuelve la fila
     */
    public int getFila() {
        return fila;
    }

    /** Devuelve la columna
     */
    public int getColumna() {
        return columna;
    }

    /** Comprueba si dos posiciones son iguales
     */
    @Override
    public boolean equals(Object obj) {

        // comprueba si son el mismo objeto
        if (this == obj) {
            return true;
        }

        // comprueba si el objeto es válido
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // convierte el objeto
        Posicion otra = (Posicion) obj;

        // compara fila y columna
        return fila == otra.fila &&
                columna == otra.columna;
    }

    /** Devuelve la posición en formato texto
     */
    @Override
    public String toString() {

        return "(" + fila + "," + columna + ")";
    }
}
