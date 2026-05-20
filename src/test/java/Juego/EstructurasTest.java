package Juego;

import Juego.listas.ListaSimplementeEnlazada;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstructurasTest {
    @Test
    void listaPermiteInsertarObtenerYEliminar() {
        ListaSimplementeEnlazada<String> lista = new ListaSimplementeEnlazada<>();
        lista.insertarUltimo("a");
        lista.insertarUltimo("b");

        assertEquals(2, lista.tamaño());
        assertEquals("b", lista.obtener(1));
        assertEquals("a", lista.eliminar(0));
        assertEquals(1, lista.tamaño());
    }

    @Test
    void colaRespetaOrdenFifo() {
        Cola<Integer> cola = new Cola<>();
        cola.enqueue(1);
        cola.enqueue(2);

        assertEquals(1, cola.dequeue());
        assertEquals(2, cola.dequeue());
        assertTrue(cola.estaVacia());
    }

    @Test
    void grafoCalculaCaminoMinimo() {
        Grafo grafo = new Grafo(false);
        int a = grafo.agregarVertice("A");
        int b = grafo.agregarVertice("B");
        int c = grafo.agregarVertice("C");
        grafo.agregarArista(a, b);
        grafo.agregarArista(b, c);

        Object[] resultado = grafo.dijkstra(a, c);

        assertNotNull(resultado);
        assertEquals(2.0, (double) resultado[0]);
    }
}
