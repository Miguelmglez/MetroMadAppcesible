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
 * Created by Miguel Maroto González on 21-10-16.
 *
 * Clase necesaria para realizar el parseo del documento /assets/metro.xml que contiene la información relativa
 * a la estaciones del metro de Madrid. A partir de este parseo se van a construir el grafo, las estaciones y las conexiones.
 *
 */

public class Parser {

        public Metro parsearMetro(File xml){
            Metro metroMadrid = new Metro();
            ArrayList<Estacion> listaEstaciones = new ArrayList<Estacion>();
            try {
                File inputFile = new File(String.valueOf(xml));
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(inputFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("sch:SubwayStation");

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    int linea = 0;
                    double latitud = 0;
                    double longitud = 0;
                    boolean accesible;
                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        String nombreEstacion = eElement.getElementsByTagName("sch:name").item(0).getTextContent();
                        String accesibleStr = eElement.getElementsByTagName("ont:isAccessible").item(0).getTextContent();
                        if (accesibleStr.equalsIgnoreCase("true")) {
                            accesible = true;
                        } else {
                            accesible = false;
                        }
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
                        }
                        Estacion estacionAux = new Estacion(linea, nombreEstacion, latitud, longitud, accesible);
                        metroMadrid.aniadirEstacion(estacionAux);
                        listaEstaciones.add(estacionAux);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            metroMadrid.aniadirContiguas(listaEstaciones);
            metroMadrid.completarContiguas();
            metroMadrid.aniadirCorrespondencias(listaEstaciones);
            return metroMadrid;
        }
    }




