package Juego.listas;

public interface Lista<T> {

    boolean estaVacia();
    void insertarPrimero(T dato);
    void insertarUltimo(T dato);
    T eliminarPrimero();
    T eliminarUltimo();
}
