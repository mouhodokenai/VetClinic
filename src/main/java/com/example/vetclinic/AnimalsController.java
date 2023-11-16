package com.example.vetclinic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.example.vetclinic.StockExchangeDB.statement;

public class AnimalsController extends SceneLoader {
    @FXML
    private TableColumn<Animals, String> nameColumn;
    @FXML
    private TableColumn<Animals, String> typeColumn;
    @FXML
    private TableColumn<Animals, String> breedColumn;
    @FXML
    private TableColumn<Animals, String> ownerColumn;
    @FXML
    private TableColumn<Animals, String> contactColumn;
    @FXML
    private TableColumn<Animals, Integer> numberColumn;
    @FXML
    private TableColumn<Animals, CheckBox> checkColumn;
    @FXML
    private Button delete;
    @FXML
    private Button edit;
    @FXML
    private Label warningLabel;
    @FXML
    private Label Title;
    @FXML
    private TableView<Animals> tableView;
    public static ArrayList<String> names = new ArrayList<>();
    @FXML
    private AnchorPane patientsRoot;
    ObservableList<Animals> dataAnimals = FXCollections.observableArrayList();

    public void Add() throws IOException {
        loadNewScene("animalsInserter.fxml", patientsRoot);
    }
    public void Show() throws SQLException {
        String queryS = "SELECT * FROM animals";
        ResultSet resultSet = statement.executeQuery(queryS);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Animals, String>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Animals, String>("type"));
        breedColumn.setCellValueFactory(new PropertyValueFactory<Animals, String>("breed"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<Animals, String>("owner"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<Animals, String>("contacts"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<Animals, Integer>("number"));
        checkColumn.setCellValueFactory(new PropertyValueFactory<Animals, CheckBox>("checkBox"));
        while (resultSet.next()) {
            CheckBox checkBox = new CheckBox();
            Animals animal = new Animals();
            animal.setName(resultSet.getString("name"));
            animal.setType(resultSet.getString("type"));
            animal.setBreed(resultSet.getString("breed"));
            animal.setOwner(resultSet.getString("owner"));
            animal.setContacts(resultSet.getString("contact"));
            animal.setNumber(resultSet.getInt("id"));
            animal.setCheckBox(checkBox);

            dataAnimals.add(animal);
        }
    }

    public void initialize() throws SQLException {
        Show();
        tableView.setItems(dataAnimals);
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 ) {
                Animals selectedItem = tableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    DiseaseHistoryController.setId(selectedItem.getNumber());
                    DiseaseHistoryController.setNam(selectedItem.getName());
                    DiseaseHistoryController.setOwner(selectedItem.getOwner());
                    DiseaseHistoryController.setType(selectedItem.getType());
                    try {
                        SceneLoader.loadNewScene("disease_history.fxml", patientsRoot);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void Back() throws IOException {
        loadNewScene("menu.fxml", patientsRoot);
    }

    public void Edit() throws IOException {
        int c = 0;
        for (int i = 0; i < tableView.getItems().size() ; i++){
            if (tableView.getItems().get(i).getCheckBox().isSelected()){
                c++;
            }
        }
        if (c == 1){
            for (int i = 0; i < tableView.getItems().size() ; i++) {
                if (tableView.getItems().get(i).getCheckBox().isSelected()) {
                    int idE = tableView.getItems().get(i).getNumber();
                    AnimalsInserterController.getEditId(idE);
                    AnimalsInserterController.editOrAdd = true;
                    SceneLoader.loadNewScene("animalsInserter.fxml", patientsRoot);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Выберете одну запись!");
            alert.showAndWait();
        }
    }

    public void Delete() {
        boolean isAnySelected = false;
        for (int i = 0; i < tableView.getItems().size(); i++) {
            if (tableView.getItems().get(i).getCheckBox().isSelected()) {
                isAnySelected = true;
                break;
            }
        }
        if (!isAnySelected) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Выберите хотя бы одну запись для удаления.");
            alert.showAndWait();
        } else {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Подтверждение удаления");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Вы уверены, что хотите удалить выбранные записи?");
            ButtonType okButton = new ButtonType("Да");
            ButtonType cancelButton = new ButtonType("Нет");

            confirmAlert.getButtonTypes().setAll(okButton, cancelButton);

            confirmAlert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == okButton) {
                    for (int i = 0; i < tableView.getItems().size(); i++) {
                        if (tableView.getItems().get(i).getCheckBox().isSelected()) {
                            int idD = tableView.getItems().get(i).getNumber();
                            tableView.getItems().remove(tableView.getItems().get(i));
                            try {
                                String deleteQuery = "delete from animals where id = " + idD;
                                statement.execute(deleteQuery);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            });
        }
    }
}
