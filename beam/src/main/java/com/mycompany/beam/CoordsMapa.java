package com.mycompany.beam;

import java.io.Serializable;

public class CoordsMapa implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 5756493244892935705L;
    public String type;
    public Geometry geometry;
    public Properties properties;

    public CoordsMapa(Coords datos){
        this.type = "Feature";

        this.geometry = new Geometry();
        this.geometry.coordinates[0] = datos.getLongitud();
        this.geometry.coordinates[1] = datos.getLatitud();

        this.properties = new Properties();
        this.properties.aceleracion = datos.getAceleracion();
        this.properties.nombre = datos.getNombre();
    }

    public class Geometry implements Serializable{
        /**
         *
         */
        private static final long serialVersionUID = 6662112635938710182L;
        
        public double[] coordinates;
        public String type;

        public Geometry(){
            this.type = "Point";
            this.coordinates = new double[2];
        }
    }

    public class Properties implements Serializable{
        /**
         *
         */
        private static final long serialVersionUID = -2203613787995756268L;
        public String nombre;
        public double aceleracion;
    }
}