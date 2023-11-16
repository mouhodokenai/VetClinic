package com.example.vetclinic;
import javafx.scene.control.CheckBox;

public class Services {
    private Integer number;
    private CheckBox checkBox;
    private String name;
    private String data;
    private double coast;
    private String patient;
    private String veterinarian;
    private String diagnosis;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getCoast() {
        return coast;
    }

    public void setCoast(double coast) {
        this.coast = coast;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String appointment) {
        this.patient = appointment;
    }

    public String getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(String veterinarian) {
        this.veterinarian = veterinarian;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}
