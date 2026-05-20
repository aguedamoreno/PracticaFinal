package Juego;
import Juego.listas.ListaSimplementeEnlazada;

public class Habitacion {
    private Celda[][] matriz;
    private int filas;
    private int columnas;
    private String id; // Identificador único de la habitación

    public Habitacion(String id, int filas, int columnas) {
        this.id = id;
        this.filas = filas;
        this.columnas = columnas;
        this.matriz = new Celda[filas][columnas];

        // Inicializar todas las celdas como vacías
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = new Celda(Celda.Tipo.VACIA);
            }
        }
    }

    public String getId() {
        return id;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public Celda getCelda(int fila, int columna) {
        if (esPosicionValida(fila, columna)) {
            return matriz[fila][columna];
        }
        return null;
    }

    public void setCelda(int fila, int columna, Celda celda) {
        if (esPosicionValida(fila, columna)) {
            matriz[fila][columna] = celda;
        }
    }

    public boolean esPosicionValida(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    /**
     * Coloca un objeto en una posición específica
     * @param fila Fila de la matriz
     * @param columna Columna de la matriz
     * @param objeto Objeto a colocar
     */
    public void colocarObjeto(int fila, int columna, Object objeto) {
        if (esPosicionValida(fila, columna)) {
            Celda celda = getCelda(fila, columna);
            celda.setTipo(Celda.Tipo.OBJETO);
            celda.setContenido(objeto);
        }
    }

    /**
     * Coloca un enemigo en una posición específica
     * @param fila Fila de la matriz
     * @param columna Columna de la matriz
     * @param enemigo Enemigo a colocar
     */
    public void colocarEnemigo(int fila, int columna, Object enemigo) {
        if (esPosicionValida(fila, columna)) {
            Celda celda = getCelda(fila, columna);
            celda.setTipo(Celda.Tipo.ENEMIGO);
            celda.setContenido(enemigo);
        }
    }

    /**
     * Coloca una puerta en una posición específica
     * @param fila Fila de la matriz
     * @param columna Columna de la matriz
     * @param destino Id de la habitación de destino (opcional)
     */
    public void colocarPuerta(int fila, int columna, String destino) {
        if (esPosicionValida(fila, columna)) {
            Celda celda = getCelda(fila, columna);
            celda.setTipo(Celda.Tipo.PUERTA);
            celda.setContenido(destino); // El destino puede ser el id de otra habitación
        }
    }

    /**
     * Coloca una trampa en una posición específica
     * @param fila Fila de la matriz
     * @param columna Columna de la matriz
     */
    public void colocarTrampa(int fila, int columna) {
        if (esPosicionValida(fila, columna)) {
            Celda celda = getCelda(fila, columna);
            celda.setTipo(Celda.Tipo.TRAMPA);
            celda.setAccesible(false); // Las trampas no son accesibles inicialmente
        }
    }

    /**
     * Coloca una salida en una posición específica
     * @param fila Fila de la matriz
     * @param columna Columna de la matriz
     */
    public void colocarSalida(int fila, int columna) {
        if (esPosicionValida(fila, columna)) {
            Celda celda = getCelda(fila, columna);
            celda.setTipo(Celda.Tipo.SALIDA);
        }
    }

    /**
     * Obtiene todas las posiciones vacías en la habitación
     * @return Lista de coordenadas (fila, columna) de celdas vacías
     */
    public ListaSimplementeEnlazada<Posicion> obtenerPosicionesVacias() {
        ListaSimplementeEnlazada<Posicion> posiciones = new ListaSimplementeEnlazada<>();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (matriz[i][j].getTipo() == Celda.Tipo.VACIA && matriz[i][j].isAccesible()) {
                    posiciones.insertarUltimo(new Posicion(i, j));
                }
            }
        }
        return posiciones;
    }

    /**
     * Obtiene todas las posiciones con objetos
     * @return Lista de coordenadas (fila, columna) de celdas con objetos
     */
    public ListaSimplementeEnlazada<Posicion> obtenerPosicionesConObjetos() {
        ListaSimplementeEnlazada<Posicion> posiciones = new ListaSimplementeEnlazada<>();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (matriz[i][j].getTipo() == Celda.Tipo.OBJETO) {
                    posiciones.insertarUltimo(new Posicion(i, j));
                }
            }
        }
        return posiciones;
    }

    /**
     * Obtiene todas las posiciones con enemigos
     * @return Lista de coordenadas (fila, columna) de celdas con enemigos
     */
    public ListaSimplementeEnlazada<Posicion> obtenerPosicionesConEnemigos() {
        ListaSimplementeEnlazada<Posicion> posiciones = new ListaSimplementeEnlazada<>();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (matriz[i][j].getTipo() == Celda.Tipo.ENEMIGO) {
                    posiciones.insertarUltimo(new Posicion(i, j));
                }
            }
        }
        return posiciones;
    }

    /**
     * Obtiene todas las posiciones con puertas
     * @return Lista de coordenadas (fila, columna) de celdas con puertas
     */
    public ListaSimplementeEnlazada<Posicion> obtenerPosicionesConPuertas() {
        ListaSimplementeEnlazada<Posicion> posiciones = new ListaSimplementeEnlazada<>();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (matriz[i][j].getTipo() == Celda.Tipo.PUERTA) {
                    posiciones.insertarUltimo(new Posicion(i, j));
                }
            }
        }
        return posiciones;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Habitación ").append(id).append(" (").append(filas).append("x").append(columnas).append("):\n");
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                sb.append(matriz[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

/**
 * Clase simple para representar una posición (fila, columna)
 */
class Posicion {
    private int fila;
    private int columna;

    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Posicion otra = (Posicion) obj;
        return fila == otra.fila && columna == otra.columna;
    }

    @Override
    public String toString() {
        return "(" + fila + "," + columna + ")";
    }
}
