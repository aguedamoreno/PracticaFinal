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

    private MotorJuego motor;

    public VentanaJuego() {

        motor = MotorJuego.crearPartidaDemo();

        inicializarComponentes();
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
        inventarioView.setPrefHeight(200);

        moverBtn = new Button("Mover");
        atacarBtn = new Button("Atacar");
        usarBtn = new Button("Usar Objeto");
        recogerBtn = new Button("Recoger");
    }

    private void construirVentana() {

        VBox panelJugador = new VBox(10,
                new Label("ESTADO JUGADOR"),
                vidaLabel,
                ataqueLabel,
                defensaLabel,
                turnosLabel,
                new Separator(),
                new Label("INVENTARIO"),
                inventarioView
        );

        panelJugador.setPadding(new Insets(10));
        panelJugador.setPrefWidth(250);

        HBox acciones = new HBox(10,
                moverBtn,
                atacarBtn,
                usarBtn,
                recogerBtn
        );

        acciones.setPadding(new Insets(10));

        VBox zonaInferior = new VBox(10,
                new Label("REGISTRO DEL JUEGO"),
                areaRegistro,
                acciones
        );

        zonaInferior.setPadding(new Insets(10));

        root.setCenter(gridMapa);
        root.setRight(panelJugador);
        root.setBottom(zonaInferior);
    }

    private void actualizarVista() {

        actualizarMapa();
        actualizarJugador();
        actualizarInventario();
        actualizarRegistro();
    }

    private void actualizarMapa() {

        gridMapa.getChildren().clear();

        Habitacion habitacion = motor.getHabitacionActual();

        for(int i = 0; i < habitacion.getFilas(); i++) {

            for(int j = 0; j < habitacion.getColumnas(); j++) {

                Button celda = new Button();
                celda.setMinSize(60, 60);

                if(i == motor.getJugador().getPosicionX() &&
                        j == motor.getJugador().getPosicionY()) {

                    celda.setText("J");
                    celda.setStyle("-fx-background-color: lightblue;");

                } else {

                    switch(habitacion.getCelda(i, j).getTipo()) {

                        case VACIA:
                            celda.setText(" ");
                            break;

                        case ENEMIGO:
                            celda.setText("E");
                            celda.setStyle("-fx-background-color: salmon;");
                            break;

                        case OBJETO:
                            celda.setText("O");
                            celda.setStyle("-fx-background-color: lightgreen;");
                            break;

                        case PUERTA:
                            celda.setText("P");
                            break;

                        case TRAMPA:
                            celda.setText("T");
                            break;

                        case SALIDA:
                            celda.setText("S");
                            celda.setStyle("-fx-background-color: gold;");
                            break;
                    }
                }

                int fila = i;
                int columna = j;

                celda.setOnAction(e -> {

                    try {

                        motor.moverJugador(fila, columna);
                        actualizarVista();

                    } catch(Exception ex) {

                        mostrarError(ex.getMessage());
                    }
                });

                gridMapa.add(celda, j, i);
            }
        }
    }

    private void actualizarJugador() {

        vidaLabel.setText(
                "Vida: " + motor.getJugador().getVidaActual()
        );

        ataqueLabel.setText(
                "Ataque: " + motor.getJugador().getAtaqueTotal()
        );

        defensaLabel.setText(
                "Defensa: " + motor.getJugador().getDefensaTotal()
        );

        turnosLabel.setText(
                "Turnos restantes: " + motor.getTurnosRestantes()
        );
    }

    private void actualizarInventario() {

        inventarioView.getItems().clear();

        for(int i = 0;
            i < motor.getJugador().getInventario().tamaño();
            i++) {

            Objeto objeto =
                    motor.getJugador().getInventario().obtener(i);

            inventarioView.getItems().add(
                    objeto.getNombre() +
                            " (" +
                            objeto.getTipo() +
                            ")"
            );
        }
    }

    private void actualizarRegistro() {

        areaRegistro.setText(
                motor.getRegistro().comoTexto()
        );
    }

    private void mostrarError(String mensaje) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }

    public Parent getRoot() {
        return root;
    }
}