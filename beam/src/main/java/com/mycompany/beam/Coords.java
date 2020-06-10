package com.mycompany.beam;


import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coords implements Serializable{
    private String nombre;
    private double latitud;
    private double longitud;
    private double aceleracion;

    public String getNombre(){
        return this.nombre;
    }

    public double getLatitud(){
        return this.latitud;
    }

    public double getLongitud(){
        return this.longitud;
    }

    public double getAceleracion(){
        return this.aceleracion;
    }
}