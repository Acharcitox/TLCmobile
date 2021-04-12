package com.acharcitox.telocuido.model;

public class Conductores {
//Campos de la base de datos
    private String Id_conductor;
    private String Mail;
    private String usuario;
    private String Password;
    private String Nombre;
    private String Apellido;
    private String Telefono;
    private Boolean Estado;
//Constructor
    public Conductores() {
    }
// Getters and Setters

    public String getId_conductor() {
        return Id_conductor;
    }

    public void setId_conductor(String id_conductor) {
        Id_conductor = id_conductor;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public Boolean getEstado() {
        return Estado;
    }

    public void setEstado(Boolean estado) {
        Estado = estado;
    }

    //ToString
    @Override
    public String toString() {
        return Id_conductor;
    }
}
