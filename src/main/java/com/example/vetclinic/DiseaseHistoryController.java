package com.example.vetclinic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DiseaseHistoryController extends StockExchangeDB implements Initializable {
    @FXML

    private TableColumn<DiseaseHistory, String> numberColumn;
    @FXML
    private TableColumn<DiseaseHistory, String> dateColumn;
    @FXML
    private TableColumn<DiseaseHistory, String> serviceColumn;
    @FXML
    private TableColumn<DiseaseHistory, String> diagnosisColumn;
    @FXML
    private TableColumn<DiseaseHistory, String> veterinarianColumn;
    static int PatientNumber;
    static String PatientName;
    static String PatientType;
    static String PatientOwner;
    @FXML
    private TableView<DiseaseHistory> tableView;
    @FXML
    public Label name;
    @FXML
    public Label owner;
    @FXML
    public Label type;
    @FXML
    public AnchorPane historyPane;
    ObservableList<DiseaseHistory> dataHistory = FXCollections.observableArrayList();

    public static void setId(int id){
        PatientNumber = id;
    }
    public static void setNam(String name){
        PatientName = "История пациента " + name;
    }
    public static void setType(String type){
        PatientType = "Вид питомца: " + type;
    }
    public static void setOwner(String owner){
        PatientOwner = "Владелец: " + owner;
    }

    public void Show() throws SQLException {
        name.setText(PatientName);
        owner.setText(PatientOwner);
        type.setText(PatientType);
        String query = "select s.id, s.diagnosis, s.name as servname, v.name as vetname, s.date\n" +
                "                from services s\n" +
                "                join animals an \n" +
                "                on s.animals_id = an.id\n" +
                "                join veterinarians v\n" +
                "                on v.id = s.veterinarians_id\n" +
                "                where an.id = " + PatientNumber;
        ResultSet resultSet = statement.executeQuery(query);
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service")); //сюда вставляем из класса!
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        veterinarianColumn.setCellValueFactory(new PropertyValueFactory<>("veterinarian"));
        diagnosisColumn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        while (resultSet.next()) {
            DiseaseHistory history = new DiseaseHistory();
            history.setNumber(resultSet.getInt("s.id")); //а сюда из таблички
            history.setDate(resultSet.getString("date"));
            history.setService(resultSet.getString("servname"));
            history.setVeterinarian(resultSet.getString("vetname"));
            history.setDiagnosis(resultSet.getString("diagnosis"));
            dataHistory.add(history);
        }
    }

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        try {
            Show();
            tableView.setItems(dataHistory);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void Back() throws IOException {
        SceneLoader.loadNewScene("animals.fxml", historyPane);
    }
}
