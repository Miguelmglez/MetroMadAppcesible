package com.miguel.metromadappcesible.code;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Created by MMG on 21-10-16.
 */

public class Parser {


    public static void main(String[] args) {

        Metro metroMadrid = new Metro();
        ArrayList<Estacion> listaEstaciones = new ArrayList<Estacion>();
        try {
            File inputFile = new File("metro.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("sch:SubwayStation");
            System.out.println("--------------LISTA ESTACIONES--------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                int linea = 0;
                double latitud = 0;
                double longitud = 0;
                boolean accesible;
                Node nNode = nList.item(temp);
                System.out.println("\n");
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String nombreEstacion = eElement.getElementsByTagName("sch:name").item(0).getTextContent();
                    String accesibleStr = eElement.getElementsByTagName("ont:isAccessible").item(0).getTextContent();
                    if (accesibleStr.equalsIgnoreCase("true")) {
                        accesible = true;
                    } else {
                        accesible = false;
                    }
                    System.out.println("Nombre : " + nombreEstacion);
                    System.out.println("Accesible : " + accesible);
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



        /* ---- Para ver el recorrido de la ruta encontrada y sus transbordos
        Estacion e1 = metroMadrid.mapaEstaciones.get("Estrella").get(0);
        Estacion e2 = metroMadrid.mapaEstaciones.get("Metropolitano").get(0);

        ArrayList<Estacion> visitadasOrigen = new ArrayList<>();
        ArrayList<Estacion> visitadasDestino = new ArrayList<>();
        ArrayList<Estacion> listaOrigen = metroMadrid.listaAccesibles(e1,visitadasOrigen);
        ArrayList<Estacion> listaDestino = metroMadrid.listaAccesibles(e2,visitadasDestino);
        System.out.println("Lista origen:");
        for (int i = 0; i<listaOrigen.size();i++){
            System.out.println(listaOrigen.get(i).getNombre());
        }
        System.out.println("Lista Destino:");
        for (int i = 0; i<listaDestino.size();i++){
            System.out.println(listaDestino.get(i).getNombre());
        }
        boolean transbordos = false;
        ArrayList rutaFinal = metroMadrid.mejorCamino(listaOrigen,listaDestino,transbordos);

        for (int i = 0; i<rutaFinal.size();i++){
            Conexion c = (Conexion) rutaFinal.get(i);
            if (c.getEstacionDestino().getNombre().equals(c.getEstacionOrigen().getNombre())){
                System.out.println("-------------TRANSBORDO--------- "+ c.getEstacionDestino().getNombre());
            }
            else{
                System.out.println("DESDE:  "+c.getEstacionOrigen().getNombre()+ "        A LA ESTACION:  "+ c.getEstacionDestino().getNombre());
            }
        }
        System.out.print(rutaFinal.size());
        */

        /* ---- Para calcular una ruta

        Estacion laGranja = metroMadrid.mapaEstaciones.get("Avenida de America").get(0);
        Estacion elCasar = metroMadrid.mapaEstaciones.get("Portazgo").get(0);
        DijkstraShortestPath dijkstra = new DijkstraShortestPath(metroMadrid.grafoEstaciones,laGranja,elCasar);
        ArrayList ruta = (ArrayList)dijkstra.getPathEdgeList();
        System.out.print(ruta.size());
*/
        /* ---- Para ver las correspondencias de cada estación
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("EMPIEZAN LAS CORRESPONDENCIAS.....................");
            for (int i = 0; i<listaEstaciones.size();i++){
                System.out.println();
                Estacion e = (Estacion) listaEstaciones.get(i);
                System.out.print ("ESTACION: " +e.getNombre());
                System.out.println();
                System.out.println();
                if (e.getEstacionAnterior()==null){
                    System.out.println("null!!!!!!!!!");
                    System.out.println();
                }
                else {
                    System.out.println("Anterior:       " + e.getEstacionAnterior().getNombre());
                    System.out.println();
                }
                if (e.getEstacionSiguiente() == null || e.getEstacionSiguiente().getLinea()!=e.getLinea() ){
                    System.out.println("null!!!!!!!!!");

                }
                else {
                    System.out.println("Siguiente:      " + e.getEstacionSiguiente().getNombre());

                }
                System.out.println("---------------------------");
            } */
    }
    }


