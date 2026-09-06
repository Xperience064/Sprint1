package com.example.aplicacion1.datos.repositorio;

import com.example.aplicacion1.datos.dto.creacionproducto.RespuestaProducto;
import com.example.aplicacion1.datos.dto.creacionproducto.SolicitudProducto;
import com.example.aplicacion1.datos.remoto.catalogo.ApiProductos;
import com.example.aplicacion1.nucleo.red.ClienteApi;

import retrofit2.Call;

public class RepositorioProducto {

    private final ApiProductos apiProductos;

    public RepositorioProducto() {
        apiProductos = ClienteApi
                .obtenerRetrofit()
                .create(ApiProductos.class);
    }

    public Call<RespuestaProducto> crearProducto(
            SolicitudProducto solicitudProducto
    ) {
        return apiProductos.crearProducto(solicitudProducto);
    }
}