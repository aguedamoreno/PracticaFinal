package Juego; // indica el paquete al que pertenece esta clase.

import Juego.listas.ListaSimplementeEnlazada; // importa una clase externa necesaria para este archivo.

/**
 * Clase que representa una habitación rectangular formada por celdas y ofrece métodos para colocar elementos y consultar posiciones.
 *
 * Comentarios añadidos para explicar la función de la clase, sus variables
 * y los bloques principales de código sin cambiar la lógica original.
 */
public class Habitacion { // declara una clase que agrupa datos y métodos relacionados.
    private Celda[][] matriz; // declara un atributo/campo de la clase donde se guarda estado.
    private int filas; // declara un atributo/campo de la clase donde se guarda estado.
    private int columnas; // declara un atributo/campo de la clase donde se guarda estado.
    private String id; // Identificador único de la habitación

    /**
     * Constructor que inicializa los atributos principales del objeto.
     */
    public Habitacion(String id, int filas, int columnas) {
        this.id = id; // guarda el valor recibido dentro del atributo del objeto actual.
        this.filas = filas; // guarda el valor recibido dentro del atributo del objeto actual.
        this.columnas = columnas; // guarda el valor recibido dentro del atributo del objeto actual.
        this.matriz = new Celda[filas][columnas]; // crea un nuevo objeto para poder usarlo después.

        // Inicializar todas las celdas como vacías
        for (int i = 0; i < filas; i++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
            for (int j = 0; j < columnas; j++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
                matriz[i][j] = new Celda(Celda.Tipo.VACIA); // crea un nuevo objeto para poder usarlo después.
            }
        }
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public String getId() {
        return id; // devuelve el resultado calculado por el método.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getFilas() {
        return filas; // devuelve el resultado calculado por el método.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getColumnas() {
        return columnas; // devuelve el resultado calculado por el método.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public Celda getCelda(int fila, int columna) {
        if (esPosicionValida(fila, columna)) { // comprueba una condición para decidir qué camino sigue el programa.
            return matriz[fila][columna]; // devuelve el resultado calculado por el método.
        }
        return null; // devuelve el resultado calculado por el método.
    }

    /**
     * Método setter que modifica el valor de un atributo.
     */
    public void setCelda(int fila, int columna, Celda celda) {
        if (esPosicionValida(fila, columna)) { // comprueba una condición para decidir qué camino sigue el programa.
            matriz[fila][columna] = celda; // asigna o actualiza un valor necesario para el estado del programa.
        }
    }

    /**
     * Método de apoyo usado por la clase para completar la lógica del juego.
     */
    public boolean esPosicionValida(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas; // devuelve el resultado calculado por el método.
    }

    /**
     * Coloca un objeto en una posición específica
     * @param fila Fila de la matriz
     * @param columna Columna de la matriz
     * @param objeto Objeto a colocar
     */
    public void colocarObjeto(int fila, int columna, Object objeto) {
        if (esPosicionValida(fila, columna)) { // comprueba una condición para decidir qué camino sigue el programa.
            Celda celda = getCelda(fila, columna); // asigna o actualiza un valor necesario para el estado del programa.
            celda.setTipo(Celda.Tipo.OBJETO); // ejecuta una llamada a un método para realizar una acción concreta.
            celda.setContenido(objeto); // ejecuta una llamada a un método para realizar una acción concreta.
        }
    }

    /**
     * Coloca un enemigo en una posición específica
     * @param fila Fila de la matriz
     * @param columna Columna de la matriz
     * @param enemigo Enemigo a colocar
     */
    public void colocarEnemigo(int fila, int columna, Object enemigo) {
        if (esPosicionValida(fila, columna)) { // comprueba una condición para decidir qué camino sigue el programa.
            Celda celda = getCelda(fila, columna); // asigna o actualiza un valor necesario para el estado del programa.
            celda.setTipo(Celda.Tipo.ENEMIGO); // ejecuta una llamada a un método para realizar una acción concreta.
            celda.setContenido(enemigo); // ejecuta una llamada a un método para realizar una acción concreta.

            // El enemigo también guarda internamente su posición.
            if (enemigo instanceof Enemigo) { // comprueba una condición para decidir qué camino sigue el programa.
                ((Enemigo) enemigo).setPosicion(fila, columna); // ejecuta una llamada a un método para realizar una acción concreta.
            }
        }
    }

    /**
     * Coloca una puerta en una posición específica
     * @param fila Fila de la matriz
     * @param columna Columna de la matriz
     * @param destino Id de la habitación de destino (opcional)
     */
    public void colocarPuerta(int fila, int columna, String destino) {
        if (esPosicionValida(fila, columna)) { // comprueba una condición para decidir qué camino sigue el programa.
            Celda celda = getCelda(fila, columna); // asigna o actualiza un valor necesario para el estado del programa.
            celda.setTipo(Celda.Tipo.PUERTA); // ejecuta una llamada a un método para realizar una acción concreta.
            celda.setContenido(destino); // El destino puede ser el id de otra habitación
        }
    }

    /**
     * Coloca una trampa en una posición específica
     * @param fila Fila de la matriz
     * @param columna Columna de la matriz
     */
    public void colocarTrampa(int fila, int columna) {
        if (esPosicionValida(fila, columna)) { // comprueba una condición para decidir qué camino sigue el programa.
            Celda celda = getCelda(fila, columna); // asigna o actualiza un valor necesario para el estado del programa.
            celda.setTipo(Celda.Tipo.TRAMPA); // ejecuta una llamada a un método para realizar una acción concreta.
            celda.setAccesible(true); // Las trampas SÍ son accesibles: el jugador puede pisarlas y se activan
        }
    }

    /**
     * Coloca una salida en una posición específica
     * @param fila Fila de la matriz
     * @param columna Columna de la matriz
     */
    public void colocarSalida(int fila, int columna) {
        if (esPosicionValida(fila, columna)) { // comprueba una condición para decidir qué camino sigue el programa.
            Celda celda = getCelda(fila, columna); // asigna o actualiza un valor necesario para el estado del programa.
            celda.setTipo(Celda.Tipo.SALIDA); // ejecuta una llamada a un método para realizar una acción concreta.
        }
    }

    /**
     * Obtiene todas las posiciones vacías en la habitación
     * @return Lista de coordenadas (fila, columna) de celdas vacías
     */
    public ListaSimplementeEnlazada<Posicion> obtenerPosicionesVacias() {
        ListaSimplementeEnlazada<Posicion> posiciones = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.
        for (int i = 0; i < filas; i++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
            for (int j = 0; j < columnas; j++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
                if (matriz[i][j].getTipo() == Celda.Tipo.VACIA && matriz[i][j].isAccesible()) { // comprueba una condición para decidir qué camino sigue el programa.
                    posiciones.insertarUltimo(new Posicion(i, j)); // crea un nuevo objeto para poder usarlo después.
                }
            }
        }
        return posiciones; // devuelve el resultado calculado por el método.
    }

    /**
     * Obtiene todas las posiciones con objetos
     * @return Lista de coordenadas (fila, columna) de celdas con objetos
     */
    public ListaSimplementeEnlazada<Posicion> obtenerPosicionesConObjetos() {
        ListaSimplementeEnlazada<Posicion> posiciones = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.
        for (int i = 0; i < filas; i++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
            for (int j = 0; j < columnas; j++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
                if (matriz[i][j].getTipo() == Celda.Tipo.OBJETO) { // comprueba una condición para decidir qué camino sigue el programa.
                    posiciones.insertarUltimo(new Posicion(i, j)); // crea un nuevo objeto para poder usarlo después.
                }
            }
        }
        return posiciones; // devuelve el resultado calculado por el método.
    }

    /**
     * Obtiene todas las posiciones con enemigos
     * @return Lista de coordenadas (fila, columna) de celdas con enemigos
     */
    public ListaSimplementeEnlazada<Posicion> obtenerPosicionesConEnemigos() {
        ListaSimplementeEnlazada<Posicion> posiciones = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.
        for (int i = 0; i < filas; i++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
            for (int j = 0; j < columnas; j++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
                if (matriz[i][j].getTipo() == Celda.Tipo.ENEMIGO) { // comprueba una condición para decidir qué camino sigue el programa.
                    posiciones.insertarUltimo(new Posicion(i, j)); // crea un nuevo objeto para poder usarlo después.
                }
            }
        }
        return posiciones; // devuelve el resultado calculado por el método.
    }

    /**
     * Obtiene todas las posiciones con puertas
     * @return Lista de coordenadas (fila, columna) de celdas con puertas
     */
    public ListaSimplementeEnlazada<Posicion> obtenerPosicionesConPuertas() {
        ListaSimplementeEnlazada<Posicion> posiciones = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.
        for (int i = 0; i < filas; i++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
            for (int j = 0; j < columnas; j++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
                if (matriz[i][j].getTipo() == Celda.Tipo.PUERTA) { // comprueba una condición para decidir qué camino sigue el programa.
                    posiciones.insertarUltimo(new Posicion(i, j)); // crea un nuevo objeto para poder usarlo después.
                }
            }
        }
        return posiciones; // devuelve el resultado calculado por el método.
    }

    /**
     * Calcula las posiciones alcanzables en cruz (solo arriba, abajo, izquierda, derecha)
     * a una distancia máxima dada (por regla, 2 casillas).
     */
    public ListaSimplementeEnlazada<Posicion> calcularPosicionesAlcanzables(Posicion origen, int distanciaMaxima) {
        ListaSimplementeEnlazada<Posicion> alcanzables = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.

        if (origen == null) return alcanzables; // comprueba una condición para decidir qué camino sigue el programa.

        int filaOrig = origen.getFila(); // asigna o actualiza un valor necesario para el estado del programa.
        int colOrig = origen.getColumna(); // asigna o actualiza un valor necesario para el estado del programa.

        // El propio origen es alcanzable (por si decide no moverse)
        alcanzables.insertarUltimo(origen); // ejecuta una llamada a un método para realizar una acción concreta.

        // Direcciones en cruz: {fila, columna} -> Arriba, Abajo, Izquierda, Derecha
        int[][] direcciones = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} }; // asigna o actualiza un valor necesario para el estado del programa.

        for (int[] dir : direcciones) { // bucle que repite instrucciones recorriendo elementos o posiciones.
            for (int d = 1; d <= distanciaMaxima; d++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
                int nuevaFila = filaOrig + (dir[0] * d); // asigna o actualiza un valor necesario para el estado del programa.
                int nuevaCol = colOrig + (dir[1] * d); // asigna o actualiza un valor necesario para el estado del programa.

                if (esPosicionValida(nuevaFila, nuevaCol)) { // comprueba una condición para decidir qué camino sigue el programa.
                    Celda celda = getCelda(nuevaFila, nuevaCol); // asigna o actualiza un valor necesario para el estado del programa.
                    // Si hay un muro o no es accesible, se corta la línea en esa dirección
                    if (celda != null && celda.estaLibreParaMovimiento()) { // comprueba una condición para decidir qué camino sigue el programa.
                        alcanzables.insertarUltimo(new Posicion(nuevaFila, nuevaCol)); // crea un nuevo objeto para poder usarlo después.
                    } else {
                        // Si se topa con un obstáculo infranqueable, no puede saltárselo
                        break; // controla el flujo del bucle actual.
                    }
                }
            }
        }

        return alcanzables; // devuelve el resultado calculado por el método.
    }


    @Override
    /**
     * Devuelve una representación en texto del objeto para mostrarlo fácilmente.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder(); // crea un nuevo objeto para poder usarlo después.
        sb.append("Habitación ").append(id).append(" (").append(filas).append("x").append(columnas).append("):\n"); // ejecuta una llamada a un método para realizar una acción concreta.
        for (int i = 0; i < filas; i++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
            for (int j = 0; j < columnas; j++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
                sb.append(matriz[i][j]).append(" "); // ejecuta una llamada a un método para realizar una acción concreta.
            }
            sb.append("\n"); // ejecuta una llamada a un método para realizar una acción concreta.
        }
        return sb.toString(); // devuelve el resultado calculado por el método.
    }
}

/**
 * Clase simple para representar una posición (fila, columna)
 */
class Posicion { // declara una clase que agrupa datos y métodos relacionados.
    private int fila; // declara un atributo/campo de la clase donde se guarda estado.
    private int columna; // declara un atributo/campo de la clase donde se guarda estado.

    /**
     * Método de apoyo usado por la clase para completar la lógica del juego.
     */
    public Posicion(int fila, int columna) {
        this.fila = fila; // guarda el valor recibido dentro del atributo del objeto actual.
        this.columna = columna; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getFila() {
        return fila; // devuelve el resultado calculado por el método.
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getColumna() {
        return columna; // devuelve el resultado calculado por el método.
    }

    @Override
    /**
     * Compara este objeto con otro para saber si representan lo mismo.
     */
    public boolean equals(Object obj) {
        if (this == obj) return true; // comprueba una condición para decidir qué camino sigue el programa.
        if (obj == null || getClass() != obj.getClass()) return false; // comprueba una condición para decidir qué camino sigue el programa.
        Posicion otra = (Posicion) obj; // asigna o actualiza un valor necesario para el estado del programa.
        return fila == otra.fila && columna == otra.columna; // devuelve el resultado calculado por el método.
    }

    @Override
    /**
     * Devuelve una representación en texto del objeto para mostrarlo fácilmente.
     */
    public String toString() {
        return "(" + fila + "," + columna + ")"; // devuelve el resultado calculado por el método.
    }
}

