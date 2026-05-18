package Juego.listas;

public class Pila<T> implements InterfazPila<T> {
    //atributo que hace referencia al elemento que está arriba de la pila
    ElementoSE<T> cima;

    /** Constructor que inicializa la pila como vacía
     */
    public Pila() {
        cima = null;
    }

    /** Metodo que añade un elemento a la pila
     */
    @Override
    public void push(T dato) {
        ElementoSE<T> nuevo = new ElementoSE<>(dato);   //creamos un nuevo elemento
        nuevo.siguiente = cima; //el nuevo elemento apunta al elemento que está en la cima
        cima = nuevo;   //actualizamos la cima, el nuevo elemento pasa a ser el que está en la cima
    }

    /** Metodo que elimina el elemento de la cima de la pila
     */
    @Override
    public T pop() {
        if (cima == null) return  null; //si la pila está vacía no hay nada que eliminar
        T dato = cima.dato; //guardamos el dato del elemento que vamos a eliminar
        cima = cima.siguiente;  //actualizamos la cima, el segundo elemento pasa a ser el que está en la cima y desaparece así el que era el primero
        return dato;    //devuelve el dato del elemento eliminado
    }

    /** Metodo que devuelve el elemento de la cima sin eliminarlo
     */
    @Override
    public T peek() {
        if (cima == null) return null;  //si la pila está vacía no hay nada que devolver
        return  cima.dato;  //devuelve el dato del primer elemento
    }

    /** Metodo que devuelve el dato del elemento del fondo
     */
    @Override
    public T elementoFondo() {
        if (cima == null) return null;  //si la pila está vacía devuelve null

        ElementoSE<T> actual = cima;    //inicializamos el elemento actual como la cima, es decir, el primer elemento
        while (actual.siguiente != null) {  //recorremos la lista hasta que el siguiente del actual sea nulo
            actual = actual.siguiente;
        }

        return actual.dato; //devuelve el dato del elemento final
    }

    /** Metodo que duplica el elemento de la cima
     */
    @Override
    public void duplicarCima() {
        if (cima != null) {
            push(cima.dato);    //usamos el metodo push para añadir un nuevo elemento con ese mismo dato, y como es una pila, se añade a la cima
        }
    }

    /** Metodo que intercambia los dos primeros elementos de la pila
     */
    @Override
    public void intercambiarDosPrimeros() {
        if (cima == null || cima.siguiente == null) return; //si la pila es nula no devuelve nada

        ElementoSE<T> primero = cima;   //inicializamos el elemento primero como la cima
        ElementoSE<T> segundo = cima.siguiente; //inicializamos el elemento segundo como el siguiente de cima
        primero.siguiente = segundo.siguiente;  //el primero ahora apunta al de después del segundo
        segundo.siguiente = primero;    //y el segundo ahora apunta al primero, por lo tanto quedan: segundo--> primero--> siguiente...
        cima = segundo; //actualizamos la cima, que ahora es el que era el segundo
    }

    /** Metodo que inserta un elemento en el fondo de la pila
     */
    @Override
    public void insertarEnFondo(T dato) {
        if (cima == null) { //si la pila está vacía, inserta el dato dado, y así sera el primero y el último porque sera el único
            push(dato);
            return;
        }

        Pila<T> auxiliar = new Pila<>();    //creamos una nueva pila auxiliar vacía

        while (cima != null) {  //recorremos la lista y sacamos cada dato de la pila y lo metemos en la pila auxiliar
            auxiliar.push(pop());
        }

        push(dato); //ahora metemos el dato que queremos en la pila principal

        while (auxiliar.cima != null) { // y por último recorremos la pila auxiliar, y sacamos cada dato de ella y lo metemos en la pila principal otra vez
            push(auxiliar.pop());
        }
    }

    /** Metodo que invierte el orden de la pila
     */
    @Override
    public void invertir() {
        Pila<T> auxiliar = new Pila<>();    //creamos una nueva pila auxiliar

        while (cima != null) {  //recorremos la pila y sacamos cada elemento y lo metemos en la nueva pila
            auxiliar.push(pop());
        }
        cima = auxiliar.cima;   //y la nueva cima es la cima de la pila auxiliar
    }   //así, como en la pila el último que entra es el primero que sale, así en la nueva pila quedan invertidos

    /** Metodo que crea una copia de la pila
     */
    @Override
    public Pila<T> copiar() {
        Pila<T> auxiliar = new Pila<>();
        Pila<T> copia = new Pila<>();   //creamos una pila nueva que va a ser la copia

        ElementoSE<T> actual = cima;
        while (actual != null) {    //recorremos la pila, y metemos en la pila auxiliar todos los elementos de la pila principal, sin sacarlos de esta, es decir copiándolos
            auxiliar.push(actual.dato);
            actual = actual.siguiente;
        }

        while (auxiliar.cima != null) { //por último, recorremos la pila auxiliar y metemos los datos copiados en la pila copia
            T dato = auxiliar.pop();
            copia.push(dato);
        }
        return copia;
    }

    /** Metodo que pasa la pila a texto
     */
    @Override
    public String toString() {
        if (cima == null) return "Pila vacía";

        String texto = "";
        ElementoSE<T> actual = cima;    //inicializamos el elemento actual como la cima

        while (actual != null) {    //recorremos la pila y vamos añadiendo al texto el dato de cada elemento
            texto += actual.dato;
            if (actual.siguiente != null) texto += " -> ";
            actual = actual.siguiente;
        }
        return texto;
    }
}