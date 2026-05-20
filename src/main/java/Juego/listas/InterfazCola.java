package Juego.listas;

public interface InterfazCola<T> {
    void enqueue(T dato);
    T dequeue();
    T peek();
    T peekFinal();
    boolean estaVacia();
    void desencolarTodos();
    void invertir();
    int contarOcurrencias(T dato);
    boolean eliminarPrimerDato(T dato);
    Cola<T> copiar();
    String toString();

}
