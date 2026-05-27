module com.example.practicafinal {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    exports Interfaz;
    exports Juego;
    exports Juego.listas;

    opens Interfaz to javafx.fxml;
}