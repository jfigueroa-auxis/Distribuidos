package com.mycompany.beam;

public class CoordsMapa {
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
    }

    public class Geometry{
        public double[] coordinates;
        public String type;

        public Geometry(){
            this.type = "Point";
            this.coordinates = new double[2];
        }
    }

    public class Properties{
        public String nombre;
        public double aceleracion;
    }
}