package com.example.aplicacion1.nucleo.seguridad;

import com.example.aplicacion1.modelo.Rol;

public class MapeadorRol {

    public Rol obtenerRolPorIdUsuario(int idUsuario) {

        if (idUsuario == 1 || idUsuario == 2) {
            return Rol.ADMINISTRADOR;
        }

        if (idUsuario == 3) {
            return Rol.AUDITOR;
        }

        return Rol.CLIENTE;
    }
}