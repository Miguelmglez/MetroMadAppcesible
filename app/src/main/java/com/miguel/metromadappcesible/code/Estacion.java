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

    public Estacion(){
        this.linea =0;
        this.nombre= "Vacío";
        this.latitud=0.0;
        this.longitud=0.0;

    }

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

    public Estacion getEstacionAnterior() {
            return this.estacionAnterior;
    }

    public void setEstacionAnterior(Estacion estacionAnterior) {
        this.estacionAnterior = estacionAnterior;
    }

    public Estacion getEstacionSiguiente() {
        return this.estacionSiguiente;
    }

    public void setEstacionSiguiente(Estacion estacionSiguiente) {
        this.estacionSiguiente = estacionSiguiente;
    }
}

