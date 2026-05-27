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

/** Clase encargada de guardar y cargar una partida completa usando JSON. Convierte el estado del juego a JSON para poder guardarlo
 * en un archivo, y también reconstruye la partida leyendo ese archivo
 */
public class PersistenciaJson {

    // objeto Gson que se usa para convertir a texto JSON
    // setPrettyPrinting() hace que el archivo se guarde bonito y legible
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /** Guarda el estado actual de la partida en un archivo JSON
     */
    public void guardarEstado(MotorJuego motor, Path ruta) throws IOException {

        // si no hay motor, no hay partida que guardar
        if (motor == null) {
            throw new IOException("No hay partida activa para guardar.");
        }

        // si la ruta tiene una carpeta padre, nos aseguramos de que exista; por ejemplo, si la ruta es saves/partida.json, crea la carpeta saves
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

    /** Carga una partida guardada desde un archivo JSON
     */
    public MotorJuego cargarEstado(Path ruta) throws IOException {

        // si el archivo no existe, no se puede cargar nada
        if (!Files.exists(ruta)) {
            throw new IOException("No existe ninguna partida guardada todavía en " + ruta.toAbsolutePath());
        }

        // leemos el archivo JSON como texto
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

    /** Devuelve un ejemplo de configuración en formato JSON
     */
    public String configuracionDemoJson() {

        // creamos una partida demo, la pasamos a JsonObject y luego a texto JSON
        return gson.toJson(crearJsonDesdeMotor(MotorJuego.crearPartidaDemo()));
    }

    /**
     * Comprueba que el archivo JSON tenga las partes básicas necesarias.
     */
    private void validarEstado(JsonObject estado) throws IOException {

        // si estado es null, el archivo no contiene una partida usable
        if (estado == null) {
            throw new IOException("El archivo JSON no contiene una partida válida.");
        }

        // comprobamos que exista el bloque jugador y que sea un objeto JSON
        if (!estado.has("jugador") || !estado.get("jugador").isJsonObject()) {
            throw new IOException("El JSON no contiene los datos del jugador.");
        }

        // comprobamos que exista el array habitaciones
        if (!estado.has("habitaciones") || !estado.get("habitaciones").isJsonArray()) {
            throw new IOException("El JSON no contiene habitaciones.");
        }

        // si el array habitaciones está vacío, no hay mapa que cargar
        if (estado.getAsJsonArray("habitaciones").size() == 0) {
            throw new IOException("El JSON no contiene ninguna habitación.");
        }

        // leemos el índice de la habitación actual
        // si no existe, usamos -1 para detectar error
        int indice = getInt(estado, "habitacionActualIndice", -1);

        // el índice debe estar dentro del rango de habitaciones
        if (indice < 0 || indice >= estado.getAsJsonArray("habitaciones").size()) {
            throw new IOException("El índice de la habitación actual no es válido.");
        }
    }

    /** Crea un JsonObject con el estado del motor
     */
    private JsonObject crearJsonDesdeMotor(MotorJuego motor) {

        // este será el objeto JSON principal
        JsonObject estado = new JsonObject();

        // guardamos el índice de la habitación actual dentro del grafo
        estado.addProperty("habitacionActualIndice", motor.getHabitacionActualIndice());

        // guardamos cuántos turnos quedan
        estado.addProperty("turnosRestantes", motor.getTurnosRestantes());

        // guardamos si la partida ya está ganada o perdida
        estado.addProperty("victoria", motor.isVictoria());
        estado.addProperty("derrota", motor.isDerrota());

        // guardamos si el jugador ya se movió en el turno actual o si realizó una acción
        estado.addProperty("jugadorYaSeMovioEnEsteTurno", motor.isJugadorYaSeMovioEnEsteTurno());
        estado.addProperty("jugadorYaActuoEnEsteTurno", motor.isJugadorYaActuoEnEsteTurno());

        // guardamos todos los datos del jugador
        estado.add("jugador", crearJsonJugador(motor.getJugador()));

        // guardamos todas las habitaciones del grafo
        estado.add("habitaciones", crearJsonHabitaciones(motor.getMapa()));

        // guardamos las conexiones entre habitaciones
        estado.add("conexiones", crearJsonConexiones(motor.getMapa()));

        // devolvemos el JSON completo
        return estado;
    }

    /** Convierte un jugador a JSON
     */
    private JsonObject crearJsonJugador(Jugador jugador) {

        // JSON donde meteremos todos los datos del jugador
        JsonObject json = new JsonObject();

        // guardamos la vida máxima y la vida actual
        json.addProperty("vidaMax", jugador.getVidaMax());
        json.addProperty("vidaActual", jugador.getVidaActual());

        // Guardamos el ataque base y la defensa base, sin equipamiento.
        json.addProperty("ataqueBase", jugador.getAtaqueBase());
        json.addProperty("defensaBase", jugador.getDefensaBase());

        // guardamos la velocidad del jugador
        json.addProperty("velocidad", jugador.getVelocidad());

        // guardamos el bonus de ataque y defensa por equipamiento
        json.addProperty("ataqueEquipamiento", jugador.getAtaqueEquipamiento());
        json.addProperty("defensaEquipamiento", jugador.getDefensaEquipamiento());

        // guardamos el id de la habitación donde está el jugador
        json.addProperty("habitacionActualId", jugador.getHabitacionActualId());

        // guardamos la fila y columna del jugador
        json.addProperty("posicionX", jugador.getPosicionX());
        json.addProperty("posicionY", jugador.getPosicionY());

        // guardamos si el jugador sigue vivo
        json.addProperty("vivo", jugador.isVivo());

        // Array JSON donde guardaremos los objetos del inventario
        JsonArray inventario = new JsonArray();

        // recorremos el inventario real del jugador
        for (int i = 0; i < jugador.getInventario().tamaño(); i++) {

            // y convertimos cada objeto del inventario a JSON
            inventario.add(crearJsonObjeto(jugador.getInventario().obtener(i)));
        }

        // añadimos el inventario completo al JSON del jugador
        json.add("inventario", inventario);

        // sevolvemos el jugador convertido a JSON
        return json;
    }

    /** Convierte todas las habitaciones del grafo a JSON
     */
    private JsonArray crearJsonHabitaciones(Grafo mapa) {

        // Array donde guardaremos todas las habitaciones
        JsonArray habitaciones = new JsonArray();

        // recorremos todos los vértices del grafo
        for (int i = 0; i < mapa.getSize(); i++) {

            // en cada vértice del grafo hay una habitación
            Habitacion habitacion = (Habitacion) mapa.obtenerDatosVertice(i);

            // JSON de una habitación concreta
            JsonObject jsonHab = new JsonObject();

            // guardamos el identificador de la habitación
            jsonHab.addProperty("id", habitacion.getId());

            // Guardamos el número de filas y columnas
            jsonHab.addProperty("filas", habitacion.getFilas());
            jsonHab.addProperty("columnas", habitacion.getColumnas());

            // array donde guardaremos todas las celdas de esta habitación
            JsonArray celdas = new JsonArray();

            // recorremos todas las filas y las columnas y obtenemos la celda concreta
            for (int fila = 0; fila < habitacion.getFilas(); fila++) {
                for (int columna = 0; columna < habitacion.getColumnas(); columna++) {
                    Celda celda = habitacion.getCelda(fila, columna);

                    // JSON de una celda concreta
                    JsonObject jsonCelda = new JsonObject();

                    // guardamos la fila y columna de la celda
                    jsonCelda.addProperty("fila", fila);
                    jsonCelda.addProperty("columna", columna);

                    // guardamos el tipo de celda como texto
                    jsonCelda.addProperty("tipo", celda.getTipo().name());

                    // guardamos si la celda es accesible
                    jsonCelda.addProperty("accesible", celda.isAccesible());

                    // si la celda contiene un objeto, un enemigo, lo guardamos; y si contiene un String, suele ser el destino de una puerta
                    if (celda.getContenido() instanceof Objeto) {
                        jsonCelda.add("objeto", crearJsonObjeto((Objeto) celda.getContenido()));
                    } else if (celda.getContenido() instanceof Enemigo) {
                        jsonCelda.add("enemigo", crearJsonEnemigo((Enemigo) celda.getContenido()));
                    } else if (celda.getContenido() instanceof String) {
                        jsonCelda.addProperty("destinoPuerta", (String) celda.getContenido());
                    }

                    // añadimos esta celda al array de celdas
                    celdas.add(jsonCelda);
                }
            }

            // añadimos todas las celdas al JSON de la habitación
            jsonHab.add("celdas", celdas);

            // y añadimos la habitación completa al array de habitaciones
            habitaciones.add(jsonHab);
        }

        // devolvemos todas las habitaciones
        return habitaciones;
    }

    /** Convierte las conexiones del grafo a JSON
     */
    private JsonArray crearJsonConexiones(Grafo mapa) {

        // array donde guardaremos todas las conexiones
        JsonArray conexiones = new JsonArray();

        // recorremos cada vértice como origen
        for (int origen = 0; origen < mapa.getSize(); origen++) {

            // obtenemos los vecinos del origen
            ListaSimplementeEnlazada<Integer> adyacentes = mapa.obtenerAdyacentes(origen);

            // recorremos todos los destinos conectados al origen
            for (int i = 0; i < adyacentes.tamaño(); i++) {

                // índice de la habitación destino
                int destino = adyacentes.obtener(i);

                // como el grafo es no dirigido, la arista aparece dos veces: origen-destino y destino-origen, y para no duplicarla, solo guardamos cuando origen < destino.
                if (origen < destino) {

                    // JSON de una conexión concreta
                    JsonObject conexion = new JsonObject();

                    // guardamos el índice origen y destino
                    conexion.addProperty("origen", origen);
                    conexion.addProperty("destino", destino);

                    // guardamos el peso de la conexión
                    conexion.addProperty("peso", mapa.obtenerPesoArista(origen, destino));

                    // añadimos la conexión al array
                    conexiones.add(conexion);
                }
            }
        }

        // devolvemos todas las conexiones
        return conexiones;
    }

    /** Convierte un objeto del juego a JSON
     */
    private JsonObject crearJsonObjeto(Objeto objeto) {

        // JSON donde guardaremos el objeto
        JsonObject json = new JsonObject();

        // guardamos el tipo del objeto, el nombre, la descripción, el valor del efecto y los usos restantes
        json.addProperty("tipo", objeto.getTipo().name());
        json.addProperty("nombre", objeto.getNombre());
        json.addProperty("descripcion", objeto.getDescripcion());
        json.addProperty("valor", objeto.getValor());
        json.addProperty("usosRestantes", objeto.getUsosRestantes());

        // devolvemos el objeto convertido
        return json;
    }

    /** Convierte un enemigo a JSON
     */
    private JsonObject crearJsonEnemigo(Enemigo enemigo) {

        // JSON donde guardaremos el enemigo
        JsonObject json = new JsonObject();

        // guardamos el tipo de enemigo, la vida actual, y la fila y columna del enemigo
        json.addProperty("tipo", enemigo.getTipo().name());
        json.addProperty("vidaActual", enemigo.getVidaActual());
        json.addProperty("posicionX", enemigo.getPosicionX());
        json.addProperty("posicionY", enemigo.getPosicionY());

        // guardamos si está equipado
        json.addProperty("equipado", enemigo.isEquipado());

        // guardamos el daño extra de arma y la defensa extra de armadura
        json.addProperty("danoArma", enemigo.getDanoArma());
        json.addProperty("defensaArmadura", enemigo.getDefensaArmadura());

        // devolvemos el enemigo convertido
        return json;
    }

    /** Reconstruye un MotorJuego completo desde JSON.
     */
    private MotorJuego crearMotorDesdeJson(JsonObject estado) throws IOException {

        // creamos un grafo nuevo, no dirigido
        Grafo mapa = new Grafo(false);

        // obtenemos el array de habitaciones guardadas
        JsonArray habitaciones = estado.getAsJsonArray("habitaciones");

        // recorremos cada habitación guardada, y convertimos cada JSON de habitación en una habitación real
        for (JsonElement elemento : habitaciones) {
            mapa.agregarVertice(crearHabitacionDesdeJson(elemento.getAsJsonObject()));
        }

        // obtenemos las conexiones guardadas
        JsonArray conexiones = getArray(estado, "conexiones");

        // recorremos las conexiones y convertimos el elemento a JsonObject
        for (JsonElement elemento : conexiones) {
            JsonObject conexion = elemento.getAsJsonObject();

            // añadimos la arista al grafo
            mapa.agregarArista(
                    getInt(conexion, "origen", 0),
                    getInt(conexion, "destino", 0),
                    getDouble(conexion, "peso", 1.0)
            );
        }

        // reconstruimos el jugador desde JSON
        Jugador jugador = crearJugadorDesdeJson(estado.getAsJsonObject("jugador"));

        // creamos el motor usando mapa, jugador, habitación actual y turnos
        MotorJuego motor = new MotorJuego(
                mapa,
                jugador,
                getInt(estado, "habitacionActualIndice", 0),
                getInt(estado, "turnosRestantes", 30)
        );

        // aplicamos los estados extra de la partida
        motor.aplicarEstadoPartida(
                getInt(estado, "habitacionActualIndice", 0),
                getInt(estado, "turnosRestantes", 30),
                getBoolean(estado, "victoria", false),
                getBoolean(estado, "derrota", false),
                getBoolean(estado, "jugadorYaSeMovioEnEsteTurno", false),
                getBoolean(estado, "jugadorYaActuoEnEsteTurno", false)
        );

        // restauramos el id de habitación actual del jugador
        jugador.setHabitacionActualId(
                getString(
                        estado.getAsJsonObject("jugador"),
                        "habitacionActualId",
                        jugador.getHabitacionActualId()
                )
        );

        // devolvemos el motor cargado
        return motor;
    }

    /** Reconstruye una habitación desde JSON.
     */
    private Habitacion crearHabitacionDesdeJson(JsonObject jsonHab) throws IOException {

        // creamos la habitación con id, filas y columnas guardadas
        Habitacion habitacion = new Habitacion(
                getString(jsonHab, "id", "Habitacion"),
                getInt(jsonHab, "filas", 5),
                getInt(jsonHab, "columnas", 5)
        );

        // obtenemos el array de celdas
        JsonArray celdas = getArray(jsonHab, "celdas");

        // recorremos todas las celdas guardadas
        for (JsonElement elemento : celdas) {

            // convertimos cada celda a JsonObject
            JsonObject jsonCelda = elemento.getAsJsonObject();

            // leemos fila y columna de la celda
            int fila = getInt(jsonCelda, "fila", 0);
            int columna = getInt(jsonCelda, "columna", 0);

            // leemos el tipo de celda en texto
            String tipoTexto = getString(jsonCelda, "tipo", Celda.Tipo.VACIA.name());

            // convertimos el texto al enum Celda.Tipo
            Celda.Tipo tipo = Celda.Tipo.valueOf(tipoTexto);

            // creamos una celda nueva con ese tipo
            Celda celda = new Celda(tipo);

            // restauramos si la celda era accesible
            celda.setAccesible(getBoolean(jsonCelda, "accesible", true));

            // según el tipo, restauramos el contenido
            switch (tipo) {

                case OBJETO:
                    // si la celda era de objeto, buscamos el JSON del objeto
                    if (jsonCelda.has("objeto") && jsonCelda.get("objeto").isJsonObject()) {
                        celda.setContenido(crearObjetoDesdeJson(jsonCelda.getAsJsonObject("objeto")));
                    }
                    break;

                case ENEMIGO:
                    // si la celda era de enemigo, reconstruimos el enemigo
                    if (jsonCelda.has("enemigo") && jsonCelda.get("enemigo").isJsonObject()) {

                        // creamos el enemigo desde JSON
                        Enemigo enemigo = crearEnemigoDesdeJson(jsonCelda.getAsJsonObject("enemigo"));

                        // aseguramos que su posición interna coincida con la celda
                        enemigo.setPosicion(fila, columna);

                        // metemos el enemigo dentro de la celda
                        celda.setContenido(enemigo);
                    }
                    break;

                case PUERTA:
                    // si la celda es puerta, su contenido es el id de destino
                    celda.setContenido(getString(jsonCelda, "destinoPuerta", null));
                    break;

                default:
                    // para vacía, trampa o salida normalmente no necesitamos contenido
                    celda.setContenido(null);
                    break;
            }

            // solo colocamos la celda si la posición existe dentro de la habitación
            if (habitacion.esPosicionValida(fila, columna)) {
                habitacion.setCelda(fila, columna, celda);
            }
        }

        // devolvemos la habitación reconstruida
        return habitacion;
    }

    /** Reconstruye el jugador desde JSON.
     */
    private Jugador crearJugadorDesdeJson(JsonObject json) {

        // creamos el jugador con sus estadísticas base
        Jugador jugador = new Jugador(
                getInt(json, "vidaMax", 100),
                getInt(json, "ataqueBase", 10),
                getInt(json, "defensaBase", 3),
                getInt(json, "velocidad", 3)
        );

        // restauramos la vida actual
        jugador.setVidaActual(getInt(json, "vidaActual", jugador.getVidaActual()));

        // restauramos el bonus de ataque, de defensa, el id de la habitación actual, la fila y columna y si está vivo
        jugador.setAtaqueEquipamiento(getInt(json, "ataqueEquipamiento", 0));
        jugador.setDefensaEquipamiento(getInt(json, "defensaEquipamiento", 0));
        jugador.setHabitacionActualId(getString(json, "habitacionActualId", null));
        jugador.setPosicionX(getInt(json, "posicionX", 0));
        jugador.setPosicionY(getInt(json, "posicionY", 0));
        jugador.setVivo(getBoolean(json, "vivo", true));

        // obtenemos el inventario guardado y recorremos sus objetos
        JsonArray inventario = getArray(json, "inventario");
        for (JsonElement elemento : inventario) {

            // solo cargamos elementos que sean objetos JSON
            if (elemento.isJsonObject()) {
                jugador.getInventario().insertarUltimo(
                        crearObjetoDesdeJson(elemento.getAsJsonObject())
                );
            }
        }

        // devolvemos el jugador reconstruido
        return jugador;
    }

    /** Reconstruye un objeto desde JSON
     */
    private Objeto crearObjetoDesdeJson(JsonObject json) {

        // creamos el objeto con tipo, nombre, descripción y valor
        Objeto objeto = new Objeto(
                Objeto.Tipo.valueOf(getString(json, "tipo", Objeto.Tipo.OBJETO_ESPECIAL.name())),
                getString(json, "nombre", "Objeto"),
                getString(json, "descripcion", ""),
                getInt(json, "valor", 0)
        );

        // restauramos los usos restantes
        objeto.setUsosRestantes(getInt(json, "usosRestantes", objeto.getUsosRestantes()));

        // devolvemos el objeto reconstruido
        return objeto;
    }

    /** Reconstruye un enemigo desde JSON
     */
    private Enemigo crearEnemigoDesdeJson(JsonObject json) {

        // creamos el enemigo según su tipo
        Enemigo enemigo = new Enemigo(
                Enemigo.Tipo.valueOf(getString(json, "tipo", Enemigo.Tipo.GOBLIN.name()))
        );

        // restauramos su vida actual
        enemigo.setVidaActual(getInt(json, "vidaActual", enemigo.getVidaActual()));

        // restauramos su posición
        enemigo.setPosicion(
                getInt(json, "posicionX", -1),
                getInt(json, "posicionY", -1)
        );

        // Restauramos si está equipado, el daño y la defensa extra
        enemigo.setEquipado(getBoolean(json, "equipado", false));
        enemigo.setDanoArma(getInt(json, "danoArma", 0));
        enemigo.setDefensaArmadura(getInt(json, "defensaArmadura", 0));

        // devolvemos el enemigo reconstruido
        return enemigo;
    }

    /** Lee un array JSON de forma segura
     */
    private JsonArray getArray(JsonObject json, String clave) {

        // si existe la clave y además es un array, lo devolvemos
        if (json.has(clave) && json.get(clave).isJsonArray()) {
            return json.getAsJsonArray(clave);
        }

        // si no existe o no es array, devolvemos un array vacío
        return new JsonArray();
    }

    /** Lee un String de forma segura
     */
    private String getString(JsonObject json, String clave, String defecto) {

        // si existe la clave y no es null, devolvemos su texto
        if (json.has(clave) && !json.get(clave).isJsonNull()) {
            return json.get(clave).getAsString();
        }

        // si no existe, devolvemos el valor por defecto
        return defecto;
    }

    /** Lee un int de forma segura
     */
    private int getInt(JsonObject json, String clave, int defecto) {

        // si existe la clave y no es null, devolvemos su valor entero
        if (json.has(clave) && !json.get(clave).isJsonNull()) {
            return json.get(clave).getAsInt();
        }

        // si no existe, devolvemos el valor por defecto
        return defecto;
    }

    /** Lee un double de forma segura
     */
    private double getDouble(JsonObject json, String clave, double defecto) {

        // si existe la clave y no es null, devolvemos su valor decimal
        if (json.has(clave) && !json.get(clave).isJsonNull()) {
            return json.get(clave).getAsDouble();
        }

        // si no existe, devolvemos el valor por defecto
        return defecto;
    }

    /** Lee un boolean de forma segura
     */
    private boolean getBoolean(JsonObject json, String clave, boolean defecto) {

        // si existe la clave y no es null, devolvemos true o false
        if (json.has(clave) && !json.get(clave).isJsonNull()) {
            return json.get(clave).getAsBoolean();
        }

        // si no existe, devolvemos el valor por defecto
        return defecto;
    }
}