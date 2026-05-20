package Juego;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MotorJuegoTest {
    @Test
    void jugadorPuedeMoverseDentroDeRango() throws JuegoException {
        MotorJuego motor = MotorJuego.crearPartidaDemo();

        motor.moverJugador(1, 0);

        assertEquals(1, motor.getJugador().getPosicionX());
        assertEquals(0, motor.getJugador().getPosicionY());
        assertEquals(29, motor.getTurnosRestantes());
    }

    @Test
    void movimientoFueraDeRangoLanzaExcepcion() {
        MotorJuego motor = MotorJuego.crearPartidaDemo();

        assertThrows(JuegoException.class, () -> motor.moverJugador(4, 4));
    }

    @Test
    void persistenciaGuardaEstadoJson() throws IOException {
        MotorJuego motor = MotorJuego.crearPartidaDemo();
        Path temporal = Files.createTempFile("partida", ".json");

        new PersistenciaJson().guardarEstado(motor, temporal);

        String json = Files.readString(temporal);
        assertTrue(json.contains("habitacionActual"));
        assertTrue(json.contains("jugador"));
        Files.deleteIfExists(temporal);
    }
}
