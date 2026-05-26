package Juego; // indica el paquete al que pertenece esta clase.
import Juego.listas.ListaSimplementeEnlazada; // importa una clase externa necesaria para este archivo.
import Juego.listas.Cola; // importa una clase externa necesaria para este archivo.

import Juego.listas.ListaSimplementeEnlazada; // importa una clase externa necesaria para este archivo.

/**
 * Clase que representa el mapa general como un grafo de habitaciones, con vértices, aristas, pesos y algoritmos de búsqueda de caminos.
 *
 * Comentarios añadidos para explicar la función de la clase, sus variables
 * y los bloques principales de código sin cambiar la lógica original.
 */
public class Grafo { // declara una clase que agrupa datos y métodos relacionados.
    private class Arista {
        int destino;
        double peso; // Para algoritmos como Dijkstra

        Arista(int destino, double peso) {
            this.destino = destino; // guarda el valor recibido dentro del atributo del objeto actual.
            this.peso = peso; // guarda el valor recibido dentro del atributo del objeto actual.
        }
    }



    private class Vertice {
        ListaSimplementeEnlazada<Arista> aristas;
        Object datos; // Para almacenar datos asociados al vértice (ej: Habitación)

        Vertice(Object datos) {
            this.datos = datos; // guarda el valor recibido dentro del atributo del objeto actual.
            this.aristas = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.
        }
    }

    private ListaSimplementeEnlazada<Vertice> vertices; // declara un atributo/campo de la clase donde se guarda estado.
    private boolean dirigido; // declara un atributo/campo de la clase donde se guarda estado.
    private int size; // declara un atributo/campo de la clase donde se guarda estado.

    /**
     * Constructor
     * @param dirigido true si el grafo es dirigido, false si no dirigido
     */
    public Grafo(boolean dirigido) {
        this.vertices = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.
        this.dirigido = dirigido; // guarda el valor recibido dentro del atributo del objeto actual.
        this.size = 0; // guarda el valor recibido dentro del atributo del objeto actual.
    }

    /**
     * Constructor que inicializa los atributos principales del objeto.
     */
    public Grafo() {
        this(false); // Por defecto, grafo no dirigido
    }

    /**
     * Añade un vértice al grafo
     * @param datos Datos asociados al vértice
     * @return Índice del vértice añadido
     */
    public int agregarVertice(Object datos) {
        vertices.insertarUltimo(new Vertice(datos)); // crea un nuevo objeto para poder usarlo después.
        size++;
        return size - 1; // devuelve el resultado calculado por el método.
    }

    /**
     * Añade una arista entre dos vértices
     * @param origen Índice del vértice origen
     * @param destino Índice del vértice destino
     * @param peso Peso de la arista (1.0 por defecto para no ponderado)
     * @throws IndexOutOfBoundsException Si algún índice es inválido
     */
    public void agregarArista(int origen, int destino, double peso) {
        validarIndiceVertice(origen); // ejecuta una llamada a un método para realizar una acción concreta.
        validarIndiceVertice(destino); // ejecuta una llamada a un método para realizar una acción concreta.

        vertices.obtener(origen).aristas.insertarUltimo(new Arista(destino, peso)); // crea un nuevo objeto para poder usarlo después.

        // Si es no dirigido, añadir la arista inversa
        if (!dirigido) { // comprueba una condición para decidir qué camino sigue el programa.
            vertices.obtener(destino).aristas.insertarUltimo(new Arista(origen, peso)); // crea un nuevo objeto para poder usarlo después.
        }
    }

    /**
     * Método de apoyo usado por la clase para completar la lógica del juego.
     */
    public void agregarArista(int origen, int destino) {
        agregarArista(origen, destino, 1.0); // Peso por defecto
    }

    /**
     * Obtiene los vértices adyacentes a un vértice dado
     * @param vertice Índice del vértice
     * @return Lista de índices de vértices adyacentes
     * @throws IndexOutOfBoundsException Si el índice es inválido
     */
    public ListaSimplementeEnlazada<Integer> obtenerAdyacentes(int vertice) {
        validarIndiceVertice(vertice); // ejecuta una llamada a un método para realizar una acción concreta.
        ListaSimplementeEnlazada<Integer> resultado = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.
        ListaSimplementeEnlazada<Arista> aristas = vertices.obtener(vertice).aristas; // asigna o actualiza un valor necesario para el estado del programa.

        // Usamos el iterador de tu ListaSimplementeEnlazada
        for (int i = 0; i < aristas.tamaño(); i++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
            Arista arista = aristas.obtener(i); // asigna o actualiza un valor necesario para el estado del programa.
            resultado.insertarUltimo(arista.destino); // ejecuta una llamada a un método para realizar una acción concreta.
        }
        return resultado; // devuelve el resultado calculado por el método.
    }

    /**
     * Obtiene el peso de una arista
     * @param origen Índice del vértice origen
     * @param destino Índice del vértice destino
     * @return Peso de la arista, o Double.POSITIVE_INFINITY si no existe
     */
    public double obtenerPesoArista(int origen, int destino) {
        validarIndiceVertice(origen); // ejecuta una llamada a un método para realizar una acción concreta.
        validarIndiceVertice(destino); // ejecuta una llamada a un método para realizar una acción concreta.

        ListaSimplementeEnlazada<Arista> aristas = vertices.obtener(origen).aristas; // asigna o actualiza un valor necesario para el estado del programa.
        for (int i = 0; i < aristas.tamaño(); i++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
            Arista arista = aristas.obtener(i); // asigna o actualiza un valor necesario para el estado del programa.
            if (arista.destino == destino) { // comprueba una condición para decidir qué camino sigue el programa.
                return arista.peso; // devuelve el resultado calculado por el método.
            }
        }
        return Double.POSITIVE_INFINITY; // No hay arista directa
    }

    /**
     * Algoritmo de BFS (Búsqueda en Anchura)
     * Útil para encontrar todas las casillas alcanzables dentro de un rango de movimiento
     * @param inicio Índice del vértice de inicio
     * @param maxDistancia Distancia máxima a explorar (en número de aristas)
     * @return Lista de vértices alcanzables dentro de maxDistancia
     */
    public ListaSimplementeEnlazada<Integer> bfs(int inicio, int maxDistancia) {
        validarIndiceVertice(inicio); // ejecuta una llamada a un método para realizar una acción concreta.

        ListaSimplementeEnlazada<Integer> resultado = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.
        boolean[] visitados = new boolean[size]; // crea un nuevo objeto para poder usarlo después.
        Cola<Integer> cola = new Cola<>(); // crea un nuevo objeto para poder usarlo después.
        Cola<Integer> distanciaCola = new Cola<>(); // crea un nuevo objeto para poder usarlo después.

        cola.enqueue(inicio); // ejecuta una llamada a un método para realizar una acción concreta.
        distanciaCola.enqueue(0); // ejecuta una llamada a un método para realizar una acción concreta.
        visitados[inicio] = true; // asigna o actualiza un valor necesario para el estado del programa.

        while (!cola.estaVacia()) { // bucle que se repite mientras la condición sea verdadera.
            int actual = cola.dequeue(); // asigna o actualiza un valor necesario para el estado del programa.
            int dist = distanciaCola.dequeue(); // asigna o actualiza un valor necesario para el estado del programa.

            if (dist <= maxDistancia) { // comprueba una condición para decidir qué camino sigue el programa.
                resultado.insertarUltimo(actual); // ejecuta una llamada a un método para realizar una acción concreta.

                // Si aún podemos explorar más lejos
                if (dist < maxDistancia) { // comprueba una condición para decidir qué camino sigue el programa.
                    ListaSimplementeEnlazada<Integer> adyacentes = obtenerAdyacentes(actual); // asigna o actualiza un valor necesario para el estado del programa.
                    for (int i = 0; i < adyacentes.tamaño(); i++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
                        int adyacente = adyacentes.obtener(i); // asigna o actualiza un valor necesario para el estado del programa.
                        if (!visitados[adyacente]) { // comprueba una condición para decidir qué camino sigue el programa.
                            visitados[adyacente] = true; // asigna o actualiza un valor necesario para el estado del programa.
                            cola.enqueue(adyacente); // ejecuta una llamada a un método para realizar una acción concreta.
                            distanciaCola.enqueue(dist + 1); // ejecuta una llamada a un método para realizar una acción concreta.
                        }
                    }
                }
            }
        }
        return resultado; // devuelve el resultado calculado por el método.
    }

    /**
     * Algoritmo de Dijkstra para encontrar el camino más corto
     * @param inicio Índice del vértice de inicio
     * @param fin Índice del vértice de destino
     * @return Array con [distancia, camino] o null si no hay camino
     *         El camino es una lista de índices de vértices desde inicio hasta fin
     */
    public Object[] dijkstra(int inicio, int fin) {
        validarIndiceVertice(inicio); // ejecuta una llamada a un método para realizar una acción concreta.
        validarIndiceVertice(fin); // ejecuta una llamada a un método para realizar una acción concreta.

        double[] distancias = new double[size]; // crea un nuevo objeto para poder usarlo después.
        int[] anteriores = new int[size]; // crea un nuevo objeto para poder usarlo después.
        boolean[] visitados = new boolean[size]; // crea un nuevo objeto para poder usarlo después.

        for (int i = 0; i < size; i++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
            distancias[i] = Double.POSITIVE_INFINITY; // asigna o actualiza un valor necesario para el estado del programa.
            anteriores[i] = -1; // asigna o actualiza un valor necesario para el estado del programa.
        }
        distancias[inicio] = 0; // asigna o actualiza un valor necesario para el estado del programa.

        // Cola de prioridad simple usando búsqueda lineal (para no complicar)
        for (int i = 0; i < size; i++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
            // Encontrar el vértice no visitado con distancia mínima
            int u = -1; // asigna o actualiza un valor necesario para el estado del programa.
            double minDist = Double.POSITIVE_INFINITY; // asigna o actualiza un valor necesario para el estado del programa.
            for (int v = 0; v < size; v++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
                if (!visitados[v] && distancias[v] < minDist) { // comprueba una condición para decidir qué camino sigue el programa.
                    minDist = distancias[v]; // asigna o actualiza un valor necesario para el estado del programa.
                    u = v; // asigna o actualiza un valor necesario para el estado del programa.
                }
            }

            if (u == -1) break; // No quedan vértices alcanzables
            if (u == fin) break; // Llegamos al destino

            visitados[u] = true; // asigna o actualiza un valor necesario para el estado del programa.

            // Actualizar distancias de los vecinos
            ListaSimplementeEnlazada<Integer> adyacentes = obtenerAdyacentes(u); // asigna o actualiza un valor necesario para el estado del programa.
            for (int j = 0; j < adyacentes.tamaño(); j++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
                int v = adyacentes.obtener(j); // asigna o actualiza un valor necesario para el estado del programa.
                if (!visitados[v]) { // comprueba una condición para decidir qué camino sigue el programa.
                    double alternativa = distancias[u] + obtenerPesoArista(u, v); // asigna o actualiza un valor necesario para el estado del programa.
                    if (alternativa < distancias[v]) { // comprueba una condición para decidir qué camino sigue el programa.
                        distancias[v] = alternativa; // asigna o actualiza un valor necesario para el estado del programa.
                        anteriores[v] = u; // asigna o actualiza un valor necesario para el estado del programa.
                    }
                }
            }
        }

        // Reconstruir camino si existe
        if (distancias[fin] == Double.POSITIVE_INFINITY) { // comprueba una condición para decidir qué camino sigue el programa.
            return null; // No hay camino
        }

        ListaSimplementeEnlazada<Integer> camino = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.
        for (int actual = fin; actual != -1; actual = anteriores[actual]) { // bucle que repite instrucciones recorriendo elementos o posiciones.
            camino.insertarPrimero(actual); // Insertar al inicio para invertir el orden
        }

        return new Object[]{distancias[fin], camino}; // crea un nuevo objeto para poder usarlo después.
    }

    /**
     * Método de apoyo usado por la clase para completar la lógica del juego.
     */
    private void validarIndiceVertice(int indice) {
        if (indice < 0 || indice >= size) { // comprueba una condición para decidir qué camino sigue el programa.
            throw new IndexOutOfBoundsException("Índice de vértice inválido: " + indice + // lanza una excepción para avisar de que la acción no es válida.
                    ". Tamaño del grafo: " + size); // ejecuta una llamada a un método para realizar una acción concreta.
        }
    }

    /**
     * Método getter que devuelve el valor actual de un atributo.
     */
    public int getSize() {
        return size; // devuelve el resultado calculado por el método.
    }

    /**
     * Método de apoyo usado por la clase para completar la lógica del juego.
     */
    public boolean estaVacio() {
        return size == 0; // devuelve el resultado calculado por el método.
    }

    /**
     * Método de apoyo usado por la clase para completar la lógica del juego.
     */
    public Object obtenerDatosVertice(int indice) {
        validarIndiceVertice(indice); // ejecuta una llamada a un método para realizar una acción concreta.
        return vertices.obtener(indice).datos; // devuelve el resultado calculado por el método.
    }

    /**
     * Método de apoyo usado por la clase para completar la lógica del juego.
     */
    public void establecerDatosVertice(int indice, Object datos) {
        validarIndiceVertice(indice); // ejecuta una llamada a un método para realizar una acción concreta.
        vertices.obtener(indice).datos = datos; // asigna o actualiza un valor necesario para el estado del programa.
    }
}
