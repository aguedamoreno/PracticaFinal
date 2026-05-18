package Juego.listas;

/** Elemento para lista doblemente enlazada
 */
public class ElementoDE<T> {
    T dato;
    //referencia al elemento anterior de la lista
    ElementoDE<T> anterior;
    ElementoDE<T> siguiente;

    /** Constructor que enlaza los datos formando la estructura de lista
     */
    public ElementoDE(T dato) {
        this.dato = dato;
        this.anterior = null;
        this.siguiente = null;
    }
}
