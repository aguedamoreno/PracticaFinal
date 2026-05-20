package Juego.listas;

import java.util.Iterator;

public class IteradorLDE <T> implements Iterator<T> {

    private ElementoDE<T> actual;

    public IteradorLDE (ElementoDE<T> primero) {
        this.actual = primero;
    }

    @Override
    public boolean hasNext() {
        return actual != null;
    }

    @Override
    public T next() {
        if (actual == null) return null;
        T dato = actual.dato;
        actual = actual.siguiente;
        return dato;
    }
}
