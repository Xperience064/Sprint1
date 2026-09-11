package com.example.aplicacion1.datos.dto; // Ajusta el package si lo metes en otra carpeta

import com.google.gson.annotations.SerializedName;

public class UsuarioDto {
    private int id;
    private String email;
    private String username;
    private String phone;
    private Nombre name;
    private Direccion address;

    // Clases anidadas para soportar la estructura del JSON sin que la app colapse (Regla de negocio)

    public static class Nombre {
        private String firstname;
        private String lastname;

        public String getNombreCompleto() { return firstname + " " + lastname; }
    }

    public static class Direccion {
        private String city;
        private String street;
        private int number;
        private String zipcode;
        private Geo geolocation;
    }

    public static class Geo {
        private String lat;
        @SerializedName("long") // Se usa esto porque 'long' es palabra reservada en Java
        private String longitud;
    }

    // Getters principales para la vista (Escenario 1)
    public String getEmail() { return email; }
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPhone() { return phone; }
    public Nombre getName() { return name; }
}
