package com.example.miguel.urjctfgappmetromad;

import java.util.ArrayList;

/**
 * Created by MMG on 18-10-16.
 */

public class Linea {
    private String color;
    private int numero;
    private ArrayList<Estacion> estaciones;

    public Linea(String color, int numero, ArrayList<Estacion> estaciones) {
        this.color = color;
        this.numero = numero;
        this.estaciones = estaciones;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public ArrayList<Estacion> getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(ArrayList<Estacion> estaciones) {
        this.estaciones = estaciones;
    }
}
