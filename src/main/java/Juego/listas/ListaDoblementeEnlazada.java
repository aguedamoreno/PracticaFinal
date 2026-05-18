package Juego.listas;

import java.util.Iterator;

public class ListaDoblementeEnlazada<T> implements Lista<T>, Iterable<T> {
    //primer elemento de la lista
    ElementoDE<T> primero;
    //último elemento de la lista
    ElementoDE<T> ultimo;

    /** Constructor que inicializa la lista como vacía
     */
    public ListaDoblementeEnlazada() {
        primero = null;
        ultimo = null;
    }

    /** Metodo que comprueba si la lista está vacía
     */
    @Override
    public boolean estaVacia() {
        return primero == null;
    }

    /** Metodo para añadir un nuevo elemento al inicio de la lista
     */
    @Override
    public void insertarPrimero(T dato) {
        ElementoDE<T> nuevo = new ElementoDE<>(dato);   //creamos nuevo elemento
        if (estaVacia()) {  //si la lista está vacía, el nuevo elemento es el primero y el último a la vez
            primero = nuevo;
            ultimo = nuevo;}
        else {  //si la lista no está vacía
            nuevo.siguiente = primero;  //el nuevo elemento apunta al primero
            primero.anterior = nuevo;   //el primer elemento apunta al nuevo como su anterior
            primero = nuevo;    // el nuevo pasa a ser el primero
        }
    }

    /** Metodo para añadir nuevo elemento como último
     */
    @Override
    public void insertarUltimo(T dato) {
        ElementoDE<T> nuevo = new ElementoDE<>(dato);
        if (estaVacia()) {
            primero = nuevo;
            ultimo = nuevo;}
        else {
            nuevo.anterior = ultimo;    //el nuevo elemento apunta al último como su anterior
            ultimo.siguiente = nuevo;   //el último elemento apunta al nuevo como su siguiente
            ultimo = nuevo; // el nuevo pasa a ser el último
        }
    }

    /** Metodo para eliminar el primer elemento
     */
    @Override
    public T eliminarPrimero() {
        if (estaVacia()) return null;   //si está vacía, devuelve null
        T dato = primero.dato;  //guardamos el dato del primer elemento porque lo vamos a eliminar
        if (primero == ultimo) {    //si solo hay un elemento, pasa a ser nulo
            primero = null;
            ultimo = null;}
        else {
            primero = primero.siguiente;    //el primero pasa a ser el siguiente
            primero.anterior = null;        //eliminamos el anterior primero
        }
        return dato;    //devuelve el dato del elemento eliminado
    }

    /** Metodo para eliminar el último elemento
     */
    @Override
    public T eliminarUltimo() {
        if (estaVacia()) return  null;
        T dato = ultimo.dato;   //guardamos el dato del elemento que vamos a eliminar
        if (primero == ultimo) {    //si solo hay un elemento, pasa a ser nulo
            primero = null;
            ultimo = null;}
        else {
            ultimo = ultimo.anterior;   //el último elemento pasa a ser el anterior
            ultimo.siguiente = null;    //eliminamos el anterior último
        }
        return dato;    //devuelve el dato del elemento eliminado
    }

    /** Metodo que recorre la lista empleando el iterador
     */
    @Override
    public Iterator<T> iterator() {
        return new IteradorLDE<>(primero);
    }

    /** Metodo que recorre la lista de delante hacia atrás
     */
    public void mostrarDelante() {
        ElementoDE<T> actual = primero; //apunta al primer elemento de la lista
        while (actual != null) {    //recorremos la lista hasta el final, empezando en el primer elemento
            System.out.println((actual.dato));  //imprimimos cada dato
            actual = actual.siguiente;  //el actual pasa a ser el siguiente
        }
    }

    /** Metodo que recorre la lista de atrás hacia delante
     */
    public void mostrarDetras() {
        ElementoDE<T> actual = ultimo;  //ahora apunta al último elemento
        while (actual != null) {    //recorremos la lista hasta el principio, empezando en el último elemento
            System.out.println(actual.dato);    //imprimimos cada dato
            actual = actual.anterior;   //el actual pasa a ser el anterior
        }
    }
}
