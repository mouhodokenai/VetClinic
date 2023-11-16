package com.example.vetclinic;
import javafx.scene.control.CheckBox;

public class Appointments {

    private CheckBox checkBox;
    private Integer number;
    private String date;
    private String patient;
    private String veterinarian;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(String veterinarian) {
        this.veterinarian = veterinarian;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}
