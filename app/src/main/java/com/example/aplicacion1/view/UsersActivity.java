package com.example.aplicacion1.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aplicacion1.adapter.SimpleTextAdapter;
import com.example.aplicacion1.datos.dto.UsuarioDto;
import com.example.aplicacion1.modelo.Rol;
import com.example.aplicacion1.network.RetrofitClient;
import com.example.aplicacion1.nucleo.sesion.GestorSesionLocal;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersActivity extends AppCompatActivity {
    private ProgressBar progress;
    private TextView error;
    private Button retry;
    private SimpleTextAdapter adapter;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        Rol rol = new GestorSesionLocal(this).obtenerRol();
        if (rol != Rol.ADMINISTRADOR && rol != Rol.AUDITOR) {
            Toast.makeText(this, "Acceso denegado", Toast.LENGTH_SHORT).show(); finish(); return;
        }
        setTitle("Usuarios registrados");
        buildUi();
        load();
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(20,20,20,20);
        progress = new ProgressBar(this); error = new TextView(this); retry = new Button(this); retry.setText("Reintentar");
        RecyclerView list = new RecyclerView(this); list.setLayoutManager(new LinearLayoutManager(this)); adapter = new SimpleTextAdapter(); list.setAdapter(adapter);
        root.addView(progress); root.addView(error); root.addView(retry); root.addView(list, new LinearLayout.LayoutParams(-1,0,1));
        retry.setOnClickListener(v -> load()); setContentView(root);
    }

    private void load() {
        progress.setVisibility(View.VISIBLE); error.setVisibility(View.GONE); retry.setVisibility(View.GONE);
        RetrofitClient.getApiService().getUsers().enqueue(new Callback<List<UsuarioDto>>() {
            @Override public void onResponse(Call<List<UsuarioDto>> call, Response<List<UsuarioDto>> response) {
                progress.setVisibility(View.GONE);
                if (!response.isSuccessful() || response.body() == null) { showError("No se pudieron cargar los usuarios. Código: " + response.code()); return; }
                List<String> rows = new ArrayList<>();
                for (UsuarioDto user : response.body()) {
                    String name = user.getName() == null ? "Sin nombre" : user.getName().getNombreCompleto();
                    rows.add(name + "\nCorreo: " + user.getEmail() + "\nTeléfono: " + user.getPhone() + "\nUsuario: " + user.getUsername());
                }
                adapter.submit(rows);
            }
            @Override public void onFailure(Call<List<UsuarioDto>> call, Throwable t) { progress.setVisibility(View.GONE); showError("Error de conexión. Revisa tu Internet."); }
        });
    }
    private void showError(String message) { error.setText(message); error.setVisibility(View.VISIBLE); retry.setVisibility(View.VISIBLE); }
}
