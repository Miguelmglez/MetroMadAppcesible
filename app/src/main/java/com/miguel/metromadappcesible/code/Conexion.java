package com.example.miguel.urjctfgappmetromad;

import org.jgrapht.EdgeFactory;

/**
 * Created by MMG on 15-11-16.
 */

public class Conexion implements EdgeFactory<Estacion,Conexion> {

    Estacion estacionOrigen,estacionDestino;


    public Conexion(Estacion estacionOrigen, Estacion estacionDestino) {
        this.estacionOrigen = estacionOrigen;
        this.estacionDestino = estacionDestino;
    }

    public Estacion getEstacionOrigen() {
        return estacionOrigen;
    }

    public void setEstacionOrigen(Estacion estacionOrigen) {
        this.estacionOrigen = estacionOrigen;
    }

    public Estacion getEstacionDestino() {
        return estacionDestino;
    }

    public void setEstacionDestino(Estacion estacionDestino) {
        this.estacionDestino = estacionDestino;
    }
    /**
     * Creates a new edge whose endpoints are the specified source and target vertices.
     *
     * @param sourceVertex the source vertex.
     * @param targetVertex the target vertex.
     * @return a new edge whose endpoints are the specified source and target vertices.
     */
    @Override
    public Conexion createEdge(Estacion sourceVertex, Estacion targetVertex) {
        return new Conexion(sourceVertex,targetVertex);
    }
}
