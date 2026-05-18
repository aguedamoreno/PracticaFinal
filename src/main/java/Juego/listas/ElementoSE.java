package Juego.listas;

/** Elemento para lista simplemente enlazada
 */
public class ElementoSE<T> {
    // dato de tipo genético T
    T dato;
    // referencia al siguiente elemento de la lista
    ElementoSE<T> siguiente;

    /** Constructor que enlaza los elementos formando la estructura de lista
     */
    public ElementoSE(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }
}
