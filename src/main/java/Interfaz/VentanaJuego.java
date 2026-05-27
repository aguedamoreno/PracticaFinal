package Interfaz; // indica el paquete al que pertenece esta clase.

import Juego.*; // importa una clase externa necesaria para este archivo.
import javafx.geometry.Insets; // importa una clase externa necesaria para este archivo.
import javafx.scene.Parent; // importa una clase externa necesaria para este archivo.
import javafx.scene.control.*; // importa una clase externa necesaria para este archivo.
import javafx.scene.layout.*; // importa una clase externa necesaria para este archivo.

import java.io.IOException; // importa una clase externa necesaria para este archivo.
import java.nio.file.Path; // importa una clase externa necesaria para este archivo.
import java.nio.file.Paths; // importa una clase externa necesaria para este archivo.

/**
 * Clase que construye y actualiza la interfaz gráfica JavaFX: mapa, botones, inventario, estadísticas y mensajes.
 *
 * Comentarios añadidos para explicar la función de la clase, sus variables
 * y los bloques principales de código sin cambiar la lógica original.
 */
public class VentanaJuego { // declara una clase que agrupa datos y métodos relacionados

    private BorderPane root; // declara un atributo/campo de la clase donde se guarda estado
    private GridPane gridMapa; // declara un atributo/campo de la clase donde se guarda estado
    private TextArea areaRegistro; // declara un atributo/campo de la clase donde se guarda estado

    private Label vidaLabel; // declara un atributo/campo de la clase donde se guarda estado
    private Label ataqueLabel; // declara un atributo/campo de la clase donde se guarda estado
    private Label defensaLabel; // declara un atributo/campo de la clase donde se guarda estado
    private Label turnosLabel; // declara un atributo/campo de la clase donde se guarda estado

    private ListView<String> inventarioView; // declara un atributo/campo de la clase donde se guarda estado

    private Button moverBtn; // declara un atributo/campo de la clase donde se guarda estado
    private Button atacarBtn; // declara un atributo/campo de la clase donde se guarda estado
    private Button usarBtn; // declara un atributo/campo de la clase donde se guarda estado
    private Button recogerBtn; // declara un atributo/campo de la clase donde se guarda estado
    private Button pasarTurnoBtn; // declara un atributo/campo de la clase donde se guarda estado
    private Button guardarBtn; // declara un atributo/campo de la clase donde se guarda estado
    private Button cargarBtn; // declara un atributo/campo de la clase donde se guarda estado

    private MotorJuego motor; // declara un atributo/campo de la clase donde se guarda estado
    private PersistenciaJson persistenciaJson; // declara un atributo/campo de la clase donde se guarda estado
    private static final Path RUTA_GUARDADO = Paths.get(System.getProperty("user.home"), "partida_guardada.json"); // declara un atributo/campo de la clase donde se guarda estado

    // --- NUEVAS VARIABLES PARA SELECCIÓN DE CASILLAS ---
    private int filaSeleccionada = -1; // declara un atributo/campo de la clase donde se guarda estado
    private int columnaSeleccionada = -1; // declara un atributo/campo de la clase donde se guarda estado
    private Button botonSeleccionadoAnterior = null; // declara un atributo/campo de la clase donde se guarda estado

    /**
     * Constructor que inicializa los atributos principales del objeto
     */
    public VentanaJuego() {
        motor = MotorJuego.crearPartidaDemo(); // asigna o actualiza un valor necesario para el estado del programa
        persistenciaJson = new PersistenciaJson(); // crea un nuevo objeto para poder usarlo después
        inicializarComponentes(); // ejecuta una llamada a un método para realizar una acción concreta
        configurarAccionesBotones(); // <- NUEVA LLAMADA
        construirVentana(); // ejecuta una llamada a un método para realizar una acción concreta
        actualizarVista(); // ejecuta una llamada a un método para realizar una acción concreta
    }

    /**
     * Crea los controles visuales antes de colocarlos en la ventana
     */
    private void inicializarComponentes() {
        root = new BorderPane(); // crea un nuevo objeto para poder usarlo después

        gridMapa = new GridPane(); // crea un nuevo objeto para poder usarlo después
        gridMapa.setHgap(2); // ejecuta una llamada a un método para realizar una acción concreta
        gridMapa.setVgap(2); // ejecuta una llamada a un método para realizar una acción concreta
        gridMapa.setPadding(new Insets(10)); // crea un nuevo objeto para poder usarlo después

        areaRegistro = new TextArea(); // crea un nuevo objeto para poder usarlo después
        areaRegistro.setEditable(false); // ejecuta una llamada a un método para realizar una acción concreta
        areaRegistro.setPrefHeight(180); // ejecuta una llamada a un método para realizar una acción concreta

        vidaLabel = new Label(); // crea un nuevo objeto para poder usarlo después
        ataqueLabel = new Label(); // crea un nuevo objeto para poder usarlo después
        defensaLabel = new Label(); // crea un nuevo objeto para poder usarlo después
        turnosLabel = new Label(); // crea un nuevo objeto para poder usarlo después

        inventarioView = new ListView<>(); // crea un nuevo objeto para poder usarlo después
        inventarioView.setPrefWidth(200); // ejecuta una llamada a un método para realizar una acción concreta

        moverBtn = new Button("Mover a Selección"); // crea un nuevo objeto para poder usarlo después
        atacarBtn = new Button("Atacar Enemigo"); // crea un nuevo objeto para poder usarlo después
        usarBtn = new Button("Usar Objeto Seleccionado"); // crea un nuevo objeto para poder usarlo después
        recogerBtn = new Button("Recoger Objeto"); // crea un nuevo objeto para poder usarlo después
        pasarTurnoBtn = new Button("Pasar Turno"); // crea un nuevo objeto para poder usarlo después
        guardarBtn = new Button("Guardar Partida"); // crea un nuevo objeto para poder usarlo después
        cargarBtn = new Button("Cargar Partida"); // crea un nuevo objeto para poder usarlo después
    }

    /**
     * NUEVO METODO: Conecta los botones inferiores con la lógica del MotorJuego
     */
    private void configurarAccionesBotones() {

        // ACCIÓN DEL BOTÓN RECOGER OBJETO
        recogerBtn.setOnAction(e -> {
            if (filaSeleccionada == -1 || columnaSeleccionada == -1) { // comprueba una condición para decidir qué camino sigue el programa
                mostrarError("Primero debes seleccionar una casilla en el mapa."); // ejecuta una llamada a un método para realizar una acción concreta
                return;
            }
            try { // intenta ejecutar código que puede producir una excepción
                // Ejecutamos la acción en el motor
                motor.recogerObjetoAdyacente(filaSeleccionada, columnaSeleccionada); // ejecuta una llamada a un método para realizar una acción concreta
                // Reseteamos selección tras el éxito
                limpiarSeleccion(); // ejecuta una llamada a un método para realizar una acción concreta
                actualizarVista(); // ejecuta una llamada a un método para realizar una acción concreta
            } catch (JuegoException ex) {
                mostrarError(ex.getMessage()); // ejecuta una llamada a un método para realizar una acción concreta
            }
        }); // ejecuta una llamada a un método para realizar una acción concreta

        // ACCIÓN DEL BOTÓN ATACAR
        atacarBtn.setOnAction(e -> {
            if (filaSeleccionada == -1 || columnaSeleccionada == -1) { // comprueba una condición para decidir qué camino sigue el programa
                mostrarError("Primero debes seleccionar una casilla donde haya un enemigo."); // ejecuta una llamada a un método para realizar una acción concreta
                return;
            }
            try { // intenta ejecutar código que puede producir una excepción
                motor.atacarAdyacente(filaSeleccionada, columnaSeleccionada); // ejecuta una llamada a un método para realizar una acción concreta
                limpiarSeleccion(); // ejecuta una llamada a un método para realizar una acción concreta
                actualizarVista(); // ejecuta una llamada a un método para realizar una acción concreta
            } catch (JuegoException ex) {
                mostrarError(ex.getMessage()); // ejecuta una llamada a un método para realizar una acción concreta
            }
        }); // ejecuta una llamada a un método para realizar una acción concreta

        // ACCIÓN DEL BOTÓN MOVER
        moverBtn.setOnAction(e -> {
            if (filaSeleccionada == -1 || columnaSeleccionada == -1) { // comprueba una condición para decidir qué camino sigue el programa
                mostrarError("Primero debes seleccionar a qué casilla te quieres mover."); // ejecuta una llamada a un método para realizar una acción concreta
                return;
            }
            try { // intenta ejecutar código que puede producir una excepción
                motor.moverJugador(filaSeleccionada, columnaSeleccionada); // ejecuta una llamada a un método para realizar una acción concreta
                limpiarSeleccion(); // ejecuta una llamada a un método para realizar una acción concreta
                actualizarVista(); // ejecuta una llamada a un método para realizar una acción concreta
            } catch (JuegoException ex) {
                mostrarError(ex.getMessage()); // ejecuta una llamada a un método para realizar una acción concreta
            }
        }); // ejecuta una llamada a un método para realizar una acción concreta

        // ACCIÓN DEL BOTÓN USAR OBJETO DEL INVENTARIO
        usarBtn.setOnAction(e -> {
            int indiceSeleccionado = inventarioView.getSelectionModel().getSelectedIndex(); // asigna o actualiza un valor necesario para el estado del programa
            if (indiceSeleccionado == -1) { // comprueba una condición para decidir qué camino sigue el programa
                mostrarError("Debes seleccionar un objeto de la lista de tu inventario."); // ejecuta una llamada a un método para realizar una acción concreta
                return;
            }
            try { // intenta ejecutar código que puede producir una excepción
                motor.usarObjeto(indiceSeleccionado); // ejecuta una llamada a un método para realizar una acción concreta
                actualizarVista(); // ejecuta una llamada a un método para realizar una acción concreta
            } catch (JuegoException ex) {
                mostrarError(ex.getMessage()); // ejecuta una llamada a un método para realizar una acción concreta
            }
        }); // ejecuta una llamada a un método para realizar una acción concreta

        // ACCION DEL BOTÓN PASARTURNO
        pasarTurnoBtn.setOnAction(e -> {
            motor.finalizarTurnoVoluntariamente(); // Llama al motor para pasar de turno
            actualizarVista();                     // Refresca el mapa, la vida y los turnos restantes
            actualizarRegistro();                  // Muestra el texto en el panel de la derecha

            // Opcional: limpiar la selección de casillas para el siguiente turno
            filaSeleccionada = -1; // asigna o actualiza un valor necesario para el estado del programa
            columnaSeleccionada = -1; // asigna o actualiza un valor necesario para el estado del programa
            if (botonSeleccionadoAnterior != null) { // comprueba una condición para decidir qué camino sigue el programa
                botonSeleccionadoAnterior.setStyle(""); // ejecuta una llamada a un método para realizar una acción concreta
                botonSeleccionadoAnterior = null; // asigna o actualiza un valor necesario para el estado del programa
            }
        }); // ejecuta una llamada a un método para realizar una acción concreta

        // ACCIÓN DEL BOTÓN GUARDAR PARTIDA
        guardarBtn.setOnAction(e -> {
            try { // intenta ejecutar código que puede producir una excepción
                persistenciaJson.guardarEstado(motor, RUTA_GUARDADO); // ejecuta una llamada a un método para realizar una acción concreta
                actualizarRegistro(); // ejecuta una llamada a un método para realizar una acción concreta
                mostrarMensajeInformativo("Partida guardada", "La partida se ha guardado en el archivo " + RUTA_GUARDADO + "."); // ejecuta una llamada a un método para realizar una acción concreta
            } catch (Exception ex) {
                ex.printStackTrace(); // ejecuta una llamada a un método para realizar una acción concreta
                mostrarError("No se ha podido guardar la partida: " + ex.getClass().getSimpleName() + " - " + ex.getMessage()); // ejecuta una llamada a un método para realizar una acción concreta
            }
        }); // ejecuta una llamada a un método para realizar una acción concreta

        // ACCIÓN DEL BOTÓN CARGAR PARTIDA
        cargarBtn.setOnAction(e -> {
            try { // intenta ejecutar código que puede producir una excepción
                motor = persistenciaJson.cargarEstado(RUTA_GUARDADO); // asigna o actualiza un valor necesario para el estado del programa
                limpiarSeleccion(); // ejecuta una llamada a un método para realizar una acción concreta
                activarControles(); // ejecuta una llamada a un método para realizar una acción concreta
                actualizarVista(); // ejecuta una llamada a un método para realizar una acción concreta
                mostrarMensajeInformativo("Partida cargada", "La partida se ha cargado desde el archivo " + RUTA_GUARDADO + "."); // ejecuta una llamada a un método para realizar una acción concreta
            } catch (Exception ex) {
                ex.printStackTrace(); // ejecuta una llamada a un método para realizar una acción concreta
                mostrarError("No se ha podido cargar la partida: " + ex.getClass().getSimpleName() + " - " + ex.getMessage()); // ejecuta una llamada a un método para realizar una acción concreta
            }
        }); // ejecuta una llamada a un método para realizar una acción concreta
    }

    /**
     * Borra la casilla seleccionada y quita el estilo visual de selección
     */
    private void limpiarSeleccion() {
        filaSeleccionada = -1; // asigna o actualiza un valor necesario para el estado del programa
        columnaSeleccionada = -1; // asigna o actualiza un valor necesario para el estado del programa
        if (botonSeleccionadoAnterior != null) { // comprueba una condición para decidir qué camino sigue el programa
            botonSeleccionadoAnterior.setStyle(""); // Quitar el borde de marcado
            botonSeleccionadoAnterior = null; // asigna o actualiza un valor necesario para el estado del programa
        }
    }

    /**
     * Organiza los paneles de la interfaz dentro del BorderPane principal
     */
    private void construirVentana() {
        VBox panelDerecho = new VBox(10); // crea un nuevo objeto para poder usarlo después
        panelDerecho.setPadding(new Insets(10)); // crea un nuevo objeto para poder usarlo después
        panelDerecho.getChildren().addAll(
                new Label("--- ESTADO JUGADOR ---"),
                vidaLabel, ataqueLabel, defensaLabel, turnosLabel,
                new Label("--- INVENTARIO ---"),
                inventarioView,
                usarBtn
        ); // ejecuta una llamada a un método para realizar una acción concreta

        HBox panelInferiorBotones = new HBox(10); // crea un nuevo objeto para poder usarlo después
        panelInferiorBotones.setPadding(new Insets(10)); // crea un nuevo objeto para poder usarlo después
        panelInferiorBotones.getChildren().addAll(moverBtn, recogerBtn, atacarBtn, pasarTurnoBtn, guardarBtn, cargarBtn); // ejecuta una llamada a un método para realizar una acción concreta

        VBox panelInferiorCompleto = new VBox(5); // crea un nuevo objeto para poder usarlo después
        panelInferiorCompleto.getChildren().addAll(panelInferiorBotones, areaRegistro); // ejecuta una llamada a un método para realizar una acción concreta

        root.setCenter(gridMapa); // ejecuta una llamada a un método para realizar una acción concreta
        root.setRight(panelDerecho); // ejecuta una llamada a un método para realizar una acción concreta
        root.setBottom(panelInferiorCompleto); // ejecuta una llamada a un método para realizar una acción concreta
    }

    /**
     * Refresca toda la pantalla después de cualquier acción
     */
    public void actualizarVista() {
        actualizarMapa(); // ejecuta una llamada a un método para realizar una acción concreta
        actualizarJugador(); // ejecuta una llamada a un método para realizar una acción concreta
        actualizarInventario(); // ejecuta una llamada a un método para realizar una acción concreta
        actualizarRegistro(); // ejecuta una llamada a un método para realizar una acción concreta
        verificarFinPartida(); // ejecuta una llamada a un método para realizar una acción concreta
    }

    /**
     * Reconstruye el mapa visual de botones a partir del estado actual de la habitación
     */
    private void actualizarMapa() {
        gridMapa.getChildren().clear(); // ejecuta una llamada a un método para realizar una acción concreta
        Habitacion hab = motor.getHabitacionActual(); // asigna o actualiza un valor necesario para el estado del programa
        Jugador jugador = motor.getJugador(); // asigna o actualiza un valor necesario para el estado del programa

        for (int i = 0; i < hab.getFilas(); i++) { // bucle que repite instrucciones recorriendo elementos o posiciones
            for (int j = 0; j < hab.getColumnas(); j++) { // bucle que repite instrucciones recorriendo elementos o posiciones
                Celda celda = hab.getCelda(i, j); // asigna o actualiza un valor necesario para el estado del programa
                Button btnCelda = new Button(); // crea un nuevo objeto para poder usarlo después
                btnCelda.setPrefSize(65, 65); // ejecuta una llamada a un método para realizar una acción concreta

                // Estilos visuales según el tipo de contenido
                if (i == jugador.getPosicionX() && j == jugador.getPosicionY()) { // comprueba una condición para decidir qué camino sigue el programa
                    btnCelda.setText("J"); // Jugador
                    btnCelda.setStyle("-fx-background-color: #87CEEB; -fx-font-weight: bold;"); // ejecuta una llamada a un método para realizar una acción concreta.
                } else {
                    switch (celda.getTipo()) {
                        case VACIA:
                            btnCelda.setText(""); // ejecuta una llamada a un método para realizar una acción concreta.
                            break; // controla el flujo del bucle actual.
                        case OBJETO:
                            btnCelda.setText("O"); // ejecuta una llamada a un método para realizar una acción concreta.
                            btnCelda.setStyle("-fx-background-color: #FFE4B5;"); // ejecuta una llamada a un método para realizar una acción concreta.
                            break; // controla el flujo del bucle actual.
                        case ENEMIGO:
                            btnCelda.setText("E"); // ejecuta una llamada a un método para realizar una acción concreta.
                            btnCelda.setStyle("-fx-background-color: #FFB6C1;"); // ejecuta una llamada a un método para realizar una acción concreta.
                            break; // controla el flujo del bucle actual.
                        case TRAMPA:
                            btnCelda.setText("T"); // ejecuta una llamada a un método para realizar una acción concreta.
                            btnCelda.setStyle("-fx-background-color: #D3D3D3;"); // ejecuta una llamada a un método para realizar una acción concreta.
                            break; // controla el flujo del bucle actual.
                        case PUERTA:
                            btnCelda.setText("P"); // ejecuta una llamada a un método para realizar una acción concreta.
                            btnCelda.setStyle("-fx-background-color: #98FB98;"); // ejecuta una llamada a un método para realizar una acción concreta.
                            break; // controla el flujo del bucle actual.
                        case SALIDA:
                            btnCelda.setText("S"); // ejecuta una llamada a un método para realizar una acción concreta.
                            btnCelda.setStyle("-fx-background-color: #GOLD;"); // ejecuta una llamada a un método para realizar una acción concreta.
                            break; // controla el flujo del bucle actual.
                    }
                }

                // --- GESTIÓN DEL CLICK EN EL MAPA ---
                final int f = i; // asigna o actualiza un valor necesario para el estado del programa.
                final int c = j; // asigna o actualiza un valor necesario para el estado del programa.
                btnCelda.setOnAction(e -> {
                    // Si ya había un botón seleccionado antes, le quitamos el borde rojo
                    if (botonSeleccionadoAnterior != null) { // comprueba una condición para decidir qué camino sigue el programa.
                        botonSeleccionadoAnterior.setStyle(botonSeleccionadoAnterior.getStyle().replace("-fx-border-color: red; -fx-border-width: 2px;", "")); // ejecuta una llamada a un método para realizar una acción concreta.
                    }

                    // Guardamos la nueva posición seleccionada
                    filaSeleccionada = f; // asigna o actualiza un valor necesario para el estado del programa.
                    columnaSeleccionada = c; // asigna o actualiza un valor necesario para el estado del programa.
                    botonSeleccionadoAnterior = btnCelda; // asigna o actualiza un valor necesario para el estado del programa.

                    // Le ponemos un borde rojo llamativo al botón clicado para que sepamos cuál es
                    btnCelda.setStyle(btnCelda.getStyle() + "-fx-border-color: red; -fx-border-width: 2px;"); // ejecuta una llamada a un método para realizar una acción concreta.
                }); // ejecuta una llamada a un método para realizar una acción concreta.

                gridMapa.add(btnCelda, j, i); // En GridPane se añade (columna, fila)
            }
        }
    }

    /**
     * Comprueba si la partida ha terminado por victoria o derrota y desactiva controles.
     */
    private void verificarFinPartida() {
        if (motor.isVictoria()) { // comprueba una condición para decidir qué camino sigue el programa.
            mostrarMensajeInformativo("¡VICTORIA!", "¡Enhorabuena! Has logrado escapar de la mazmorra."); // ejecuta una llamada a un método para realizar una acción concreta.
            desactivarControles(); // ejecuta una llamada a un método para realizar una acción concreta.
        } else if (motor.isDerrota()) {
            mostrarMensajeInformativo("GAME OVER", "Has sido derrotado. Inténtalo de nuevo."); // ejecuta una llamada a un método para realizar una acción concreta.
            desactivarControles(); // ejecuta una llamada a un método para realizar una acción concreta.
        }
    }

    /**
     * Bloquea los botones cuando ya no se puede jugar.
     */
    private void desactivarControles() {
        gridMapa.setDisable(true); // ejecuta una llamada a un método para realizar una acción concreta.
        moverBtn.setDisable(true); // ejecuta una llamada a un método para realizar una acción concreta.
        atacarBtn.setDisable(true); // ejecuta una llamada a un método para realizar una acción concreta.
        recogerBtn.setDisable(true); // ejecuta una llamada a un método para realizar una acción concreta.
        usarBtn.setDisable(true); // ejecuta una llamada a un método para realizar una acción concreta.
        pasarTurnoBtn.setDisable(true); // ejecuta una llamada a un método para realizar una acción concreta.
    }

    /**
     * Vuelve a habilitar los botones de acción.
     */
    private void activarControles() {
        gridMapa.setDisable(false); // ejecuta una llamada a un método para realizar una acción concreta.
        moverBtn.setDisable(false); // ejecuta una llamada a un método para realizar una acción concreta.
        atacarBtn.setDisable(false); // ejecuta una llamada a un método para realizar una acción concreta.
        recogerBtn.setDisable(false); // ejecuta una llamada a un método para realizar una acción concreta.
        usarBtn.setDisable(false); // ejecuta una llamada a un método para realizar una acción concreta.
        pasarTurnoBtn.setDisable(false); // ejecuta una llamada a un método para realizar una acción concreta.
    }

    /**
     * Muestra en pantalla las estadísticas actuales del jugador.
     */
    private void actualizarJugador() {
        vidaLabel.setText("Vida: " + motor.getJugador().getVidaActual() + "/" + motor.getJugador().getVidaMax()); // ejecuta una llamada a un método para realizar una acción concreta.
        ataqueLabel.setText("Ataque: " + motor.getJugador().getAtaqueTotal()); // ejecuta una llamada a un método para realizar una acción concreta.
        defensaLabel.setText("Defensa: " + motor.getJugador().getDefensaTotal()); // ejecuta una llamada a un método para realizar una acción concreta.
        turnosLabel.setText("Turnos restantes: " + motor.getTurnosRestantes()); // ejecuta una llamada a un método para realizar una acción concreta.
    }

    /**
     * Actualiza la lista visible de objetos del inventario.
     */
    private void actualizarInventario() {
        inventarioView.getItems().clear(); // ejecuta una llamada a un método para realizar una acción concreta.
        for (int i = 0; i < motor.getJugador().getInventario().tamaño(); i++) { // bucle que repite instrucciones recorriendo elementos o posiciones.
            Objeto objeto = motor.getJugador().getInventario().obtener(i); // asigna o actualiza un valor necesario para el estado del programa.
            inventarioView.getItems().add(objeto.getNombre() + " (" + objeto.getTipo() + ")"); // ejecuta una llamada a un método para realizar una acción concreta.
        }
    }

    /**
     * Muestra en el área de texto el historial de eventos de la partida.
     */
    private void actualizarRegistro() {
        areaRegistro.setText(motor.getRegistro().comoTexto()); // ejecuta una llamada a un método para realizar una acción concreta.
        areaRegistro.setScrollTop(Double.MAX_VALUE); // Auto-scroll hacia abajo
    }

    /**
     * Abre una alerta de error con un mensaje para el usuario.
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // crea un nuevo objeto para poder usarlo después.
        alert.setTitle("Error en la Acción"); // ejecuta una llamada a un método para realizar una acción concreta.
        alert.setHeaderText(null); // ejecuta una llamada a un método para realizar una acción concreta.
        alert.setContentText(mensaje); // ejecuta una llamada a un método para realizar una acción concreta.
        alert.showAndWait(); // ejecuta una llamada a un método para realizar una acción concreta.
    }

    /**
     * Abre una alerta informativa con título y mensaje.
     */
    private void mostrarMensajeInformativo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // crea un nuevo objeto para poder usarlo después.
        alert.setTitle(titulo); // ejecuta una llamada a un método para realizar una acción concreta.
        alert.setHeaderText(null); // ejecuta una llamada a un método para realizar una acción concreta.
        alert.setContentText(mensaje); // ejecuta una llamada a un método para realizar una acción concreta.
        alert.showAndWait(); // ejecuta una llamada a un método para realizar una acción concreta.
    }

    /**
     * Devuelve el contenedor raíz para insertarlo en la escena JavaFX.
     */
    public Parent getRoot() {
        return root; // devuelve el resultado calculado por el método.
    }
}
