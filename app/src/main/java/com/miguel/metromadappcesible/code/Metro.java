package com.miguel.metromadappcesible.code;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by MMG on 18-10-16.
 */

public class Metro {

    HashMap<String, ArrayList<Estacion>> mapaEstaciones;
    WeightedMultigraph<Estacion, Conexion> grafoEstaciones;

    public Metro() {
        this.mapaEstaciones = new HashMap<String, ArrayList<Estacion>>() {
        };
        this.grafoEstaciones = new WeightedMultigraph<>(Conexion.class);
    }
    public Metro nuevoMetroMadrid() {
        Metro metroMadrid = new Metro();
        ArrayList<Estacion> listaEstaciones = new ArrayList<Estacion>();
        try {
            File inputFile = new File("metro.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("sch:SubwayStation");
            //System.out.println("--------------LISTA ESTACIONES--------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                int linea = 0;
                double latitud = 0;
                double longitud = 0;
                boolean accesible;
                Node nNode = nList.item(temp);
               // System.out.println("\n");
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String nombreEstacion = eElement.getElementsByTagName("sch:name").item(0).getTextContent();
                    String accesibleStr = eElement.getElementsByTagName("ont:isAccessible").item(0).getTextContent();
                    if (accesibleStr.equalsIgnoreCase("true")) {
                        accesible = true;
                    } else {
                        accesible = false;
                    }
                    //System.out.println("Nombre : " + nombreEstacion);
                    //System.out.println("Accesible : " + accesible);
                    NodeList nListaux = doc.getElementsByTagName("geo:feature");
                    Node nNodeAux = nListaux.item(0);
                    if (nNodeAux.getNodeType() == Node.ELEMENT_NODE) {
                        String lineaStr = eElement.getElementsByTagName("geo:name").item(0).getTextContent();
                        String[] lineaAux = lineaStr.split(" ");
                        String lineaFinal = lineaAux[0];
                        String localizacion = eElement.getElementsByTagName("cal:location").item(0).getTextContent();
                        String[] localizacionAux = localizacion.split(" ");
                        String latitudStr = localizacionAux[0];
                        String longitudStr = localizacionAux[1];
                        latitud = Double.parseDouble(latitudStr);
                        longitud = Double.parseDouble(longitudStr);
                        linea = Integer.valueOf(lineaFinal);
                        /*
                        System.out.println("Línea : " + lineaFinal);
                        System.out.println("LATITUD : " + latitudStr);
                        System.out.println("LONGITUD : " + longitudStr);
                        System.out.println("LATITUD  --- Float : " + Float.parseFloat(latitudStr));
                        System.out.println("LONGITUD --- Float : " + Float.parseFloat(longitudStr));
                        System.out.println("LATITUD  --- Double: " + latitud);
                        System.out.println("LONGITUD --- Double: " + longitud);
                        */
                    }

                    Estacion estacionNueva = new Estacion(linea, nombreEstacion, latitud, longitud, accesible);
                    metroMadrid.aniadirEstacion(estacionNueva);
                    listaEstaciones.add(estacionNueva);
                    // System.out.println(metroMadrid.mapaEstaciones.get(estacionAux.nombre).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Para comprobar las estaciones y sus correspondencias
        for (int i = 0; i< listaEstaciones.size(); i++){
            Estacion e = (Estacion) listaEstaciones.get(i);
            String nombreEstacion = e.getNombre();
            int numEstaciones = metroMadrid.mapaEstaciones.get(nombreEstacion).size();
            System.out.println(nombreEstacion + ":" + numEstaciones);

        }
        */
        metroMadrid.aniadirContiguas(listaEstaciones);
        metroMadrid.completarContiguas(metroMadrid.mapaEstaciones);
        metroMadrid.aniadirCorrespondencias(listaEstaciones);
        return metroMadrid;
    }

    protected Estacion aniadirEstacion(Estacion e) {
        this.grafoEstaciones.addVertex(e);
        if (this.mapaEstaciones.containsKey(e.getNombre())) {
            this.mapaEstaciones.get(e.getNombre()).add(e);
        } else {
            ArrayList<Estacion> estaciones = new ArrayList<>();
            estaciones.add(e);
            this.mapaEstaciones.put(e.getNombre(), estaciones);
            {
            }
        }
        return e;
    }

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

    private Estacion aniadirSiguiente(Estacion siguiente, Estacion actual) {
        if (siguiente.getLinea() == actual.getLinea()) {
            actual.setEstacionSiguiente(siguiente);
            Conexion c = new Conexion(actual, siguiente);
        } else {
            actual.setEstacionSiguiente(null);
        }
        return siguiente;
    }

    protected void aniadirContiguas(ArrayList<Estacion> listaEstaciones) {
        for (int i = 0; i < listaEstaciones.size(); i++) {
            if (listaEstaciones.get(i) != null && i + 1 < listaEstaciones.size()) {
                aniadirSiguiente(listaEstaciones.get(i + 1), listaEstaciones.get(i));
            }
            if (i != 0 && i + 1 <= listaEstaciones.size()) {
                aniadirAnterior(listaEstaciones.get(i - 1), listaEstaciones.get(i));
            }
        }
    }

    protected void completarContiguas(HashMap<String, ArrayList<Estacion>> mapaEstaciones) {
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

    protected void aniadirCorrespondencias(ArrayList<Estacion> listaEstaciones) {
        for (int i = 0; i < listaEstaciones.size(); i++) {
            ArrayList<Estacion> e = this.mapaEstaciones.get(listaEstaciones.get(i).getNombre());
            if (e.size() > 1) {
                for (int j = 0; j < e.size(); j++) {
                    Estacion origen = e.get(j);
                    for (int x = 0; x < e.size(); x++) {
                        Estacion destino = e.get(x);
                        if (origen != destino && (origen.accesible && destino.accesible)) {
                            Conexion c = new Conexion(origen, destino);
                            this.grafoEstaciones.addEdge(origen, destino, c);
                        }
                    }
                }
            }
        }
    }

    protected ArrayList<Estacion> listaAccesibles(Estacion estacion, ArrayList<Estacion> visitadas) {
        ArrayList<Estacion> resultado = new ArrayList<>();
        ArrayList<Estacion> aux = new ArrayList<>();
        ArrayList<Estacion> aux2 = new ArrayList<>();
        ArrayList<Estacion> correspondencias = this.mapaEstaciones.get(estacion.getNombre());
        for (int i = 0; i < correspondencias.size(); i++) {
            visitadas.add(correspondencias.get(i));
            if (correspondencias.get(i).isAccesible()) {
                resultado.add(correspondencias.get(i));
            }
            if (correspondencias.get(i).estacionAnterior != null) {
                if (!(visitadas.contains(correspondencias.get(i).estacionAnterior))) {
                    aux.add(correspondencias.get(i).estacionAnterior);
                }
            }
            if (correspondencias.get(i).estacionSiguiente != null) {
                if(!(visitadas.contains(correspondencias.get(i).estacionSiguiente))) {
                    aux.add(correspondencias.get(i).estacionSiguiente);
                }
            }
        }
        if (resultado.size() == 0) {
            for (int i = 0; i < aux.size(); i++) {
                if (resultado.size()==0) {
                    resultado = listaAccesibles(aux.get(i),visitadas);
                }
            }
            if (resultado.size() == 0) {
                for (int i = 0; i<aux.size(); i++) {
                    ArrayList auxIzq = this.anteriorHastaAccesible(aux.get(i));
                    ArrayList auxDer = this.siguienteHastaAccesible(aux.get(i));
                    if (auxIzq.size() < aux2.size()) {
                        aux2 = auxIzq;
                    }
                    if (auxDer.size() < aux2.size()) {
                        aux2 = auxDer;
                    }
                }
                resultado.add(aux2.get(aux2.size()-1));
                for(int i=0; i<this.mapaEstaciones.get(resultado.get(0).getNombre()).size();i++){
                resultado.add(this.mapaEstaciones.get(aux2.get(aux2.size()-1).getNombre()).get(i));
                }
            }

        }
        return resultado;
    }

    protected ArrayList mejorCamino(ArrayList<Estacion> listaEstacionesOrigen, ArrayList<Estacion> listaEstacionesDestino, boolean transbordos) {
        ArrayList resultadoRuta = new ArrayList();
        ArrayList aux = new ArrayList();
        ArrayList <Ruta> rutas =  new ArrayList<>();
        Ruta auxTransbordos = new Ruta(null,0);
        for (int i = 0; i < listaEstacionesOrigen.size(); i++) {
            Estacion origen = listaEstacionesOrigen.get(i);
            for (int j = 0; j < listaEstacionesDestino.size(); j++) {
                Estacion destino = listaEstacionesDestino.get(j);
                DijkstraShortestPath dijkstra = new DijkstraShortestPath(this.grafoEstaciones, origen, destino);
                aux = (ArrayList) dijkstra.getPathEdgeList();
                int contadorTransbordos =0;
                if (transbordos){
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
                if (auxTransbordos.getTransbordos() > r.getTransbordos()){
                    auxTransbordos = r;
                } else if (auxTransbordos.getTransbordos() == r.getTransbordos()) {
                    if (auxTransbordos.getRuta().size()>r.getRuta().size()){
                        auxTransbordos = r;
                    }
                }
                resultadoRuta = auxTransbordos.getRuta();
            }
        }
        return resultadoRuta;
    }
    private ArrayList<Estacion> siguienteHastaAccesible(Estacion estacion) {
        ArrayList<Estacion> diferencia = new ArrayList();
        diferencia.add(estacion);
        while (!estacion.isAccesible()) {
            if (estacion.getEstacionSiguiente()!= null) {
                diferencia.add(estacion.getEstacionSiguiente());
                estacion = estacion.getEstacionSiguiente();
            }
            else {
                return diferencia;
            }
        }
        return diferencia;
    }
    private ArrayList<Estacion> anteriorHastaAccesible(Estacion estacion) {
        ArrayList<Estacion> diferencia = new ArrayList();
        diferencia.add(estacion);
        while (!(estacion.isAccesible())) {
            if (estacion.getEstacionAnterior()!= null) {
                diferencia.add(estacion.getEstacionAnterior());
                estacion = estacion.getEstacionAnterior();
            }
            else{
                return diferencia;
            }
        }
        return diferencia;
    }
    public ArrayList calcularCaminoDefinitivo (Estacion origen, Estacion destino, boolean transbordos){
        ArrayList<Estacion> visitadasOrigen = new ArrayList<>();
        ArrayList<Estacion> visitadasDestino = new ArrayList<>();
        ArrayList<Estacion> listaOrigen = this.listaAccesibles(origen,visitadasOrigen);
        ArrayList<Estacion> listaDestino = this.listaAccesibles(destino,visitadasDestino);
        ArrayList rutaFinal = this.mejorCamino(listaOrigen,listaDestino,transbordos);
        return rutaFinal;
    }
}
