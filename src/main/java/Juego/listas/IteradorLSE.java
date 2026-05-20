package Juego.listas;

import java.util.Iterator;

public class IteradorLSE<T> implements Iterator<T> {
    private ElementoSE<T> actual;

    public IteradorLSE(ElementoSE<T> primero) {
        this.actual = primero;
    }

    @Override
    public boolean hasNext() {
        return actual != null;
    }

    @Override
    public T next() {
        T dato = actual.dato;
        actual = actual.siguiente;
        return dato;
    }
}
