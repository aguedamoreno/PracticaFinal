package Juego.listas;

public interface InterfazPila<T> {

    void push(T dato);
    T pop();
    T peek();
    T elementoFondo();
    void duplicarCima();
    void intercambiarDosPrimeros();
    void insertarEnFondo(T dato);
    void invertir();
    Pila<T> copiar();
   String toString();

}
