package view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import model.CartItem;
import controller.CartManager;

public class CartActivity extends AppCompatActivity {

    private TextView tvTotal, tvEmptyMessage;
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Asigna tu layout XML correspondiente
        // setContentView(R.layout.activity_cart);

        refreshCartUI();
    }

    public void refreshCartUI() {
        List<CartItem> items = CartManager.getInstance().getCartItems();

        // US10 - Criterio 3: Estado de carrito vacío
        if (items.isEmpty()) {
            if (btnCheckout != null) btnCheckout.setEnabled(false);
            if (tvEmptyMessage != null) {
                tvEmptyMessage.setVisibility(View.VISIBLE);
                tvEmptyMessage.setText("Tu carrito está vacío");
            }
            if (tvTotal != null) tvTotal.setText("Total: $0.00");
        } else {
            if (btnCheckout != null) btnCheckout.setEnabled(true);
            if (tvEmptyMessage != null) tvEmptyMessage.setVisibility(View.GONE);

            // US10 - Criterio 1: Recálculo de total redondeado
            if (tvTotal != null) {
                tvTotal.setText("Total: $" + CartManager.getInstance().getTotalPrice());
            }
        }
    }
}