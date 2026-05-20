package com.example.practicafinal;

import Juego.Celda;
import Juego.Habitacion;
import Juego.JuegoException;
import Juego.MotorJuego;
import Juego.PersistenciaJson;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

public class HelloApplication extends Application {
    private MotorJuego motor;
    private GridPane tablero;
    private Label estadoJugador;
    private Label ayudaCamino;
    private TextArea registro;

    @Override
    public void start(Stage stage) throws IOException {
        motor = MotorJuego.crearPartidaDemo();
        BorderPane raiz = new BorderPane();
        raiz.setPadding(new Insets(10));

        tablero = new GridPane();
        tablero.setHgap(4);
        tablero.setVgap(4);

        estadoJugador = new Label();
        ayudaCamino = new Label();
        registro = new TextArea();
        registro.setEditable(false);
        registro.setPrefRowCount(10);

        VBox lateral = new VBox(8, estadoJugador, ayudaCamino, crearAcciones(), new Label("Log"), registro);
        lateral.setPadding(new Insets(0, 0, 0, 12));
        lateral.setPrefWidth(330);

        raiz.setCenter(tablero);
        raiz.setRight(lateral);
        refrescar();

        Scene scene = new Scene(raiz, 920, 620);
        stage.setTitle("Practica Final - Juego de Habitaciones");
        stage.setScene(scene);
        stage.show();
    }

    private HBox crearAcciones() {
        Button guardar = new Button("Guardar JSON");
        guardar.setOnAction(e -> {
            try {
                new PersistenciaJson().guardarEstado(motor, Path.of("partida-guardada.json"));
                motor.getRegistro().registrar("Partida guardada en partida-guardada.json.");
                refrescar();
            } catch (IOException ex) {
                motor.getRegistro().registrar("Error guardando JSON: " + ex.getMessage());
                refrescar();
            }
        });

        Button cargar = new Button("Cargar JSON");
        cargar.setOnAction(e -> {
            try {
                motor = new PersistenciaJson().cargarEstado(Path.of("partida-guardada.json"));
                refrescar();
            } catch (IOException ex) {
                motor.getRegistro().registrar("Error cargando JSON: " + ex.getMessage());
                refrescar();
            }
        });
        return new HBox(8, guardar, cargar);
    }

    private void refrescar() {
        tablero.getChildren().clear();
        Habitacion habitacion = motor.getHabitacionActual();
        for (int fila = 0; fila < habitacion.getFilas(); fila++) {
            for (int columna = 0; columna < habitacion.getColumnas(); columna++) {
                Button boton = new Button(textoCelda(habitacion.getCelda(fila, columna), fila, columna));
                boton.setMinSize(72, 58);
                final int f = fila;
                final int c = columna;
                boton.setOnAction(e -> ejecutarClick(f, c));
                tablero.add(boton, columna, fila);
            }
        }
        Object[] camino = motor.getCaminoMinimoSalida();
        String ayuda = camino == null ? "Sin camino a salida" : "Habitaciones minimas a salida: " + camino[0];
        estadoJugador.setText(motor.getJugador() + "\nTurnos: " + motor.getTurnosRestantes()
                + "\nHabitacion: " + habitacion.getId()
                + "\nClick celda vacia: mover. Click objeto/enemigo adyacente: recoger/atacar.");
        ayudaCamino.setText(ayuda);
        registro.setText(motor.getRegistro().comoTexto());
    }

    private String textoCelda(Celda celda, int fila, int columna) {
        if (motor.getJugador().getPosicionX() == fila && motor.getJugador().getPosicionY() == columna) {
            return "J";
        }
        switch (celda.getTipo()) {
            case VACIA: return ".";
            case ENEMIGO: return "E";
            case OBJETO: return "O";
            case PUERTA: return "P";
            case TRAMPA: return "T";
            case SALIDA: return "S";
            default: return "?";
        }
    }

    private void ejecutarClick(int fila, int columna) {
        try {
            Celda celda = motor.getHabitacionActual().getCelda(fila, columna);
            int distancia = Math.abs(fila - motor.getJugador().getPosicionX()) + Math.abs(columna - motor.getJugador().getPosicionY());
            if (distancia == 1 && celda.getTipo() == Celda.Tipo.ENEMIGO) {
                motor.atacarAdyacente(fila, columna);
            } else if (distancia == 1 && celda.getTipo() == Celda.Tipo.OBJETO) {
                motor.recogerObjetoAdyacente(fila, columna);
            } else {
                motor.moverJugador(fila, columna);
            }
        } catch (JuegoException ex) {
            motor.getRegistro().registrar("Accion no valida: " + ex.getMessage());
        }
        refrescar();
    }
}
