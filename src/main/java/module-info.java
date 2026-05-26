module com.example.practicafinal {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    exports Interfaz;
    exports Juego;

    opens Interfaz to javafx.fxml;
}