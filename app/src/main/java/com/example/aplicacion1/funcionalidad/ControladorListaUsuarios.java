package com.example.aplicacion1.funcionalidad;

import com.example.aplicacion1.modelo.Rol;

public class ControladorListaUsuarios {

    // ESCENARIO 2: Restricción de acceso estricta
    public boolean tienePermisoParaVerUsuarios(Rol rolUsuario) {
        // Solo Auditores y Administradores pasan. Si es CLIENTE, se bloquea.
        if (rolUsuario == Rol.CLIENTE) {
            System.out.println("Bloqueo de seguridad: El menú omite renderizar el botón y bloquea la ruta.");
            return false;
        }
        return true;
    }
}