module com.example.tudien {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires freetts;


    opens com.example.tudien to javafx.fxml;
    exports com.example.tudien;
}