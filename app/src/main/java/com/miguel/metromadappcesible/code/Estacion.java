package com.miguel.metromadappcesible.code;

/**
 * Created by MMG on 18-10-16.
 */

public class Estacion{
    int linea;
    String nombre;
    Estacion estacionAnterior,estacionSiguiente = null;
    double latitud,longitud;
    boolean accesible;

    public Estacion(int linea, String nombre, double latitud, double longitud, boolean accesible) {
        this.linea = linea;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.accesible = accesible;
    }

    public int getLinea() {
        return this.linea;
    }


    public String getNombre() {
        return this.nombre;
    }


    public double getLatitud() {
        return this.latitud;
    }


    public double getLongitud() {
        return this.longitud;
    }


    public boolean isAccesible() {
        return this.accesible;
    }


    public void setEstacionAnterior(Estacion estacionAnterior) {
        this.estacionAnterior = estacionAnterior;
    }

    public void setEstacionSiguiente(Estacion estacionSiguiente) {
        this.estacionSiguiente = estacionSiguiente;
    }
}

