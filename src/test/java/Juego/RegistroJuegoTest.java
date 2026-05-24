package Juego;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistroJuegoTest {

    @Test
    void registrar() {
        RegistroJuego registro = new RegistroJuego();

        registro.registrar("Empieza la partida");

        assertEquals(1, registro.getEventos().tamaño());
        assertEquals("Empieza la partida", registro.getEventos().obtener(0));
    }

    @Test
    void getEventos() {
        RegistroJuego registro = new RegistroJuego();

        assertNotNull(registro.getEventos());
        assertEquals(0, registro.getEventos().tamaño());
    }

    @Test
    void comoTexto() {
        RegistroJuego registro = new RegistroJuego();
        registro.registrar("Primer evento");
        registro.registrar("Segundo evento");

        String texto = registro.comoTexto();

        assertTrue(texto.contains("1. Primer evento"));
        assertTrue(texto.contains("2. Segundo evento"));
    }
}