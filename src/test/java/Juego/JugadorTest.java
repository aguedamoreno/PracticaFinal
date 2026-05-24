package Juego;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JugadorTest {

    @Test
    void getVidaMax() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        assertEquals(100, jugador.getVidaMax());
    }

    @Test
    void getVidaActual() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        assertEquals(100, jugador.getVidaActual());
    }

    @Test
    void setVidaActual() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        jugador.setVidaActual(50);
        assertEquals(50, jugador.getVidaActual());

        jugador.setVidaActual(-10);
        assertEquals(0, jugador.getVidaActual());

        jugador.setVidaActual(999);
        assertEquals(100, jugador.getVidaActual());
    }

    @Test
    void getAtaqueBase() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        assertEquals(10, jugador.getAtaqueBase());
    }

    @Test
    void getDefensaBase() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        assertEquals(3, jugador.getDefensaBase());
    }

    @Test
    void getVelocidad() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        assertEquals(2, jugador.getVelocidad());
    }

    @Test
    void getAtaqueEquipamiento() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        assertEquals(0, jugador.getAtaqueEquipamiento());
    }

    @Test
    void setAtaqueEquipamiento() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        jugador.setAtaqueEquipamiento(5);
        assertEquals(5, jugador.getAtaqueEquipamiento());
    }

    @Test
    void getDefensaEquipamiento() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        assertEquals(0, jugador.getDefensaEquipamiento());
    }

    @Test
    void setDefensaEquipamiento() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        jugador.setDefensaEquipamiento(4);
        assertEquals(4, jugador.getDefensaEquipamiento());
    }

    @Test
    void getAtaqueTotal() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        jugador.setAtaqueEquipamiento(5);
        assertEquals(15, jugador.getAtaqueTotal());
    }

    @Test
    void getDefensaTotal() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        jugador.setDefensaEquipamiento(4);
        assertEquals(7, jugador.getDefensaTotal());
    }

    @Test
    void getInventario() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        assertNotNull(jugador.getInventario());
        assertEquals(0, jugador.getInventario().tamaño());
    }

    @Test
    void getHabitacionActualId() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        assertNull(jugador.getHabitacionActualId());
    }

    @Test
    void setHabitacionActualId() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        jugador.setHabitacionActualId("Entrada");
        assertEquals("Entrada", jugador.getHabitacionActualId());
    }

    @Test
    void getPosicionX() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        assertEquals(0, jugador.getPosicionX());
    }

    @Test
    void setPosicionX() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        jugador.setPosicionX(3);
        assertEquals(3, jugador.getPosicionX());
    }

    @Test
    void getPosicionY() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        assertEquals(0, jugador.getPosicionY());
    }

    @Test
    void setPosicionY() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        jugador.setPosicionY(4);
        assertEquals(4, jugador.getPosicionY());
    }

    @Test
    void isVivo() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        assertTrue(jugador.isVivo());
    }

    @Test
    void setVivo() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        jugador.setVivo(false);
        assertFalse(jugador.isVivo());
    }

    @Test
    void agregarObjetoInventario() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        Objeto objeto = new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10);

        jugador.agregarObjetoInventario(objeto);

        assertEquals(1, jugador.getInventario().tamaño());
        assertSame(objeto, jugador.getInventario().obtener(0));
    }

    @Test
    void eliminarObjetoInventario() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        Objeto objeto = new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10);
        jugador.agregarObjetoInventario(objeto);

        assertSame(objeto, jugador.eliminarObjetoInventario(0));
        assertEquals(0, jugador.getInventario().tamaño());
        assertNull(jugador.eliminarObjetoInventario(99));
    }

    @Test
    void usarObjetoInventario() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        Objeto pocion = new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10);
        jugador.agregarObjetoInventario(pocion);

        assertTrue(jugador.usarObjetoInventario(0));
        assertEquals(0, jugador.getInventario().tamaño());
        assertFalse(jugador.usarObjetoInventario(0));
    }

    @Test
    void equipoObjetoInventario() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        Objeto espada = new Objeto(Objeto.Tipo.ARMA, "Espada", "+4 ataque", 4);
        Objeto pocion = new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10);
        jugador.agregarObjetoInventario(espada);
        jugador.agregarObjetoInventario(pocion);

        assertTrue(jugador.equipoObjetoInventario(0));
        assertEquals(4, jugador.getAtaqueEquipamiento());
        assertFalse(jugador.equipoObjetoInventario(1));
        assertFalse(jugador.equipoObjetoInventario(99));
    }

    @Test
    void desequipoObjetoInventario() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        Objeto espada = new Objeto(Objeto.Tipo.ARMA, "Espada", "+4 ataque", 4);
        jugador.agregarObjetoInventario(espada);
        jugador.equipoObjetoInventario(0);

        assertTrue(jugador.desequipoObjetoInventario(0));
        assertEquals(0, jugador.getAtaqueEquipamiento());
        assertFalse(jugador.desequipoObjetoInventario(99));
    }

    @Test
    void esPosicionValida() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        assertTrue(jugador.esPosicionValida(0, 0));
        assertTrue(jugador.esPosicionValida(3, 5));
        assertFalse(jugador.esPosicionValida(-1, 0));
        assertFalse(jugador.esPosicionValida(0, -1));
    }

    @Test
    void recibirDano() {
        Jugador jugador = new Jugador(100, 10, 3, 2);

        int danoReal = jugador.recibirDano(13); // defensa 3 => daño 10
        assertEquals(10, danoReal);
        assertEquals(90, jugador.getVidaActual());
        assertTrue(jugador.isVivo());

        jugador.recibirDano(999);
        assertEquals(0, jugador.getVidaActual());
        assertFalse(jugador.isVivo());
    }

    @Test
    void testToString() {
        Jugador jugador = new Jugador(100, 10, 3, 2);
        jugador.setPosicionX(1);
        jugador.setPosicionY(2);

        String texto = jugador.toString();

        assertTrue(texto.contains("Jugador"));
        assertTrue(texto.contains("Vida"));
        assertTrue(texto.contains("Ataque"));
        assertTrue(texto.contains("Defensa"));
        assertTrue(texto.contains("(1,2)"));
    }
}