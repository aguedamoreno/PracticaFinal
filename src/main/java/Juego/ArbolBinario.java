package Juego;
import Juego.listas.ListaSimplementeEnlazada;

public class ArbolBinario<T extends Comparable<T>> {
    private Nodo<T> raiz;
    private int size;

    private static class Nodo<T> {
        T dato;
        Nodo<T> izquierdo;
        Nodo<T> derecho;

        Nodo(T dato) {
            this.dato = dato;
            this.izquierdo = null;
            this.derecho = null;
        }
    }

    public ArbolBinario() {
        this.raiz = null;
        this.size = 0;
    }

    /**
     * Inserta un elemento en el árbol binario de búsqueda
     * @param dato Elemento a insertar
     */
    public void insertar(T dato) {
        raiz = insertarRecursivo(raiz, dato);
    }

    private Nodo<T> insertarRecursivo(Nodo<T> actual, T dato) {
        if (actual == null) {
            size++;
            return new Nodo<>(dato);
        }

        if (dato.compareTo(actual.dato) < 0) {
            actual.izquierdo = insertarRecursivo(actual.izquierdo, dato);
        } else if (dato.compareTo(actual.dato) > 0) {
            actual.derecho = insertarRecursivo(actual.derecho, dato);
        }
        // Si es igual, no insertamos duplicados
        return actual;
    }

    /**
     * Busca un elemento en el árbol
     * @param dato Elemento a buscar
     * @return true si existe, false en caso contrario
     */
    public boolean buscar(T dato) {
        return buscarRecursivo(raiz, dato);
    }

    private boolean buscarRecursivo(Nodo<T> actual, T dato) {
        if (actual == null) {
            return false;
        }

        if (dato.equals(actual.dato)) {
            return true;
        }

        if (dato.compareTo(actual.dato) < 0) {
            return buscarRecursivo(actual.izquierdo, dato);
        } else {
            return buscarRecursivo(actual.derecho, dato);
        }
    }

    /**
     * Recorrido inorden (izquierdo, raíz, derecho)
     * @return Lista con los elementos en orden
     */
    public ListaSimplementeEnlazada<T> inorden() {
        ListaSimplementeEnlazada<T> resultado = new ListaSimplementeEnlazada<>();
        inordenRecursivo(raiz, resultado);
        return resultado;
    }

    private void inordenRecursivo(Nodo<T> nodo, ListaSimplementeEnlazada<T> lista) {
        if (nodo != null) {
            inordenRecursivo(nodo.izquierdo, lista);
            lista.insertarUltimo(nodo.dato);
            inordenRecursivo(nodo.derecho, lista);
        }
    }

    /**
     * Recorrido preorden (raíz, izquierdo, derecho)
     * @return Lista con los elementos en preorden
     */
    public ListaSimplementeEnlazada<T> preorden() {
        ListaSimplementeEnlazada<T> resultado = new ListaSimplementeEnlazada<>();
        preordenRecursivo(raiz, resultado);
        return resultado;
    }

    private void preordenRecursivo(Nodo<T> nodo, ListaSimplementeEnlazada<T> lista) {
        if (nodo != null) {
            lista.insertarUltimo(nodo.dato);
            preordenRecursivo(nodo.izquierdo, lista);
            preordenRecursivo(nodo.derecho, lista);
        }
    }

    /**
     * Recorrido postorden (izquierdo, derecho, raíz)
     * @return Lista con los elementos en postorden
     */
    public ListaSimplementeEnlazada<T> postorden() {
        ListaSimplementeEnlazada<T> resultado = new ListaSimplementeEnlazada<>();
        postordenRecursivo(raiz, resultado);
        return resultado;
    }

    private void postordenRecursivo(Nodo<T> nodo, ListaSimplementeEnlazada<T> lista) {
        if (nodo != null) {
            postordenRecursivo(nodo.izquierdo, lista);
            postordenRecursivo(nodo.derecho, lista);
            lista.insertarUltimo(nodo.dato);
        }
    }

    /**
     * Elimina un elemento del árbol
     * @param dato Elemento a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminar(T dato) {
        if (!buscar(dato)) {
            return false;
        }
        raiz = eliminarRecursivo(raiz, dato);
        size--;
        return true;
    }

    private Nodo<T> eliminarRecursivo(Nodo<T> actual, T dato) {
        if (actual == null) {
            return null;
        }

        if (dato.compareTo(actual.dato) < 0) {
            actual.izquierdo = eliminarRecursivo(actual.izquierdo, dato);
        } else if (dato.compareTo(actual.dato) > 0) {
            actual.derecho = eliminarRecursivo(actual.derecho, dato);
        } else {
            // Nodo con solo un hijo o ningún hijo
            if (actual.izquierdo == null) {
                return actual.derecho;
            } else if (actual.derecho == null) {
                return actual.izquierdo;
            }

            // Nodo con dos hijos: obtener el sucesor inorden (mínimo en subárbol derecho)
            actual.dato = minValor(actual.derecho);
            actual.derecho = eliminarRecursivo(actual.derecho, actual.dato);
        }
        return actual;
    }

    private T minValor(Nodo<T> nodo) {
        T minv = nodo.dato;
        while (nodo.izquierdo != null) {
            minv = nodo.izquierdo.dato;
            nodo = nodo.izquierdo;
        }
        return minv;
    }

    /**
     * Obtiene la altura del árbol
     * @return Altura del árbol (número de niveles)
     */
    public int altura() {
        return alturaRecursiva(raiz);
    }

    private int alturaRecursiva(Nodo<T> nodo) {
        if (nodo == null) {
            return -1;
        }
        int alturaIzq = alturaRecursiva(nodo.izquierdo);
        int alturaDer = alturaRecursiva(nodo.derecho);
        return Math.max(alturaIzq, alturaDer) + 1;
    }

    public int size() {
        return size;
    }

    public boolean estaVacia() {
        return size == 0;
    }
}
