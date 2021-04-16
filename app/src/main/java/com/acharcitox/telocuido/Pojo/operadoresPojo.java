package com.acharcitox.telocuido.Pojo;

// Esta es la clase orientada a los objetos
public class operadoresPojo {

    private String tipo_operador;
    private String Nombre;
    private int Calificacion_promedio;
    private float Latitud;
    private float Longitude;

    public operadoresPojo() {

    }

    public String getTipo_operador() {
        return tipo_operador;
    }

    public String getNombre() {
        return Nombre;
    }

    public int getCalificacion_promedio() {
        return Calificacion_promedio;
    }

    public float getLatitud() {
        return Latitud;
    }

    public float getLongitude() {
        return Longitude;
    }
}
