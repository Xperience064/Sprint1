package com.example.aplicacion1.funcionalidad;

import com.example.aplicacion1.model.Product;
import com.example.aplicacion1.modelo.Rol;

import java.util.List;
// Asegúrate de importar aquí tu ProductoDto si está en otro paquete

public class ControladorCarritos {

    // Escenario 2 y Regla Estricta: Solo Auditor/Administrador, sin botones de alteración
    public boolean tienePermisoParaAuditar(Rol rolUsuario) {
        if (rolUsuario == Rol.CLIENTE) {
            System.out.println("Bloqueo: La aplicación oculta la sección al Cliente.");
            return false;
        }
        return true;
    }

    // Regla de Negocio (Reto lógico): Cruce de datos para mostrar el título real
    // (Simulamos recibir el catálogo de productos ya descargado de la otra API)
    public String resolverNombreProducto(int idBuscado, List<Product> catalogoGlobal) {
        for (Product producto : catalogoGlobal) {
            if (producto.getId() == idBuscado) {
                return producto.getTitle();
            }
        }
        return "Producto Desconocido";
    }
}

