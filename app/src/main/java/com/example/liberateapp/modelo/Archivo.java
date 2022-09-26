package com.example.liberateapp.modelo;

public class Archivo {
    public String nombre;
    public String url;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Archivo(String nombre, String url) {
        this.nombre = nombre;
        this.url = url;
    }




}
