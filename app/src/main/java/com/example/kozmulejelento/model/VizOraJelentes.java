package com.example.kozmulejelento.model;

public class VizOraJelentes {
    private String id;
    private String userID;
    private String oraAzonosito;
    private String oraAllas;
    private String datum;

    public VizOraJelentes() {}

    public VizOraJelentes(String userID, String oraAzonosito, String oraAllas, String datum) {
        this.userID = userID;
        this.oraAzonosito = oraAzonosito;
        this.oraAllas = oraAllas;
        this.datum = datum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOraAzonosito() {
        return oraAzonosito;
    }

    public void setOraAzonosito(String oraAzonosito) {
        this.oraAzonosito = oraAzonosito;
    }

    public String getOraAllas() {
        return oraAllas;
    }

    public void setOraAllas(String oraAllas) {
        this.oraAllas = oraAllas;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    @Override
    public String toString() {
        return "Óraszám: " + oraAzonosito + ", Állás: " + oraAllas + ", Dátum: " + datum;
    }
}