package Interfaz;

import Juego.*;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class VentanaJuego {

    private BorderPane root;
    private GridPane gridMapa;
    private TextArea areaRegistro;

    private Label vidaLabel;
    private Label ataqueLabel;
    private Label defensaLabel;
    private Label turnosLabel;

    private ListView<String> inventarioView;

    private Button moverBtn;
    private Button atacarBtn;
    private Button usarBtn;
    private Button recogerBtn;
    private Button pasarTurnoBtn;

    private MotorJuego motor;

    // --- NUEVAS VARIABLES PARA SELECCIÓN DE CASILLAS ---
    private int filaSeleccionada = -1;
    private int columnaSeleccionada = -1;
    private Button botonSeleccionadoAnterior = null;

    public VentanaJuego() {
        motor = MotorJuego.crearPartidaDemo();
        inicializarComponentes();
        configurarAccionesBotones(); // <- NUEVA LLAMADA
        construirVentana();
        actualizarVista();
    }

    private void inicializarComponentes() {
        root = new BorderPane();

        gridMapa = new GridPane();
        gridMapa.setHgap(2);
        gridMapa.setVgap(2);
        gridMapa.setPadding(new Insets(10));

        areaRegistro = new TextArea();
        areaRegistro.setEditable(false);
        areaRegistro.setPrefHeight(180);

        vidaLabel = new Label();
        ataqueLabel = new Label();
        defensaLabel = new Label();
        turnosLabel = new Label();

        inventarioView = new ListView<>();
        inventarioView.setPrefWidth(200);

        moverBtn = new Button("Mover a Selección");
        atacarBtn = new Button("Atacar Enemigo");
        usarBtn = new Button("Usar Objeto Seleccionado");
        recogerBtn = new Button("Recoger Objeto");
        pasarTurnoBtn = new Button("Pasar Turno");
    }

    /**
     * NUEVO METODO: Conecta los botones inferiores con la lógica del MotorJuego
     */
    private void configurarAccionesBotones() {

        // ACCIÓN DEL BOTÓN RECOGER OBJETO
        recogerBtn.setOnAction(e -> {
            if (filaSeleccionada == -1 || columnaSeleccionada == -1) {
                mostrarError("Primero debes seleccionar una casilla en el mapa.");
                return;
            }
            try {
                // Ejecutamos la acción en el motor
                motor.recogerObjetoAdyacente();
                // Reseteamos selección tras el éxito
                limpiarSeleccion();
                actualizarVista();
            } catch (JuegoException ex) {
                mostrarError(ex.getMessage());
            }
        });

        // ACCIÓN DEL BOTÓN ATACAR
        atacarBtn.setOnAction(e -> {
            if (filaSeleccionada == -1 || columnaSeleccionada == -1) {
                mostrarError("Primero debes seleccionar una casilla donde haya un enemigo.");
                return;
            }
            try {
                motor.atacarAdyacente(filaSeleccionada, columnaSeleccionada);
                limpiarSeleccion();
                actualizarVista();
            } catch (JuegoException ex) {
                mostrarError(ex.getMessage());
            }
        });

        // ACCIÓN DEL BOTÓN MOVER
        moverBtn.setOnAction(e -> {
            if (filaSeleccionada == -1 || columnaSeleccionada == -1) {
                mostrarError("Primero debes seleccionar a qué casilla te quieres mover.");
                return;
            }
            try {
                motor.moverJugador(filaSeleccionada, columnaSeleccionada);
                limpiarSeleccion();
                actualizarVista();
            } catch (JuegoException ex) {
                mostrarError(ex.getMessage());
            }
        });

        // ACCIÓN DEL BOTÓN USAR OBJETO DEL INVENTARIO
        usarBtn.setOnAction(e -> {
            int indiceSeleccionado = inventarioView.getSelectionModel().getSelectedIndex();
            if (indiceSeleccionado == -1) {
                mostrarError("Debes seleccionar un objeto de la lista de tu inventario.");
                return;
            }
            try {
                motor.usarObjeto(indiceSeleccionado);
                actualizarVista();
            } catch (JuegoException ex) {
                mostrarError(ex.getMessage());
            }
        });

        // ACCION DEL BOTÓN PASARTURNO
        pasarTurnoBtn.setOnAction(e -> {
            motor.finalizarTurnoVoluntariamente(); // Llama al motor para pasar de turno
            actualizarVista();                     // Refresca el mapa, la vida y los turnos restantes
            actualizarRegistro();                  // Muestra el texto en el panel de la derecha

            // Opcional: limpiar la selección de casillas para el siguiente turno
            filaSeleccionada = -1;
            columnaSeleccionada = -1;
            if (botonSeleccionadoAnterior != null) {
                botonSeleccionadoAnterior.setStyle("");
                botonSeleccionadoAnterior = null;
            }
        });
    }

    private void limpiarSeleccion() {
        filaSeleccionada = -1;
        columnaSeleccionada = -1;
        if (botonSeleccionadoAnterior != null) {
            botonSeleccionadoAnterior.setStyle(""); // Quitar el borde de marcado
            botonSeleccionadoAnterior = null;
        }
    }

    private void construirVentana() {
        VBox panelDerecho = new VBox(10);
        panelDerecho.setPadding(new Insets(10));
        panelDerecho.getChildren().addAll(
                new Label("--- ESTADO JUGADOR ---"),
                vidaLabel, ataqueLabel, defensaLabel, turnosLabel,
                new Label("--- INVENTARIO ---"),
                inventarioView,
                usarBtn
        );

        HBox panelInferiorBotones = new HBox(10);
        panelInferiorBotones.setPadding(new Insets(10));
        panelInferiorBotones.getChildren().addAll(moverBtn, recogerBtn, atacarBtn, pasarTurnoBtn);

        VBox panelInferiorCompleto = new VBox(5);
        panelInferiorCompleto.getChildren().addAll(panelInferiorBotones, areaRegistro);

        root.setCenter(gridMapa);
        root.setRight(panelDerecho);
        root.setBottom(panelInferiorCompleto);
    }

    public void actualizarVista() {
        actualizarMapa();
        actualizarJugador();
        actualizarInventario();
        actualizarRegistro();
        verificarFinPartida();
    }

    private void actualizarMapa() {
        gridMapa.getChildren().clear();
        Habitacion hab = motor.getHabitacionActual();
        Jugador jugador = motor.getJugador();

        for (int i = 0; i < hab.getFilas(); i++) {
            for (int j = 0; j < hab.getColumnas(); j++) {
                Celda celda = hab.getCelda(i, j);
                Button btnCelda = new Button();
                btnCelda.setPrefSize(65, 65);

                // Estilos visuales según el tipo de contenido
                if (i == jugador.getPosicionX() && j == jugador.getPosicionY()) {
                    btnCelda.setText("J"); // Jugador
                    btnCelda.setStyle("-fx-background-color: #87CEEB; -fx-font-weight: bold;");
                } else {
                    switch (celda.getTipo()) {
                        case VACIA:
                            btnCelda.setText("");
                            break;
                        case OBJETO:
                            btnCelda.setText("O");
                            btnCelda.setStyle("-fx-background-color: #FFE4B5;");
                            break;
                        case ENEMIGO:
                            btnCelda.setText("E");
                            btnCelda.setStyle("-fx-background-color: #FFB6C1;");
                            break;
                        case TRAMPA:
                            btnCelda.setText("T");
                            btnCelda.setStyle("-fx-background-color: #D3D3D3;");
                            break;
                        case PUERTA:
                            btnCelda.setText("P");
                            btnCelda.setStyle("-fx-background-color: #98FB98;");
                            break;
                        case SALIDA:
                            btnCelda.setText("S");
                            btnCelda.setStyle("-fx-background-color: #GOLD;");
                            break;
                    }
                }

                // --- GESTIÓN DEL CLICK EN EL MAPA ---
                final int f = i;
                final int c = j;
                btnCelda.setOnAction(e -> {
                    // Si ya había un botón seleccionado antes, le quitamos el borde rojo
                    if (botonSeleccionadoAnterior != null) {
                        botonSeleccionadoAnterior.setStyle(botonSeleccionadoAnterior.getStyle().replace("-fx-border-color: red; -fx-border-width: 2px;", ""));
                    }

                    // Guardamos la nueva posición seleccionada
                    filaSeleccionada = f;
                    columnaSeleccionada = c;
                    botonSeleccionadoAnterior = btnCelda;

                    // Le ponemos un borde rojo llamativo al botón clicado para que sepamos cuál es
                    btnCelda.setStyle(btnCelda.getStyle() + "-fx-border-color: red; -fx-border-width: 2px;");
                });

                gridMapa.add(btnCelda, j, i); // En GridPane se añade (columna, fila)
            }
        }
    }

    private void verificarFinPartida() {
        if (motor.isVictoria()) {
            mostrarMensajeInformativo("¡VICTORIA!", "¡Enhorabuena! Has logrado escapar de la mazmorra.");
            desactivarControles();
        } else if (motor.isDerrota()) {
            mostrarMensajeInformativo("GAME OVER", "Has sido derrotado. Inténtalo de nuevo.");
            desactivarControles();
        }
    }

    private void desactivarControles() {
        gridMapa.setDisable(true);
        moverBtn.setDisable(true);
        atacarBtn.setDisable(true);
        recogerBtn.setDisable(true);
        usarBtn.setDisable(true);
    }

    private void actualizarJugador() {
        vidaLabel.setText("Vida: " + motor.getJugador().getVidaActual() + "/" + motor.getJugador().getVidaMax());
        ataqueLabel.setText("Ataque: " + motor.getJugador().getAtaqueTotal());
        defensaLabel.setText("Defensa: " + motor.getJugador().getDefensaTotal());
        turnosLabel.setText("Turnos restantes: " + motor.getTurnosRestantes());
    }

    private void actualizarInventario() {
        inventarioView.getItems().clear();
        for (int i = 0; i < motor.getJugador().getInventario().tamaño(); i++) {
            Objeto objeto = motor.getJugador().getInventario().obtener(i);
            inventarioView.getItems().add(objeto.getNombre() + " (" + objeto.getTipo() + ")");
        }
    }

    private void actualizarRegistro() {
        areaRegistro.setText(motor.getRegistro().comoTexto());
        areaRegistro.setScrollTop(Double.MAX_VALUE); // Auto-scroll hacia abajo
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error en la Acción");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarMensajeInformativo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public Parent getRoot() {
        return root;
    }
}