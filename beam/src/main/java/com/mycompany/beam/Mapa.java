package com.mycompany.beam;

import java.io.Serializable;

public class Mapa implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6784348773023514246L;
    public String type;
    public CoordsMapa[] features;

    public Mapa(CoordsMapa[] datos){
        this.type = "FeatureCollection";
        this.features = datos;
    }
}