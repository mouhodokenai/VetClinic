package com.example.vetclinic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AppointmentsInserterController extends StockExchangeDB implements Initializable {
    public static boolean editOrAdd = false;
    @FXML
    private Button ins;
    @FXML
    private Button back;
    @FXML
    private AnchorPane InsAppRoot;
    @FXML
    private DatePicker data;
    @FXML
    private Spinner<Integer> spinHours;
    @FXML
    private Spinner<Integer> spinMinutes;
    static Integer editId;
    @FXML
    private Label Title;
    @FXML
    private ComboBox<String> animal;
    @FXML
    private ComboBox<String> veterinarian;

    public static void getEditId(int idE) {
        editId = idE;
    }

    public void Save() throws NumberFormatException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        try {
            int selectedAnimal = getId(animal.getValue());
            LocalDate selectedDate = data.getValue();
            String selectedDateAsString = selectedDate.toString();
            int selectedVeterinarian = getId(veterinarian.getValue());
            int selectedHour = spinHours.getValue();
            int selectedMinute = spinMinutes.getValue();
            if (editOrAdd){
                Edit(selectedDateAsString, selectedHour, selectedMinute, selectedAnimal, selectedVeterinarian);
            } else {
                Ins(selectedDateAsString, selectedHour, selectedMinute, selectedAnimal, selectedVeterinarian);
            }
            SceneLoader.loadNewScene("appointments.fxml", InsAppRoot);
        } catch (NumberFormatException e) {
            alert.setContentText("Заполните все поля со звездочкой!");
            alert.showAndWait();
        } catch (RuntimeException a){
            if (a.getMessage().contains("Duplicate entry")) {
                alert.setContentText("Запись с таким временем уже существует!");
                alert.showAndWait();
            } else {
                alert.setContentText("Заполните все поля со звездочкой!");
                alert.showAndWait();
                throw new RuntimeException(a);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        editOrAdd = false;
    }


    public void Ins(String data, int hour, int minute, int animal, int veterinarian) {
        String date = data + " " + hour + ":" + minute + ":" + "00";
        try {
            String query = "insert into appointments(data, animals_id, veterinarians_id) values ('" + date + "', '" + animal + "', '" + veterinarian + "')";
            statement.execute(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        editOrAdd = false;
    }
    public static void Edit(String data, int hour, int minute, int animal, int veterinarian){
        editOrAdd = true;
        String date = data + " " + hour + ":" + minute + ":" + "00";
        try {
            String query = "UPDATE appointments\n" +
                    "SET data = '" + date + "', animals_id = '" + animal + "', veterinarians_id = '" + veterinarian + "'\n" +
                    "WHERE id = " + editId + ";";
            System.out.println(query);
            statement.execute(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        editOrAdd = false;
    }

    public void Back() throws IOException {
        editOrAdd = false;
        SceneLoader.loadNewScene("appointments.fxml", InsAppRoot);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (editOrAdd){
            String query = "select a.id, a.data, an.id as anid, v.id as vid, an.name as anname, v.name as vetname\n" +
                    "from appointments a\n" +
                    "join animals an\n" +
                    "on a.animals_id = an.id \n" +
                    "join veterinarians v \n" +
                    "on v.id = a.veterinarians_id where a.id = " + editId;
            try {
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    StringBuilder dataTime = new StringBuilder(resultSet.getString("data"));
                    String year = dataTime.substring(0, 4);
                    String month = dataTime.substring(5, 7);
                    String day = dataTime.substring(8, 10);
                    String date = dataTime.substring(0, 10);
                    String hour = dataTime.substring(11, 13);
                    String minute = dataTime.substring(14);
                    String time = hour + ":" + minute;
                    System.out.println(date + " " + time);

                    animal.setValue(resultSet.getString("anid") + " " + resultSet.getString("anname"));
                    veterinarian.setValue(resultSet.getString("vid") + " " + resultSet.getString("vetname"));
                    data.setValue(LocalDate.parse(date));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Title.setText("Редактирование записи");
        }
        SpinnerValueFactory<Integer> secondFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        SpinnerValueFactory<Integer> firstFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        spinMinutes.setValueFactory(secondFactory);
        spinHours.setValueFactory(firstFactory);

        ObservableList<String> veterinariansList;
        try {
            veterinariansList = FXCollections.observableArrayList();
            String query = "select * from veterinarians";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                veterinariansList.add(resultSet.getInt("id") + " " + resultSet.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        veterinarian.setItems(veterinariansList);

        ObservableList<String> animalsNames;
        try {
            animalsNames = FXCollections.observableArrayList();
            String query = "select * from animals";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                animalsNames.add(resultSet.getInt("id") + " " + resultSet.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        animal.setItems(animalsNames);
    }
    public static int getId(String object) {
        int number;
        StringBuilder strNumber;
        strNumber = new StringBuilder();
        for (char sym : object.toCharArray()) {
            if (Character.isDigit(sym)) {
                strNumber.append(sym);
            }
        }
        number = Integer.parseInt(strNumber.toString());
        return number;
    }
}

