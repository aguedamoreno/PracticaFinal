package Juego;

import Juego.listas.ListaSimplementeEnlazada; // Lo usamos para guardar vértices, aristas y resultados sin usar ArrayList
import Juego.listas.Cola; // Lo usamos en el algoritmo BFS para recorrer el grafo por niveles

/** Clase Grafo, que en el juego representa el mapa completo:
 * Cada vértice puede ser una habitación.
 * Cada arista representa una conexión entre habitaciones, por ejemplo una puerta.
 * El peso sirve para calcular caminos, por ejemplo con Dijkstra.
 */
public class Grafo {
    /** Clase interna Arista, representa una conexión desde un vértice hacia otro
     * Por ejemplo: Entrada ---- Sala
     * Si Entrada tiene una arista hacia Sala, destino guardará el índice de Sala
     */
    private static class Arista {
        int destino; // Índice del vértice al que lleva esta conexión
        double peso; // Coste de moverse por esta conexión, en Dijkstra lo usamos para calcular caminos mínimos

        /** Constructor de una arista
         */
        Arista(int destino, double peso) {
            this.destino = destino; // Guarda el índice del vértice destino
            this.peso = peso; // Guarda el coste de esta conexión
        }
    }

    /** Clase interna Vertice, representa un nodo del grafo
     * En el juego, cada vértice guarda una Habitacion
     */
    private static class Vertice {

        ListaSimplementeEnlazada<Arista> aristas; // Lista de conexiones que salen desde este vértice
        Object datos; // Información guardada en el vértice, que suele ser una Habitacion

        /** Constructor de un vértice
         */
        Vertice(Object datos) {
            this.datos = datos; // Guarda la habitación u objeto asociado al vértice
            this.aristas = new ListaSimplementeEnlazada<>(); // Crea la lista vacía de conexiones de este vértice
        }
    }

    private final ListaSimplementeEnlazada<Vertice> vertices; // Lista de todos los vértices/habitaciones del mapa

    private final boolean dirigido; // Indica si las conexiones tienen una sola dirección o si se puede ir y volver

    private int size; // Número total de vértices que tiene el grafo

    /** Constructor principal del grafo
     */
    public Grafo(boolean dirigido) {
        this.vertices = new ListaSimplementeEnlazada<>(); // Inicializa la lista de habitaciones/vértices vacía
        this.dirigido = dirigido; // Guarda si el grafo será dirigido o no dirigido
        this.size = 0; // Al crear el grafo todavía no hay habitaciones añadidas
    }

    /** Constructor del grafo por defecto
     */
    public Grafo() {
        this(false); // Llama al otro constructor pasando false, ya que crea un grafo no dirigido por defecto
        // Porque normalmente una puerta conecta dos habitaciones en ambos sentidos
    }

    /** Metodo que añade una nueva habitación/vértice al grafo
    */
    public int agregarVertice(Object datos) {
        vertices.insertarUltimo(new Vertice(datos)); // Crea un vértice con esos datos y lo añade al final de la lista
        size++; // Aumenta el contador porque ahora hay un vértice más
        return size - 1; // Devuelve el índice del nuevo vértice. Si hay 3 vértices, el último índice es 2
    }

    /** Metodo que añade una conexión entre dos vértices (es decir, que dos habitaciones están conectadas)
     */
    public void agregarArista(int origen, int destino, double peso) {
        validarIndiceVertice(origen); // Comprueba que el índice de origen existe
        validarIndiceVertice(destino); // Comprueba que el índice de destino existe

        vertices.obtener(origen).aristas.insertarUltimo(new Arista(destino, peso)); // Añade al origen una arista hacia destino

        if (!dirigido) { // Si el grafo no es dirigido, la conexión también existe en sentido contrario
            vertices.obtener(destino).aristas.insertarUltimo(new Arista(origen, peso));
        }
    }

    /** Metodo que añade una conexión sin indicar peso (Usa peso 1.0 por defecto)
     */
    public void agregarArista(int origen, int destino) {
        agregarArista(origen, destino, 1.0); // Llama al metodo anterior usando peso 1.0.
    }

    /** Metodo que devuelve los vértices conectados directamente con uno dado (por ejemplo, si Entrada conecta con Sala, Sala será adyacente a Entrada)
     */
    public ListaSimplementeEnlazada<Integer> obtenerAdyacentes(int vertice) {
        validarIndiceVertice(vertice); // Comprueba que el vértice existe.
        ListaSimplementeEnlazada<Integer> resultado = new ListaSimplementeEnlazada<>(); // Aquí se guardarán los índices adyacentes
        ListaSimplementeEnlazada<Arista> aristas = vertices.obtener(vertice).aristas; // Obtiene las conexiones del vértice indicado

        for (int i = 0; i < aristas.tamaño(); i++) { // Recorre todas las aristas de ese vértice
            Arista arista = aristas.obtener(i); // Obtiene una conexión concreta
            resultado.insertarUltimo(arista.destino); // Guarda el índice del vértice destino
        }

        return resultado; // Devuelve la lista de habitaciones conectadas
    }

    /** Metodo que busca el peso de la conexión entre dos vértices
     */
    public double obtenerPesoArista(int origen, int destino) {
        validarIndiceVertice(origen);
        validarIndiceVertice(destino);

        ListaSimplementeEnlazada<Arista> aristas = vertices.obtener(origen).aristas; // Obtiene las conexiones del origen

        for (int i = 0; i < aristas.tamaño(); i++) { // Recorre todas las conexiones del origen
            Arista arista = aristas.obtener(i); // Obtiene una arista concreta

            if (arista.destino == destino) { // Si esa arista llega justo al destino buscado
                return arista.peso; // Devuelve el peso de esa conexión
            }
        }

        return Double.POSITIVE_INFINITY; // Si no hay conexión directa, devuelve infinito
    }

    /** BFS: búsqueda en anchura
     * Sirve para explorar el grafo por capas: primero distancia 0, luego distancia 1, luego distancia 2...
     */
    public ListaSimplementeEnlazada<Integer> bfs(int inicio, int maxDistancia) {
        validarIndiceVertice(inicio);

        ListaSimplementeEnlazada<Integer> resultado = new ListaSimplementeEnlazada<>(); // Lista final de vértices alcanzables

        boolean[] visitados = new boolean[size]; // Marca qué vértices ya se han visitado para no repetirlos

        Cola<Integer> cola = new Cola<>(); // Cola con los vértices pendientes de explorar

        Cola<Integer> distanciaCola = new Cola<>(); // Cola paralela que guarda la distancia de cada vértice

        cola.enqueue(inicio); // Mete el vértice inicial en la cola

        distanciaCola.enqueue(0); // El vértice inicial está a distancia 0

        visitados[inicio] = true; // Marca el inicio como visitado

        while (!cola.estaVacia()) { // Mientras queden vértices pendientes de explorar
            int actual = cola.dequeue(); // Saca el siguiente vértice
            int dist = distanciaCola.dequeue(); // Saca la distancia correspondiente a ese vértice

            if (dist <= maxDistancia) { // Si está dentro del límite permitido
                resultado.insertarUltimo(actual); // Lo añade a la lista de alcanzables

                if (dist < maxDistancia) { // Si aún puede seguir explorando más lejos
                    ListaSimplementeEnlazada<Integer> adyacentes = obtenerAdyacentes(actual); // Obtiene vecinos del vértice actual

                    for (int i = 0; i < adyacentes.tamaño(); i++) { // Recorre cada vecino
                        int adyacente = adyacentes.obtener(i); // Obtiene el índice del vecino

                        if (!visitados[adyacente]) { // Si ese vecino todavía no se había visitado
                            visitados[adyacente] = true; // Lo marca como visitado
                            cola.enqueue(adyacente); // Lo mete en la cola para explorarlo después
                            distanciaCola.enqueue(dist + 1); // Su distancia es una más que la del vértice actual
                        }
                    }
                }
            }
        }

        return resultado; // Devuelve todos los vértices alcanzables dentro de la distancia máxima
    }

    /** Dijkstra: busca el camino más corto entre dos habitaciones del grafo
    */
    public Object[] dijkstra(int inicio, int fin) {
        validarIndiceVertice(inicio);
        validarIndiceVertice(fin);

        double[] distancias = new double[size]; // Guarda la mejor distancia conocida hasta cada vértice

        int[] anteriores = new int[size]; // Guarda desde qué vértice se llegó a cada uno para reconstruir el camino

        boolean[] visitados = new boolean[size]; // Marca qué vértices ya quedaron cerrados/finalizados

        for (int i = 0; i < size; i++) { // Inicializa todos los vértices
            distancias[i] = Double.POSITIVE_INFINITY; // Al principio no se sabe llegar a ninguno
            anteriores[i] = -1; // -1 significa que todavía no tiene vértice anterior
        }

        distancias[inicio] = 0; // La distancia desde inicio hasta sí mismo es 0

        for (int i = 0; i < size; i++) { // Como mucho se procesan todos los vértices una vez
            int u = -1; // Guardará el vértice no visitado con menor distancia

            double minDist = Double.POSITIVE_INFINITY; // Mejor distancia encontrada en esta vuelta

            for (int v = 0; v < size; v++) { // Busca entre todos los vértices
                if (!visitados[v] && distancias[v] < minDist) { // Si no está visitado y su distancia es menor
                    minDist = distancias[v]; // Actualiza la mejor distancia
                    u = v; // Guarda ese vértice como candidato
                }
            }

            if (u == -1) break; // Si no se encontró ninguno, ya no quedan vértices alcanzables

            if (u == fin) break; // Si ya llegamos al destino, no hace falta seguir

            visitados[u] = true; // Marca ese vértice como visitado

            ListaSimplementeEnlazada<Integer> adyacentes = obtenerAdyacentes(u); // Obtiene los vecinos de u

            for (int j = 0; j < adyacentes.tamaño(); j++) { // Recorre sus vecinos
                int v = adyacentes.obtener(j); // Obtiene un vecino concreto

                if (!visitados[v]) { // Solo intenta mejorar vecinos no procesados
                    double alternativa = distancias[u] + obtenerPesoArista(u, v); // Distancia hasta u + peso de ir de u a v

                    if (alternativa < distancias[v]) { // Si este camino es mejor que el que tenía antes
                        distancias[v] = alternativa; // Actualiza la mejor distancia conocida
                        anteriores[v] = u; // Guarda que para llegar a v se viene desde u
                    }
                }
            }
        }

        if (distancias[fin] == Double.POSITIVE_INFINITY) { // Si el destino sigue en infinito, no hay camino
            return null; // Devuelve null porque no se puede llegar
        }

        ListaSimplementeEnlazada<Integer> camino = new ListaSimplementeEnlazada<>(); // Lista donde se guardará el camino final

        for (int actual = fin; actual != -1; actual = anteriores[actual]) { // Va desde el final hacia atrás usando anteriores
            camino.insertarPrimero(actual); // Inserta al principio para que el camino quede en orden inicio -> fin
        }

        return new Object[]{distancias[fin], camino}; // Devuelve distancia total y camino encontrado
    }

    /** Metodo que comprueba que un índice de vértice sea válido.
     */
    private void validarIndiceVertice(int indice) {
        if (indice < 0 || indice >= size) { // Si el índice es negativo o mayor/igual que size, no existe
            throw new IndexOutOfBoundsException(
                    "Índice de vértice inválido: " + indice +
                            ". Tamaño del grafo: " + size
            ); // Lanza un error explicando qué índice falló
        }
    }

    /** Metodo que devuelve cuántos vértices/habitaciones tiene el grafo
     */
    public int getSize() {
        return size; // Devuelve el número de vértices guardados
    }

    /** Metodo que indica si el grafo está vacío
     */
    public boolean estaVacio() {
        return size == 0; // Devuelve true si no hay ningún vértice
    }

    /** Metodo que devuelve los datos guardados dentro de un vértice
     */
    public Object obtenerDatosVertice(int indice) {
        validarIndiceVertice(indice); // Comprueba que el índice existe
        return vertices.obtener(indice).datos; // Devuelve la habitación u objeto guardado en ese vértice
    }

    /** Metodo que cambia los datos guardados en un vértice
     */
    public void establecerDatosVertice(int indice, Object datos) {
        validarIndiceVertice(indice);
        vertices.obtener(indice).datos = datos; // Sustituye los datos guardados en ese vértice
    }
}
