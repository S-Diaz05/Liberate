package com.example.liberateapp.modelo;

/**
 * Archivo que se encuentra en la nube
 */
public class Archivo {
    private String nombre;
    private String url;
    private String tipo; //Informes, Boletines, Revistas, Capacitaciones
    private String extension;

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

    public String getTipo(){return tipo;}

    /**
     * Crear archivo
     * @param nombre
     * @param url
     * @param tipo Informes, Boletines, Revistas, Capacitaciones
     * @param extension Pdf, doc, etc.
     */
    public Archivo(String nombre, String url, String tipo, String extension) {
        this.nombre = nombre;
        this.url = url;
        this.tipo = tipo;
        this.extension = extension;
    }
}
