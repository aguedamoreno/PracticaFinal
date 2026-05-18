package Juego.listas;

public interface InterfazListaCircular<T> {

    void insertar(T dato);
    void mostrar();
    T getPrimero();
    T getUltimo();
    void insertarAlPrincipio(T dato);
    T eliminarPrimero();
    T eliminarUltimo();
    void rotar();
    String toString();

}
