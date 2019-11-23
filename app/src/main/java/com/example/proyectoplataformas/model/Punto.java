package com.example.proyectoplataformas.model;


public class Punto {
    Double latitud;
    Double longitud;

    public Punto (String lat, String lon) {
        this.latitud = Double.parseDouble(lat);
        this.longitud = Double.parseDouble(lon);
    }

    public Double getLatitud(){
        return latitud;
    }

    public Double getLongitud(){
        return longitud;
    }

}
