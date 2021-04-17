package com.acharcitox.telocuido.model;

import java.util.Date;

public class Operadores {
    //Campos de la base de datos
    private Integer CI_operador;
    private String Nombre;
    private String Apellido;
    private String Direccion;
    private String Telefono;
    private String Mail;
    private String Password;
    private Boolean Estado;
    private String Tipo_operador;
    private Integer Id_lugar_asignado;
    private String Nombre_ubicacion;
    private Integer Cantidad_total_lugares;
    private String Ubicacion;
    private Float Latitud;
    private Float Longitud;
    private Integer Cantidad_restante_lugares;
    private String Hora_inicio_trabajo;
    private String Hora_fin_trabajo;
    private Integer Calificacion_promedio;

    //Constructor
    public Operadores() {
    }

    // Getters and Setters
    public Integer getCI_operador() {
        return CI_operador;
    }

    public void setCI_operador(Integer CI_operador) {
        this.CI_operador = CI_operador;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Boolean getEstado() {
        return Estado;
    }

    public void setEstado(Boolean estado) {
        Estado = estado;
    }

    public String getTipo_operador() {
        return Tipo_operador;
    }

    public void setTipo_operador(String tipo_operador) {
        Tipo_operador = tipo_operador;
    }

    public Integer getId_lugar_asignado() {
        return Id_lugar_asignado;
    }

    public void setId_lugar_asignado(Integer id_lugar_asignado) {
        Id_lugar_asignado = id_lugar_asignado;
    }

    public String getNombre_ubicacion() {
        return Nombre_ubicacion;
    }

    public void setNombre_ubicacion(String nombre_ubicacion) {
        Nombre_ubicacion = nombre_ubicacion;
    }

    public Integer getCantidad_total_lugares() {
        return Cantidad_total_lugares;
    }

    public void setCantidad_total_lugares(Integer cantidad_total_lugares) {
        Cantidad_total_lugares = cantidad_total_lugares;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
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

    public Integer getCantidad_restante_lugares() {
        return Cantidad_restante_lugares;
    }

    public void setCantidad_restante_lugares(Integer cantidad_restante_lugares) {
        Cantidad_restante_lugares = cantidad_restante_lugares;
    }

    public String getHora_inicio_trabajo() {
        return Hora_inicio_trabajo;
    }

    public void setHora_inicio_trabajo(String hora_inicio_trabajo) {
        Hora_inicio_trabajo = hora_inicio_trabajo;
    }

    public String getHora_fin_trabajo() {
        return Hora_fin_trabajo;
    }

    public void setHora_fin_trabajo(String hora_fin_trabajo) {
        Hora_fin_trabajo = hora_fin_trabajo;
    }

    public Integer getCalificacion_promedio() {
        return Calificacion_promedio;
    }

    public void setCalificacion_promedio(Integer calificacion_promedio) {
        Calificacion_promedio = calificacion_promedio;
    }

    //ToString
    @Override
    public String toString() {
        return Nombre;
    }
}





