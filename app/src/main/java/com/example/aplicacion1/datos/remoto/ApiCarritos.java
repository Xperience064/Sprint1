package com.example.aplicacion1.datos.remoto;

import com.example.aplicacion1.datos.dto.CarritoDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiCarritos {
    // Escenario 1: Consumir el endpoint /carts global
    @GET("carts")
    Call<List<CarritoDto>> obtenerCarritosGlobales();
}