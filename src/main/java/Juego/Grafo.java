package Juego;

import Juego.listas.ListaSimplementeEnlazada;

public class Grafo {
    private class Arista {
        int destino;
        double peso; // Para algoritmos como Dijkstra

        Arista(int destino, double peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    private class Vertice {
        ListaSimplementeEnlazada<Arista> aristas;
        Object datos; // Para almacenar datos asociados al vértice (ej: Habitación)

        Vertice(Object datos) {
            this.datos = datos;
            this.aristas = new ListaSimplementeEnlazada<>();
        }
    }

    private ListaSimplementeEnlazada<Vertice> vertices;
    private boolean dirigido;
    private int size;

    /**
     * Constructor
     * @param dirigido true si el grafo es dirigido, false si no dirigido
     */
    public Grafo(boolean dirigido) {
        this.vertices = new ListaSimplementeEnlazada<>();
        this.dirigido = dirigido;
        this.size = 0;
    }

    public Grafo() {
        this(false); // Por defecto, grafo no dirigido
    }

    /**
     * Añade un vértice al grafo
     * @param datos Datos asociados al vértice
     * @return Índice del vértice añadido
     */
    public int agregarVertice(Object datos) {
        vertices.insertarUltimo(new Vertice(datos));
        size++;
        return size - 1;
    }

    /**
     * Añade una arista entre dos vértices
     * @param origen Índice del vértice origen
     * @param destino Índice del vértice destino
     * @param peso Peso de la arista (1.0 por defecto para no ponderado)
     * @throws IndexOutOfBoundsException Si algún índice es inválido
     */
    public void agregarArista(int origen, int destino, double peso) {
        validarIndiceVertice(origen);
        validarIndiceVertice(destino);

        vertices.obtener(origen).aristas.insertarUltimo(new Arista(destino, peso));

        // Si es no dirigido, añadir la arista inversa
        if (!dirigido) {
            vertices.obtener(destino).aristas.insertarUltimo(new Arista(origen, peso));
        }
    }

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
        validarIndiceVertice(vertice);
        ListaSimplementeEnlazada<Integer> resultado = new ListaSimplementeEnlazada<>();
        ListaSimplementeEnlazada<Arista> aristas = vertices.obtener(vertice).aristas;

        // Usamos el iterador de tu ListaSimplementeEnlazada
        for (int i = 0; i < aristas.tamaño(); i++) {
            Arista arista = aristas.obtener(i);
            resultado.insertarUltimo(arista.destino);
        }
        return resultado;
    }

    /**
     * Obtiene el peso de una arista
     * @param origen Índice del vértice origen
     * @param destino Índice del vértice destino
     * @return Peso de la arista, o Double.POSITIVE_INFINITY si no existe
     */
    public double obtenerPesoArista(int origen, int destino) {
        validarIndiceVertice(origen);
        validarIndiceVertice(destino);

        ListaSimplementeEnlazada<Arista> aristas = vertices.obtener(origen).aristas;
        for (int i = 0; i < aristas.tamaño(); i++) {
            Arista arista = aristas.obtener(i);
            if (arista.destino == destino) {
                return arista.peso;
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
        validarIndiceVertice(inicio);

        ListaSimplementeEnlazada<Integer> resultado = new ListaSimplementeEnlazada<>();
        boolean[] visitados = new boolean[size];
        Cola<Integer> cola = new Cola<>();
        Cola<Integer> distanciaCola = new Cola<>();

        cola.enqueue(inicio);
        distanciaCola.enqueue(0);
        visitados[inicio] = true;

        while (!cola.estaVacia()) {
            int actual = cola.dequeue();
            int dist = distanciaCola.dequeue();

            if (dist <= maxDistancia) {
                resultado.insertarUltimo(actual);

                // Si aún podemos explorar más lejos
                if (dist < maxDistancia) {
                    ListaSimplementeEnlazada<Integer> adyacentes = obtenerAdyacentes(actual);
                    for (int i = 0; i < adyacentes.tamaño(); i++) {
                        int adyacente = adyacentes.obtener(i);
                        if (!visitados[adyacente]) {
                            visitados[adyacente] = true;
                            cola.enqueue(adyacente);
                            distanciaCola.enqueue(dist + 1);
                        }
                    }
                }
            }
        }
        return resultado;
    }

    /**
     * Algoritmo de Dijkstra para encontrar el camino más corto
     * @param inicio Índice del vértice de inicio
     * @param fin Índice del vértice de destino
     * @return Array con [distancia, camino] o null si no hay camino
     *         El camino es una lista de índices de vértices desde inicio hasta fin
     */
    public Object[] dijkstra(int inicio, int fin) {
        validarIndiceVertice(inicio);
        validarIndiceVertice(fin);

        double[] distancias = new double[size];
        int[] anteriores = new int[size];
        boolean[] visitados = new boolean[size];

        for (int i = 0; i < size; i++) {
            distancias[i] = Double.POSITIVE_INFINITY;
            anteriores[i] = -1;
        }
        distancias[inicio] = 0;

        // Cola de prioridad simple usando búsqueda lineal (para no complicar)
        for (int i = 0; i < size; i++) {
            // Encontrar el vértice no visitado con distancia mínima
            int u = -1;
            double minDist = Double.POSITIVE_INFINITY;
            for (int v = 0; v < size; v++) {
                if (!visitados[v] && distancias[v] < minDist) {
                    minDist = distancias[v];
                    u = v;
                }
            }

            if (u == -1) break; // No quedan vértices alcanzables
            if (u == fin) break; // Llegamos al destino

            visitados[u] = true;

            // Actualizar distancias de los vecinos
            ListaSimplementeEnlazada<Integer> adyacentes = obtenerAdyacentes(u);
            for (int j = 0; j < adyacentes.tamaño(); j++) {
                int v = adyacentes.obtener(j);
                if (!visitados[v]) {
                    double alternativa = distancias[u] + obtenerPesoArista(u, v);
                    if (alternativa < distancias[v]) {
                        distancias[v] = alternativa;
                        anteriores[v] = u;
                    }
                }
            }
        }

        // Reconstruir camino si existe
        if (distancias[fin] == Double.POSITIVE_INFINITY) {
            return null; // No hay camino
        }

        ListaSimplementeEnlazada<Integer> camino = new ListaSimplementeEnlazada<>();
        for (int actual = fin; actual != -1; actual = anteriores[actual]) {
            camino.insertarPrimero(actual); // Insertar al inicio para invertir el orden
        }

        return new Object[]{distancias[fin], camino};
    }

    private void validarIndiceVertice(int indice) {
        if (indice < 0 || indice >= size) {
            throw new IndexOutOfBoundsException("Índice de vértice inválido: " + indice +
                    ". Tamaño del grafo: " + size);
        }
    }

    public int getSize() {
        return size;
    }

    public boolean estaVacio() {
        return size == 0;
    }

    public Object obtenerDatosVertice(int indice) {
        validarIndiceVertice(indice);
        return vertices.obtener(indice).datos;
    }

    public void establecerDatosVertice(int indice, Object datos) {
        validarIndiceVertice(indice);
        vertices.obtener(indice).datos = datos;
    }
}
