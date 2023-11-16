package com.example.vetclinic;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AuthorizationController extends SceneLoader {
    @FXML
    private AnchorPane authoRoot;
    @FXML
    private Label autho;
    @FXML
    private Button enter;
    @FXML
    private PasswordField enterpassword;
    String auto1 = "Dog123";
    String auto2;
    public static boolean flag = false;
    Timer timer = new Timer();

    @FXML
    protected void Login() throws IOException {
        auto2 = enterpassword.getText();
        if (!(auto1.equals(auto2))) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Неверный пароль");
            alert.setHeaderText(null);
            alert.setContentText("Введен еверный пароль! Попробуйте еще раз");
            alert.showAndWait();
        } else {
            autho.setText("Успешно!");
            enterpassword.setVisible(false);
            enter.setVisible(false);
            timer.scheduleAtFixedRate(new TimerTask() {
                int i = 2;
                public void run() {
                    i--;
                    if (i < 0) {
                        timer.cancel();
                        autho.setVisible(false);
                        flag = true;
                        Platform.runLater(() -> {
                            if (flag) {
                                try {
                                    switchToNewScene();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                }
            }, 0, 1000);
        }
    }
    public void switchToNewScene() throws IOException {
        SceneLoader.loadNewScene("menu.fxml", authoRoot);
    }
}
