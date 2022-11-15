package com.example.liberateapp.modelo;

/**
 * Usuario
 */
public class Usuario {
    public String getAdmin(){return  admin;}
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @param uuid
     * @param email
     * @param nombre
     * @param admin si o no, configurar en la nube
     */
    public Usuario(String uuid, String email, String nombre, String admin) {
        this.uuid = uuid;
        this.email = email;
        this.nombre = nombre;
        this.admin = admin;
    }

    private String uuid;
    private String email;
    private String nombre;
    private String admin;

}
