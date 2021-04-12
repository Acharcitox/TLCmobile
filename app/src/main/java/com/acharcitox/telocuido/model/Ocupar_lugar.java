package com.acharcitox.telocuido.model;

import java.text.DateFormat;
import java.util.Date;

public class Ocupar_lugar {
    //Campos de la base de datos
    private String Id_transaccion;
    private Integer Id_conductor;
    private Integer CI_operador;
    private Integer Id_lugar_asignado;
    private String Fecha_hora_inicio;
    private String Fecha_hora_fin;
    private Boolean Esta_libre;
    private Integer Calificacion;
    private String Comentario;
    private Integer Monto_Propina;
    private String Fecha_hora_propina;
    private String Tipo_denuncia;
    private String Texto_denuncia;

    //Constructor
    public Ocupar_lugar() {
    }

    // Getters and Setters
    public String getId_transaccion() {
        return Id_transaccion;
    }

    public void setId_transaccion(String id_transaccion) {
        Id_transaccion = id_transaccion;
    }

    public Integer getId_conductor() {
        return Id_conductor;
    }

    public void setId_conductor(Integer id_conductor) {
        Id_conductor = id_conductor;
    }

    public Integer getCI_operador() {
        return CI_operador;
    }

    public void setCI_operador(Integer CI_operador) {
        this.CI_operador = CI_operador;
    }

    public Integer getId_lugar_asignado() {
        return Id_lugar_asignado;
    }

    public void setId_lugar_asignado(Integer id_lugar_asignado) {
        Id_lugar_asignado = id_lugar_asignado;
    }

    public String getFecha_hora_inicio() {
        return Fecha_hora_inicio;
    }

    public void setFecha_hora_inicio(String fecha_hora_inicio) {
        Fecha_hora_inicio = fecha_hora_inicio;
    }

    public String getFecha_hora_fin() {
        return Fecha_hora_fin;
    }

    public void setFecha_hora_fin(String fecha_hora_fin) {
        Fecha_hora_fin = fecha_hora_fin;
    }

    public String getFecha_hora_propina() {
        return Fecha_hora_propina;
    }

    public void setFecha_hora_propina(String fecha_hora_propina) {
        Fecha_hora_propina = fecha_hora_propina;
    }

    public Boolean getEsta_libre() {
        return Esta_libre;
    }

    public void setEsta_libre(Boolean esta_libre) {
        Esta_libre = esta_libre;
    }

    public Integer getCalificacion() {
        return Calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        Calificacion = calificacion;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
    }

    public Integer getMonto_Propina() {
        return Monto_Propina;
    }

    public void setMonto_Propina(Integer monto_Propina) {
        Monto_Propina = monto_Propina;
    }


    public String getTipo_denuncia() {
        return Tipo_denuncia;
    }

    public void setTipo_denuncia(String tipo_denuncia) {
        Tipo_denuncia = tipo_denuncia;
    }

    public String getTexto_denuncia() {
        return Texto_denuncia;
    }

    public void setTexto_denuncia(String texto_denuncia) {
        Texto_denuncia = texto_denuncia;
    }

    //ToString
    @Override
    public String toString() {
        return Id_transaccion;
    }

}
