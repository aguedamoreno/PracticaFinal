package Juego;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemigoTest {

    @Test
    void getTipo() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.GOBLIN);
        assertEquals(Enemigo.Tipo.GOBLIN, enemigo.getTipo());
    }

    @Test
    void getNombre() {
        assertEquals("Goblin", new Enemigo(Enemigo.Tipo.GOBLIN).getNombre());
        assertEquals("Orco", new Enemigo(Enemigo.Tipo.ORCO).getNombre());
    }

    @Test
    void getVidaMax() {
        assertEquals(20, new Enemigo(Enemigo.Tipo.GOBLIN).getVidaMax());
        assertEquals(100, new Enemigo(Enemigo.Tipo.DRAGON).getVidaMax());
    }

    @Test
    void getVidaActual() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.ORCO);
        assertEquals(enemigo.getVidaMax(), enemigo.getVidaActual());
    }

    @Test
    void setVidaActual() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.GOBLIN);
        enemigo.setVidaActual(10);
        assertEquals(10, enemigo.getVidaActual());

        enemigo.setVidaActual(-50);
        assertEquals(0, enemigo.getVidaActual());

        enemigo.setVidaActual(999);
        assertEquals(enemigo.getVidaMax(), enemigo.getVidaActual());
    }

    @Test
    void getAtaque() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.GOBLIN);
        assertEquals(5, enemigo.getAtaque());

        enemigo.setDanoArma(4);
        enemigo.setEquipado(true);
        assertEquals(9, enemigo.getAtaque());
    }

    @Test
    void getDefensa() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.ORCO);
        assertEquals(4, enemigo.getDefensa());

        enemigo.setDefensaArmadura(3);
        enemigo.setEquipado(true);
        assertEquals(7, enemigo.getDefensa());
    }

    @Test
    void getVelocidad() {
        assertEquals(2, new Enemigo(Enemigo.Tipo.GOBLIN).getVelocidad());
        assertEquals(3, new Enemigo(Enemigo.Tipo.ESQUELETO).getVelocidad());
    }

    @Test
    void getPosicionX() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.LADRON);
        assertEquals(-1, enemigo.getPosicionX());
    }

    @Test
    void getPosicionY() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.LADRON);
        assertEquals(-1, enemigo.getPosicionY());
    }

    @Test
    void setPosicion() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.LADRON);
        enemigo.setPosicion(2, 4);
        assertEquals(2, enemigo.getPosicionX());
        assertEquals(4, enemigo.getPosicionY());
    }

    @Test
    void isEquipado() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.GOBLIN);
        assertFalse(enemigo.isEquipado());
    }

    @Test
    void setEquipado() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.GOBLIN);
        enemigo.setEquipado(true);
        assertTrue(enemigo.isEquipado());
    }

    @Test
    void getDanoArma() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.GOBLIN);
        assertEquals(0, enemigo.getDanoArma());
    }

    @Test
    void setDanoArma() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.GOBLIN);
        enemigo.setDanoArma(6);
        assertEquals(6, enemigo.getDanoArma());
    }

    @Test
    void getDefensaArmadura() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.ORCO);
        assertEquals(0, enemigo.getDefensaArmadura());
    }

    @Test
    void setDefensaArmadura() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.ORCO);
        enemigo.setDefensaArmadura(5);
        assertEquals(5, enemigo.getDefensaArmadura());
    }

    @Test
    void estaVivo() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.GOBLIN);
        assertTrue(enemigo.estaVivo());

        enemigo.setVidaActual(0);
        assertFalse(enemigo.estaVivo());
    }

    @Test
    void recibirDano() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.GOBLIN); // defensa 2, vida 20
        enemigo.recibirDano(7); // daño real 5
        assertEquals(15, enemigo.getVidaActual());

        enemigo.recibirDano(1); // daño real 0
        assertEquals(15, enemigo.getVidaActual());
    }

    @Test
    void curar() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.GOBLIN);
        enemigo.setVidaActual(5);
        enemigo.curar(10);
        assertEquals(15, enemigo.getVidaActual());

        enemigo.curar(999);
        assertEquals(enemigo.getVidaMax(), enemigo.getVidaActual());
    }

    @Test
    void testToString() {
        Enemigo enemigo = new Enemigo(Enemigo.Tipo.GOBLIN);
        enemigo.setPosicion(1, 2);
        String texto = enemigo.toString();

        assertTrue(texto.contains("Goblin"));
        assertTrue(texto.contains("Vida"));
        assertTrue(texto.contains("Ataque"));
        assertTrue(texto.contains("Defensa"));
        assertTrue(texto.contains("(1,2)"));
    }
}