package com.example.aplicacion1.funcionalidad.autenticacion.controlador;

import android.content.Context;

import com.example.aplicacion1.datos.dto.autenticacion.RespuestaLogin;
import com.example.aplicacion1.datos.dto.autenticacion.UsuarioAutenticacion;
import com.example.aplicacion1.datos.repositorio.RepositorioAutenticacion;
import com.example.aplicacion1.modelo.Rol;
import com.example.aplicacion1.nucleo.red.UtilidadRed;
import com.example.aplicacion1.nucleo.seguridad.MapeadorRol;
import com.example.aplicacion1.nucleo.sesion.GestorSesion;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControladorLogin {

    private final RepositorioAutenticacion repositorioAutenticacion;
    private final GestorSesion gestorSesion;
    private final MapeadorRol mapeadorRol;

    public ControladorLogin(
            RepositorioAutenticacion repositorioAutenticacion,
            GestorSesion gestorSesion,
            MapeadorRol mapeadorRol
    ) {
        this.repositorioAutenticacion = repositorioAutenticacion;
        this.gestorSesion = gestorSesion;
        this.mapeadorRol = mapeadorRol;
    }

    public void iniciarSesion(
            Context contexto,
            String nombreUsuario,
            String contrasena,
            ResultadoLogin resultado
    ) {

        if (!UtilidadRed.hayConexionInternet(contexto)) {
            resultado.sinConexion();
            return;
        }

        if (nombreUsuario == null
                || nombreUsuario.trim().isEmpty()
                || contrasena == null
                || contrasena.trim().isEmpty()) {

            resultado.camposInvalidos();
            return;
        }

        resultado.cargando(true);

        repositorioAutenticacion
                .iniciarSesion(nombreUsuario.trim(), contrasena)
                .enqueue(new Callback<RespuestaLogin>() {

                    @Override
                    public void onResponse(
                            Call<RespuestaLogin> call,
                            Response<RespuestaLogin> response
                    ) {

                        if (response.code() == 401) {
                            resultado.cargando(false);
                            resultado.credencialesIncorrectas();
                            return;
                        }

                        if (!response.isSuccessful()
                                || response.body() == null
                                || response.body().getToken() == null) {

                            resultado.cargando(false);
                            resultado.error(
                                    "No fue posible iniciar sesión."
                            );
                            return;
                        }

                        String token = response.body().getToken();

                        obtenerUsuarioYGuardarSesion(
                                nombreUsuario.trim(),
                                token,
                                resultado
                        );
                    }

                    @Override
                    public void onFailure(
                            Call<RespuestaLogin> call,
                            Throwable throwable
                    ) {

                        resultado.cargando(false);

                        resultado.error(
                                "Ocurrió un problema al conectar con el servidor."
                        );
                    }
                });
    }

    private void obtenerUsuarioYGuardarSesion(
            String nombreUsuario,
            String token,
            ResultadoLogin resultado
    ) {

        repositorioAutenticacion
                .obtenerUsuarios()
                .enqueue(new Callback<List<UsuarioAutenticacion>>() {

                    @Override
                    public void onResponse(
                            Call<List<UsuarioAutenticacion>> call,
                            Response<List<UsuarioAutenticacion>> response
                    ) {

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            resultado.cargando(false);
                            resultado.error(
                                    "No se pudo obtener la información del usuario."
                            );
                            return;
                        }

                        UsuarioAutenticacion usuarioEncontrado = null;

                        for (UsuarioAutenticacion usuario : response.body()) {

                            if (usuario.getNombreUsuario() != null
                                    && usuario.getNombreUsuario()
                                    .equals(nombreUsuario)) {

                                usuarioEncontrado = usuario;
                                break;
                            }
                        }

                        if (usuarioEncontrado == null) {

                            resultado.cargando(false);
                            resultado.error(
                                    "No se encontró la información del usuario."
                            );
                            return;
                        }

                        int idUsuario = usuarioEncontrado.getId();

                        Rol rol =
                                mapeadorRol.obtenerRolPorIdUsuario(
                                        idUsuario
                                );

                        gestorSesion.guardarSesion(
                                token,
                                idUsuario,
                                rol
                        );

                        resultado.cargando(false);
                        resultado.exito(rol);
                    }

                    @Override
                    public void onFailure(
                            Call<List<UsuarioAutenticacion>> call,
                            Throwable throwable
                    ) {

                        resultado.cargando(false);

                        resultado.error(
                                "No se pudo descargar la información del usuario."
                        );
                    }
                });
    }

    public interface ResultadoLogin {

        void cargando(boolean estaCargando);

        void exito(Rol rol);

        void credencialesIncorrectas();

        void sinConexion();

        void camposInvalidos();

        void error(String mensaje);
    }
}