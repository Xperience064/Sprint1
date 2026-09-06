package com.example.aplicacion1.funcionalidad.crearproducto.controlador;

import android.util.Patterns;

import com.example.aplicacion1.datos.dto.creacionproducto.RespuestaProducto;
import com.example.aplicacion1.datos.dto.creacionproducto.SolicitudProducto;
import com.example.aplicacion1.datos.repositorio.RepositorioProducto;
import com.example.aplicacion1.modelo.Rol;
import com.example.aplicacion1.nucleo.sesion.GestorSesion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControladorCrearProducto {

    private final RepositorioProducto repositorioProducto;
    private final GestorSesion gestorSesion;

    public ControladorCrearProducto(
            RepositorioProducto repositorioProducto,
            GestorSesion gestorSesion
    ) {
        this.repositorioProducto = repositorioProducto;
        this.gestorSesion = gestorSesion;
    }

    public void crearProducto(
            String titulo,
            String precioTexto,
            String descripcion,
            String imagen,
            String categoria,
            ResultadoCrearProducto resultado
    ) {

        Rol rol = gestorSesion.obtenerRol();

        if (rol != Rol.ADMINISTRADOR) {
            resultado.accesoDenegado();
            return;
        }

        if (titulo == null || titulo.trim().isEmpty()) {
            resultado.datosInvalidos("Ingresa el título del producto");
            return;
        }

        if (precioTexto == null || precioTexto.trim().isEmpty()) {
            resultado.datosInvalidos("Ingresa el precio del producto");
            return;
        }

        double precio;

        try {
            precio = Double.parseDouble(precioTexto.trim());
        } catch (NumberFormatException e) {
            resultado.datosInvalidos("El precio debe ser numérico");
            return;
        }

        if (precio <= 0) {
            resultado.datosInvalidos("El precio debe ser mayor a cero");
            return;
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            resultado.datosInvalidos("Ingresa la descripción del producto");
            return;
        }

        if (imagen == null || imagen.trim().isEmpty()) {
            resultado.datosInvalidos("Ingresa la URL de la imagen");
            return;
        }

        if (!Patterns.WEB_URL.matcher(imagen.trim()).matches()) {
            resultado.datosInvalidos("La URL de la imagen no es válida");
            return;
        }

        if (categoria == null || categoria.trim().isEmpty()) {
            resultado.datosInvalidos("Ingresa la categoría del producto");
            return;
        }

        SolicitudProducto solicitudProducto =
                new SolicitudProducto(
                        titulo.trim(),
                        precio,
                        descripcion.trim(),
                        imagen.trim(),
                        categoria.trim()
                );

        resultado.cargando(true);

        repositorioProducto.crearProducto(solicitudProducto)
                .enqueue(new Callback<RespuestaProducto>() {

                    @Override
                    public void onResponse(
                            Call<RespuestaProducto> call,
                            Response<RespuestaProducto> response
                    ) {

                        resultado.cargando(false);

                        if (response.isSuccessful() && response.body() != null) {
                            resultado.productoCreado(response.body().getId());
                        } else {
                            resultado.error("No fue posible crear el producto");
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<RespuestaProducto> call,
                            Throwable throwable
                    ) {

                        resultado.cargando(false);
                        resultado.error("Ocurrió un problema al conectar con el servidor");
                    }
                });
    }

    public interface ResultadoCrearProducto {

        void cargando(boolean estaCargando);

        void productoCreado(int idProducto);

        void datosInvalidos(String mensaje);

        void accesoDenegado();

        void error(String mensaje);
    }
}