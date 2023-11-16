package com.example.vetclinic;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChoiceBoxExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Пример ChoiceBox");


        ChoiceBox<String> choiceBox = new ChoiceBox<>();


        ObservableList<String> items = FXCollections.observableArrayList(
                "Элемент 1",
                "Элемент 2",
                "Элемент 3",
                "Элемент 4");

        // Устанавливаем список элементов для ChoiceBox
        choiceBox.setItems(items);

        // Устанавливаем выбранный элемент (необязательно)
        choiceBox.setValue("Элемент 1");

        // Создаем макет и добавляем ChoiceBox
        VBox vbox = new VBox(choiceBox);
        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
