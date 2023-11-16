package com.example.vetclinic;

public class DiseaseHistory {
    private int Number;
    private String Date;
    private String Service;
    private String Diagnosis;
    private String Veterinarian;

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getService() {
        return Service;
    }

    public void setService(String animal) {
        Service = animal;
    }

    public String getDiagnosis() {
        return Diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        Diagnosis = diagnosis;
    }

    public String getVeterinarian() {
        return Veterinarian;
    }

    public void setVeterinarian(String veterinarian) {
        Veterinarian = veterinarian;
    }
}
