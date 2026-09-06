package com.example.aplicacion1.datos.remoto.catalogo;

import com.example.aplicacion1.datos.dto.creacionproducto.RespuestaProducto;
import com.example.aplicacion1.datos.dto.creacionproducto.SolicitudProducto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiProductos {

    @POST("products")
    Call<RespuestaProducto> crearProducto(
            @Body SolicitudProducto solicitudProducto
    );
}