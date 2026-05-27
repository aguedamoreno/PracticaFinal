package Interfaz;

import Juego.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Clase que construye y actualiza la interfaz gráfica JavaFX: mapa, botones, inventario, estadísticas y mensajes
 */
public class VentanaJuego {

    // --- CONTENEDORES PRINCIPALES DE LA FACHADA ---
    private BorderPane root; // organiza la pantalla (Centro, Derecha, Abajo)
    private GridPane gridMapa; // rejilla visual que representa el mapa bidimensional de la habitación
    private TextArea areaRegistro; // cuadro de texto donde el jugador lee la bitácora de eventos del juego

    // --- INDICADORES DE ESTADO  ---
    private Label vidaLabel; // muestra la salud actual y máxima del jugador
    private Label ataqueLabel; // muestra el poder ofensivo total del jugador
    private Label defensaLabel; // muestra la capacidad de mitigación de daño del jugador
    private Label turnosLabel; // muestra la cuenta atrás de turnos antes de perder la partida

    private ListView<String> inventarioView; // Lista desplegable con los objetos recogidos por el jugador

    // --- BOTONES DE INTERACCIÓN ---
    private Button moverBtn; // botón para desplazar al héroe a la casilla seleccionada
    private Button atacarBtn; // botón para iniciar un combate contra un monstruo adyacente
    private Button usarBtn; // botón para consumir o equipar un objeto del inventario
    private Button recogerBtn; // botón para levantar tesoros, llaves u objetos del suelo
    private Button pasarTurnoBtn; // botón para ceder la iniciativa y permitir que el juego avance
    private Button guardarBtn; // guardar el progreso actual en un archivo local
    private Button cargarBtn; // recuperar una partida guardada previamente

    // --- COMPONENTES DEL SISTEMA ---
    private MotorJuego motor; // el núcleo del juego que procesa las reglas, movimientos y combates
    private PersistenciaJson persistenciaJson; // gestor de archivos para guardar y cargar datos en formato JSON
    private static final Path RUTA_GUARDADO = Paths.get(System.getProperty("user.home"), "partida_guardada.json"); // ubicación en el disco del archivo de salvado

    // --- SISTEMA DE SELECCIÓN TÁCTICA DEL MAPA ---
    private int filaSeleccionada = -1; // coordenada X de la celda del mapa que el jugador ha pinchado
    private int columnaSeleccionada = -1; // coordenada Y de la celda del mapa que el jugador ha pinchado
    private Button botonSeleccionadoAnterior = null; // guarda la referencia del último botón clickeado para poder quitarle el borde de selección

    // --- ESTILOS VISUALES DE LA INTERFAZ ---
    private static final String COLOR_FONDO = "#0B1120"; // color principal del fondo oscuro de la mazmorra
    private static final String COLOR_PANEL = "#111827"; // color de los paneles laterales e inferiores
    private static final String COLOR_PANEL_CLARO = "#1F2937"; // color usado para tarjetas y bloques interiores
    private static final String COLOR_ACENTO = "#F97316"; // naranja usado para títulos, brillos y detalles
    private static final String COLOR_TEXTO = "#F8FAFC"; // color claro para los textos principales

    /** Constructor: Inicia la partida demo y monta los elementos visuales en la pantalla
     */
    public VentanaJuego() {
        motor = MotorJuego.crearPartidaDemo(); // genera un mapa de prueba con un jugador, monstruos y objetos
        persistenciaJson = new PersistenciaJson(); // prepara el sistema de archivos de guardado
        inicializarComponentes(); // crea las etiquetas, botones y paneles vacíos
        configurarAccionesBotones(); // asigna los comportamientos y mecánicas a cada botón del juego
        construirVentana(); // ensambla los componentes en el diseño visual definitivo
        actualizarVista(); // dibuja por primera vez el mapa, estadísticas e inventario del jugador
    }

    /** Inicializa los componentes de la interfaz de usuario con sus propiedades base
     */
    private void inicializarComponentes() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: radial-gradient(center 50% 20%, radius 80%, #1E293B, " + COLOR_FONDO + ");");

        gridMapa = new GridPane();
        gridMapa.setHgap(5); // separación horizontal entre las casillas del mapa
        gridMapa.setVgap(5); // separación vertical entre las casillas del mapa
        gridMapa.setPadding(new Insets(20));
        gridMapa.setAlignment(Pos.CENTER);
        gridMapa.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #334155, #111827);" +
                        "-fx-background-radius: 22;" +
                        "-fx-border-color: #F97316;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 22;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 22, 0.25, 0, 8);"
        );

        areaRegistro = new TextArea();
        areaRegistro.setEditable(false); // evita que el jugador pueda modificar el texto de las acciones y movimientos
        areaRegistro.setPrefHeight(185);
        areaRegistro.setWrapText(true);
        areaRegistro.setStyle(
                "-fx-control-inner-background: #020617;" +
                        "-fx-text-fill: #E2E8F0;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-family: 'Consolas';" +
                        "-fx-border-color: #475569;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-effect: innershadow(gaussian, rgba(249,115,22,0.18), 12, 0.3, 0, 0);"
        );

        vidaLabel = new Label();
        ataqueLabel = new Label();
        defensaLabel = new Label();
        turnosLabel = new Label();

        vidaLabel.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 6 10 6 10; -fx-background-color: rgba(15,23,42,0.75); -fx-background-radius: 10;");
        ataqueLabel.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 6 10 6 10; -fx-background-color: rgba(15,23,42,0.75); -fx-background-radius: 10;");
        defensaLabel.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 6 10 6 10; -fx-background-color: rgba(15,23,42,0.75); -fx-background-radius: 10;");
        turnosLabel.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 6 10 6 10; -fx-background-color: rgba(15,23,42,0.75); -fx-background-radius: 10;");

        inventarioView = new ListView<>();
        inventarioView.setPrefWidth(230);
        inventarioView.setPrefHeight(210);
        inventarioView.setStyle(
                "-fx-control-inner-background: #020617;" +
                        "-fx-text-fill: #F8FAFC;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #475569;" +
                        "-fx-border-radius: 14;" +
                        "-fx-padding: 4;"
        );

        // textos que verá el jugador en su panel de acciones
        moverBtn = new Button("🧭 Mover");
        atacarBtn = new Button("⚔ Atacar");
        usarBtn = new Button("✨ Usar Objeto");
        recogerBtn = new Button("💎 Recoger");
        pasarTurnoBtn = new Button("⏳ Pasar Turno");
        guardarBtn = new Button("💾 Guardar");
        cargarBtn = new Button("📂 Cargar");

        aplicarEstiloBoton(moverBtn);
        aplicarEstiloBoton(atacarBtn);
        aplicarEstiloBoton(usarBtn);
        aplicarEstiloBoton(recogerBtn);
        aplicarEstiloBoton(pasarTurnoBtn);
        aplicarEstiloBoton(guardarBtn);
        aplicarEstiloBoton(cargarBtn);
    }

    /** Aplica un estilo visual común a los botones para que la interfaz tenga un aspecto más uniforme
     */
    private void aplicarEstiloBoton(Button boton) {
        String estiloNormal =
                "-fx-background-color: linear-gradient(to bottom, #F97316, #C2410C);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 12px;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #FDBA74;" +
                        "-fx-border-radius: 14;" +
                        "-fx-padding: 9 14 9 14;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 10, 0.2, 0, 3);";

        String estiloHover =
                "-fx-background-color: linear-gradient(to bottom, #FDBA74, #F97316);" +
                        "-fx-text-fill: #111827;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 12px;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #FFEDD5;" +
                        "-fx-border-radius: 14;" +
                        "-fx-padding: 9 14 9 14;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(249,115,22,0.65), 14, 0.35, 0, 4);";

        boton.setMinWidth(125);
        boton.setStyle(estiloNormal);
        boton.setOnMouseEntered(e -> boton.setStyle(estiloHover));
        boton.setOnMouseExited(e -> boton.setStyle(estiloNormal));
    }

    /** Vincula los botones de la interfaz con las reglas lógicas del motor del juego
     */
    private void configurarAccionesBotones() {

        // --- MECÁNICA: RECOGER OBJETO ---
        recogerBtn.setOnAction(e -> {
            if (filaSeleccionada == -1 || columnaSeleccionada == -1) {
                mostrarError("Primero debes seleccionar una casilla en el mapa.");
                return;
            }
            try {
                // ordena al motor transferir el objeto de la celda al inventario del jugador
                motor.recogerObjetoAdyacente(filaSeleccionada, columnaSeleccionada);
                limpiarSeleccion(); // desmarca la casilla para evitar acciones repetidas involuntarias
                actualizarVista(); // refresca la pantalla para mostrar que el objeto ya no está en el suelo
            } catch (JuegoException ex) {
                mostrarError(ex.getMessage()); // muestra alertas si el objeto está lejos o el inventario está lleno
            }
        });

        // --- MECÁNICA: ATACAR ---
        atacarBtn.setOnAction(e -> {
            if (filaSeleccionada == -1 || columnaSeleccionada == -1) {
                mostrarError("Primero debes seleccionar una casilla donde haya un enemigo.");
                return;
            }
            try {
                // el jugador ataca al enemigo en las coordenadas seleccionadas
                motor.atacarAdyacente(filaSeleccionada, columnaSeleccionada);
                limpiarSeleccion();
                actualizarVista(); // muestra si el enemigo ha muerto, se ha reducido su vida o si el jugador recibió contraataque
            } catch (JuegoException ex) {
                mostrarError(ex.getMessage()); // error si intentas atacar al aire o a casillas lejanas
            }
        });

        // --- MECÁNICA: MOVIMIENTO ---
        moverBtn.setOnAction(e -> {
            if (filaSeleccionada == -1 || columnaSeleccionada == -1) {
                mostrarError("Primero debes seleccionar a qué casilla te quieres mover.");
                return;
            }
            try {
                // intenta desplazar al jugador a la celda marcada
                motor.moverJugador(filaSeleccionada, columnaSeleccionada);
                limpiarSeleccion();
                actualizarVista(); // redibuja al jugador en su nueva posición y procesa si pisa trampas
            } catch (JuegoException ex) {
                mostrarError(ex.getMessage()); // error si la casilla está bloqueada o fuera del alcance de movimiento
            }
        });

        // --- MECÁNICA: USAR OBJETO DEL INVENTARIO ---
        usarBtn.setOnAction(e -> {
            int indiceSeleccionado = inventarioView.getSelectionModel().getSelectedIndex();
            if (indiceSeleccionado == -1) {
                mostrarError("Debes seleccionar un objeto de la lista de tu inventario.");
                return;
            }
            try {
                // el jugador consume una poción o se equipa un arma
                motor.usarObjeto(indiceSeleccionado);
                actualizarVista(); // aplica los cambios inmediatos en las estadísticas del jugador
            } catch (JuegoException ex) {
                mostrarError(ex.getMessage());
            }
        });

        // --- MECÁNICA: GESTIÓN DE TURNO ---
        pasarTurnoBtn.setOnAction(e -> {
            motor.finalizarTurnoVoluntariamente(); // consume un turno del jugador y permite acciones ambientales si las hubiera
            actualizarVista();
            actualizarRegistro();

            // resetea la selección táctica para empezar el nuevo turno desde cero
            filaSeleccionada = -1;
            columnaSeleccionada = -1;
            if (botonSeleccionadoAnterior != null) {
                botonSeleccionadoAnterior.setStyle("");
                botonSeleccionadoAnterior = null;
            }
        });

        // --- SISTEMA: GUARDADO DE PARTIDA ---
        guardarBtn.setOnAction(e -> {
            try {
                persistenciaJson.guardarEstado(motor, RUTA_GUARDADO); // serializa el estado de la mazmorra y el jugador en un archivo JSON
                actualizarRegistro();
                mostrarMensajeInformativo("Partida guardada", "La partida se ha guardado en el archivo " + RUTA_GUARDADO + ".");
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarError("No se ha podido guardar la partida: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
            }
        });

        // --- SISTEMA: CARGAR PARTIDA ---
        cargarBtn.setOnAction(e -> {
            try {
                motor = persistenciaJson.cargarEstado(RUTA_GUARDADO); // reconstruye la mazmorra y el héroe desde el archivo JSON
                limpiarSeleccion();
                activarControles(); // asegura que los botones funcionen por si la partida anterior había terminado en derrota
                actualizarVista();
                mostrarMensajeInformativo("Partida cargada", "La partida se ha cargado desde el archivo " + RUTA_GUARDADO + ".");
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarError("No se ha podido cargar la partida: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
            }
        });
    }

    /** Limpia las variables de selección táctica y remueve los efectos visuales de marcado del mapa
     */
    private void limpiarSeleccion() {
        filaSeleccionada = -1;
        columnaSeleccionada = -1;
        if (botonSeleccionadoAnterior != null) {
            botonSeleccionadoAnterior.setStyle(""); // quita el borde de selección del botón
            botonSeleccionadoAnterior = null;
        }
    }

    /** Diseña y distribuye espacialmente los paneles del juego en la ventana
     */
    private void construirVentana() {
        // panel lateral derecho: Información de estadísticas del jugador e inventario de equipo
        VBox panelDerecho = new VBox(12);
        panelDerecho.setPadding(new Insets(18));
        panelDerecho.setPrefWidth(270);
        panelDerecho.setStyle(
                "-fx-background-color: linear-gradient(to bottom, " + COLOR_PANEL_CLARO + ", " + COLOR_PANEL + ");" +
                        "-fx-border-color: #475569;" +
                        "-fx-border-width: 0 0 0 2;"
        );

        Label tituloJuego = new Label("🏰 DUNGEON CRAWLER");
        tituloJuego.setStyle("-fx-text-fill: #FFEDD5; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 8 0;");

        Label subtituloJuego = new Label("Explora, lucha y escapa");
        subtituloJuego.setStyle("-fx-text-fill: #CBD5E1; -fx-font-size: 11px; -fx-padding: 0 0 12 0;");

        Label tituloEstado = new Label("⚔ ESTADO DEL HÉROE");
        tituloEstado.setStyle("-fx-text-fill: #FDBA74; -fx-font-size: 15px; -fx-font-weight: bold;");

        VBox tarjetaEstado = new VBox(8);
        tarjetaEstado.setPadding(new Insets(12));
        tarjetaEstado.setStyle("-fx-background-color: rgba(15,23,42,0.78); -fx-background-radius: 16; -fx-border-color: #334155; -fx-border-radius: 16;");
        tarjetaEstado.getChildren().addAll(vidaLabel, ataqueLabel, defensaLabel, turnosLabel);

        Label tituloInventario = new Label("🎒 INVENTARIO");
        tituloInventario.setStyle("-fx-text-fill: #FDBA74; -fx-font-size: 15px; -fx-font-weight: bold;");

        Label tituloLeyenda = new Label("🗺 LEYENDA");
        tituloLeyenda.setStyle("-fx-text-fill: #FDBA74; -fx-font-size: 15px; -fx-font-weight: bold;");

        Label leyenda = new Label("🤠 Jugador   👹 Enemigo\n💎 Objeto     🚪 Puerta\n💀 Trampa    🏁 Salida\n▢ Borde naranja: puedes moverte");
        leyenda.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 12px; -fx-padding: 10; -fx-background-color: rgba(15,23,42,0.78); -fx-background-radius: 14; -fx-border-color: #334155; -fx-border-radius: 14;");

        panelDerecho.getChildren().addAll(
                tituloJuego,
                subtituloJuego,
                tituloEstado,
                tarjetaEstado,
                tituloInventario,
                inventarioView,
                usarBtn,
                tituloLeyenda,
                leyenda
        );

        //panel inferior: botones de comandos y acciones
        HBox panelInferiorBotones = new HBox(10);
        panelInferiorBotones.setPadding(new Insets(10));
        panelInferiorBotones.setAlignment(Pos.CENTER);
        panelInferiorBotones.setStyle("-fx-background-color: transparent;");
        panelInferiorBotones.getChildren().addAll(moverBtn, recogerBtn, atacarBtn, pasarTurnoBtn, guardarBtn, cargarBtn);

        // une los botones y el cuadro de texto en la zona inferior de la pantalla
        VBox panelInferiorCompleto = new VBox(5);
        panelInferiorCompleto.setPadding(new Insets(12));
        panelInferiorCompleto.setStyle(
                "-fx-background-color: linear-gradient(to right, " + COLOR_PANEL + ", #020617);" +
                        "-fx-border-color: #475569;" +
                        "-fx-border-width: 2 0 0 0;"
        );

        Label tituloRegistro = new Label("📜 Registro de la aventura");
        tituloRegistro.setStyle("-fx-text-fill: #FDBA74; -fx-font-size: 14px; -fx-font-weight: bold;");

        panelInferiorCompleto.getChildren().addAll(panelInferiorBotones, tituloRegistro, areaRegistro);

        // distribución final en el contenedor raíz
        VBox zonaCentral = new VBox(14);
        zonaCentral.setAlignment(Pos.CENTER);
        zonaCentral.setPadding(new Insets(22));
        zonaCentral.setStyle("-fx-background-color: transparent;");

        Label tituloMapa = new Label("✨ Habitación actual: mazmorra activa");
        tituloMapa.setStyle("-fx-text-fill: #FFEDD5; -fx-font-size: 20px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(249,115,22,0.55), 12, 0.4, 0, 2);");

        Label ayudaMapa = new Label("Selecciona una casilla iluminada para moverte o una casilla cercana para interactuar");
        ayudaMapa.setStyle("-fx-text-fill: #CBD5E1; -fx-font-size: 12px;");

        zonaCentral.getChildren().addAll(tituloMapa, ayudaMapa, gridMapa);

        root.setCenter(zonaCentral); // el mapa de la mazmorra ocupa el centro de la pantalla
        root.setRight(panelDerecho); // las estadísticas a la derecha
        root.setBottom(panelInferiorCompleto); // el registro y los controles abajo
    }

    /** Sincroniza y redibuja la interfaz gráfica completa con el estado interno del juego
     */
    public void actualizarVista() {
        actualizarMapa(); // redibuja las celdas, el jugador y los peligros
        actualizarJugador(); // actualiza la salud, el ataque y turnos del jugador en el HUD
        actualizarInventario(); // sincroniza la lista visual de ítems en las bolsas del jugador
        actualizarRegistro(); // agrega los últimos mensajes de combate o exploración
        verificarFinPartida(); // analiza si el jugador ha ganado (escapado) o muerto
    }

    /** Transforma la matriz lógica de la habitación en un mapa visual de botones interactivos con iconos
     */
    private void actualizarMapa() {
        gridMapa.getChildren().clear(); // vacía el mapa anterior para redibujarlo desde cero
        Habitacion hab = motor.getHabitacionActual();
        Jugador jugador = motor.getJugador();

        // recorrido de la matriz bidimensional de la habitación
        for (int i = 0; i < hab.getFilas(); i++) {
            for (int j = 0; j < hab.getColumnas(); j++) {
                Celda celda = hab.getCelda(i, j);
                Button btnCelda = new Button();
                btnCelda.setPrefSize(68, 68); // define el tamaño cuadrado de cada casilla del mapa
                btnCelda.setStyle("-fx-background-color: linear-gradient(to bottom, #E2E8F0, #CBD5E1); -fx-background-radius: 14; -fx-border-color: #94A3B8; -fx-border-radius: 14; -fx-font-size: 22px; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.22), 7, 0.2, 0, 2);");

                // --- RENDERIZADO VISUAL SEGÚN EL CONTENIDO DE LA CELDA ---
                if (i == jugador.getPosicionX() && j == jugador.getPosicionY()) {
                    btnCelda.setText("\uD83E\uDD20️"); // icono del jugador
                    btnCelda.setStyle("-fx-background-color: linear-gradient(to bottom, #7DD3FC, #0284C7); -fx-background-radius: 14; -fx-border-color: #E0F2FE; -fx-border-width: 2; -fx-border-radius: 14; -fx-font-size: 23px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(56,189,248,0.65), 14, 0.35, 0, 0);");
                } else {
                    switch (celda.getTipo()) {
                        case VACIA:
                            btnCelda.setText(""); // suelo normal transitable
                            break;
                        case OBJETO:
                            btnCelda.setText("💎"); // tesoro, poción o equipamiento en el suelo
                            btnCelda.setStyle("-fx-background-color: linear-gradient(to bottom, #FEF3C7, #FBBF24); -fx-background-radius: 14; -fx-border-color: #FDE68A; -fx-border-width: 2; -fx-border-radius: 14; -fx-font-size: 22px; -fx-effect: dropshadow(gaussian, rgba(251,191,36,0.45), 10, 0.3, 0, 0);");
                            break;
                        case ENEMIGO:
                            btnCelda.setText("👹"); // monstruo agresivo esperando combate
                            btnCelda.setStyle("-fx-background-color: linear-gradient(to bottom, #FCA5A5, #DC2626); -fx-background-radius: 14; -fx-border-color: #FEE2E2; -fx-border-width: 2; -fx-border-radius: 14; -fx-font-size: 22px; -fx-effect: dropshadow(gaussian, rgba(239,68,68,0.55), 12, 0.35, 0, 0);");
                            break;
                        case TRAMPA:
                            btnCelda.setText("\uD83D\uDC80"); // foso, pinchos o peligro oculto
                            btnCelda.setStyle("-fx-background-color: linear-gradient(to bottom, #C084FC, #7E22CE); -fx-background-radius: 14; -fx-border-color: #E9D5FF; -fx-border-width: 2; -fx-border-radius: 14; -fx-font-size: 22px; -fx-effect: dropshadow(gaussian, rgba(168,85,247,0.55), 12, 0.35, 0, 0);");
                            break;
                        case PUERTA:
                            btnCelda.setText("🚪"); // transición a una nueva zona o habitación de la mazmorra
                            btnCelda.setStyle("-fx-background-color: linear-gradient(to bottom, #BBF7D0, #16A34A); -fx-background-radius: 14; -fx-border-color: #DCFCE7; -fx-border-width: 2; -fx-border-radius: 14; -fx-font-size: 22px; -fx-effect: dropshadow(gaussian, rgba(34,197,94,0.5), 12, 0.35, 0, 0);");
                            break;
                        case SALIDA:
                            btnCelda.setText("🏁"); // meta final para escapar y ganar el juego
                            btnCelda.setStyle("-fx-background-color: linear-gradient(to bottom, #FBCFE8, #DB2777); -fx-background-radius: 14; -fx-border-color: #FCE7F3; -fx-border-width: 2; -fx-border-radius: 14; -fx-font-size: 22px; -fx-effect: dropshadow(gaussian, rgba(236,72,153,0.55), 12, 0.35, 0, 0);");
                            break;
                    }
                }

                // Si la casilla es alcanzable para moverse, se marca solo con un borde naranja clarito.
                if (!(i == jugador.getPosicionX() && j == jugador.getPosicionY())
                        && motor.esCeldaAlcanzableParaJugador(i, j)) {
                    btnCelda.setStyle(btnCelda.getStyle() + "-fx-border-color: #FDBA74; -fx-border-width: 4px; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(253,186,116,0.65), 14, 0.4, 0, 0);");
                }

                // --- GESTIÓN DE CLICKS (SELECCIÓN TÁCTICA DE CASILLAS) ---
                final int f = i;
                final int c = j;
                btnCelda.setOnAction(e -> {
                    // si el jugador pincha una celda teniendo otra seleccionada, le quita el borde de selección a la anterior
                    if (botonSeleccionadoAnterior != null) {
                        botonSeleccionadoAnterior.setStyle(botonSeleccionadoAnterior.getStyle().replace("-fx-border-color: #FFEDD5; -fx-border-width: 4px; -fx-border-radius: 14;", ""));
                    }

                    // registra las coordenadas de la celda elegida como objetivo de la próxima acción
                    filaSeleccionada = f;
                    columnaSeleccionada = c;
                    botonSeleccionadoAnterior = btnCelda;

                    // pinta un contorno oscuro y marcado alrededor de la casilla seleccionada por el jugador
                    btnCelda.setStyle(btnCelda.getStyle() + "-fx-border-color: #FFEDD5; -fx-border-width: 4px; -fx-border-radius: 14;");
                });

                gridMapa.add(btnCelda, j, i); // coloca el botón en la cuadrícula visual de JavaFX
            }
        }
    }

    /** Evalúa si las condiciones de fin de juego se han cumplido tras la última acción
     */
    private void verificarFinPartida() {
        if (motor.isVictoria()) {
            mostrarMensajeInformativo("¡VICTORIA!", "¡Enhorabuena! Has logrado escapar de la mazmorra.");
            desactivarControles(); // bloquea el juego tras ganar para evitar seguir jugando
        } else if (motor.isDerrota()) {
            mostrarMensajeInformativo("GAME OVER", "Has sido derrotado. Inténtalo de nuevo.");
            desactivarControles(); // bloquea los controles tras morir en la mazmorra
        }
    }

    /** Inhabilita la interacción con el mapa y los botones cuando la partida ha concluido
     */
    private void desactivarControles() {
        gridMapa.setDisable(true);
        moverBtn.setDisable(true);
        atacarBtn.setDisable(true);
        recogerBtn.setDisable(true);
        usarBtn.setDisable(true);
        pasarTurnoBtn.setDisable(true);
    }

    /** Habilita de nuevo los controles tácticos del juego (útil al cargar una partida en curso)
     */
    private void activarControles() {
        gridMapa.setDisable(false);
        moverBtn.setDisable(false);
        atacarBtn.setDisable(false);
        recogerBtn.setDisable(false);
        usarBtn.setDisable(false);
        pasarTurnoBtn.setDisable(false);
    }

    /** Modifica los textos informativos en el HUD con la vida, estadísticas y turnos del héroe
     */
    private void actualizarJugador() {
        vidaLabel.setText("❤️ Vida: " + motor.getJugador().getVidaActual() + "/" + motor.getJugador().getVidaMax());
        ataqueLabel.setText("⚔ Ataque: " + motor.getJugador().getAtaqueTotal());
        defensaLabel.setText("🛡 Defensa: " + motor.getJugador().getDefensaTotal());
        turnosLabel.setText("⏳ Turnos: " + motor.getTurnosRestantes());
    }

    /** Sincroniza la lista visual del inventario recorriendo los objetos que posee el jugador actualmente
     */
    private void actualizarInventario() {
        inventarioView.getItems().clear(); // Borra la lista gráfica anterior
        for (int i = 0; i < motor.getJugador().getInventario().tamaño(); i++) {
            Objeto objeto = motor.getJugador().getInventario().obtener(i);
            // Añade el nombre del ítem y su categoría (Ej: "Poción de Salud (CONSUMIBLE)")
            inventarioView.getItems().add(objeto.getNombre() + " (" + objeto.getTipo() + ")");
        }
    }

    /** Vuelca los mensajes de eventos y combates generados por el motor en el cuadro de texto inferior
     */
    private void actualizarRegistro() {
        areaRegistro.setText(motor.getRegistro().comoTexto());
        areaRegistro.setScrollTop(Double.MAX_VALUE); // Fuerza al scroll a bajar automáticamente para leer los últimos sucesos
    }

    /** Despliega una ventana emergente de alerta roja en caso de errores en las acciones
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error en la Acción");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /** Despliega una ventana emergente azul informativa para hitos (Cargar, Guardar, Victoria, Derrota)
     */
    private void mostrarMensajeInformativo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /** Proporciona el contenedor raíz del entorno visual para acoplarlo en la Escena principal de JavaFX
     */
    public Parent getRoot() {
        return root;
    }
}



