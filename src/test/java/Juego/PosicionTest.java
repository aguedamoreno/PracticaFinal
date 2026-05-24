package Juego;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PosicionTest {

    @Test
    void getFila() {
        Posicion p = new Posicion(3, 5);

        assertEquals(3, p.getFila());
    }

    @Test
    void getColumna() {
        Posicion p = new Posicion(3, 5);

        assertEquals(5, p.getColumna());
    }

    @Test
    void testEquals() {
        Posicion p1 = new Posicion(2, 4);
        Posicion p2 = new Posicion(2, 4);
        Posicion p3 = new Posicion(1, 1);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }

    @Test
    void testToString() {
        Posicion p = new Posicion(7, 8);

        assertEquals("(7,8)", p.toString());
    }
}