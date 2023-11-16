package com.example.vetclinic;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AnimalsInserterController extends StockExchangeDB implements Initializable {
    static boolean editOrAdd = false;
    @FXML
    private Button ins;
    @FXML
    private TextField owner;
    @FXML
    private Label Title;
    String own;
    @FXML
    private TextField contact;
    String con;
    @FXML
    private Button back;
    @FXML
    private AnchorPane IntAnimRoot;
    @FXML
    private TextField breed;
    String bred;
    @FXML
    private  TextField name;
    String nam;
    @FXML
    private TextField type;
    String typ;
    static int editId;

    public void Save() throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        try {
            own = String.valueOf(owner.getText());
            bred = String.valueOf(breed.getText());
            nam = String.valueOf(name.getText());
            typ = String.valueOf(type.getText());
            con = String.valueOf(contact.getText());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        String regexNum = "^(?:\\+7|8)[0-9]{10}$\n";
        if (own.isEmpty() || nam.isEmpty() || typ.isEmpty() || con.isEmpty()) {
            alert.setTitle("Предупреждение");
            alert.setContentText("Заполните все поля со звездочкой!!");
            alert.showAndWait();
            return;
        } else if (!(Pattern.matches(regexNum, nam))) {
            alert.setTitle("Неверный формат номера телефона");
            alert.setContentText("Номер телефона должен начинаться " +
                    "с +7 или 8 и содержать 11 цифр");
            alert.showAndWait();
            return;
        }
        if (editOrAdd){
            Edit(own, bred, nam, typ, con);
        } else {
            Ins(own, bred, nam, typ, con);
        }
        SceneLoader.loadNewScene("animals.fxml", IntAnimRoot);
    }

    public void Back() throws IOException {
        editOrAdd = false;
        SceneLoader.loadNewScene("animals.fxml", IntAnimRoot);
    }
    public static void getEditId(int id){
        editId = id;
    }
    
    public static void Edit(String owner, String breed, String name, String type, String contact){
        editOrAdd = true;
        try {
            String query = "UPDATE animals\n" +
                    "SET name = '" + name + "', type = '" + type + "', breed = '" + breed + "', owner = '" + owner + "', contact = '" + contact + "'\n" +
                    "WHERE id = " + editId + ";";
            statement.execute(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        editOrAdd = false;
    }

    public  static void Ins(String owner, String breed, String name, String type, String contact){
        try {
            String query = "insert into animals(name, type, breed, owner,  contact) values ('" + name + "', '" + type + "', '" + breed + "', '" + owner + "', '" + contact + "')";
            statement.execute(query);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        editOrAdd = false;
    }

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        if (editOrAdd){
            String query = "select * from animals where id = " + editId + ";";
            try {
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    name.setText(resultSet.getString("name"));
                    type.setText(resultSet.getString("type"));
                    breed.setText(resultSet.getString("breed"));
                    owner.setText(resultSet.getString("owner"));
                    contact.setText(resultSet.getString("contact"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Title.setText("Редактирование пациента");
        }
    }
}
