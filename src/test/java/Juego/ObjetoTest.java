package Juego;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjetoTest {

    @Test
    void getTipo() {
        Objeto objeto = new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10);
        assertEquals(Objeto.Tipo.POCION_VIDA, objeto.getTipo());
    }

    @Test
    void getNombre() {
        Objeto objeto = new Objeto(Objeto.Tipo.ARMA, "Espada", "+4 ataque", 4);
        assertEquals("Espada", objeto.getNombre());
    }

    @Test
    void getDescripcion() {
        Objeto objeto = new Objeto(Objeto.Tipo.ESCUDO, "Escudo", "+3 defensa", 3);
        assertEquals("+3 defensa", objeto.getDescripcion());
    }

    @Test
    void getValor() {
        Objeto objeto = new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+15 vida", 15);
        assertEquals(15, objeto.getValor());
    }

    @Test
    void isFungible() {
        assertTrue(new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10).isFungible());
        assertFalse(new Objeto(Objeto.Tipo.ARMA, "Espada", "+4 ataque", 4).isFungible());
    }

    @Test
    void isEquipable() {
        assertTrue(new Objeto(Objeto.Tipo.ARMA, "Espada", "+4 ataque", 4).isEquipable());
        assertTrue(new Objeto(Objeto.Tipo.ESCUDO, "Escudo", "+3 defensa", 3).isEquipable());
        assertFalse(new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10).isEquipable());
    }

    @Test
    void isMejoraPasiva() {
        Objeto objeto = new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10);
        assertFalse(objeto.isMejoraPasiva());
    }

    @Test
    void getUsosMaximos() {
        assertEquals(1, new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10).getUsosMaximos());
        assertEquals(-1, new Objeto(Objeto.Tipo.ARMA, "Espada", "+4 ataque", 4).getUsosMaximos());
    }

    @Test
    void getUsosRestantes() {
        assertEquals(1, new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10).getUsosRestantes());
        assertEquals(-1, new Objeto(Objeto.Tipo.ARMA, "Espada", "+4 ataque", 4).getUsosRestantes());
    }

    @Test
    void setUsosRestantes() {
        Objeto objeto = new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10);
        objeto.setUsosRestantes(0);
        assertEquals(0, objeto.getUsosRestantes());
    }

    @Test
    void getDuracionTurnos() {
        assertEquals(-1, new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10).getDuracionTurnos());
        assertEquals(3, new Objeto(Objeto.Tipo.POCION_FUERZA, "Fuerza", "+5 ataque", 5).getDuracionTurnos());
    }

    @Test
    void usar() {
        Objeto pocion = new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10);
        assertTrue(pocion.usar());
        assertEquals(0, pocion.getUsosRestantes());
        assertFalse(pocion.usar());

        Objeto espada = new Objeto(Objeto.Tipo.ARMA, "Espada", "+4 ataque", 4);
        assertTrue(espada.usar());
        assertTrue(espada.usar());
    }

    @Test
    void puedeUsarse() {
        Objeto pocion = new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10);
        assertTrue(pocion.puedeUsarse());
        pocion.usar();
        assertFalse(pocion.puedeUsarse());

        Objeto espada = new Objeto(Objeto.Tipo.ARMA, "Espada", "+4 ataque", 4);
        assertTrue(espada.puedeUsarse());
    }

    @Test
    void testToString() {
        Objeto objeto = new Objeto(Objeto.Tipo.POCION_VIDA, "Poción", "+10 vida", 10);
        assertEquals("Poción (+10 vida)", objeto.toString());
    }
}