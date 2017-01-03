package com.miguel.metromadappcesible.code;

import java.util.ArrayList;

/**
 * Created by MMG on 29-11-16.
 */

public class Ruta {
    ArrayList<Estacion> ruta;
    int transbordos;

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
