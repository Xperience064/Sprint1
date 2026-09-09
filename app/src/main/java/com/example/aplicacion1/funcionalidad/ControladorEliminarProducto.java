package com.example.aplicacion1.funcionalidad;

import com.example.aplicacion1.modelo.Rol;
import com.example.aplicacion1.datos.repositorio.RepositorioProducto;

public class ControladorEliminarProducto {

    private final RepositorioProducto repositorio;

    public ControladorEliminarProducto() {
        this.repositorio = new RepositorioProducto();
    }

    // ESCENARIO 3: Restricción de seguridad.
    // Esto se usa en la vista para decidir si dibujar o esconder el botón de "Eliminar"
    public boolean tienePermisoParaEliminar(Rol rolUsuario) {
        if (rolUsuario != Rol.ADMINISTRADOR) {
            System.out.println("Bloqueo de seguridad: Interfaz no renderiza el botón.");
            return false;
        }
        return true;
    }

    // ESCENARIO 1 y REGLAS: Se ejecuta SOLO DESPUÉS de que el usuario le dio "Aceptar" al Alert Dialog
    public boolean confirmarYEliminar(int idArticulo) {
        // La Fake Store API recibe el DELETE
        repositorio.eliminarProducto(idArticulo);

        // Aquí tu Activity/Fragment mostrará el Toast y hará la redirección
        System.out.println("Eliminación exitosa en Fake Store. Mostrando Toast y redirigiendo al catálogo...");
        return true;
    }
}