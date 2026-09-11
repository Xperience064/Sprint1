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
import com.example.aplicacion1.model.CartDto;
import com.example.aplicacion1.modelo.Rol;
import com.example.aplicacion1.network.RetrofitClient;
import com.example.aplicacion1.nucleo.sesion.GestorSesionLocal;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartHistoryActivity extends AppCompatActivity {
    private ProgressBar progress; private TextView error; private Button retry; private SimpleTextAdapter adapter;
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        Rol role = new GestorSesionLocal(this).obtenerRol();
        if (role != Rol.ADMINISTRADOR && role != Rol.AUDITOR) { Toast.makeText(this,"Acceso denegado",Toast.LENGTH_SHORT).show(); finish(); return; }
        setTitle("Histórico de carritos"); buildUi(); load();
    }
    private void buildUi() {
        LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(20,20,20,20);
        progress=new ProgressBar(this); error=new TextView(this); retry=new Button(this); retry.setText("Reintentar");
        RecyclerView list=new RecyclerView(this); list.setLayoutManager(new LinearLayoutManager(this)); adapter=new SimpleTextAdapter(); list.setAdapter(adapter);
        root.addView(progress); root.addView(error); root.addView(retry); root.addView(list,new LinearLayout.LayoutParams(-1,0,1)); retry.setOnClickListener(v->load()); setContentView(root);
    }
    private void load() {
        progress.setVisibility(View.VISIBLE); error.setVisibility(View.GONE); retry.setVisibility(View.GONE);
        RetrofitClient.getApiService().getCarts().enqueue(new Callback<List<CartDto>>() {
            @Override public void onResponse(Call<List<CartDto>> call, Response<List<CartDto>> response) {
                progress.setVisibility(View.GONE);
                if (!response.isSuccessful() || response.body()==null) { showError("No se pudieron cargar los carritos. Código: "+response.code()); return; }
                List<String> rows=new ArrayList<>();
                for (CartDto cart:response.body()) {
                    StringBuilder text=new StringBuilder("Carrito #").append(cart.getId()).append("\nFecha: ").append(cart.getDate()).append("\nUsuario ID: ").append(cart.getUserId());
                    if (cart.getProducts()!=null) for (CartDto.CartProduct product:cart.getProducts()) text.append("\n• Producto ").append(product.getProductId()).append(" — Cantidad: ").append(product.getQuantity());
                    rows.add(text.toString());
                }
                adapter.submit(rows);
            }
            @Override public void onFailure(Call<List<CartDto>> call, Throwable t) { progress.setVisibility(View.GONE); showError("Error de conexión. Revisa tu Internet."); }
        });
    }
    private void showError(String message){ error.setText(message); error.setVisibility(View.VISIBLE); retry.setVisibility(View.VISIBLE); }
}
