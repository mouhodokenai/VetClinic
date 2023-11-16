package com.example.vetclinic;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneLoader {
    public static void loadNewScene(String fxml, Pane root) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource(fxml));
        Parent newRoot = loader.load();
        Scene newScene = new Scene(newRoot);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
    }
}
