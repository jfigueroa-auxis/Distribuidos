package com.mycompany.beam;

public class Mapa {
    public String type;
    public CoordsMapa[] features;

    public Mapa(CoordsMapa[] datos){
        this.type = "FeatureCollection";
        this.features = datos;
    }
}