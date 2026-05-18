package Juego.listas;

import java.util.Iterator;

public class ListaSimplementeEnlazada<T> implements Lista<T>, Iterable<T> {
    // primer elemento de la lista
    private ElementoSE<T> primero;

    /** Constructor que inicializa la lista como vacía
     */

    public ListaSimplementeEnlazada() {
        primero = null;
    }

    /** Metodo que comprueba si la lista está vacía
     */
    @Override
    public boolean estaVacia() {
        return primero == null;
    }

    /** Metodo para insertar un nuevo elemento al inicio de la lista
     */
    @Override
    public void insertarPrimero(T dato) {
        ElementoSE<T> nuevo = new ElementoSE<>(dato);   // creamos nuevo elemento
        nuevo.siguiente = primero;  // el nuevo elemento apunta al antiguo primero
        primero = nuevo;    // el nuevo elemento pasa a ser el primero
    }

    /** Metodo para añadir un nuevo dato como último elemento
     */
    @Override
    public void insertarUltimo(T dato) {
        ElementoSE<T> nuevo = new ElementoSE<>(dato);
        if (estaVacia()) {  // si la lista está vacía, el nuevo elemento pasa a ser el primero
            primero = nuevo;}
        else {
            ElementoSE<T> actual = primero; // apunta al primer elemento de la lista
            while(actual.siguiente != null) {   // recorremos la lista hasta el último elemento
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;   // el nuevo elemento pasa a ser el último
        }
    }

    @Override
    public T eliminarPrimero() {
        if (estaVacia()) return null;   //si está vacía, devuelve null
        T dato = primero.dato;  //guardamos el dato del primer elemento porque lo vamos a eliminar
        primero = primero.siguiente;    //el primero pasa a ser el siguiente
        return dato;
    }

    @Override
    public T eliminarUltimo() {
        if (estaVacia()) {  //si la lista está vacía
            return null;
        }
        if (primero.siguiente == null) {    //si en la lista solo hay un elemento
            T dato = primero.dato;  //guardamos el dato del elemento que vamos a eliminar
            primero = null;
            return dato;
        }

        ElementoSE<T> actual = primero; //inicializamos el elemento actual como el primero
        while (actual.siguiente.siguiente != null) {    //recorremos la lista hasta llegar al penúltimo elemento
            actual = actual.siguiente;
        }
        T dato = actual.siguiente.dato; //guardamos el dato del último elemento, porque actual será el penúltimo ahora
        actual.siguiente = null;
        return dato;
    }

    /** Metodo que recorre la lista empleando el iterador
     */
    @Override
    public Iterator<T> iterator() {
        return new IteradorLSE<>(primero);
    }

    /** Metodo para mostrar la lista
     */
    public void mostrarLista() {
        ElementoSE<T> actual = primero;
        while (actual != null) {    // recorremos la lista hasta el final
            System.out.println(actual.dato);    // imprimimos cada dato
            actual = actual.siguiente;  //el actual pasa a ser el siguiente
        }
    }

    /** Metodo para calcular el tamaño de la lista
     */
    public int size() {
        int contador = 0;   //inicializamos el contador en 0
        ElementoSE<T> actual = primero; //el elemento actual apunta al primero
        while (actual != null) {    //el bucle recorre la lista hasta el final
            contador++; //se suma 1 elemento en cada vuelta del bucle
            actual = actual.siguiente;  //ahora el elemento actual apunta al siguiente
        }
        return contador;    //devuelve el número de elementos que hay
    }

    /** Metodo que comprueba si un dato está en la lista
     */
    public boolean contiene(T dato) {
        ElementoSE<T> actual = primero;
        while (actual != null) {
            if (actual.dato == dato) {   //si el dato del elemento actual es igual que el dato introducido devuelve true
                return true;
            }
            actual = actual.siguiente;
        }
        return false;   //si ningún elemento es igual devuelve false
    }

}
