package Juego.listas;

public class Cola<T> implements InterfazCola<T>{
    //primer elemento de la cola, el primero que se elimina
    ElementoSE<T> primero;
    //último elemento de la cola, el último que se elimina
    ElementoSE<T> ultimo;
    private int tamano;

    /** Constructor que inicializa la cola como vacía
     */
    public Cola() {
        primero = null;
        ultimo = null;
        tamano = 0;
    }

    /** Metodo para añadir un elemento al final de la cola
     */
    @Override
    public void enqueue(T dato) {
        ElementoSE<T> nuevo = new ElementoSE<>(dato);
        if (primero == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }
        tamano++;
    }

    /** Metodo para eliminar el primer elemento de la cola
     */
    @Override
    public T dequeue() {
        if (primero == null) return null;
        T dato = primero.dato;
        primero = primero.siguiente;
        if (primero == null) {
            ultimo = null;
        }
        tamano--;
        return dato;
    }

    /** Metodo que devuelve el primer elemento sin eliminarlo
     */
    @Override
    public T peek() {
        if (primero == null) return null;   // si la cola está vacía no devuelve nada
        return primero.dato;
    }

    /** Metodo que devuelve el último elemento sin eliminarlo
     */
    @Override
    public T peekFinal() {
        if (ultimo == null) return null;
        return ultimo.dato;
    }

    /** Metodo que elimina todos los elementos de la cola uno a uno
     */
    @Override
    public void desencolarTodos() {
        while (primero != null) {   // se repite el bucle hasta que la cola esté completamente vacía
            dequeue();  // se va eliminando el primer elemento de cada iteración
        }
    }

    /** Metodo que invierte el orden de la cola
     */
    @Override
    public void invertir() {
        ElementoSE<T> anterior = null;
        ElementoSE<T> actual = primero;
        ElementoSE<T> siguiente;
        ultimo = primero; // el primero actual pasará a ser el último

        while (actual != null) {
            siguiente = actual.siguiente;   // el elemento que hemos inicializado es el siguiente del que esté apuntando el actual
            actual.siguiente = anterior;    // ahora, como el primero es el último, ahora el primero apunta a null
            anterior = actual;  // ahora, el nuevo anterior en lugar de ser null, pasa a ser el que es el último ahora
            actual = siguiente; //y el nuevo actual pasa a ser el siguiente, que era el elemento después del primero en la cola
        }

        primero = anterior; // y por último, el nuevo elemento primero pasa a ser el último anterior que ha quedado guardado tras el bucle, que será el que era el último en la cola al principio
    }

    /** Metodo que cuenta cuántas veces aparece un dato en la cola
     */
    @Override
    public int contarOcurrencias(T dato) {
        int contador = 0;   //inicializamos el contador
        ElementoSE<T> actual = primero;

        while (actual != null) {    //mientras el elemento actual no sea nulo, se repite el bucle
            if ((dato == null && actual.dato == null) ||    //luego, si el dato dado y el del elemento actual son nulos o el dato dado es distinto de 0 y este dato es igual al del elemento actual, se suma 1 al contador
                    (dato != null && dato.equals(actual.dato))) {
                contador++;
            }
            actual = actual.siguiente;  //y pasamos actual al siguiente elemento, para compararlo con este
        }

        return contador;
    }

    /** Metodo que elimina la primera aparición de un dato en la cola
     * */
    @Override
    public boolean eliminarPrimerDato(T dato) {
        if (primero == null) return false;  //si la cola está vacía, no se puede eliminar nada asique devuelve falso

        if ((dato == null && primero.dato == null) ||   //igual que el metodo anterior, y si se cumple se elimina ese elemento
                (dato != null && dato.equals(primero.dato))) {
            dequeue();
            return true;    //devuelve true porque se ha podido eliminar
        }

        ElementoSE<T> actual = primero;

        while (actual.siguiente != null) {  //recorremos la lista, si el primero no coincidía
            if ((dato == null && actual.siguiente.dato == null) ||
                    (dato != null && dato.equals(actual.siguiente.dato))) {

                if (actual.siguiente == ultimo) {   //si el siguiente del actual es el último, actualizamos el último, y pasa a ser el anterior
                    ultimo = actual;
                }

                actual.siguiente = actual.siguiente.siguiente;  //eliminamos el elemento que coincide, es decir, eliminamos ese elemento de actual.siguiente
                return true;
            }
            actual = actual.siguiente;  //actualizamos actual
        }
        return false;
    }

    /** Metodo que crea una copia de la cola
     */
    @Override
    public Cola<T> copiar() {
        Cola<T> copia = new Cola<>();   //creamos una nueva cola, que será la copia
        ElementoSE<T> actual = primero; //inicializamos actual como el primero

        while (actual != null) {    //recorremos la cola y añadimos cada elemento a la copia
            copia.enqueue(actual.dato);
            actual = actual.siguiente;
        }
        return copia;
    }

    /** Metodo que mide el tamaño de la cola
     */
    public int size() {
        return tamano;
    }

    @Override
    public boolean estaVacia() {
        return primero == null;
    }

    /** Metodo que pasa la cola a texto
     */
    @Override
    public String toString() {
        if (primero == null) return "Cola vacía";

        String texto = "";  //inicializamos el texto vacío
        ElementoSE<T> actual = primero;

        while (actual != null) {    //recorremos la cola y añadimos al texto cada dato seguido de la flecha con la que apunta al siguiente elemento
            texto += actual.dato;
            if (actual.siguiente != null) texto += " -> ";
            actual = actual.siguiente;
        }
        return texto;
    }

}
