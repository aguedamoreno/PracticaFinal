package Interfaz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        VentanaJuego ventana = new VentanaJuego();

        Scene escena = new Scene(ventana.getRoot(), 1200, 700);

        stage.setTitle("Juego de Habitaciones");
        stage.setScene(escena);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}