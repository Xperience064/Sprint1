package com.example.aplicacion1.nucleo.sesion;

import com.example.aplicacion1.modelo.Rol;

public interface GestorSesion {

    void guardarSesion(String token, int idUsuario, Rol rol);

    String obtenerToken();

    int obtenerIdUsuario();

    Rol obtenerRol();

    boolean haySesionActiva();

    void cerrarSesion();
}