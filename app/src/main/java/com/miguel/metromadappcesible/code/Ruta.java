package com.miguel.metromadappcesible.code;

import java.util.ArrayList;

/**
 * Created by Miguel Maroto González on 29-11-16.
 *
 * Clase necesaria para controlar los transbordos y las estaciones del trayecto.
 *
 * ruta: Lista de estaciones que componen la ruta calculada.
 * transbordos: Número de transbordos efectuados en la ruta calculada.
 *
 */

public class Ruta {
    ArrayList<Estacion> ruta;
    int transbordos;
    /**
     * Constructor
     */
    public Ruta(ArrayList<Estacion> ruta, int transbordos) {
        this.ruta = ruta;
        this.transbordos = transbordos;
    }

    protected ArrayList<Estacion> getRuta() {
        return ruta;
    }

    protected int getTransbordos() {
        return transbordos;
    }

}
