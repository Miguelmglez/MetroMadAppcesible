package com.miguel.metromadappcesible.code;

/**
 * Created by Miguel Maroto González on 18-10-16.
 *
 * Esta clase representa las Estaciones del metro de madrid.
 * Atributos:
 * linea: Representa el número de la línea a la que pertenece la estación.
 * nombre: Representa el nombre de la estación.
 * estacionAnterior: Representa el objeto estación que precede al objeto dado.
 * estacionSiguiente: Representa el objeto estación posterior al objeto dado.
 * latitud: Representa el valor de latitud de la estación.
 * longitud: Representa el valor de longitud de la estación.
 * accesible: Representa el valor de accesibilidad de la estación.
 *
 */

public class Estacion{
    private int linea;
    private String nombre;
    private Estacion estacionAnterior = null;
    private Estacion estacionSiguiente = null;
    private double latitud,longitud;
    private boolean accesible;
    /**
     * Constructor
     */
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
        return estacionAnterior;
    }

    public Estacion getEstacionSiguiente() {
        return estacionSiguiente;
    }

    public void setEstacionAnterior(Estacion estacionAnterior) {
        this.estacionAnterior = estacionAnterior;
    }

    public void setEstacionSiguiente(Estacion estacionSiguiente) {
        this.estacionSiguiente = estacionSiguiente;
    }
}

