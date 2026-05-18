package Juego.listas;

public class ListaCircular<T> implements InterfazListaCircular<T> {
    ElementoSE<T> ultimo;

    /** Constructor que inicializa la lista circular como vacía
     */
    public ListaCircular() {
        ultimo = null;
    }

    /** Metodo para añadir un nuevo elemento a la lista circular
     */
    @Override
    public void insertar(T dato) {
        ElementoSE<T> nuevo = new ElementoSE<>(dato);   //creamos un nuevo elemento
        if (ultimo == null) {   //si la lista está vacía
            ultimo = nuevo; //el nuevo elemento pasa a ser el último
            ultimo.siguiente = ultimo;  //el siguiente elemento apunta al último
        } else {    //si la lista no está vacía
            nuevo.siguiente = ultimo.siguiente; //el nuevo elemento apunta al primer elemento
            ultimo.siguiente = nuevo;   //ahora el último elemento apunta al nuevo
            ultimo = nuevo; //y el nuevo elemento pasa a ser el último
        }
    }

    /** Metodo que recorre la lista circular y muestra sus elementos
     */
    @Override
    public void mostrar() {
        if (ultimo == null) return; //si la lista está vacía no hay nada que devolver
        ElementoSE<T> actual = ultimo.siguiente;    //el elemento actual apunta al primero, desde donde empezamos a recorrer la lista
        do {
            System.out.println(actual.dato);    //imprimimos el dato del elemento al que apunta el actual
            actual = actual.siguiente;  //ahora el actual apunta al siguiente
        }
        while (actual != ultimo.siguiente); //se repite hasta que el actual vuelve a apuntar al primero, que es el siguiente del último
    }

    /** Metodo que devuelve el primer elemento
     */
    @Override
    public T getPrimero() {
        if (ultimo == null) return null;
        return ultimo.siguiente.dato;   //como es una lista circular, devuelve el dato del que apunta el último, que es el primero
    }

    /** Metodo que devuelve el último elemento
     */
    @Override
    public T getUltimo() {
        if (ultimo == null) return null;
        return ultimo.dato;
    }

    /** Metodo que inserta un elemento al principio
     */
    @Override
    public void insertarAlPrincipio(T dato) {
        ElementoSE<T> nuevo = new ElementoSE<>(dato);

        if (ultimo == null) {   // si la lista está vacía, el último dato es el nuevo, por lo tanto también es el primero
            ultimo = nuevo;
            ultimo.siguiente = ultimo;  //y el último se apunta a sí mismo, porque es el único elemento en la lista
        } else {
            nuevo.siguiente = ultimo.siguiente; //si la lista no está vacía, el nuevo elemento apunta al primero
            ultimo.siguiente = nuevo;   //y el último apunta al nuevo, por lo tanto el nuevo pasa a ser el primero
        }
    }

    /** Metodo que elimina el primer elemento
     */
    @Override
    public T eliminarPrimero() {
        if (ultimo == null) return null;

        ElementoSE<T> primero = ultimo.siguiente;   //iniciamos el elemento primero como el elemento al que apunta el último

        if (ultimo == primero) {
            T dato = primero.dato;  //guardamos el dato del elemento que vamos a eliminar, en este caso el único que hay en la lista
            ultimo = null;  //y lo eliminamos
            return dato;
        }

        T dato = primero.dato;
        ultimo.siguiente = primero.siguiente; //ahora el último apunta al siguiente elemento del primero, entonces el primero se elimina automaticamente
        return dato;
    }

    /** Metodo que elimina el último elemento
     */
    @Override
    public T eliminarUltimo() {
        if (ultimo == null) return null;

        ElementoSE<T> primero = ultimo.siguiente;

        if (ultimo == primero) {
            T dato = ultimo.dato;
            ultimo = null;
            return dato;
        }

        ElementoSE<T> actual = primero;

        while (actual.siguiente != ultimo) {    //recorremos la lista hasta llegar al penúltimo elemento
            actual = actual.siguiente;
        }

        T dato = ultimo.dato;   //guardamos el dato del último elemento que queremos eliminar
        actual.siguiente = primero; //ahora el penúltimo elemento apunta al primero, y así se borra automaticamente el último elemento
        ultimo = actual; //y actualizamos el último elemento que pasa a ser el que era el penúltimo

        return dato;
    }

    /** Metodo que rota la lista circular
     */
    @Override
    public void rotar() {
        if (ultimo != null) {
            ultimo = ultimo.siguiente;  //ahora el último elemento es el primero, rotan todos una posición, y el último pasa a ser el antepenúltimo
        }
    }

    /** Metodo que vacía completamente la lista circular
     * */
    public void clear() {
        ultimo = null;  //así se elimina automáticamente toda la lista
    }

    /** Metodo que pasa la lista circular a texto
     */
    @Override
    public String toString() {
        if (ultimo == null) return "Lista circular vacía";

        String texto = "";
        ElementoSE<T> actual = ultimo.siguiente;

        while (actual != ultimo) {
            texto += actual.dato + " -> ";
            actual = actual.siguiente;
        }
        //añadimos el último elemento (para no poner flecha después)
        texto += ultimo.dato;
        return texto + " -> vuelve al primero";
    }
}