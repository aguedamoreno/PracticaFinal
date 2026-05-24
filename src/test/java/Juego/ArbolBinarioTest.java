package Juego;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArbolBinarioTest {

    @Test
    void insertar() {
        ArbolBinario<Integer> arbol = new ArbolBinario<>();

        arbol.insertar(5);
        arbol.insertar(3);
        arbol.insertar(7);
        arbol.insertar(5); // duplicado, no debe insertarse

        assertEquals(3, arbol.size());
        assertTrue(arbol.buscar(5));
        assertTrue(arbol.buscar(3));
        assertTrue(arbol.buscar(7));
    }

    @Test
    void buscar() {
        ArbolBinario<String> arbol = new ArbolBinario<>();
        arbol.insertar("B");
        arbol.insertar("A");
        arbol.insertar("C");

        assertTrue(arbol.buscar("A"));
        assertTrue(arbol.buscar("B"));
        assertFalse(arbol.buscar("Z"));
    }

    @Test
    void inorden() {
        ArbolBinario<Integer> arbol = crearArbolEjemplo();

        assertEquals(2, arbol.inorden().obtener(0));
        assertEquals(3, arbol.inorden().obtener(1));
        assertEquals(4, arbol.inorden().obtener(2));
        assertEquals(5, arbol.inorden().obtener(3));
        assertEquals(7, arbol.inorden().obtener(4));
    }

    @Test
    void preorden() {
        ArbolBinario<Integer> arbol = crearArbolEjemplo();

        assertEquals(5, arbol.preorden().obtener(0));
        assertEquals(3, arbol.preorden().obtener(1));
        assertEquals(2, arbol.preorden().obtener(2));
        assertEquals(4, arbol.preorden().obtener(3));
        assertEquals(7, arbol.preorden().obtener(4));
    }

    @Test
    void postorden() {
        ArbolBinario<Integer> arbol = crearArbolEjemplo();

        assertEquals(2, arbol.postorden().obtener(0));
        assertEquals(4, arbol.postorden().obtener(1));
        assertEquals(3, arbol.postorden().obtener(2));
        assertEquals(7, arbol.postorden().obtener(3));
        assertEquals(5, arbol.postorden().obtener(4));
    }

    @Test
    void eliminar() {
        ArbolBinario<Integer> arbol = crearArbolEjemplo();

        assertTrue(arbol.eliminar(3));
        assertFalse(arbol.buscar(3));
        assertEquals(4, arbol.size());
        assertFalse(arbol.eliminar(99));
        assertEquals(4, arbol.size());
    }

    @Test
    void altura() {
        ArbolBinario<Integer> arbol = new ArbolBinario<>();
        assertEquals(-1, arbol.altura());

        arbol.insertar(5);
        assertEquals(0, arbol.altura());

        arbol.insertar(3);
        arbol.insertar(2);
        assertEquals(2, arbol.altura());
    }

    @Test
    void size() {
        ArbolBinario<Integer> arbol = new ArbolBinario<>();
        assertEquals(0, arbol.size());

        arbol.insertar(1);
        arbol.insertar(2);
        assertEquals(2, arbol.size());
    }

    @Test
    void estaVacia() {
        ArbolBinario<Integer> arbol = new ArbolBinario<>();
        assertTrue(arbol.estaVacia());

        arbol.insertar(10);
        assertFalse(arbol.estaVacia());
    }

    private ArbolBinario<Integer> crearArbolEjemplo() {
        ArbolBinario<Integer> arbol = new ArbolBinario<>();
        arbol.insertar(5);
        arbol.insertar(3);
        arbol.insertar(7);
        arbol.insertar(2);
        arbol.insertar(4);
        return arbol;
    }
}