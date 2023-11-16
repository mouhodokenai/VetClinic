package com.example.vetclinic;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class VeterinariansInserterController extends StockExchangeDB implements Initializable {
    public static boolean editOrAdd = false;
    static int editId;
    public AnchorPane InsVetRoot;
    public TextField name;
    public TextField document;
    public Label Title;
    String doc;
    String nam;
    public ComboBox<String> specialization;
    String spec;
    public TextField contact;
    String con;
    public Button ins;
    public Button back;

    public static void getEditId(int id){
        editId = id;
    }


    @FXML
    protected void Save() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        try {
            nam = String.valueOf(name.getText());
            spec = String.valueOf(specialization.getValue());
            con = String.valueOf(contact.getText());
            doc = String.valueOf(document.getText());
            String regexNum = "^(?:\\+7|8)[0-9]{10}$";
            String regex = "\\d{4}\\s\\d{6}";

            // Проверка полей на пустоту
            if (doc.isEmpty() || nam.isEmpty() || spec.isEmpty() || con.isEmpty()) {
                alert.setContentText("Заполните все поля!");
                alert.showAndWait();
                return;
            } else if (!(Pattern.matches(regex, doc))) {
                alert.setTitle("Неверный формат паспорта");
                alert.setContentText("Серия паспорта содержит 4 цифры, " +
                        "номер вводится через пробел и имеет 6 цифр");
                alert.showAndWait();
                return;
            } else if (!(Pattern.matches(regexNum, con))) {
                alert.setTitle("Неверный формат номера телефона");
                alert.setContentText("Номер телефона должен начинаться " +
                        "с +7 или 8 и содержать 11 цифр");
                alert.showAndWait();
                return;

            }
            if (editOrAdd){
                Edit(nam, spec, con, doc);
                SceneLoader.loadNewScene("veterinarians.fxml", InsVetRoot);
            } else {
                Ins(nam, spec, con, doc);
                SceneLoader.loadNewScene("veterinarians.fxml", InsVetRoot);
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                alert.setContentText("Запись с таким номером и серией уже существует!");
                alert.showAndWait();
            } else {
                e.printStackTrace();
            }
        }
        editOrAdd = false;
    }


    public  static void Ins(String name, String specialization, String contact, String document) {
        try {
            String query = "insert into veterinarians(name, specialization, contacts, seriesNumber) values ('" + name + "', '" + specialization + "', '" + contact + "', '" + document + "')";
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void Edit(String name, String specialization, String contact, String document){
        editOrAdd = true;
        try {
            String query = "UPDATE veterinarians\n" +
                    "SET name = '" + name + "', specialization = '" + specialization + "', contacts = '" + contact + "', seriesNumber = '" + document + "'\n" +
                    "WHERE id = " + editId + ";";
            System.out.println(query);
            statement.execute(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        editOrAdd = false;
    }

        @FXML
    protected void Back() throws IOException {
        editOrAdd = false;
        SceneLoader.loadNewScene("veterinarians.fxml", InsVetRoot);
    }

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        String[] specializations = {
                "Общая практика",
                "Хирургия",
                "Стоматология",
                "Кардиология",
                "Дерматология",
                "Офтальмология",
                "Онкология",
                "Неврология",
                "Ортопедия",
                "Радиология"
        };
        specialization.getItems().addAll(specializations);
        if (editOrAdd){
            String query = "select * from veterinarians where id = " + editId + ";";
            try {
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    name.setText(resultSet.getString("name"));
                    specialization.setValue(resultSet.getString("specialization"));
                    document.setText(resultSet.getString("seriesNumber"));
                    contact.setText(resultSet.getString("contacts"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Title.setText("Редактирование врача");
        }
    }
}
