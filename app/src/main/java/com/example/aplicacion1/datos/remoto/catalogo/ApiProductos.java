package com.example.aplicacion1.datos.remoto.catalogo;

import com.example.aplicacion1.datos.dto.creacionproducto.RespuestaProducto;
import com.example.aplicacion1.datos.dto.creacionproducto.SolicitudProducto;
import retrofit2.http.DELETE;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiProductos {

    @POST("products")
    Call<RespuestaProducto> crearProducto(
            @Body SolicitudProducto solicitudProducto
    );
    // ==========================================
    // US08: MÉTODO PARA ELIMINAR (DELETE)
    // ==========================================
    @DELETE("products/{id}")
    Call<RespuestaProducto> eliminarProducto(
            @Path("id") int idArticulo
    );

    @PUT("products/{id}")
    Call<RespuestaProducto> editarProducto(
            @Path("id") int idArticulo,
            @Body SolicitudProducto solicitudProducto
    );
}
