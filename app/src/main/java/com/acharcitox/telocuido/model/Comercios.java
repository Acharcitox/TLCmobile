package com.acharcitox.telocuido.model;

public class Comercios {
    //Campos de la base de datos
    private Integer Id_comercio;
    private String Nombre;
    private String Rubro;
    private String Descripcion;
    private String Direccion;
    private Float Latitud;
    private Float Longitud;

    //Constructor
    public Comercios() {
    }

    // Getters and Setters
    public Integer getId_comercio() {
        return Id_comercio;
    }

    public void setId_comercio(Integer id_comercio) {
        Id_comercio = id_comercio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getRubro() {
        return Rubro;
    }

    public void setRubro(String rubro) {
        Rubro = rubro;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public Float getLatitud() {
        return Latitud;
    }

    public void setLatitud(Float latitud) {
        Latitud = latitud;
    }

    public Float getLongitud() {
        return Longitud;
    }

    public void setLongitud(Float longitud) {
        Longitud = longitud;
    }

    //ToString
    @Override
    public String toString() {
        return Nombre;
    }
}
