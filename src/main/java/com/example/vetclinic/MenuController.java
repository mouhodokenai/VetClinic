package com.example.vetclinic;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MenuController extends SceneLoader {
    public Label menu;
    public AnchorPane menuRoot;

    public void viewPatients() throws IOException {
        SceneLoader.loadNewScene("animals.fxml", menuRoot);
    }
    public void viewVeterinarians() throws IOException {
        SceneLoader.loadNewScene("veterinarians.fxml", menuRoot);
    }
    public void viewServices() throws IOException {
        SceneLoader.loadNewScene("services.fxml", menuRoot);
    }
    public void viewAppointments() throws IOException {
        SceneLoader.loadNewScene("appointments.fxml", menuRoot);
    }

}
