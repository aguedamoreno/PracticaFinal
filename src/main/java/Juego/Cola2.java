package Juego;

import Juego.listas.ListaSimplementeEnlazada;

public class Cola2<T> {
    private final ListaSimplementeEnlazada<T> elementos;

    public Cola2() {
        this.elementos = new ListaSimplementeEnlazada<>();
    }

    public void enqueue(T dato) {
        elementos.insertarUltimo(dato);
    }

    public T dequeue() {
        return elementos.eliminarPrimero();
    }

    public boolean estaVacia() {
        return elementos.estaVacia();
    }

    public int size() {
        return elementos.size();
    }
}