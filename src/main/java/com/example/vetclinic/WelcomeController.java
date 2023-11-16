package com.example.vetclinic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {
    @FXML
    private AnchorPane rootWelcome;
    @FXML
    private Label welcomeText;

    public void switchToAdminScene() throws IOException {
        SceneLoader.loadNewScene("authorization.fxml", rootWelcome);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}