package com.example.aplicacion1.datos.remoto;

import com.example.aplicacion1.datos.dto.UsuarioDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiUsuarios {
    // Método para obtener el directorio completo (Escenario 1)
    @GET("users")
    Call<List<UsuarioDto>> obtenerListaUsuarios();
}