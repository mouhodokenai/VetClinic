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
import static com.example.vetclinic.StockExchangeDB.statement;

public class AppointmentsController extends SceneLoader {

    @FXML
    private TableView<Appointments> tableView;
    @FXML
    private TableColumn<Appointments, String> numberColumn;
    @FXML
    private TableColumn<Appointments, String> dateColumn;
    @FXML
    private TableColumn<Appointments, String> animalColumn;
    @FXML
    private TableColumn<Appointments, String> veterinarianColumn;
    @FXML
    private TableColumn<Appointments, CheckBox> checkColumn;
    @FXML
    private Button edit;
    @FXML
    private Button delete;
    @FXML
    private Label Title;
    @FXML
    private AnchorPane appointmentsRoot;
    ObservableList<Appointments> dataAppointments = FXCollections.observableArrayList();

    public void Show() throws SQLException {
        String queryS = "select a.id, a.data, an.name as anname, v.name as vetname\n" +
                "from appointments a\n" +
                "join animals an\n" +
                "on a.animals_id = an.id \n" +
                "join veterinarians v \n" +
                "on v.id = a.veterinarians_id";
        ResultSet resultSet = statement.executeQuery(queryS);
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        animalColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
        veterinarianColumn.setCellValueFactory(new PropertyValueFactory<>("veterinarian"));
        checkColumn.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
        while (resultSet.next()) {
            CheckBox checkBox = new CheckBox();
            Appointments appointment = new Appointments();
            appointment.setNumber(Integer.parseInt(resultSet.getString("id")));
            appointment.setDate(resultSet.getString("data"));
            appointment.setPatient(resultSet.getString("anname"));
            appointment.setVeterinarian(resultSet.getString("vetname"));
            appointment.setCheckBox(checkBox);
            dataAppointments.add(appointment);
        }
    }

    public void Add() throws IOException {
        SceneLoader.loadNewScene("appointmentsInserter.fxml", appointmentsRoot);
    }

    public void initialize() throws SQLException {
        Show();
        tableView.setItems(dataAppointments);
    }

    public void Back() throws IOException {
        loadNewScene("menu.fxml", appointmentsRoot);
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
                                String deleteQuery = "delete from appointments where id = " + idD;
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
                    AppointmentsInserterController.getEditId(idE);
                    AppointmentsInserterController.editOrAdd = true;
                    SceneLoader.loadNewScene("appointmentsInserter.fxml", appointmentsRoot);
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
}
