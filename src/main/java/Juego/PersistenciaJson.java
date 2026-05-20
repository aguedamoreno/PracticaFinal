package Juego;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PersistenciaJson {
    public void guardarEstado(MotorJuego motor, Path ruta) throws IOException {
        StringBuilder sb = new StringBuilder();
        Jugador jugador = motor.getJugador();
        sb.append("{\n");
        sb.append("  \"habitacionActual\": \"").append(escapar(jugador.getHabitacionActualId())).append("\",\n");
        sb.append("  \"habitacionIndice\": ").append(motor.getHabitacionActualIndice()).append(",\n");
        sb.append("  \"turnosRestantes\": ").append(motor.getTurnosRestantes()).append(",\n");
        sb.append("  \"jugador\": {\n");
        sb.append("    \"vidaActual\": ").append(jugador.getVidaActual()).append(",\n");
        sb.append("    \"posicionX\": ").append(jugador.getPosicionX()).append(",\n");
        sb.append("    \"posicionY\": ").append(jugador.getPosicionY()).append(",\n");
        sb.append("    \"inventario\": [");
        for (int i = 0; i < jugador.getInventario().tamaño(); i++) {
            Objeto objeto = jugador.getInventario().obtener(i);
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("{\"tipo\": \"").append(objeto.getTipo()).append("\", \"nombre\": \"")
                    .append(escapar(objeto.getNombre())).append("\", \"valor\": ").append(objeto.getValor()).append("}");
        }
        sb.append("]\n");
        sb.append("  },\n");
        sb.append("  \"victoria\": ").append(motor.isVictoria()).append(",\n");
        sb.append("  \"derrota\": ").append(motor.isDerrota()).append("\n");
        sb.append("}\n");
        Files.writeString(ruta, sb.toString(), StandardCharsets.UTF_8);
    }

    public MotorJuego cargarEstado(Path ruta) throws IOException {
        String json = Files.readString(ruta, StandardCharsets.UTF_8);
        MotorJuego motor = MotorJuego.crearPartidaDemo();
        Jugador jugador = motor.getJugador();
        jugador.setVidaActual(leerEntero(json, "vidaActual", jugador.getVidaActual()));
        jugador.setPosicionX(leerEntero(json, "posicionX", jugador.getPosicionX()));
        jugador.setPosicionY(leerEntero(json, "posicionY", jugador.getPosicionY()));
        motor.getRegistro().registrar("Estado cargado desde " + ruta + ".");
        return motor;
    }

    public String configuracionDemoJson() {
        return "{\n" +
                "  \"habitaciones\": [\"Entrada\", \"Sala\", \"Salida\"],\n" +
                "  \"grafo\": [[\"Entrada\", \"Sala\"], [\"Sala\", \"Salida\"]],\n" +
                "  \"jugadorInicial\": {\"habitacion\": \"Entrada\", \"fila\": 0, \"columna\": 0},\n" +
                "  \"objetivo\": \"Llegar a una celda SALIDA\"\n" +
                "}\n";
    }

    private int leerEntero(String json, String clave, int defecto) {
        String patron = "\"" + clave + "\"";
        int indice = json.indexOf(patron);
        if (indice == -1) {
            return defecto;
        }
        int dosPuntos = json.indexOf(':', indice);
        if (dosPuntos == -1) {
            return defecto;
        }
        int inicio = dosPuntos + 1;
        while (inicio < json.length() && Character.isWhitespace(json.charAt(inicio))) {
            inicio++;
        }
        int fin = inicio;
        while (fin < json.length() && (Character.isDigit(json.charAt(fin)) || json.charAt(fin) == '-')) {
            fin++;
        }
        try {
            return Integer.parseInt(json.substring(inicio, fin));
        } catch (NumberFormatException ex) {
            return defecto;
        }
    }

    private String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
