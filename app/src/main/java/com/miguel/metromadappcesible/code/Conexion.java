package com.miguel.metromadappcesible.code;


import org.jgrapht.EdgeFactory;

/**
 * Created by Miguel Maroto González on 15-11-16.
 *
 * Esta clase es necesaria para crear conexiones dentro del grafo.
 * Se compone de dos atributos:
 *
 * estacionOrigen : Tipo estación, será un nodo de la conexión.
 * estacionDestino : Tipo estación, será un nodo de la conexión.
 *
 */

public class Conexion implements EdgeFactory<Estacion,Conexion> {

   private Estacion estacionOrigen,estacionDestino;

    /**
     * Constructor
     */
    public Conexion(Estacion estacionOrigen, Estacion estacionDestino) {
        this.estacionOrigen = estacionOrigen;
        this.estacionDestino = estacionDestino;
    }

    public Estacion getEstacionOrigen() {
        return estacionOrigen;
    }

    public Estacion getEstacionDestino() {
        return estacionDestino;
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
