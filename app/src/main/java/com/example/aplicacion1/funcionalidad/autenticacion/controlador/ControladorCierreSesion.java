package com.example.aplicacion1.funcionalidad.autenticacion.controlador;

import com.example.aplicacion1.nucleo.carrito.LimpiadorCarrito;
import com.example.aplicacion1.nucleo.sesion.GestorSesion;

public class ControladorCierreSesion {

    private final GestorSesion gestorSesion;
    private final LimpiadorCarrito limpiadorCarrito;

    public ControladorCierreSesion(
            GestorSesion gestorSesion,
            LimpiadorCarrito limpiadorCarrito
    ) {
        this.gestorSesion = gestorSesion;
        this.limpiadorCarrito = limpiadorCarrito;
    }

    public void cerrarSesion(ResultadoCierreSesion resultado) {

        gestorSesion.cerrarSesion();
        limpiadorCarrito.limpiarCarrito();

        resultado.sesionCerrada();
    }

    public interface ResultadoCierreSesion {

        void sesionCerrada();
    }
}