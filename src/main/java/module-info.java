module com.example.practicafinal {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.practicafinal to javafx.fxml;
    exports com.example.practicafinal;
}