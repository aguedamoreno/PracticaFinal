package Juego;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CeldaTest {

    @Test
    void getTipo() {
        Celda celda = new Celda(Celda.Tipo.OBJETO);
        assertEquals(Celda.Tipo.OBJETO, celda.getTipo());
    }

    @Test
    void setTipo() {
        Celda celda = new Celda();
        celda.setTipo(Celda.Tipo.ENEMIGO);
        assertEquals(Celda.Tipo.ENEMIGO, celda.getTipo());
    }

    @Test
    void getContenido() {
        Objeto objeto = new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10);
        Celda celda = new Celda(Celda.Tipo.OBJETO, objeto);
        assertSame(objeto, celda.getContenido());
    }

    @Test
    void setContenido() {
        Celda celda = new Celda();
        String destino = "Sala";
        celda.setContenido(destino);
        assertEquals(destino, celda.getContenido());
    }

    @Test
    void isAccesible() {
        Celda celda = new Celda();
        assertTrue(celda.isAccesible());
    }

    @Test
    void setAccesible() {
        Celda celda = new Celda();
        celda.setAccesible(false);
        assertFalse(celda.isAccesible());
    }

    @Test
    void estaLibreParaMovimiento() {
        Celda celdaVacia = new Celda(Celda.Tipo.VACIA);
        assertTrue(celdaVacia.estaLibreParaMovimiento());

        Celda celdaTrampa = new Celda(Celda.Tipo.TRAMPA);
        assertTrue(celdaTrampa.estaLibreParaMovimiento());

        celdaTrampa.setAccesible(false);
        assertFalse(celdaTrampa.estaLibreParaMovimiento());
    }

    @Test
    void limpiar() {
        Celda celda = new Celda(Celda.Tipo.ENEMIGO, new Enemigo(Enemigo.Tipo.GOBLIN));
        celda.setAccesible(false);

        celda.limpiar();

        assertEquals(Celda.Tipo.VACIA, celda.getTipo());
        assertNull(celda.getContenido());
        assertTrue(celda.isAccesible());
    }

    @Test
    void testToString() {
        assertEquals("[ ]", new Celda(Celda.Tipo.VACIA).toString());
        assertEquals("[E]", new Celda(Celda.Tipo.ENEMIGO).toString());
        assertEquals("[O]", new Celda(Celda.Tipo.OBJETO).toString());
        assertEquals("[P]", new Celda(Celda.Tipo.PUERTA).toString());
        assertEquals("[T]", new Celda(Celda.Tipo.TRAMPA).toString());
        assertEquals("[S]", new Celda(Celda.Tipo.SALIDA).toString());
    }
}