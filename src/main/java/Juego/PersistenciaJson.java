package Juego;

import Juego.listas.ListaSimplementeEnlazada;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** clase encargada de guardar y cargar una partida completa usando JSON. Convierte el estado del juego a JSON para poder guardarlo
 * en un archivo, y también reconstruye la partida leyendo ese archivo.
 */
public class PersistenciaJson {

    // objeto Gson que se usa para convertir a texto JSON
    // setPrettyPrinting() hace que el archivo se guarde bonito y legible.
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /** guarda el estado actual de la partida en un archivo JSON
     * @param motor motor del juego que contiene toda la partida actual
     * @param ruta ruta donde se guardará el archivo JSON
     * @throws IOException si no se puede guardar el archivo
     */
    public void guardarEstado(MotorJuego motor, Path ruta) throws IOException {

        // si no hay motor, no hay partida que guardar
        if (motor == null) {
            throw new IOException("No hay partida activa para guardar.");
        }

        // si la ruta tiene una carpeta padre, nos aseguramos de que exista
        // por ejemplo, si la ruta es saves/partida.json, crea la carpeta saves
        if (ruta.getParent() != null) {
            Files.createDirectories(ruta.getParent());
        }

        // creamos un JsonObject con todos los datos importantes del motor
        JsonObject estado = crearJsonDesdeMotor(motor);

        // convertimos el JsonObject a texto JSON usando Gson
        String json = gson.toJson(estado);

        // escribimos el JSON en el archivo usando UTF-8 para admitir tildes
        Files.writeString(ruta, json, StandardCharsets.UTF_8);

        // añadimos al registro del juego que la partida se ha guardado
        motor.getRegistro().registrar("Partida guardada en " + ruta.toAbsolutePath() + ".");
    }

    /** carga una partida guardada desde un archivo JSON
     * @param ruta ruta del archivo JSON que queremos leer
     * @return MotorJuego reconstruido con los datos del archivo
     * @throws IOException si no existe el archivo o el JSON no es válido
     */
    public MotorJuego cargarEstado(Path ruta) throws IOException {

        // si el archivo no existe, no se puede cargar nada
        if (!Files.exists(ruta)) {
            throw new IOException("No existe ninguna partida guardada todavía en " + ruta.toAbsolutePath());
        }

        // leemos todo el archivo JSON como texto
        String texto = Files.readString(ruta, StandardCharsets.UTF_8);

        // convertimos el texto leído en un JsonObject
        JsonObject estado = JsonParser.parseString(texto).getAsJsonObject();

        // comprobamos que el JSON tenga la estructura mínima necesaria
        validarEstado(estado);

        // reconstruimos el motor completo a partir del JSON
        MotorJuego motor = crearMotorDesdeJson(estado);

        // añadimos un mensaje al registro indicando que se cargó la partida
        motor.getRegistro().registrar("Estado cargado desde " + ruta.toAbsolutePath() + ".");

        // devolvemos la partida ya reconstruida
        return motor;
    }

    /** devuelve un ejemplo de configuración en formato JSON
     *
     * @return JSON de una partida demo
     */
    public String configuracionDemoJson() {

        // Creamos una partida demo, la pasamos a JsonObject y luego a texto JSON.
        return gson.toJson(crearJsonDesdeMotor(MotorJuego.crearPartidaDemo()));
    }

    /**
     * Comprueba que el archivo JSON tenga las partes básicas necesarias.
     *
     * @param estado objeto JSON completo de la partida
     * @throws IOException si falta alguna parte importante
     */
    private void validarEstado(JsonObject estado) throws IOException {

        // Si estado es null, el archivo no contiene una partida usable.
        if (estado == null) {
            throw new IOException("El archivo JSON no contiene una partida válida.");
        }

        // Comprobamos que exista el bloque jugador y que sea un objeto JSON.
        if (!estado.has("jugador") || !estado.get("jugador").isJsonObject()) {
            throw new IOException("El JSON no contiene los datos del jugador.");
        }

        // Comprobamos que exista el array habitaciones.
        if (!estado.has("habitaciones") || !estado.get("habitaciones").isJsonArray()) {
            throw new IOException("El JSON no contiene habitaciones.");
        }

        // Si el array habitaciones está vacío, no hay mapa que cargar.
        if (estado.getAsJsonArray("habitaciones").size() == 0) {
            throw new IOException("El JSON no contiene ninguna habitación.");
        }

        // Leemos el índice de la habitación actual.
        // Si no existe, usamos -1 para detectar error.
        int indice = getInt(estado, "habitacionActualIndice", -1);

        // El índice debe estar dentro del rango de habitaciones.
        if (indice < 0 || indice >= estado.getAsJsonArray("habitaciones").size()) {
            throw new IOException("El índice de la habitación actual no es válido.");
        }
    }

    /**
     * Crea un JsonObject con todo el estado del motor.
     *
     * @param motor motor actual del juego
     * @return objeto JSON con la partida completa
     */
    private JsonObject crearJsonDesdeMotor(MotorJuego motor) {

        // Este será el objeto JSON principal.
        JsonObject estado = new JsonObject();

        // Guardamos el índice de la habitación actual dentro del grafo.
        estado.addProperty("habitacionActualIndice", motor.getHabitacionActualIndice());

        // Guardamos cuántos turnos quedan.
        estado.addProperty("turnosRestantes", motor.getTurnosRestantes());

        // Guardamos si la partida ya está ganada.
        estado.addProperty("victoria", motor.isVictoria());

        // Guardamos si la partida ya está perdida.
        estado.addProperty("derrota", motor.isDerrota());

        // Guardamos si el jugador ya se movió en el turno actual.
        estado.addProperty("jugadorYaSeMovioEnEsteTurno", motor.isJugadorYaSeMovioEnEsteTurno());

        // Guardamos si el jugador ya realizó una acción en el turno actual.
        estado.addProperty("jugadorYaActuoEnEsteTurno", motor.isJugadorYaActuoEnEsteTurno());

        // Guardamos todos los datos del jugador.
        estado.add("jugador", crearJsonJugador(motor.getJugador()));

        // Guardamos todas las habitaciones del grafo.
        estado.add("habitaciones", crearJsonHabitaciones(motor.getMapa()));

        // Guardamos las conexiones entre habitaciones.
        estado.add("conexiones", crearJsonConexiones(motor.getMapa()));

        // Devolvemos el JSON completo.
        return estado;
    }

    /**
     * Convierte un jugador a JSON.
     *
     * @param jugador jugador actual
     * @return JsonObject con los datos del jugador
     */
    private JsonObject crearJsonJugador(Jugador jugador) {

        // JSON donde meteremos todos los datos del jugador.
        JsonObject json = new JsonObject();

        // Guardamos la vida máxima.
        json.addProperty("vidaMax", jugador.getVidaMax());

        // Guardamos la vida actual.
        json.addProperty("vidaActual", jugador.getVidaActual());

        // Guardamos el ataque base, sin equipamiento.
        json.addProperty("ataqueBase", jugador.getAtaqueBase());

        // Guardamos la defensa base, sin equipamiento.
        json.addProperty("defensaBase", jugador.getDefensaBase());

        // Guardamos la velocidad del jugador.
        json.addProperty("velocidad", jugador.getVelocidad());

        // Guardamos el bonus de ataque por equipamiento.
        json.addProperty("ataqueEquipamiento", jugador.getAtaqueEquipamiento());

        // Guardamos el bonus de defensa por equipamiento.
        json.addProperty("defensaEquipamiento", jugador.getDefensaEquipamiento());

        // Guardamos el id de la habitación donde está el jugador.
        json.addProperty("habitacionActualId", jugador.getHabitacionActualId());

        // Guardamos la fila del jugador.
        json.addProperty("posicionX", jugador.getPosicionX());

        // Guardamos la columna del jugador.
        json.addProperty("posicionY", jugador.getPosicionY());

        // Guardamos si el jugador sigue vivo.
        json.addProperty("vivo", jugador.isVivo());

        // Array JSON donde guardaremos los objetos del inventario.
        JsonArray inventario = new JsonArray();

        // Recorremos el inventario real del jugador.
        for (int i = 0; i < jugador.getInventario().tamaño(); i++) {

            // Convertimos cada objeto del inventario a JSON.
            inventario.add(crearJsonObjeto(jugador.getInventario().obtener(i)));
        }

        // Añadimos el inventario completo al JSON del jugador.
        json.add("inventario", inventario);

        // Devolvemos el jugador convertido a JSON.
        return json;
    }

    /**
     * Convierte todas las habitaciones del grafo a JSON.
     *
     * @param mapa grafo que contiene las habitaciones
     * @return array JSON con todas las habitaciones
     */
    private JsonArray crearJsonHabitaciones(Grafo mapa) {

        // Array donde guardaremos todas las habitaciones.
        JsonArray habitaciones = new JsonArray();

        // Recorremos todos los vértices del grafo.
        for (int i = 0; i < mapa.getSize(); i++) {

            // En cada vértice del grafo hay una habitación.
            Habitacion habitacion = (Habitacion) mapa.obtenerDatosVertice(i);

            // JSON de una habitación concreta.
            JsonObject jsonHab = new JsonObject();

            // Guardamos el identificador de la habitación.
            jsonHab.addProperty("id", habitacion.getId());

            // Guardamos el número de filas.
            jsonHab.addProperty("filas", habitacion.getFilas());

            // Guardamos el número de columnas.
            jsonHab.addProperty("columnas", habitacion.getColumnas());

            // Array donde guardaremos todas las celdas de esta habitación.
            JsonArray celdas = new JsonArray();

            // Recorremos todas las filas.
            for (int fila = 0; fila < habitacion.getFilas(); fila++) {

                // Recorremos todas las columnas.
                for (int columna = 0; columna < habitacion.getColumnas(); columna++) {

                    // Obtenemos la celda concreta.
                    Celda celda = habitacion.getCelda(fila, columna);

                    // JSON de una celda concreta.
                    JsonObject jsonCelda = new JsonObject();

                    // Guardamos la fila de la celda.
                    jsonCelda.addProperty("fila", fila);

                    // Guardamos la columna de la celda.
                    jsonCelda.addProperty("columna", columna);

                    // Guardamos el tipo de celda como texto.
                    jsonCelda.addProperty("tipo", celda.getTipo().name());

                    // Guardamos si la celda es accesible.
                    jsonCelda.addProperty("accesible", celda.isAccesible());

                    // Si la celda contiene un objeto, guardamos ese objeto.
                    if (celda.getContenido() instanceof Objeto) {
                        jsonCelda.add("objeto", crearJsonObjeto((Objeto) celda.getContenido()));

                        // Si la celda contiene un enemigo, guardamos ese enemigo.
                    } else if (celda.getContenido() instanceof Enemigo) {
                        jsonCelda.add("enemigo", crearJsonEnemigo((Enemigo) celda.getContenido()));

                        // Si la celda contiene un String, normalmente es el destino de una puerta.
                    } else if (celda.getContenido() instanceof String) {
                        jsonCelda.addProperty("destinoPuerta", (String) celda.getContenido());
                    }

                    // Añadimos esta celda al array de celdas.
                    celdas.add(jsonCelda);
                }
            }

            // Añadimos todas las celdas al JSON de la habitación.
            jsonHab.add("celdas", celdas);

            // Añadimos la habitación completa al array de habitaciones.
            habitaciones.add(jsonHab);
        }

        // Devolvemos todas las habitaciones.
        return habitaciones;
    }

    /**
     * Convierte las conexiones del grafo a JSON.
     *
     * @param mapa grafo del juego
     * @return array JSON con las conexiones entre habitaciones
     */
    private JsonArray crearJsonConexiones(Grafo mapa) {

        // Array donde guardaremos todas las conexiones.
        JsonArray conexiones = new JsonArray();

        // Recorremos cada vértice como origen.
        for (int origen = 0; origen < mapa.getSize(); origen++) {

            // Obtenemos los vecinos del origen.
            ListaSimplementeEnlazada<Integer> adyacentes = mapa.obtenerAdyacentes(origen);

            // Recorremos todos los destinos conectados al origen.
            for (int i = 0; i < adyacentes.tamaño(); i++) {

                // Índice de la habitación destino.
                int destino = adyacentes.obtener(i);

                // Como el grafo es no dirigido, la arista aparece dos veces:
                // origen-destino y destino-origen.
                // Para no duplicarla, solo guardamos cuando origen < destino.
                if (origen < destino) {

                    // JSON de una conexión concreta.
                    JsonObject conexion = new JsonObject();

                    // Guardamos el índice origen.
                    conexion.addProperty("origen", origen);

                    // Guardamos el índice destino.
                    conexion.addProperty("destino", destino);

                    // Guardamos el peso de la conexión.
                    conexion.addProperty("peso", mapa.obtenerPesoArista(origen, destino));

                    // Añadimos la conexión al array.
                    conexiones.add(conexion);
                }
            }
        }

        // Devolvemos todas las conexiones.
        return conexiones;
    }

    /**
     * Convierte un objeto del juego a JSON.
     *
     * @param objeto objeto que queremos guardar
     * @return JsonObject con los datos del objeto
     */
    private JsonObject crearJsonObjeto(Objeto objeto) {

        // JSON donde guardaremos el objeto.
        JsonObject json = new JsonObject();

        // Guardamos el tipo del objeto.
        json.addProperty("tipo", objeto.getTipo().name());

        // Guardamos el nombre.
        json.addProperty("nombre", objeto.getNombre());

        // Guardamos la descripción.
        json.addProperty("descripcion", objeto.getDescripcion());

        // Guardamos el valor del efecto.
        json.addProperty("valor", objeto.getValor());

        // Guardamos los usos restantes.
        json.addProperty("usosRestantes", objeto.getUsosRestantes());

        // Devolvemos el objeto convertido.
        return json;
    }

    /**
     * Convierte un enemigo a JSON.
     *
     * @param enemigo enemigo que queremos guardar
     * @return JsonObject con los datos del enemigo
     */
    private JsonObject crearJsonEnemigo(Enemigo enemigo) {

        // JSON donde guardaremos el enemigo.
        JsonObject json = new JsonObject();

        // Guardamos el tipo de enemigo.
        json.addProperty("tipo", enemigo.getTipo().name());

        // Guardamos la vida actual.
        json.addProperty("vidaActual", enemigo.getVidaActual());

        // Guardamos la fila del enemigo.
        json.addProperty("posicionX", enemigo.getPosicionX());

        // Guardamos la columna del enemigo.
        json.addProperty("posicionY", enemigo.getPosicionY());

        // Guardamos si está equipado.
        json.addProperty("equipado", enemigo.isEquipado());

        // Guardamos el daño extra de arma.
        json.addProperty("danoArma", enemigo.getDanoArma());

        // Guardamos la defensa extra de armadura.
        json.addProperty("defensaArmadura", enemigo.getDefensaArmadura());

        // Devolvemos el enemigo convertido.
        return json;
    }

    /**
     * Reconstruye un MotorJuego completo desde JSON.
     *
     * @param estado JSON completo de la partida
     * @return motor reconstruido
     * @throws IOException si alguna parte del JSON no puede cargarse
     */
    private MotorJuego crearMotorDesdeJson(JsonObject estado) throws IOException {

        // Creamos un grafo nuevo, no dirigido.
        Grafo mapa = new Grafo(false);

        // Obtenemos el array de habitaciones guardadas.
        JsonArray habitaciones = estado.getAsJsonArray("habitaciones");

        // Recorremos cada habitación guardada.
        for (JsonElement elemento : habitaciones) {

            // Convertimos cada JSON de habitación en una Habitacion real.
            mapa.agregarVertice(crearHabitacionDesdeJson(elemento.getAsJsonObject()));
        }

        // Obtenemos las conexiones guardadas.
        JsonArray conexiones = getArray(estado, "conexiones");

        // Recorremos las conexiones.
        for (JsonElement elemento : conexiones) {

            // Convertimos el elemento a JsonObject.
            JsonObject conexion = elemento.getAsJsonObject();

            // Añadimos la arista al grafo.
            mapa.agregarArista(
                    getInt(conexion, "origen", 0),
                    getInt(conexion, "destino", 0),
                    getDouble(conexion, "peso", 1.0)
            );
        }

        // Reconstruimos el jugador desde JSON.
        Jugador jugador = crearJugadorDesdeJson(estado.getAsJsonObject("jugador"));

        // Creamos el motor usando mapa, jugador, habitación actual y turnos.
        MotorJuego motor = new MotorJuego(
                mapa,
                jugador,
                getInt(estado, "habitacionActualIndice", 0),
                getInt(estado, "turnosRestantes", 30)
        );

        // Aplicamos los estados extra de la partida.
        motor.aplicarEstadoPartida(
                getInt(estado, "habitacionActualIndice", 0),
                getInt(estado, "turnosRestantes", 30),
                getBoolean(estado, "victoria", false),
                getBoolean(estado, "derrota", false),
                getBoolean(estado, "jugadorYaSeMovioEnEsteTurno", false),
                getBoolean(estado, "jugadorYaActuoEnEsteTurno", false)
        );

        // Restauramos el id de habitación actual del jugador.
        jugador.setHabitacionActualId(
                getString(
                        estado.getAsJsonObject("jugador"),
                        "habitacionActualId",
                        jugador.getHabitacionActualId()
                )
        );

        // Devolvemos el motor cargado.
        return motor;
    }

    /**
     * Reconstruye una habitación desde JSON.
     *
     * @param jsonHab JSON de la habitación
     * @return habitación reconstruida
     * @throws IOException si hay algún problema cargando la habitación
     */
    private Habitacion crearHabitacionDesdeJson(JsonObject jsonHab) throws IOException {

        // Creamos la habitación con id, filas y columnas guardadas.
        Habitacion habitacion = new Habitacion(
                getString(jsonHab, "id", "Habitacion"),
                getInt(jsonHab, "filas", 5),
                getInt(jsonHab, "columnas", 5)
        );

        // Obtenemos el array de celdas.
        JsonArray celdas = getArray(jsonHab, "celdas");

        // Recorremos todas las celdas guardadas.
        for (JsonElement elemento : celdas) {

            // Convertimos cada celda a JsonObject.
            JsonObject jsonCelda = elemento.getAsJsonObject();

            // Leemos fila y columna de la celda.
            int fila = getInt(jsonCelda, "fila", 0);
            int columna = getInt(jsonCelda, "columna", 0);

            // Leemos el tipo de celda en texto.
            String tipoTexto = getString(jsonCelda, "tipo", Celda.Tipo.VACIA.name());

            // Convertimos el texto al enum Celda.Tipo.
            Celda.Tipo tipo = Celda.Tipo.valueOf(tipoTexto);

            // Creamos una celda nueva con ese tipo.
            Celda celda = new Celda(tipo);

            // Restauramos si la celda era accesible.
            celda.setAccesible(getBoolean(jsonCelda, "accesible", true));

            // Según el tipo, restauramos el contenido.
            switch (tipo) {

                case OBJETO:
                    // Si la celda era de objeto, buscamos el JSON del objeto.
                    if (jsonCelda.has("objeto") && jsonCelda.get("objeto").isJsonObject()) {
                        celda.setContenido(crearObjetoDesdeJson(jsonCelda.getAsJsonObject("objeto")));
                    }
                    break;

                case ENEMIGO:
                    // Si la celda era de enemigo, reconstruimos el enemigo.
                    if (jsonCelda.has("enemigo") && jsonCelda.get("enemigo").isJsonObject()) {

                        // Creamos el enemigo desde JSON.
                        Enemigo enemigo = crearEnemigoDesdeJson(jsonCelda.getAsJsonObject("enemigo"));

                        // Aseguramos que su posición interna coincida con la celda.
                        enemigo.setPosicion(fila, columna);

                        // Metemos el enemigo dentro de la celda.
                        celda.setContenido(enemigo);
                    }
                    break;

                case PUERTA:
                    // Si la celda es puerta, su contenido es el id de destino.
                    celda.setContenido(getString(jsonCelda, "destinoPuerta", null));
                    break;

                default:
                    // Para vacía, trampa o salida normalmente no necesitamos contenido.
                    celda.setContenido(null);
                    break;
            }

            // Solo colocamos la celda si la posición existe dentro de la habitación.
            if (habitacion.esPosicionValida(fila, columna)) {
                habitacion.setCelda(fila, columna, celda);
            }
        }

        // Devolvemos la habitación reconstruida.
        return habitacion;
    }

    /**
     * Reconstruye el jugador desde JSON.
     *
     * @param json JSON del jugador
     * @return jugador reconstruido
     */
    private Jugador crearJugadorDesdeJson(JsonObject json) {

        // Creamos el jugador con sus estadísticas base.
        Jugador jugador = new Jugador(
                getInt(json, "vidaMax", 100),
                getInt(json, "ataqueBase", 10),
                getInt(json, "defensaBase", 3),
                getInt(json, "velocidad", 3)
        );

        // Restauramos la vida actual.
        jugador.setVidaActual(getInt(json, "vidaActual", jugador.getVidaActual()));

        // Restauramos el bonus de ataque.
        jugador.setAtaqueEquipamiento(getInt(json, "ataqueEquipamiento", 0));

        // Restauramos el bonus de defensa.
        jugador.setDefensaEquipamiento(getInt(json, "defensaEquipamiento", 0));

        // Restauramos el id de habitación actual.
        jugador.setHabitacionActualId(getString(json, "habitacionActualId", null));

        // Restauramos la fila.
        jugador.setPosicionX(getInt(json, "posicionX", 0));

        // Restauramos la columna.
        jugador.setPosicionY(getInt(json, "posicionY", 0));

        // Restauramos si está vivo.
        jugador.setVivo(getBoolean(json, "vivo", true));

        // Obtenemos el inventario guardado.
        JsonArray inventario = getArray(json, "inventario");

        // Recorremos los objetos del inventario.
        for (JsonElement elemento : inventario) {

            // Solo cargamos elementos que sean objetos JSON.
            if (elemento.isJsonObject()) {
                jugador.getInventario().insertarUltimo(
                        crearObjetoDesdeJson(elemento.getAsJsonObject())
                );
            }
        }

        // Devolvemos el jugador reconstruido.
        return jugador;
    }

    /**
     * Reconstruye un objeto desde JSON.
     */
    private Objeto crearObjetoDesdeJson(JsonObject json) {

        // Creamos el objeto con tipo, nombre, descripción y valor.
        Objeto objeto = new Objeto(
                Objeto.Tipo.valueOf(getString(json, "tipo", Objeto.Tipo.OBJETO_ESPECIAL.name())),
                getString(json, "nombre", "Objeto"),
                getString(json, "descripcion", ""),
                getInt(json, "valor", 0)
        );

        // Restauramos los usos restantes.
        objeto.setUsosRestantes(getInt(json, "usosRestantes", objeto.getUsosRestantes()));

        // Devolvemos el objeto reconstruido.
        return objeto;
    }

    /**
     * Reconstruye un enemigo desde JSON.
     */
    private Enemigo crearEnemigoDesdeJson(JsonObject json) {

        // Creamos el enemigo según su tipo.
        Enemigo enemigo = new Enemigo(
                Enemigo.Tipo.valueOf(getString(json, "tipo", Enemigo.Tipo.GOBLIN.name()))
        );

        // Restauramos su vida actual.
        enemigo.setVidaActual(getInt(json, "vidaActual", enemigo.getVidaActual()));

        // Restauramos su posición.
        enemigo.setPosicion(
                getInt(json, "posicionX", -1),
                getInt(json, "posicionY", -1)
        );

        // Restauramos si está equipado.
        enemigo.setEquipado(getBoolean(json, "equipado", false));

        // Restauramos daño extra.
        enemigo.setDanoArma(getInt(json, "danoArma", 0));

        // Restauramos defensa extra.
        enemigo.setDefensaArmadura(getInt(json, "defensaArmadura", 0));

        // Devolvemos el enemigo reconstruido.
        return enemigo;
    }

    /**
     * Lee un array JSON de forma segura.
     */
    private JsonArray getArray(JsonObject json, String clave) {

        // Si existe la clave y además es un array, lo devolvemos.
        if (json.has(clave) && json.get(clave).isJsonArray()) {
            return json.getAsJsonArray(clave);
        }

        // Si no existe o no es array, devolvemos un array vacío.
        return new JsonArray();
    }

    /**
     * Lee un String de forma segura.
     */
    private String getString(JsonObject json, String clave, String defecto) {

        // Si existe la clave y no es null, devolvemos su texto.
        if (json.has(clave) && !json.get(clave).isJsonNull()) {
            return json.get(clave).getAsString();
        }

        // Si no existe, devolvemos el valor por defecto.
        return defecto;
    }

    /**
     * Lee un int de forma segura.
     */
    private int getInt(JsonObject json, String clave, int defecto) {

        // Si existe la clave y no es null, devolvemos su valor entero.
        if (json.has(clave) && !json.get(clave).isJsonNull()) {
            return json.get(clave).getAsInt();
        }

        // Si no existe, devolvemos el valor por defecto.
        return defecto;
    }

    /**
     * Lee un double de forma segura.
     */
    private double getDouble(JsonObject json, String clave, double defecto) {

        // Si existe la clave y no es null, devolvemos su valor decimal.
        if (json.has(clave) && !json.get(clave).isJsonNull()) {
            return json.get(clave).getAsDouble();
        }

        // Si no existe, devolvemos el valor por defecto.
        return defecto;
    }

    /**
     * Lee un boolean de forma segura.
     */
    private boolean getBoolean(JsonObject json, String clave, boolean defecto) {

        // Si existe la clave y no es null, devolvemos true o false.
        if (json.has(clave) && !json.get(clave).isJsonNull()) {
            return json.get(clave).getAsBoolean();
        }

        // Si no existe, devolvemos el valor por defecto.
        return defecto;
    }
}


