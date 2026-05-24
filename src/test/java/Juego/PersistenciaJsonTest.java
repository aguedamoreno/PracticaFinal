package Juego;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PersistenciaJsonTest {

    @Test
    void guardarEstado() throws IOException {
        PersistenciaJson persistencia = new PersistenciaJson();
        MotorJuego motor = MotorJuego.crearPartidaDemo();
        Path ruta = Files.createTempFile("partida", ".json");

        persistencia.guardarEstado(motor, ruta);
        String contenido = Files.readString(ruta);

        assertTrue(Files.exists(ruta));
        assertTrue(contenido.contains("habitacionActual"));
        assertTrue(contenido.contains("turnosRestantes"));
        assertTrue(contenido.contains("jugador"));
        assertTrue(contenido.contains("vidaActual"));
    }

    @Test
    void cargarEstado() throws IOException {
        PersistenciaJson persistencia = new PersistenciaJson();
        Path ruta = Files.createTempFile("partida-cargada", ".json");
        String json = "{\n" +
                "  \"vidaActual\": 55,\n" +
                "  \"posicionX\": 2,\n" +
                "  \"posicionY\": 3\n" +
                "}";
        Files.writeString(ruta, json);

        MotorJuego motor = persistencia.cargarEstado(ruta);

        assertEquals(55, motor.getJugador().getVidaActual());
        assertEquals(2, motor.getJugador().getPosicionX());
        assertEquals(3, motor.getJugador().getPosicionY());
        assertTrue(motor.getRegistro().comoTexto().contains("Estado cargado"));
    }

    @Test
    void configuracionDemoJson() {
        PersistenciaJson persistencia = new PersistenciaJson();
        String json = persistencia.configuracionDemoJson();

        assertTrue(json.contains("habitaciones"));
        assertTrue(json.contains("Entrada"));
        assertTrue(json.contains("Sala"));
        assertTrue(json.contains("Salida"));
        assertTrue(json.contains("objetivo"));
    }
}