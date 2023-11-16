module com.example.vetclinic {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires mysql.connector.java;
    requires com.jfoenix;

    opens com.example.vetclinic to javafx.fxml;
    exports com.example.vetclinic;
}

