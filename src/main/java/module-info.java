module com.example.practicafinal {

    requires javafx.controls;
    requires javafx.fxml;

    exports Interfaz;
    exports Juego;

    opens Interfaz to javafx.fxml;
}