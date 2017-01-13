package com.miguel.metromadappcesible.code;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.Multigraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Miguel Maroto González on 18-10-16.
 *
 * Clase que representa el Metro con el que trabaja la aplicación, objeto generado a partir de los datos del fichero /assets/metro.xml
 *
 * después de ser parseado por la clase Parser
 *
 * mapaEstaciones: Mapa con clave nombre de la estación y valor, una lista de estaciones que representan todas las correspondencias
 * de la estación.
 *
 * grafoEstaciones: Multigrafo no dirigido que contiene los nodos (estaciones) y sus conexiones.
 *
 * listaNombreEstaciones: Lista que contiene los nombres de las estaciones, sin repeticiones.
 *
 */

public class Metro {

    private HashMap<String, ArrayList<Estacion>> mapaEstaciones;
    private Multigraph<Estacion, Conexion> grafoEstaciones;
    private ArrayList<String> listaNombreEstaciones = new ArrayList<>();

    /**
     * Constructor
     */
    public Metro() {
        this.mapaEstaciones = new HashMap<String, ArrayList<Estacion>>() {
        };
        this.grafoEstaciones = new Multigraph<>(Conexion.class);

    }
    public HashMap<String, ArrayList<Estacion>> getMapaEstaciones() {
        return mapaEstaciones;
    }

    public ArrayList<String> getListaNombreEstaciones() {
        return listaNombreEstaciones;
    }

    /**
     * Método que añade una estación al grafo, al mapa y a la lista.
     */
    public Estacion aniadirEstacion(Estacion e) {
        this.grafoEstaciones.addVertex(e);
        if (this.mapaEstaciones.containsKey(e.getNombre())) {
            this.mapaEstaciones.get(e.getNombre()).add(e);
        } else {
            ArrayList<Estacion> estaciones = new ArrayList<>();
            estaciones.add(e);
            this.mapaEstaciones.put(e.getNombre(), estaciones);
            this.listaNombreEstaciones.add(e.getNombre().toString());
        }
        return e;
    }
    /**
     * Método que coloca la estación anterior a la estación actual.
     */
    private Estacion aniadirAnterior(Estacion anterior, Estacion actual) {
        if (anterior.getLinea() == actual.getLinea()) {
            actual.setEstacionAnterior(anterior);
            Conexion c = new Conexion(anterior, actual);

            this.grafoEstaciones.addEdge(anterior, actual, c);
        } else {
            actual.setEstacionAnterior(null);
        }
        return anterior;
    }
    /**
     * Método que coloca la estación siguiente a la estación actual.
     */
    private Estacion aniadirSiguiente(Estacion siguiente, Estacion actual) {
        if (siguiente.getLinea() == actual.getLinea()) {
            actual.setEstacionSiguiente(siguiente);
        } else {
            actual.setEstacionSiguiente(null);
        }
        return siguiente;
    }
    /**
     * Método que llama a los métodos aniadirAnterior y aniadirSiguiente para ordenar las estaciones.
     */
    public void aniadirContiguas(ArrayList<Estacion> listaEstaciones) {
        for (int i = 0; i < listaEstaciones.size(); i++) {
            if (listaEstaciones.get(i) != null && i + 1 < listaEstaciones.size()) {
                aniadirSiguiente(listaEstaciones.get(i + 1), listaEstaciones.get(i));
            }
            if (i != 0 && i + 1 <= listaEstaciones.size()) {
                aniadirAnterior(listaEstaciones.get(i - 1), listaEstaciones.get(i));
            }
        }
    }
    /**
     * Método que resuelve la particularidad de las líneas 12 y 6, que son circulares, conectando el principio con el final de la línea
     */
    public void completarContiguas() {
        ArrayList<Estacion> puertaDelSurLista = this.mapaEstaciones.get("Puerta del Sur");
        int aux = 0;
        for (int i = 0; i < puertaDelSurLista.size(); i++) {
            if (puertaDelSurLista.get(i).getLinea() == 12) {
                aux = i;
            }
        }
        Estacion puertaDelSur = puertaDelSurLista.get(aux);
        Estacion sanNicasio = this.mapaEstaciones.get("San Nicasio").get(0);
        this.aniadirAnterior(sanNicasio, puertaDelSur);
        this.aniadirSiguiente(puertaDelSur, sanNicasio);
        Estacion lucero = this.mapaEstaciones.get("Lucero").get(0);
        Estacion laguna = this.mapaEstaciones.get("Laguna").get(0);
        this.aniadirAnterior(lucero, laguna);
        this.aniadirSiguiente(laguna, lucero);
    }
    /**
     * Método conecta las correspondencias de cada estación en caso de cumplir con el requisito de accesibilidad.
     */
    public void aniadirCorrespondencias(ArrayList<Estacion> listaEstaciones) {
        for (int i = 0; i < listaEstaciones.size(); i++) {
            ArrayList<Estacion> e = this.mapaEstaciones.get(listaEstaciones.get(i).getNombre());
            if (e.size() > 1) {
                for (int j = 0; j < e.size(); j++) {
                    Estacion origen = e.get(j);
                    for (int x = 0; x < e.size(); x++) {
                        Estacion destino = e.get(x);
                        if (origen != destino && (origen.isAccesible() && destino.isAccesible())) {
                            Conexion c = new Conexion(origen, destino);
                            this.grafoEstaciones.addEdge(origen, destino, c);
                        }
                    }
                }
            }
        }
    }
    /**
     * Método que devuelve una lista de estaciones accesibles dada una estación.
     */
    public ArrayList<Estacion> accesibleList(Estacion estacion, LinkedList estacionesContiguas, ArrayList<Estacion> visitadas) {
        ArrayList<Estacion> resultado = new ArrayList<>();
        ArrayList<Estacion> correspondencias = this.mapaEstaciones.get(estacion.getNombre());
        for (int i = 0; i < correspondencias.size(); i++) {
            if (!visitadas.contains(correspondencias.get(i))) {
                if (correspondencias.get(i).getEstacionAnterior()!= null) {
                    estacionesContiguas.addLast(correspondencias.get(i).getEstacionAnterior());
                }
                if (correspondencias.get(i).getEstacionSiguiente() != null) {
                    estacionesContiguas.addLast(correspondencias.get(i).getEstacionSiguiente());
                }
                if (correspondencias.get(i).isAccesible()) {
                    resultado.add(correspondencias.get(i));
                }
                visitadas.add(correspondencias.get(i));
            }
        }
        if (resultado.size() == 0) {
            for (int i=0; i<estacionesContiguas.size();i++){
                if (!visitadas.contains(estacionesContiguas.get(i))) {
                    resultado = this.accesibleList((Estacion) estacionesContiguas.get(i), estacionesContiguas, visitadas);
                }
                if (resultado.size()!=0){
                    return resultado;
                }
            }
        }
        return resultado;
    }
    /**
     * Método que calcula mediante el algoritmo de Dijkstra el camino más corto entre las estaciones recibidas.
     *
     * Recibe una lista de estaciones accesibles origen y una lista de estaciones accesibles destino.
     *
     * Evaluándo la mejor opción (Camino con menos transbordos o Con menos estaciones) según haya seleccionado el usuario.
     */
    public ArrayList mejorCamino(ArrayList<Estacion> listaEstacionesOrigen, ArrayList<Estacion> listaEstacionesDestino, boolean transbordos) {
        ArrayList resultadoRuta = new ArrayList();
        ArrayList aux;
        ArrayList <Ruta> rutas =  new ArrayList<>();
        Ruta auxTransbordos = new Ruta(null,0);
        for (int i = 0; i < listaEstacionesOrigen.size(); i++) {
            Estacion origen = listaEstacionesOrigen.get(i);
            for (int j = 0; j < listaEstacionesDestino.size(); j++) {
                Estacion destino = listaEstacionesDestino.get(j);
                DijkstraShortestPath dijkstra = new DijkstraShortestPath(this.grafoEstaciones, origen, destino);
                aux = (ArrayList) dijkstra.getPathEdgeList();
                if (transbordos){
                    int contadorTransbordos =0;
                    for (int x=0; x<aux.size();x++){
                        Conexion c = (Conexion) aux.get(x);
                        if (c.getEstacionDestino().getNombre().equals(c.getEstacionOrigen().getNombre())){
                            contadorTransbordos ++;
                        }
                    }
                    Ruta r = new Ruta(aux,contadorTransbordos);
                    rutas.add(r);
                }
                if (aux.size() < resultadoRuta.size() || resultadoRuta.size() == 0) {
                    resultadoRuta = aux;
                }
            }
        }
        if (transbordos) {
            for (int y = 0; y < rutas.size(); y++) {
                Ruta r = rutas.get(y);
                if (auxTransbordos.getRuta() == null) {
                    auxTransbordos = r;
                }
                if (auxTransbordos.getTransbordos() > r.getTransbordos()) {
                    auxTransbordos = r;
                } else {
                    if (auxTransbordos.getTransbordos() == r.getTransbordos()) {
                        if (auxTransbordos.getRuta().size() > r.getRuta().size()) {
                            auxTransbordos = r;
                        }
                    }
                }
                resultadoRuta = auxTransbordos.getRuta();
            }
        }
        return resultadoRuta;
    }
    /**
     * Método que recibe una estación origen, una estación destino y una opción de ruta (menos transbordos o menos estaciones)
     *
     * y devuelve la ruta final accesible entre las dos estaciones con el criterio de ruta seleccionado por el usuario.
     */
    public ArrayList calcularCaminoDefinitivo (Estacion origen, Estacion destino, boolean transbordos){
        ArrayList<Estacion> visitadasOrigen = new ArrayList<>();
        ArrayList<Estacion> visitadasDestino = new ArrayList<>();
        LinkedList<Estacion> porVisitarOrigen = new LinkedList<>();
        LinkedList<Estacion> porVisitarDestino = new LinkedList<>();
        ArrayList<Estacion> listaOrigen = this.accesibleList(origen,porVisitarOrigen,visitadasOrigen);
        ArrayList<Estacion> listaDestino = this.accesibleList(destino,porVisitarDestino,visitadasDestino);
        ArrayList rutaFinal = this.mejorCamino(listaOrigen,listaDestino,transbordos);
        return rutaFinal;
    }
}
