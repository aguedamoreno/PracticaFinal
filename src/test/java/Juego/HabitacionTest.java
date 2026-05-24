package Juego;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HabitacionTest {

    @Test
    void getId() {
        Habitacion habitacion = new Habitacion("Entrada", 5, 5);
        assertEquals("Entrada", habitacion.getId());
    }

    @Test
    void getFilas() {
        Habitacion habitacion = new Habitacion("Entrada", 5, 7);
        assertEquals(5, habitacion.getFilas());
    }

    @Test
    void getColumnas() {
        Habitacion habitacion = new Habitacion("Entrada", 5, 7);
        assertEquals(7, habitacion.getColumnas());
    }

    @Test
    void getCelda() {
        Habitacion habitacion = new Habitacion("Entrada", 3, 3);
        assertNotNull(habitacion.getCelda(0, 0));
        assertNull(habitacion.getCelda(-1, 0));
        assertNull(habitacion.getCelda(0, 99));
    }

    @Test
    void setCelda() {
        Habitacion habitacion = new Habitacion("Entrada", 3, 3);
        Celda celda = new Celda(Celda.Tipo.SALIDA);

        habitacion.setCelda(1, 1, celda);

        assertSame(celda, habitacion.getCelda(1, 1));
    }

    @Test
    void esPosicionValida() {
        Habitacion habitacion = new Habitacion("Entrada", 3, 4);
        assertTrue(habitacion.esPosicionValida(0, 0));
        assertTrue(habitacion.esPosicionValida(2, 3));
        assertFalse(habitacion.esPosicionValida(-1, 0));
        assertFalse(habitacion.esPosicionValida(3, 0));
        assertFalse(habitacion.esPosicionValida(0, 4));
    }

    @Test
    void colocarObjeto() {
        Habitacion habitacion = new Habitacion("Entrada", 3, 3);
        Objeto objeto = new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10);

        habitacion.colocarObjeto(1, 1, objeto);

        assertEquals(Celda.Tipo.OBJETO, habitacion.getCelda(1, 1).getTipo());
        assertSame(objeto, habitacion.getCelda(1, 1).getContenido());
    }

    @Test
    void colocarEnemigo() {
        Habitacion habitacion = new Habitacion("Entrada", 3, 3);
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.GOBLIN);

        habitacion.colocarEnemigo(2, 1, enemigo);

        assertEquals(Celda.Tipo.ENEMIGO, habitacion.getCelda(2, 1).getTipo());
        assertSame(enemigo, habitacion.getCelda(2, 1).getContenido());
        assertEquals(2, enemigo.getPosicionX());
        assertEquals(1, enemigo.getPosicionY());
    }

    @Test
    void colocarPuerta() {
        Habitacion habitacion = new Habitacion("Entrada", 3, 3);
        habitacion.colocarPuerta(0, 2, "Sala");

        assertEquals(Celda.Tipo.PUERTA, habitacion.getCelda(0, 2).getTipo());
        assertEquals("Sala", habitacion.getCelda(0, 2).getContenido());
    }

    @Test
    void colocarTrampa() {
        Habitacion habitacion = new Habitacion("Entrada", 3, 3);
        habitacion.colocarTrampa(1, 2);

        assertEquals(Celda.Tipo.TRAMPA, habitacion.getCelda(1, 2).getTipo());
        assertTrue(habitacion.getCelda(1, 2).isAccesible());
    }

    @Test
    void colocarSalida() {
        Habitacion habitacion = new Habitacion("Entrada", 3, 3);
        habitacion.colocarSalida(2, 2);

        assertEquals(Celda.Tipo.SALIDA, habitacion.getCelda(2, 2).getTipo());
    }

    @Test
    void obtenerPosicionesVacias() {
        Habitacion habitacion = new Habitacion("Entrada", 2, 2);
        habitacion.colocarObjeto(0, 0, new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10));

        assertEquals(3, habitacion.obtenerPosicionesVacias().tamaño());
    }

    @Test
    void obtenerPosicionesConObjetos() {
        Habitacion habitacion = new Habitacion("Entrada", 3, 3);
        habitacion.colocarObjeto(0, 0, new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10));
        habitacion.colocarObjeto(1, 1, new Objeto(Objeto.Tipo.ARMA, "Espada", "+4 ataque", 4));

        assertEquals(2, habitacion.obtenerPosicionesConObjetos().tamaño());
    }

    @Test
    void obtenerPosicionesConEnemigos() {
        Habitacion habitacion = new Habitacion("Entrada", 3, 3);
        habitacion.colocarEnemigo(0, 0, new Enemigo(Enemigo.Tipo.GOBLIN));
        habitacion.colocarEnemigo(1, 1, new Enemigo(Enemigo.Tipo.ORCO));

        assertEquals(2, habitacion.obtenerPosicionesConEnemigos().tamaño());
    }

    @Test
    void obtenerPosicionesConPuertas() {
        Habitacion habitacion = new Habitacion("Entrada", 3, 3);
        habitacion.colocarPuerta(0, 0, "Sala");
        habitacion.colocarPuerta(2, 2, "Salida");

        assertEquals(2, habitacion.obtenerPosicionesConPuertas().tamaño());
    }

    @Test
    void calcularPosicionesAlcanzables() {
        Habitacion habitacion = new Habitacion("Entrada", 5, 5);
        Posicion origen = new Posicion(2, 2);

        assertEquals(9, habitacion.calcularPosicionesAlcanzables(origen, 2).tamaño());

        habitacion.getCelda(2, 3).setAccesible(false);
        assertEquals(7, habitacion.calcularPosicionesAlcanzables(origen, 2).tamaño());
    }

    @Test
    void testToString() {
        Habitacion habitacion = new Habitacion("Entrada", 2, 2);
        habitacion.colocarSalida(1, 1);

        String texto = habitacion.toString();

        assertTrue(texto.contains("Habitación Entrada"));
        assertTrue(texto.contains("2x2"));
        assertTrue(texto.contains("[S]"));
    }
}