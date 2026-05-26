package Interfaz; // indica el paquete al que pertenece esta clase.

import javafx.application.Application; // importa una clase externa necesaria para este archivo.
import javafx.scene.Scene; // importa una clase externa necesaria para este archivo.
import javafx.stage.Stage; // importa una clase externa necesaria para este archivo.

/**
 * Clase de arranque de la aplicación JavaFX. Crea la ventana principal y lanza la interfaz gráfica.
 *
 * Comentarios añadidos para explicar la función de la clase, sus variables
 * y los bloques principales de código sin cambiar la lógica original.
 */
public class Main extends Application { // declara una clase que agrupa datos y métodos relacionados.

    @Override
    /**
     * Método principal de JavaFX que se ejecuta al abrir la aplicación.
     */
    public void start(Stage stage) {

        VentanaJuego ventana = new VentanaJuego(); // crea un nuevo objeto para poder usarlo después.

        Scene escena = new Scene(ventana.getRoot(), 1200, 700); // crea un nuevo objeto para poder usarlo después.

        stage.setTitle("Juego de Habitaciones"); // ejecuta una llamada a un método para realizar una acción concreta.
        stage.setScene(escena); // ejecuta una llamada a un método para realizar una acción concreta.
        stage.show(); // ejecuta una llamada a un método para realizar una acción concreta.
    }

    /**
     * Punto de entrada del programa.
     */
    public static void main(String[] args) {
        launch(args); // ejecuta una llamada a un método para realizar una acción concreta.
    }
}