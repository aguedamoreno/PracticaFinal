package Juego; // indica el paquete al que pertenece esta clase.
import Juego.listas.ListaSimplementeEnlazada; // importa una clase externa necesaria para este archivo.

/** Estructura de datos de árbol binario de búsqueda genérico. Permite insertar, buscar, eliminar y recorrer elementos ordenados.
 */
public class ArbolBinario<T extends Comparable<T>> {
    // Primer nodo del árbol, desde él se accede al resto de nodos
    private Nodo<T> raiz;
    // Número de elementos guardados en el árbol
    private int size;

    /** Constructor de la clase Nodo, crea un nuevo Nodo del arbol binario
     */
    private static class Nodo<T> {
        T dato;
        // Nodos a los que podemos acceder
        Nodo<T> izquierdo;
        Nodo<T> derecho;

        Nodo(T dato) {
            this.dato = dato; // guarda el valor recibido dentro del atributo del objeto actual
            this.izquierdo = null;  // inicializamos los atributos
            this.derecho = null;
        }
    }

    /** Constructor que inicializa los atributos principales del objeto
     */
    public ArbolBinario() {
        this.raiz = null;
        this.size = 0;
    }

    /** Metodo inserta un elemento en el árbol binario de búsqueda
     */
    public void insertar(T dato) {
        raiz = insertarRecursivo(raiz, dato); // asigna o actualiza un valor necesario para el estado del programa
    }

    /** Metodo que baja recursivamente por el árbol hasta encontrar la posición correcta para insertar
     */
    private Nodo<T> insertarRecursivo(Nodo<T> actual, T dato) {
        if (actual == null) { // comprueba si es o no vacío
            size++;
            return new Nodo<>(dato); // crea un nuevo objeto
        }

        if (dato.compareTo(actual.dato) < 0) { // si el dato es menor que 0, va hacia la izquierda
            actual.izquierdo = insertarRecursivo(actual.izquierdo, dato); // actualiza el nodo actual
        } else if (dato.compareTo(actual.dato) > 0) {   // si es mayor que 0, va hacia la derecha
            actual.derecho = insertarRecursivo(actual.derecho, dato);
        }
        // Si los datos son iguales, no lo insertar
        return actual; // devuelve el resultado calculado por el metodo
    }

    /** Metodo que busca un elemento en el árbol
     */
    public boolean buscar(T dato) {
        return buscarRecursivo(raiz, dato);
    }

    /** Metodo que busca recursivamente comparando el dato con cada nodo
     */
    private boolean buscarRecursivo(Nodo<T> actual, T dato) {
        if (actual == null) { // comprueba una condición para decidir qué camino sigue el programa.
            return false; // devuelve el resultado calculado por el método.
        }

        if (dato.equals(actual.dato)) { // comprueba una condición para decidir qué camino sigue el programa.
            return true; // devuelve el resultado calculado por el método.
        }

        if (dato.compareTo(actual.dato) < 0) { // comprueba una condición para decidir qué camino sigue el programa.
            return buscarRecursivo(actual.izquierdo, dato); // devuelve el resultado calculado por el método.
        } else {
            return buscarRecursivo(actual.derecho, dato); // devuelve el resultado calculado por el método.
        }
    }

    /**
     * Recorrido inorden (izquierdo, raíz, derecho)
     * @return Lista con los elementos en orden
     */
    public ListaSimplementeEnlazada<T> inorden() {
        ListaSimplementeEnlazada<T> resultado = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.
        inordenRecursivo(raiz, resultado); // ejecuta una llamada a un método para realizar una acción concreta.
        return resultado; // devuelve el resultado calculado por el método.
    }

    /**
     * Recorre primero el subárbol izquierdo, luego el nodo actual y después el subárbol derecho.
     */
    private void inordenRecursivo(Nodo<T> nodo, ListaSimplementeEnlazada<T> lista) {
        if (nodo != null) { // comprueba una condición para decidir qué camino sigue el programa.
            inordenRecursivo(nodo.izquierdo, lista); // ejecuta una llamada a un método para realizar una acción concreta.
            lista.insertarUltimo(nodo.dato); // ejecuta una llamada a un método para realizar una acción concreta.
            inordenRecursivo(nodo.derecho, lista); // ejecuta una llamada a un método para realizar una acción concreta.
        }
    }

    /**
     * Recorrido preorden (raíz, izquierdo, derecho)
     * @return Lista con los elementos en preorden
     */
    public ListaSimplementeEnlazada<T> preorden() {
        ListaSimplementeEnlazada<T> resultado = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.
        preordenRecursivo(raiz, resultado); // ejecuta una llamada a un método para realizar una acción concreta.
        return resultado; // devuelve el resultado calculado por el método.
    }

    /**
     * Añade primero el nodo actual y luego recorre recursivamente sus hijos.
     */
    private void preordenRecursivo(Nodo<T> nodo, ListaSimplementeEnlazada<T> lista) {
        if (nodo != null) { // comprueba una condición para decidir qué camino sigue el programa.
            lista.insertarUltimo(nodo.dato); // ejecuta una llamada a un método para realizar una acción concreta.
            preordenRecursivo(nodo.izquierdo, lista); // ejecuta una llamada a un método para realizar una acción concreta.
            preordenRecursivo(nodo.derecho, lista); // ejecuta una llamada a un método para realizar una acción concreta.
        }
    }

    /**
     * Recorrido postorden (izquierdo, derecho, raíz)
     * @return Lista con los elementos en postorden
     */
    public ListaSimplementeEnlazada<T> postorden() {
        ListaSimplementeEnlazada<T> resultado = new ListaSimplementeEnlazada<>(); // crea un nuevo objeto para poder usarlo después.
        postordenRecursivo(raiz, resultado); // ejecuta una llamada a un método para realizar una acción concreta.
        return resultado; // devuelve el resultado calculado por el método.
    }

    /**
     * Recorre primero los subárboles y añade el nodo actual al final.
     */
    private void postordenRecursivo(Nodo<T> nodo, ListaSimplementeEnlazada<T> lista) {
        if (nodo != null) { // comprueba una condición para decidir qué camino sigue el programa.
            postordenRecursivo(nodo.izquierdo, lista); // ejecuta una llamada a un método para realizar una acción concreta.
            postordenRecursivo(nodo.derecho, lista); // ejecuta una llamada a un método para realizar una acción concreta.
            lista.insertarUltimo(nodo.dato); // ejecuta una llamada a un método para realizar una acción concreta.
        }
    }

    /**
     * Elimina un elemento del árbol
     * @param dato Elemento a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminar(T dato) {
        if (!buscar(dato)) { // comprueba una condición para decidir qué camino sigue el programa.
            return false; // devuelve el resultado calculado por el método.
        }
        raiz = eliminarRecursivo(raiz, dato); // asigna o actualiza un valor necesario para el estado del programa.
        size--;
        return true; // devuelve el resultado calculado por el método.
    }

    /**
     * Método auxiliar que localiza el nodo a borrar y reorganiza el árbol.
     */
    private Nodo<T> eliminarRecursivo(Nodo<T> actual, T dato) {
        if (actual == null) { // comprueba una condición para decidir qué camino sigue el programa.
            return null; // devuelve el resultado calculado por el método.
        }

        if (dato.compareTo(actual.dato) < 0) { // comprueba una condición para decidir qué camino sigue el programa.
            actual.izquierdo = eliminarRecursivo(actual.izquierdo, dato); // asigna o actualiza un valor necesario para el estado del programa.
        } else if (dato.compareTo(actual.dato) > 0) {
            actual.derecho = eliminarRecursivo(actual.derecho, dato); // asigna o actualiza un valor necesario para el estado del programa.
        } else {
            // Nodo con solo un hijo o ningún hijo
            if (actual.izquierdo == null) { // comprueba una condición para decidir qué camino sigue el programa.
                return actual.derecho; // devuelve el resultado calculado por el método.
            } else if (actual.derecho == null) {
                return actual.izquierdo; // devuelve el resultado calculado por el método.
            }

            // Nodo con dos hijos: obtener el sucesor inorden (mínimo en subárbol derecho)
            actual.dato = minValor(actual.derecho); // asigna o actualiza un valor necesario para el estado del programa.
            actual.derecho = eliminarRecursivo(actual.derecho, actual.dato); // asigna o actualiza un valor necesario para el estado del programa.
        }
        return actual; // devuelve el resultado calculado por el método.
    }

    /**
     * Busca el valor mínimo dentro de un subárbol.
     */
    private T minValor(Nodo<T> nodo) {
        T minv = nodo.dato; // asigna o actualiza un valor necesario para el estado del programa.
        while (nodo.izquierdo != null) { // bucle que se repite mientras la condición sea verdadera.
            minv = nodo.izquierdo.dato; // asigna o actualiza un valor necesario para el estado del programa.
            nodo = nodo.izquierdo; // asigna o actualiza un valor necesario para el estado del programa.
        }
        return minv; // devuelve el resultado calculado por el método.
    }

    /**
     * Obtiene la altura del árbol
     * @return Altura del árbol (número de niveles)
     */
    public int altura() {
        return alturaRecursiva(raiz); // devuelve el resultado calculado por el método.
    }

    /**
     * Calcula recursivamente la altura de cada subárbol.
     */
    private int alturaRecursiva(Nodo<T> nodo) {
        if (nodo == null) { // comprueba una condición para decidir qué camino sigue el programa.
            return -1; // devuelve el resultado calculado por el método.
        }
        int alturaIzq = alturaRecursiva(nodo.izquierdo); // asigna o actualiza un valor necesario para el estado del programa.
        int alturaDer = alturaRecursiva(nodo.derecho); // asigna o actualiza un valor necesario para el estado del programa.
        return Math.max(alturaIzq, alturaDer) + 1; // devuelve el resultado calculado por el método.
    }

    /**
     * Devuelve cuántos elementos hay guardados.
     */
    public int size() {
        return size; // devuelve el resultado calculado por el método.
    }

    /**
     * Indica si la estructura no contiene ningún elemento.
     */
    public boolean estaVacia() {
        return size == 0; // devuelve el resultado calculado por el método.
    }
}


