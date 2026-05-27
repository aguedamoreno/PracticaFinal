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
        // Si llegamos a null, hemos bajado por donde debería estar el dato pero no existe
        if (actual == null) {
            return false;
        }
        // equals comprueba si el dato buscado es igual al dato guardado en el nodo actual
        if (dato.equals(actual.dato)) {
            return true;
        }
        // Si el dato buscado es menor, se busca solamente en la rama izquierda
        if (dato.compareTo(actual.dato) < 0) {
            return buscarRecursivo(actual.izquierdo, dato);
        } else {
            // Si no es menor y tampoco era igual, entonces es mayor y se busca por la derecha
            return buscarRecursivo(actual.derecho, dato);
        }
    }

    /** Metodo que devuelve los elementos del árbol en orden ascendente, de menor a mayor
     */
    public ListaSimplementeEnlazada<T> inorden() {
        // Lista del proyecto donde se irán guardando los datos encontrados durante el recorrido
        ListaSimplementeEnlazada<T> resultado = new ListaSimplementeEnlazada<>();

        // Se empieza el recorrido desde la raíz
        inordenRecursivo(raiz, resultado);

        // Devuelve la lista ya rellenada con los datos del árbol
        return resultado;
    }

    /** Metodo que recorre el árbol en orden: izquierdo, nodo actual, derecho.
     */
    private void inordenRecursivo(Nodo<T> nodo, ListaSimplementeEnlazada<T> lista) {
        // Si el nodo es vacío, no hay nada que recorrer en esta rama
        if (nodo != null) {
            // Primero se recorren todos los valores menores que el nodo actual
            inordenRecursivo(nodo.izquierdo, lista);
            // Después se añade el dato del nodo actual a la lista resultado
            lista.insertarUltimo(nodo.dato);
            // Por último se recorren todos los valores mayores que el nodo actual
            inordenRecursivo(nodo.derecho, lista);
        }
    }

    /** Metodo que devuelve los elementos del árbol empezando en el nodo actual y luego sus hijos
     */
    public ListaSimplementeEnlazada<T> preorden() {
        // Lista donde se almacenarán los datos
        ListaSimplementeEnlazada<T> resultado = new ListaSimplementeEnlazada<>();
        // Se empieza a recorrer desde la raíz
        preordenRecursivo(raiz, resultado);
        // Devuelve la lista completa.
        return resultado;
    }

    /** Metodo que recorre el árbol en orden: nodo actual, izquierdo, derecho
     */
    private void preordenRecursivo(Nodo<T> nodo, ListaSimplementeEnlazada<T> lista) {
        // Si nodo es null, significa que esa rama no existe y no se hace nada
        if (nodo != null) {
            // Primero se añade el dato del nodo actual
            lista.insertarUltimo(nodo.dato);
            // Después se recorre el subárbol izquierdo
            preordenRecursivo(nodo.izquierdo, lista);
            // Finalmente se recorre el subárbol derecho.
            preordenRecursivo(nodo.derecho, lista);
        }
    }

    /** Metodo que devuelve los elementos del árbol empezando por los hijos y luego el nodo actual
     */
    public ListaSimplementeEnlazada<T> postorden() {
        // Lista donde se irán insertando los datos visitados
        ListaSimplementeEnlazada<T> resultado = new ListaSimplementeEnlazada<>();
        // Se empieza el recorrido desde la raíz
        postordenRecursivo(raiz, resultado);
        // Devuelve la lista con el recorrido completo
        return resultado;
    }

    /** Metodo que recorre el árbol en orden: izquierdo, derecho, nodo actual
    */
    private void postordenRecursivo(Nodo<T> nodo, ListaSimplementeEnlazada<T> lista) {
        // Si nodo es null, no hay datos que añadir
        if (nodo != null) {
            // Primero se recorre el hijo izquierdo
            postordenRecursivo(nodo.izquierdo, lista);
            // Después se recorre el hijo derecho
            postordenRecursivo(nodo.derecho, lista);
            // Al final se añade el dato del nodo actual
            lista.insertarUltimo(nodo.dato);
        }
    }

    /** Metodo que elimina un dato del árbol
     */
    public boolean eliminar(T dato) {
        // Si el dato no existe, no se puede eliminar
        if (!buscar(dato)) {
            return false;
        }
        // Si existe, se elimina empezando desde la raíz, y se puede actualizar la raíz si justo se elimina el nodo raíz
        raiz = eliminarRecursivo(raiz, dato);
        // Como se ha eliminado un dato real, se reduce el tamaño del árbol
        size--;
        return true;
    }

    /** Metodo que busca el nodo que contiene el dato y lo elimina manteniendo la estructura del árbol
     * Hay tres casos al eliminar:
     * 1. Nodo sin hijos
     * 2. Nodo con un solo hijo
     * 3. Nodo con dos hijos
    */
    private Nodo<T> eliminarRecursivo(Nodo<T> actual, T dato) {
        // Si se llega a null, no hay nada que eliminar en esta rama
        if (actual == null) {
            return null;
        }
        if (dato.compareTo(actual.dato) < 0) {
            // Si el dato a eliminar es menor que el dato actual, estará en el subárbol izquierdo
            actual.izquierdo = eliminarRecursivo(actual.izquierdo, dato);
        } else if (dato.compareTo(actual.dato) > 0) {
            // Si el dato a eliminar es mayor que el dato actual, estará en el subárbol derecho
            actual.derecho = eliminarRecursivo(actual.derecho, dato);
        } else {    // Si no es menor ni mayor, entonces el dato es igual al actual
            // CASO 1 :
            // Si no tiene hijo izquierdo, se devuelve su hijo derecho
            // Si derecho también es null, se elimina el nodo
            // Si derecho no es null, el hijo derecho ocupa su lugar
            if (actual.izquierdo == null) {
                return actual.derecho;
            } else if (actual.derecho == null) {
                // CASO 2 :
                // Si no tiene hijo derecho pero sí tiene izquierdo
                // el hijo izquierdo ocupa el lugar del nodo eliminado
                return actual.izquierdo;
            }
            // CASO 3:
            // No se puede quitar directamente sin romper el árbol, así que se busca el valor más pequeño del subarbol derecho
            actual.dato = minValor(actual.derecho);
            // Después de copiar ese valor en el nodo actual, hay que eliminar el nodo original que contenía ese hijo en el subárbol derecho
            actual.derecho = eliminarRecursivo(actual.derecho, actual.dato);
        }
        // Devuelve este nodo con sus hijos ya actualizados
        return actual;
    }

    /** Metodo que obtiene el dato más pequeño dentro de un subárbol
     */
    private T minValor(Nodo<T> nodo) {
        // Se empieza suponiendo que el mínimo es el dato del nodo recibido
        T minv = nodo.dato;
        // Mientras exista un hijo izquierdo, todavía hay un valor más pequeño
        while (nodo.izquierdo != null) {
            // El nuevo mínimo pasa a ser el dato del hijo izquierdo
            minv = nodo.izquierdo.dato;
            // Bajamos un nivel hacia la izquierda para seguir buscando
            nodo = nodo.izquierdo;
        }
        // Cuando ya no hay más hijos izquierdos, minv contiene el menor valor
        return minv;
    }

    /** Metodo que calcula la altura del árbol completo (un árbol vacío tiene altura -1)
     */
    public int altura() {
        return alturaRecursiva(raiz); // Calcula la altura empezando desde la raíz
    }

    /** Metodo que calcula la altura de un nodo de forma recursiva  (La altura de un nodo es: 1 + la mayor altura entre su subárbol izquierdo y su subárbol derecho)
     */
    private int alturaRecursiva(Nodo<T> nodo) {
        // Caso base: si el nodo no existe, su altura se considera -1
        if (nodo == null) {
            return -1;
        }
        // Calcula la altura del subárbol izquierdo
        int alturaIzq = alturaRecursiva(nodo.izquierdo);
        // Calcula la altura del subárbol derecho
        int alturaDer = alturaRecursiva(nodo.derecho);

        // Se queda con la rama más alta y suma 1 para contar el nodo actual
        return Math.max(alturaIzq, alturaDer) + 1;
    }

    /** Metodo que devuelve el número de elementos guardados en el árbol
     */
    public int size() {
        return size; // Devuelve el contador actualizado por insertar() y eliminar()
    }

    /** Metodo que comprueba si el árbol está vacío.
     */
    public boolean estaVacia() {
        return size == 0; // Si size es 0, no hay nodos guardados
    }
}
