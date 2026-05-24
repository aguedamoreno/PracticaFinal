package Juego;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrafoTest {

    @Test
    void agregarVertice() {
        Grafo grafo = new Grafo();

        int indiceA = grafo.agregarVertice("A");
        int indiceB = grafo.agregarVertice("B");

        assertEquals(0, indiceA);
        assertEquals(1, indiceB);
        assertEquals(2, grafo.getSize());
    }

    @Test
    void agregarArista() {
        Grafo grafo = new Grafo(false);
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");

        grafo.agregarArista(0, 1, 2.5);

        assertEquals(2.5, grafo.obtenerPesoArista(0, 1));
        assertEquals(2.5, grafo.obtenerPesoArista(1, 0));
    }

    @Test
    void testAgregarArista() {
        Grafo grafo = new Grafo();
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");

        grafo.agregarArista(0, 1);

        assertEquals(1.0, grafo.obtenerPesoArista(0, 1));
    }

    @Test
    void obtenerAdyacentes() {
        Grafo grafo = new Grafo();
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista(0, 1);
        grafo.agregarArista(0, 2);

        assertEquals(2, grafo.obtenerAdyacentes(0).tamaño());
        assertEquals(1, grafo.obtenerAdyacentes(0).obtener(0));
        assertEquals(2, grafo.obtenerAdyacentes(0).obtener(1));
    }

    @Test
    void obtenerPesoArista() {
        Grafo grafo = new Grafo();
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista(0, 1, 4.0);

        assertEquals(4.0, grafo.obtenerPesoArista(0, 1));
        assertEquals(Double.POSITIVE_INFINITY, grafo.obtenerPesoArista(0, 2));
    }

    @Test
    void bfs() {
        Grafo grafo = crearGrafoEjemplo();

        assertEquals(1, grafo.bfs(0, 0).tamaño());
        assertEquals(3, grafo.bfs(0, 1).tamaño());
        assertEquals(4, grafo.bfs(0, 2).tamaño());
    }

    @Test
    void dijkstra() {
        Grafo grafo = new Grafo();
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista(0, 1, 2.0);
        grafo.agregarArista(1, 2, 3.0);
        grafo.agregarArista(0, 2, 10.0);

        Object[] resultado = grafo.dijkstra(0, 2);

        assertNotNull(resultado);
        assertEquals(5.0, (double) resultado[0]);
        assertNotNull(resultado[1]);
    }

    @Test
    void getSize() {
        Grafo grafo = new Grafo();
        assertEquals(0, grafo.getSize());
        grafo.agregarVertice("A");
        assertEquals(1, grafo.getSize());
    }

    @Test
    void estaVacio() {
        Grafo grafo = new Grafo();
        assertTrue(grafo.estaVacio());
        grafo.agregarVertice("A");
        assertFalse(grafo.estaVacio());
    }

    @Test
    void obtenerDatosVertice() {
        Grafo grafo = new Grafo();
        Habitacion habitacion = new Habitacion("Entrada", 2, 2);
        grafo.agregarVertice(habitacion);

        assertSame(habitacion, grafo.obtenerDatosVertice(0));
    }

    @Test
    void establecerDatosVertice() {
        Grafo grafo = new Grafo();
        grafo.agregarVertice("A");

        grafo.establecerDatosVertice(0, "Nueva A");

        assertEquals("Nueva A", grafo.obtenerDatosVertice(0));
    }

    @Test
    void indiceInvalidoLanzaExcepcion() {
        Grafo grafo = new Grafo();
        grafo.agregarVertice("A");

        assertThrows(IndexOutOfBoundsException.class, () -> grafo.obtenerAdyacentes(99));
        assertThrows(IndexOutOfBoundsException.class, () -> grafo.agregarArista(0, 99));
    }

    private Grafo crearGrafoEjemplo() {
        Grafo grafo = new Grafo();
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarVertice("D");
        grafo.agregarArista(0, 1);
        grafo.agregarArista(0, 2);
        grafo.agregarArista(1, 3);
        return grafo;
    }
}