package view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.aplicacion1.R;
import controller.CartManager;
import model.CartItem;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private CartAdapter adapter;
    private TextView tvTotal;
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Vistas vinculadas con los IDs reales de tu activity_cart.xml
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        tvTotal = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        if (recyclerViewCart != null) {
            recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        }
        if (recyclerViewCart != null) {
            recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        }

        btnCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Procesando la orden...", Toast.LENGTH_SHORT).show();
        });

        setupAdapter();
        setupAdapter();
        refreshCartUI();
    }

    private void setupAdapter() {
        List<CartItem> items = CartManager.getInstance().getCartItems();

        adapter = new CartAdapter(items, new CartAdapter.OnCartChangeListener() {
            @Override
            public void onQuantityChanged() {
                refreshCartUI();
            }

            @Override
            public void onItemDeleted(CartItem item) {
                CartManager.getInstance().getCartItems().remove(item);
                adapter.notifyDataSetChanged();
                refreshCartUI();
                Toast.makeText(CartActivity.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        if (recyclerViewCart != null) {
            recyclerViewCart.setAdapter(adapter);
        }
    }

    public void refreshCartUI() {
        List<CartItem> items = CartManager.getInstance().getCartItems();

        // US10 - Criterios de actualización de total y estado del botón
        if (items.isEmpty()) {
            if (btnCheckout != null) btnCheckout.setEnabled(false);
            if (tvTotal != null) tvTotal.setText("$0.00");
        } else {
            if (btnCheckout != null) btnCheckout.setEnabled(true);
            if (tvTotal != null) {
                tvTotal.setText(String.format("$%.2f", CartManager.getInstance().getTotalPrice()));
            }
        }
    }
}