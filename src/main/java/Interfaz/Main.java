package Interfaz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Clase de arranque de la aplicación JavaFX. Actúa como el "lanzador" del juego, despertando el motor gráfico y preparando el lienzo donde se jugará
 */
public class Main extends Application {

    @Override
    /** Punto de partida del entorno gráfico de JavaFX. Se encarga de levantar el escenario y encajar la interfaz de la mazmorra
     */
    public void start(Stage stage) {

        // invoca y construye la pantalla del juego (inicializa el mapa, botones de acción e inventario)
        VentanaJuego ventana = new VentanaJuego();

        // define las dimensiones de la ventana de juego y le introduce la interfaz
        Scene escena = new Scene(ventana.getRoot(), 1200, 700);

        stage.setTitle("Juego de Habitaciones - Dungeon Crawler"); // título que aparecerá en la barra superior de la ventana
        stage.setScene(escena); // monta la escena con los gráficos del juego en el escenario principal
        stage.show(); // despierta la ventana y la hace visible para que el jugador pueda empezar la partida
    }

    /** El metodo de entrada estándar de Java. Sirve como el interruptor de encendido que le dice al sistema operativo que ejecute el motor gráfico
     */
    public static void main(String[] args) {
        launch(args); // pone en marcha la infraestructura de JavaFX y llama automáticamente al metodo 'start'
    }
}