package com.example.liberateapp.modelo;

/**
 * Archivo que se encuentra en la nube
 */
public class Archivo {
    private String nombre;

    public Archivo(String nombre, String url, String tipo, String extension) {
        this.nombre = nombre;
        this.url = url;
        this.tipo = tipo;
        this.extension = extension;
    }

    private String url;
    private String tipo; //Informes, Boletines, Revistas, Capacitaciones

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

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
}
