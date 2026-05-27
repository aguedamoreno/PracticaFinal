package Interfaz; // Esta clase pertenece al paquete Interfaz, donde están las clases encargadas de la parte visual del juego.

import Juego.*; // Importa todas las clases del paquete Juego: MotorJuego, Jugador, Habitacion, Celda, Objeto, JuegoException, PersistenciaJson, etc.
import javafx.geometry.Insets; // Permite poner márgenes internos alrededor de los paneles JavaFX.
import javafx.scene.Parent; // Tipo genérico de JavaFX que representa un nodo raíz que puede meterse en una Scene.
import javafx.scene.control.*; // Importa controles de interfaz como Button, Label, TextArea, ListView y Alert.
import javafx.scene.layout.*; // Importa paneles de organización como BorderPane, GridPane, VBox y HBox.

import java.nio.file.Path; // Representa una ruta de archivo de forma moderna y segura.
import java.nio.file.Paths; // Sirve para construir objetos Path a partir de carpetas y nombres de archivo.

/**
 * Clase encargada de construir y actualizar toda la ventana principal del juego.
 *
 * Esta clase NO contiene directamente las reglas del juego. Esa parte la lleva MotorJuego.
 * Lo que hace VentanaJuego es:
 * - Crear los botones, etiquetas, lista de inventario, mapa y zona de registro.
 * - Mostrar el estado actual del jugador y de la habitación.
 * - Detectar qué botón pulsa el usuario.
 * - Llamar al motor para mover, atacar, recoger objetos, usar objetos, guardar o cargar.
 * - Refrescar la pantalla después de cada acción para que se vea el estado actualizado.
 */
public class VentanaJuego {

    // Panel principal de la ventana. BorderPane divide la interfaz en zonas: centro, derecha, abajo, arriba e izquierda.
    private BorderPane root;

    // Cuadrícula donde se dibuja el mapa de la habitación. Cada celda del mapa se representa con un Button.
    private GridPane gridMapa;

    // Área de texto donde se muestran los mensajes del registro de la partida: movimientos, ataques, errores, turnos, etc.
    private TextArea areaRegistro;

    // Etiqueta que muestra la vida actual y máxima del jugador.
    private Label vidaLabel;

    // Etiqueta que muestra el ataque total del jugador, contando posibles mejoras de objetos.
    private Label ataqueLabel;

    // Etiqueta que muestra la defensa total del jugador, contando posibles mejoras de objetos.
    private Label defensaLabel;

    // Etiqueta que muestra cuántos turnos quedan antes de perder o antes de que acabe la partida.
    private Label turnosLabel;

    // Lista visual donde aparecen los objetos que tiene el jugador en el inventario.
    // Guarda Strings porque en pantalla se muestra texto, por ejemplo: "Poción (POCION_VIDA)".
    private ListView<String> inventarioView;

    // Botón que intenta mover al jugador a la casilla seleccionada del mapa.
    private Button moverBtn;

    // Botón que intenta atacar al enemigo situado en la casilla seleccionada.
    private Button atacarBtn;

    // Botón que intenta usar el objeto seleccionado en la lista del inventario.
    private Button usarBtn;

    // Botón que intenta recoger un objeto de la casilla seleccionada.
    private Button recogerBtn;

    // Botón que permite gastar/pasar el turno sin hacer otra acción.
    private Button pasarTurnoBtn;

    // Botón que guarda la partida actual en un archivo JSON.
    private Button guardarBtn;

    // Botón que carga una partida previamente guardada desde un archivo JSON.
    private Button cargarBtn;

    // Objeto que contiene la lógica real del juego: jugador, habitación, turnos, victoria, derrota, acciones, etc.
    private MotorJuego motor;

    // Objeto encargado de convertir el estado del juego a JSON y recuperarlo desde JSON.
    private PersistenciaJson persistenciaJson;

    // Ruta donde se guarda la partida. Usa la carpeta personal del usuario y crea/lee el archivo "partida_guardada.json".
    private static final Path RUTA_GUARDADO = Paths.get(System.getProperty("user.home"), "partida_guardada.json");

    // Fila de la casilla que el jugador ha seleccionado en el mapa.
    // Vale -1 cuando todavía no hay ninguna casilla seleccionada.
    private int filaSeleccionada = -1;

    // Columna de la casilla que el jugador ha seleccionado en el mapa.
    // Vale -1 cuando todavía no hay ninguna casilla seleccionada.
    private int columnaSeleccionada = -1;

    // Guarda el botón de la casilla que estaba marcada antes.
    // Se usa para quitarle el borde rojo cuando el jugador selecciona otra casilla.
    private Button botonSeleccionadoAnterior = null;

    /**
     * Constructor de la ventana principal del juego.
     *
     * Se ejecuta cuando se crea un objeto VentanaJuego.
     * Prepara una partida de demostración, crea los componentes gráficos,
     * conecta los botones con sus acciones, coloca los paneles en la ventana
     * y pinta en pantalla el estado inicial del juego.
     */
    public VentanaJuego() {
        motor = MotorJuego.crearPartidaDemo(); // Crea una partida inicial de prueba con jugador, mapa, objetos/enemigos y estado inicial.
        persistenciaJson = new PersistenciaJson(); // Crea el objeto que se usará para guardar y cargar la partida en formato JSON.
        inicializarComponentes(); // Crea todos los controles JavaFX: botones, etiquetas, mapa, inventario y registro.
        configurarAccionesBotones(); // Asocia a cada botón lo que debe ocurrir cuando el usuario lo pulsa.
        construirVentana(); // Coloca los controles dentro del BorderPane principal.
        actualizarVista(); // Rellena la interfaz con los datos actuales de la partida.
    }

    /**
     * Crea todos los componentes visuales de la ventana, pero todavía no los coloca.
     *
     * Aquí se inicializan los atributos gráficos para evitar que sean null
     * cuando se usen después en construirVentana() o actualizarVista().
     */
    private void inicializarComponentes() {
        root = new BorderPane(); // Crea el contenedor principal de la ventana.

        gridMapa = new GridPane(); // Crea la cuadrícula donde se dibujará la habitación.
        gridMapa.setHgap(2); // Deja 2 píxeles de separación horizontal entre botones/celdas.
        gridMapa.setVgap(2); // Deja 2 píxeles de separación vertical entre botones/celdas.
        gridMapa.setPadding(new Insets(10)); // Deja un margen interno de 10 píxeles alrededor del mapa.

        areaRegistro = new TextArea(); // Crea el área donde se verá el historial de la partida.
        areaRegistro.setEditable(false); // Impide que el usuario escriba manualmente en el registro.
        areaRegistro.setPrefHeight(180); // Fija una altura preferida para que el registro ocupe espacio en la parte inferior.

        vidaLabel = new Label(); // Crea la etiqueta donde se mostrará la vida.
        ataqueLabel = new Label(); // Crea la etiqueta donde se mostrará el ataque.
        defensaLabel = new Label(); // Crea la etiqueta donde se mostrará la defensa.
        turnosLabel = new Label(); // Crea la etiqueta donde se mostrarán los turnos restantes.

        inventarioView = new ListView<>(); // Crea la lista visual del inventario.
        inventarioView.setPrefWidth(200); // Da anchura suficiente al panel de inventario de la derecha.

        moverBtn = new Button("Mover a Selección"); // Crea el botón para moverse a la casilla seleccionada.
        atacarBtn = new Button("Atacar Enemigo"); // Crea el botón para atacar en la casilla seleccionada.
        usarBtn = new Button("Usar Objeto Seleccionado"); // Crea el botón para usar el objeto marcado en el inventario.
        recogerBtn = new Button("Recoger Objeto"); // Crea el botón para recoger un objeto del mapa.
        pasarTurnoBtn = new Button("Pasar Turno"); // Crea el botón para finalizar voluntariamente el turno.
        guardarBtn = new Button("Guardar Partida"); // Crea el botón para guardar la partida.
        cargarBtn = new Button("Cargar Partida"); // Crea el botón para cargar una partida guardada.
    }

    /**
     * Conecta cada botón con el código que se ejecuta al pulsarlo.
     *
     * Cada setOnAction recibe una expresión lambda e -> { ... }.
     * Ese bloque se ejecuta cuando el usuario pulsa el botón correspondiente.
     */
    private void configurarAccionesBotones() {

        // Cuando el usuario pulsa "Recoger Objeto", se intenta recoger el objeto de la casilla seleccionada.
        recogerBtn.setOnAction(e -> {
            if (filaSeleccionada == -1 || columnaSeleccionada == -1) { // Si alguna coordenada vale -1, significa que no hay casilla seleccionada.
                mostrarError("Primero debes seleccionar una casilla en el mapa."); // Avisa al usuario de que debe clicar una casilla antes.
                return; // Sale de la acción del botón para no llamar al motor con coordenadas inválidas.
            }
            try {
                motor.recogerObjetoAdyacente(filaSeleccionada, columnaSeleccionada); // Pide al motor que recoja el objeto de esa posición si la acción es válida.
                limpiarSeleccion(); // Quita la selección visual porque la acción ya se ha realizado.
                actualizarVista(); // Redibuja mapa, inventario, estadísticas y registro con el nuevo estado del juego.
            } catch (JuegoException ex) { // Captura errores propios del juego, por ejemplo si no hay objeto o no se puede recoger.
                mostrarError(ex.getMessage()); // Muestra el mensaje concreto que ha generado el motor.
            }
        });

        // Cuando el usuario pulsa "Atacar Enemigo", se intenta atacar al enemigo de la casilla seleccionada.
        atacarBtn.setOnAction(e -> {
            if (filaSeleccionada == -1 || columnaSeleccionada == -1) { // Comprueba que se haya elegido una casilla del mapa.
                mostrarError("Primero debes seleccionar una casilla donde haya un enemigo."); // Explica qué falta para poder atacar.
                return; // Cancela la acción porque no hay coordenadas válidas.
            }
            try {
                motor.atacarAdyacente(filaSeleccionada, columnaSeleccionada); // Pide al motor que ataque al enemigo de esa casilla si está permitido.
                limpiarSeleccion(); // Borra la selección después del ataque.
                actualizarVista(); // Actualiza la interfaz por si cambió la vida del enemigo, el mapa, el registro o los turnos.
            } catch (JuegoException ex) { // Captura errores como atacar una casilla vacía o no adyacente.
                mostrarError(ex.getMessage()); // Enseña al jugador el motivo del fallo.
            }
        });

        // Cuando el usuario pulsa "Mover a Selección", se intenta mover al jugador a la casilla seleccionada.
        moverBtn.setOnAction(e -> {
            if (filaSeleccionada == -1 || columnaSeleccionada == -1) { // Si no hay casilla marcada, no se sabe a dónde mover al jugador.
                mostrarError("Primero debes seleccionar a qué casilla te quieres mover."); // Pide al usuario que seleccione destino.
                return; // Termina la acción sin mover nada.
            }
            try {
                motor.moverJugador(filaSeleccionada, columnaSeleccionada); // Pide al motor mover al jugador a esas coordenadas.
                limpiarSeleccion(); // Elimina el borde rojo de la casilla tras moverse.
                actualizarVista(); // Refresca el mapa para mostrar la nueva posición del jugador.
            } catch (JuegoException ex) { // Captura errores como movimiento no permitido, trampa, puerta cerrada, etc.
                mostrarError(ex.getMessage()); // Muestra el error concreto al usuario.
            }
        });

        // Cuando el usuario pulsa "Usar Objeto Seleccionado", se intenta usar el objeto marcado en el inventario.
        usarBtn.setOnAction(e -> {
            int indiceSeleccionado = inventarioView.getSelectionModel().getSelectedIndex(); // Obtiene la posición del objeto seleccionado en la lista visual.
            if (indiceSeleccionado == -1) { // JavaFX devuelve -1 si no hay ningún elemento seleccionado en la ListView.
                mostrarError("Debes seleccionar un objeto de la lista de tu inventario."); // Avisa de que primero hay que marcar un objeto.
                return; // Cancela la acción para no intentar usar un objeto inexistente.
            }
            try {
                motor.usarObjeto(indiceSeleccionado); // Pide al motor usar el objeto que está en esa posición del inventario real.
                actualizarVista(); // Actualiza estadísticas e inventario, porque el objeto puede curar, equiparse o desaparecer.
            } catch (JuegoException ex) { // Captura errores como objeto no usable o índice inválido.
                mostrarError(ex.getMessage()); // Muestra la explicación del error.
            }
        });

        // Cuando el usuario pulsa "Pasar Turno", se finaliza el turno actual sin otra acción.
        pasarTurnoBtn.setOnAction(e -> {
            motor.finalizarTurnoVoluntariamente(); // Indica al motor que el jugador decide pasar turno.
            actualizarVista(); // Refresca toda la pantalla porque pueden cambiar turnos, registro o estado del jugador.
            actualizarRegistro(); // Vuelve a actualizar el registro para asegurar que el mensaje de pasar turno se vea al final.

            filaSeleccionada = -1; // Borra la fila seleccionada porque al pasar turno no queda ninguna casilla activa.
            columnaSeleccionada = -1; // Borra la columna seleccionada por el mismo motivo.
            if (botonSeleccionadoAnterior != null) { // Si había un botón marcado con borde rojo...
                botonSeleccionadoAnterior.setStyle(""); // ...se le borra el estilo para quitar la marca visual.
                botonSeleccionadoAnterior = null; // Se elimina la referencia al botón anterior porque ya no hay selección.
            }
        });

        // Cuando el usuario pulsa "Guardar Partida", se guarda el estado actual del motor en un archivo JSON.
        guardarBtn.setOnAction(e -> {
            try {
                persistenciaJson.guardarEstado(motor, RUTA_GUARDADO); // Serializa el motor y lo escribe en la ruta indicada.
                actualizarRegistro(); // Refresca el registro por si el guardado añade algún mensaje.
                mostrarMensajeInformativo("Partida guardada", "La partida se ha guardado en el archivo " + RUTA_GUARDADO + "."); // Confirma al usuario dónde se guardó.
            } catch (Exception ex) { // Captura cualquier fallo de guardado: permisos, ruta, errores de JSON, etc.
                ex.printStackTrace(); // Imprime el error completo en consola para poder depurarlo.
                mostrarError("No se ha podido guardar la partida: " + ex.getClass().getSimpleName() + " - " + ex.getMessage()); // Muestra un resumen del error en una alerta.
            }
        });

        // Cuando el usuario pulsa "Cargar Partida", se reemplaza la partida actual por la guardada en JSON.
        cargarBtn.setOnAction(e -> {
            try {
                motor = persistenciaJson.cargarEstado(RUTA_GUARDADO); // Lee el JSON y reconstruye un MotorJuego con la partida guardada.
                limpiarSeleccion(); // Borra cualquier casilla seleccionada antes de cargar, porque el mapa puede haber cambiado.
                activarControles(); // Reactiva los controles por si veníamos de una partida terminada.
                actualizarVista(); // Pinta en pantalla el estado que se acaba de cargar.
                mostrarMensajeInformativo("Partida cargada", "La partida se ha cargado desde el archivo " + RUTA_GUARDADO + "."); // Confirma que la carga funcionó.
            } catch (Exception ex) { // Captura errores si no existe el archivo, está corrupto o no se puede leer.
                ex.printStackTrace(); // Muestra la traza completa en consola para depuración.
                mostrarError("No se ha podido cargar la partida: " + ex.getClass().getSimpleName() + " - " + ex.getMessage()); // Enseña al usuario un mensaje entendible.
            }
        });
    }

    /**
     * Elimina la selección actual de una casilla del mapa.
     *
     * Internamente pone las coordenadas a -1 y, si había un botón marcado,
     * le borra el estilo para que deje de verse seleccionado.
     */
    private void limpiarSeleccion() {
        filaSeleccionada = -1; // -1 indica que no hay fila seleccionada.
        columnaSeleccionada = -1; // -1 indica que no hay columna seleccionada.
        if (botonSeleccionadoAnterior != null) { // Solo se intenta limpiar el estilo si realmente había un botón seleccionado.
            botonSeleccionadoAnterior.setStyle(""); // Borra todo el estilo del botón seleccionado anteriormente.
            botonSeleccionadoAnterior = null; // El programa deja de recordar ese botón como seleccionado.
        }
    }

    /**
     * Coloca los componentes ya creados dentro del panel principal.
     *
     * La ventana queda organizada así:
     * - Centro: mapa.
     * - Derecha: estado del jugador e inventario.
     * - Abajo: botones principales y registro de eventos.
     */
    private void construirVentana() {
        VBox panelDerecho = new VBox(10); // Crea una caja vertical con 10 píxeles de separación entre elementos.
        panelDerecho.setPadding(new Insets(10)); // Añade margen interno para que el contenido no quede pegado al borde.
        panelDerecho.getChildren().addAll( // Añade, en orden vertical, las etiquetas de estado, inventario y botón de usar.
                new Label("--- ESTADO JUGADOR ---"), // Título visual de la sección de estadísticas.
                vidaLabel, ataqueLabel, defensaLabel, turnosLabel, // Etiquetas que se rellenan en actualizarJugador().
                new Label("--- INVENTARIO ---"), // Título visual de la sección de inventario.
                inventarioView, // Lista donde aparecen los objetos del jugador.
                usarBtn // Botón para usar el objeto seleccionado en la lista.
        );

        HBox panelInferiorBotones = new HBox(10); // Crea una caja horizontal con 10 píxeles de separación entre botones.
        panelInferiorBotones.setPadding(new Insets(10)); // Añade margen interno alrededor de la fila de botones.
        panelInferiorBotones.getChildren().addAll(moverBtn, recogerBtn, atacarBtn, pasarTurnoBtn, guardarBtn, cargarBtn); // Coloca los botones de acción en la parte inferior.

        VBox panelInferiorCompleto = new VBox(5); // Crea un panel vertical para meter arriba los botones y debajo el registro.
        panelInferiorCompleto.getChildren().addAll(panelInferiorBotones, areaRegistro); // Añade primero la fila de botones y luego el área de texto.

        root.setCenter(gridMapa); // Coloca el mapa en el centro de la ventana.
        root.setRight(panelDerecho); // Coloca estadísticas e inventario a la derecha.
        root.setBottom(panelInferiorCompleto); // Coloca botones y registro en la zona inferior.
    }

    /**
     * Actualiza todas las partes visuales de la ventana.
     *
     * Se llama después de crear la ventana y después de cada acción del jugador.
     */
    public void actualizarVista() {
        actualizarMapa(); // Redibuja los botones del mapa según la habitación actual.
        actualizarJugador(); // Actualiza vida, ataque, defensa y turnos.
        actualizarInventario(); // Reconstruye la lista del inventario.
        actualizarRegistro(); // Actualiza el texto del historial de eventos.
        verificarFinPartida(); // Comprueba si la acción anterior provocó victoria o derrota.
    }

    /**
     * Reconstruye el mapa visual a partir del estado actual de la habitación.
     *
     * Borra los botones antiguos y crea un botón nuevo por cada celda.
     * Cada botón muestra una letra y un color según lo que haya en esa casilla.
     */
    private void actualizarMapa() {
        gridMapa.getChildren().clear(); // Vacía la cuadrícula para no dejar botones antiguos duplicados.
        Habitacion hab = motor.getHabitacionActual(); // Obtiene la habitación en la que está actualmente el jugador.
        Jugador jugador = motor.getJugador(); // Obtiene el jugador para saber su posición y dibujarlo con la letra J.

        for (int i = 0; i < hab.getFilas(); i++) { // Recorre todas las filas de la habitación.
            for (int j = 0; j < hab.getColumnas(); j++) { // Recorre todas las columnas de la fila actual.
                Celda celda = hab.getCelda(i, j); // Obtiene la celda lógica situada en la posición (i, j).
                Button btnCelda = new Button(); // Crea el botón que representará visualmente esa celda.
                btnCelda.setPrefSize(65, 65); // Hace que todas las casillas del mapa tengan el mismo tamaño.

                if (i == jugador.getPosicionX() && j == jugador.getPosicionY()) { // Si la celda actual coincide con la posición del jugador...
                    btnCelda.setText("J"); // ...se escribe J para indicar dónde está el jugador.
                    btnCelda.setStyle("-fx-background-color: #87CEEB; -fx-font-weight: bold;"); // Se pinta en azul claro y en negrita para destacar al jugador.
                } else { // Si la celda no es la posición del jugador, se dibuja según el tipo de celda.
                    switch (celda.getTipo()) { // Mira el tipo de contenido de la celda: vacía, objeto, enemigo, trampa, puerta o salida.
                        case VACIA:
                            btnCelda.setText(""); // Una celda vacía no muestra ninguna letra.
                            break; // Termina este caso del switch.
                        case OBJETO:
                            btnCelda.setText("O"); // La letra O representa que hay un objeto en esa casilla.
                            btnCelda.setStyle("-fx-background-color: #FFE4B5;"); // Se pinta con color claro para diferenciar los objetos.
                            break;
                        case ENEMIGO:
                            btnCelda.setText("E"); // La letra E representa que hay un enemigo.
                            btnCelda.setStyle("-fx-background-color: #FFB6C1;"); // Se pinta en rosa/rojo claro para indicar peligro.
                            break;
                        case TRAMPA:
                            btnCelda.setText("T"); // La letra T representa una trampa.
                            btnCelda.setStyle("-fx-background-color: #D3D3D3;"); // Se pinta en gris para distinguirla del resto.
                            break;
                        case PUERTA:
                            btnCelda.setText("P"); // La letra P representa una puerta.
                            btnCelda.setStyle("-fx-background-color: #98FB98;"); // Se pinta en verde claro.
                            break;
                        case SALIDA:
                            btnCelda.setText("S"); // La letra S representa la salida de la mazmorra/habitación.
                            btnCelda.setStyle("-fx-background-color: GOLD;"); // Se pinta en dorado para destacar que es el objetivo.
                            break;
                    }
                }

                final int f = i; // Guarda la fila actual en una variable final para poder usarla dentro de la lambda del click.
                final int c = j; // Guarda la columna actual en una variable final para poder usarla dentro de la lambda del click.
                btnCelda.setOnAction(e -> { // Define lo que pasa cuando el usuario pulsa esta casilla del mapa.
                    if (botonSeleccionadoAnterior != null) { // Si ya había otra casilla seleccionada antes...
                        botonSeleccionadoAnterior.setStyle(botonSeleccionadoAnterior.getStyle().replace("-fx-border-color: red; -fx-border-width: 2px;", "")); // ...se quita solo el borde rojo de selección anterior.
                    }

                    filaSeleccionada = f; // Guarda como seleccionada la fila de la casilla que se acaba de pulsar.
                    columnaSeleccionada = c; // Guarda como seleccionada la columna de la casilla que se acaba de pulsar.
                    botonSeleccionadoAnterior = btnCelda; // Recuerda este botón para poder quitarle el borde si luego se selecciona otro.

                    btnCelda.setStyle(btnCelda.getStyle() + "-fx-border-color: red; -fx-border-width: 2px;"); // Añade un borde rojo para que se vea qué casilla está seleccionada.
                });

                gridMapa.add(btnCelda, j, i); // Añade el botón al GridPane. JavaFX pide primero columna (j) y luego fila (i).
            }
        }
    }

    /**
     * Comprueba si la partida ya ha terminado.
     *
     * Si hay victoria o derrota, muestra una alerta y desactiva los controles
     * para que el jugador no pueda seguir realizando acciones sobre una partida acabada.
     */
    private void verificarFinPartida() {
        if (motor.isVictoria()) { // Pregunta al motor si el jugador ha ganado.
            mostrarMensajeInformativo("¡VICTORIA!", "¡Enhorabuena! Has logrado escapar de la mazmorra."); // Muestra mensaje de victoria.
            desactivarControles(); // Bloquea botones y mapa para impedir más acciones.
        } else if (motor.isDerrota()) { // Si no ganó, comprueba si perdió.
            mostrarMensajeInformativo("GAME OVER", "Has sido derrotado. Inténtalo de nuevo."); // Muestra mensaje de derrota.
            desactivarControles(); // Bloquea botones y mapa porque la partida terminó.
        }
    }

    /**
     * Desactiva los controles que permiten jugar.
     *
     * Se usa cuando la partida termina para que el usuario no pueda moverse,
     * atacar, recoger objetos ni pasar turnos después de ganar o perder.
     */
    private void desactivarControles() {
        gridMapa.setDisable(true); // Desactiva todas las casillas del mapa.
        moverBtn.setDisable(true); // Desactiva el botón de movimiento.
        atacarBtn.setDisable(true); // Desactiva el botón de ataque.
        recogerBtn.setDisable(true); // Desactiva el botón de recoger objeto.
        usarBtn.setDisable(true); // Desactiva el botón de usar objeto.
        pasarTurnoBtn.setDisable(true); // Desactiva el botón de pasar turno.
    }

    /**
     * Vuelve a activar los controles de juego.
     *
     * Se usa al cargar una partida, porque puede que antes los controles estuvieran
     * bloqueados por una victoria o derrota anterior.
     */
    private void activarControles() {
        gridMapa.setDisable(false); // Permite volver a pulsar casillas del mapa.
        moverBtn.setDisable(false); // Reactiva el botón de movimiento.
        atacarBtn.setDisable(false); // Reactiva el botón de ataque.
        recogerBtn.setDisable(false); // Reactiva el botón de recoger objeto.
        usarBtn.setDisable(false); // Reactiva el botón de usar objeto.
        pasarTurnoBtn.setDisable(false); // Reactiva el botón de pasar turno.
    }

    /**
     * Actualiza las etiquetas con las estadísticas actuales del jugador.
     *
     * Lee los datos desde el motor y los convierte en texto para mostrarlos en pantalla.
     */
    private void actualizarJugador() {
        vidaLabel.setText("Vida: " + motor.getJugador().getVidaActual() + "/" + motor.getJugador().getVidaMax()); // Muestra vida actual y vida máxima, por ejemplo "Vida: 80/100".
        ataqueLabel.setText("Ataque: " + motor.getJugador().getAtaqueTotal()); // Muestra el ataque total del jugador, incluyendo mejoras.
        defensaLabel.setText("Defensa: " + motor.getJugador().getDefensaTotal()); // Muestra la defensa total del jugador, incluyendo mejoras.
        turnosLabel.setText("Turnos restantes: " + motor.getTurnosRestantes()); // Muestra cuántos turnos quedan.
    }

    /**
     * Actualiza la lista visible del inventario.
     *
     * Primero borra lo que se estaba mostrando y luego vuelve a añadir
     * todos los objetos que hay actualmente en el inventario real del jugador.
     */
    private void actualizarInventario() {
        inventarioView.getItems().clear(); // Limpia la lista visual para que no se dupliquen objetos al refrescar.
        for (int i = 0; i < motor.getJugador().getInventario().tamaño(); i++) { // Recorre el inventario desde el primer objeto hasta el último.
            Objeto objeto = motor.getJugador().getInventario().obtener(i); // Obtiene el objeto real que está en la posición i del inventario.
            inventarioView.getItems().add(objeto.getNombre() + " (" + objeto.getTipo() + ")"); // Añade a la lista un texto con el nombre y tipo del objeto.
        }
    }

    /**
     * Actualiza el área del registro de eventos.
     *
     * El registro viene del motor y se muestra completo en el TextArea.
     */
    private void actualizarRegistro() {
        areaRegistro.setText(motor.getRegistro().comoTexto()); // Coloca en el área de texto todos los mensajes guardados en el registro del juego.
        areaRegistro.setScrollTop(Double.MAX_VALUE); // Baja el scroll al final para que se vea el último mensaje añadido.
    }

    /**
     * Muestra una ventana emergente de error.
     *
     * @param mensaje Texto concreto que explica qué ha fallado.
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Crea una alerta de tipo error, con icono y estilo de error.
        alert.setTitle("Error en la Acción"); // Pone el título de la ventana emergente.
        alert.setHeaderText(null); // Elimina el encabezado para que solo se vea el mensaje principal.
        alert.setContentText(mensaje); // Coloca el mensaje recibido como contenido de la alerta.
        alert.showAndWait(); // Muestra la alerta y espera a que el usuario la cierre.
    }

    /**
     * Muestra una ventana emergente informativa.
     *
     * @param titulo Título de la ventana emergente.
     * @param mensaje Mensaje que se quiere enseñar al usuario.
     */
    private void mostrarMensajeInformativo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Crea una alerta informativa, no de error.
        alert.setTitle(titulo); // Pone el título recibido por parámetro.
        alert.setHeaderText(null); // Quita el encabezado para simplificar la ventana.
        alert.setContentText(mensaje); // Pone el mensaje recibido como contenido principal.
        alert.showAndWait(); // Muestra la alerta y espera a que el usuario pulse aceptar/cerrar.
    }

    /**
     * Devuelve el nodo raíz de la interfaz.
     *
     * Normalmente el Main llama a este método para meter esta ventana
     * dentro de una Scene de JavaFX.
     *
     * @return el BorderPane principal, pero devuelto como Parent porque JavaFX acepta ese tipo para construir escenas.
     */
    public Parent getRoot() {
        return root; // Devuelve el panel principal ya construido.
    }
}

