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

public class ServicesController extends StockExchangeDB {
    @FXML
    private TableColumn<Services, String> nameColumn;
    @FXML
    private TableColumn<Services, String> dateColumn;
    @FXML
    private TableColumn<Services, String> coastColumn;
    @FXML
    private TableColumn<Services, String> animalColumn;
    @FXML
    private TableColumn<Services, String> vetColumn;
    @FXML
    private TableView<Services> tableView;
    @FXML
    private AnchorPane servicesRoot;
    @FXML
    private TableColumn<Services, String> diagnosisColumn;
    @FXML
    private TableColumn<Services, CheckBox> checkColumn;
    @FXML
    private TableColumn<Services, String> numberColumn;
    @FXML
    private Button edit;
    @FXML
    private Button delete;
    @FXML
    private Label Title;

    ObservableList<Services> dataServices = FXCollections.observableArrayList();

    public void Show() throws SQLException {
        String query = "select s.id, s.diagnosis, s.name as servname, v.name as vetname, an.name as anname, s.coast, s.date\n" +
                "from services s\n" +
                "join animals an \n" +
                "on s.animals_id = an.id\n" +
                "join veterinarians v\n" +
                "on v.id = s.veterinarians_id";
        ResultSet resultSet = statement.executeQuery(query);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name")); //сюда вставляем из класса!
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        animalColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
        vetColumn.setCellValueFactory(new PropertyValueFactory<>("veterinarian"));
        coastColumn.setCellValueFactory(new PropertyValueFactory<>("coast"));
        diagnosisColumn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        checkColumn.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
        while (resultSet.next()) {
            Services service = new Services();
            CheckBox checkBox = new CheckBox();
            service.setName(resultSet.getString("servname")); //а сюда из таблички
            service.setData(resultSet.getString("date"));
            service.setPatient(resultSet.getString("anname"));
            service.setVeterinarian(resultSet.getString("vetname"));
            service.setCoast(Double.parseDouble(resultSet.getString("coast")));
            service.setDiagnosis(resultSet.getString("diagnosis"));
            service.setNumber(resultSet.getInt("id"));
            service.setCheckBox(checkBox);
            dataServices.add(service);
        }
    }

    public void Add() throws IOException {
        SceneLoader.loadNewScene("servicesInserter.fxml", servicesRoot);
    }
    public void initialize() throws SQLException {
        Show();
        tableView.setItems(dataServices);

    }
    public void Back() throws IOException {
        SceneLoader.loadNewScene("menu.fxml", servicesRoot);
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
                    ServicesInserterController.getEditId(idE);
                    ServicesInserterController.editOrAdd = true;
                    SceneLoader.loadNewScene("servicesInserter.fxml", servicesRoot);
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
        // Проверяем, есть ли хотя бы одна запись, выбранная для удаления
        boolean isAnySelected = false;
        for (int i = 0; i < tableView.getItems().size(); i++) {
            if (tableView.getItems().get(i).getCheckBox().isSelected()) {
                isAnySelected = true;
                break;
            }
        }

        if (!isAnySelected) {
            // Если ни одна запись не выбрана, показываем предупреждающее окно
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Выберите хотя бы одну запись для удаления.");
            alert.showAndWait();
        } else {
            // Иначе, показываем окно подтверждения удаления
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Подтверждение удаления");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Вы уверены, что хотите удалить выбранные записи?");

            ButtonType okButton = new ButtonType("Да");
            ButtonType cancelButton = new ButtonType("Нет");

            confirmAlert.getButtonTypes().setAll(okButton, cancelButton);

            // Показываем диалоговое окно и ждем, пока пользователь выберет опцию
            confirmAlert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == okButton) {
                    // Пользователь нажал "Да", выполняем удаление
                    for (int i = 0; i < tableView.getItems().size(); i++) {
                        if (tableView.getItems().get(i).getCheckBox().isSelected()) {
                            int idD = tableView.getItems().get(i).getNumber();
                            tableView.getItems().remove(tableView.getItems().get(i));
                            try {
                                String deleteQuery = "delete from services where id = " + idD;
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
