package com.example.vetclinic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ServicesInserterController extends StockExchangeDB implements Initializable {

    public static boolean editOrAdd = false;
    static int editId;
    public AnchorPane InsServRoot;
    public Button back;
    public Button ins;

    public TextField coast;

    public ComboBox<String> name;
    public ComboBox<String> animal;
    public DatePicker data;
    public ComboBox<String> veterinarian;
    public Spinner<Integer> spinHours;
    public Spinner<Integer> spinMinutes;
    public ComboBox<String> diagnosis;

    public Label Title;

    public static void getEditId(int idE) {
        editId = idE;
    }
    public void Save()  {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        String selectedDateAsString = null;
        try {
            try {
                LocalDate selectedDate = data.getValue();
                selectedDateAsString = selectedDate.toString();

            } catch (Exception e) {
                alert.setContentText("Заполните все поля!");
                alert.showAndWait();
            }
            try {
                String nam = String.valueOf(name.getValue());

                String dia = String.valueOf(diagnosis.getValue());

                double selectedCoast = Double.parseDouble(coast.getText());
                System.out.println(selectedCoast + "цена");

                int selectedAnimal = getId(animal.getValue());
                System.out.println(selectedAnimal);

                int selectedVeterinarian = getId(veterinarian.getValue());
                System.out.println(selectedVeterinarian);

                int selectedHour = spinHours.getValue();
                int selectedMinute = spinMinutes.getValue();
                if (editOrAdd){
                    Edit(nam, selectedDateAsString, selectedHour, selectedMinute, selectedAnimal, selectedVeterinarian, selectedCoast, dia);
                } else {
                    Ins(nam, selectedDateAsString, selectedHour, selectedMinute, selectedAnimal, selectedVeterinarian, selectedCoast, dia);
                }


            } catch (NumberFormatException e) {
                alert.setContentText("Заполните все поля!");
                alert.showAndWait();
            } catch (RuntimeException a){
                if (a.getMessage().contains("Duplicate entry")) {
                    alert.setContentText("Услуга с таким временем уже существует!");
                    alert.showAndWait();
                } else {
                    throw new RuntimeException(a);
                }
            }
            SceneLoader.loadNewScene("services.fxml", InsServRoot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        editOrAdd = false;
    }

    public void Ins(String name, String data, int hour, int minute, int animal, int veterinarian, double coast, String diagnosis) {
        String date = data + " " + hour + ":" + minute + ":" + "00";
        try {
            String query = "insert into services(name, veterinarians_id, animals_id, coast, date, diagnosis) values ('" + name + "', '" + veterinarian + "', '" + animal + "', '" + coast + "', '" + date + "', '" + diagnosis + "')";
            statement.execute(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void Edit(String name, String data, int hour, int minute, int animal, int veterinarian, double coast, String diagnosis){
        editOrAdd = true;
        String date = data + " " + hour + ":" + minute + ":" + "00";
        try {
            String query = "UPDATE services\n" +
                    "SET name = '" + name + "', date = '" + date + "', veterinarians_id = '" + veterinarian + "', animals_id = '" + animal + "', coast = '" + coast + "', diagnosis = '" + diagnosis + "'\n" +
                    "WHERE id = " + editId + ";";
            System.out.println(query);
            statement.execute(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        editOrAdd = false;
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (editOrAdd){
            String query = "select s.id, s.diagnosis, v.id as vid, an.id as anid, s.name as servname, v.name as vetname, an.name as anname, s.coast, s.date\n" +
                    "from services s\n" +
                    "join animals an \n" +
                    "on s.animals_id = an.id\n" +
                    "join veterinarians v\n" +
                    "on v.id = s.veterinarians_id where s.id = " + editId;
            try {
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    StringBuilder dataTime = new StringBuilder(resultSet.getString("date"));
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
                    coast.setText(resultSet.getString("coast"));
                    name.setValue(resultSet.getString("servname"));
                    diagnosis.setValue(resultSet.getString("diagnosis"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Title.setText("Редактирование услуги");
        }
        SpinnerValueFactory<Integer> secondFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        SpinnerValueFactory<Integer> firstFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        spinMinutes.setValueFactory(secondFactory);
        spinHours.setValueFactory(firstFactory);

        String[] servicesNamesArray = {
                "Прием врача-ветеринара",
                "Вакцинация",
                "Общий осмотр животного",
                "Лечение заболеваний",
                "Рентгенография",
                "Ультразвуковое исследование",
                "Хирургические операции",
                "Стоматологические процедуры",
                "Стерилизация и кастрация",
                "Чипирование",
                "Лабораторные анализы",
                "Профилактические консультации",
                "Травматология и ортопедия",
                "Акупунктура",
                "Физиотерапия",
                "Интенсивная терапия"
        };
        String[] animalDiagnosesArray = {
                "Вирусный ринит",
                "Дерматит",
                "Острый гастрит",
                "Пневмония",
                "Заболевание почек",
                "Глисты",
                "Диабет",
                "Гепатит",
                "Инсульт",
                "Отравление",
                "Сколиоз",
                "Гипертония",
                "Гипотония",
                "Аллергия",
                "Анемия",
                "Пищевое отравление",
                "Ожог",
                "Повреждение лапы",
                "Укусы/ушибы",
                "Заворот желудка",
                "Глазные инфекции",
                "Сахарный диабет",
                "Ожирение",
                "Артрит",
                "Повреждение хвоста"
        };

        diagnosis.getItems().addAll(animalDiagnosesArray);
        name.getItems().addAll(servicesNamesArray);

        ObservableList<String> vetNames;
        try {
            vetNames = FXCollections.observableArrayList();
            String query1 = "select * from veterinarians";
            ResultSet resultVet = statement.executeQuery(query1);
            while (resultVet.next()) {
                vetNames.add(resultVet.getInt("id") + " " + resultVet.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        veterinarian.setItems(vetNames);

        ObservableList<String> animalsNames;
        try {
            animalsNames = FXCollections.observableArrayList();
            String query2 = "select * from animals";
            ResultSet resultApp = statement.executeQuery(query2);
            while (resultApp.next()) {
                animalsNames.add(resultApp.getInt("id") + " " + resultApp.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        animal.setItems(animalsNames);

    }

    public void Back() throws IOException {
        SceneLoader.loadNewScene("services.fxml", InsServRoot);
    }

    public static int getId(String object) {
        int number;
        StringBuilder strNumber;
        strNumber = new StringBuilder();
        for (char sym : object.toCharArray()) {
            if (Character.isDigit(sym)) {
                strNumber.append(sym);
            } else {
                continue;
            }

        }
        number = Integer.parseInt(strNumber.toString());
        return number;

    }


}
