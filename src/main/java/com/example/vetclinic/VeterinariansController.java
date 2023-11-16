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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import static com.example.vetclinic.StockExchangeDB.statement;
import static javafx.scene.control.Alert.AlertType.*;

public class VeterinariansController extends SceneLoader {
    public TableColumn<Object, Object> nameColumn;
    public TableColumn<Object, Object> specializationColumn;
    public TableColumn<Object, Object> contactColumn;
    public TableColumn<Object, Object> idColumn;
    public TableColumn<Object, Object> documentColumn;
    public TableColumn<Veterinarians, CheckBox> checkColumn;
    public Button edit;
    public Button delete;

    public Label Title;
    @FXML
    private TableView<Veterinarians> tableView;
    public AnchorPane veterinariansRoot;
    ObservableList<Veterinarians> dataVets = FXCollections.observableArrayList();



    public void Add() throws IOException {
        SceneLoader.loadNewScene("veterinariansInserter.fxml", veterinariansRoot);
    }
    public void Show() throws SQLException {

        String queryS = "SELECT * FROM veterinarians";
        ResultSet resultSet = statement.executeQuery(queryS);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        documentColumn.setCellValueFactory(new PropertyValueFactory<>("document"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        checkColumn.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
        while (resultSet.next()) {
            CheckBox checkBox = new CheckBox();
            Veterinarians veterinarian = new Veterinarians();
            veterinarian.setName(resultSet.getString("name"));
            veterinarian.setNumber(Integer.parseInt(resultSet.getString("id")));
            veterinarian.setDocument(resultSet.getString("seriesNumber"));
            veterinarian.setSpecialization(resultSet.getString("specialization"));
            veterinarian.setContact(resultSet.getString("contacts"));
            veterinarian.setCheckBox(checkBox);
            // Заполните другие поля, если они есть

            dataVets.add(veterinarian);


        }
       // System.out.println(names.toString());

    }
    public void initialize() throws SQLException {
        Show();
        tableView.setItems(dataVets);

    }

    public void Back() throws IOException {
        SceneLoader.loadNewScene("menu.fxml", veterinariansRoot);
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
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Выберите хотя бы одну запись для удаления.");
            alert.showAndWait();
        } else {
            // Иначе, показываем окно подтверждения удаления
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
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
                                String deleteQuery = "delete from veterinarians where id = " + idD;
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
                    VeterinariansInserterController.getEditId(idE);
                    VeterinariansInserterController.editOrAdd = true;
                    SceneLoader.loadNewScene("veterinariansInserter.fxml", veterinariansRoot);
                }
            }
        } else {
            Alert alert = new Alert(WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Выберете одну запись!");
            alert.showAndWait();
        }
    }
}
