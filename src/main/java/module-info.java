module com.example.toymarket2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.example.toymarket2 to javafx.fxml;
    exports com.example.toymarket2;
}