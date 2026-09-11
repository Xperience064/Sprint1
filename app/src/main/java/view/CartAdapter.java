package view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacion1.R;
import com.bumptech.glide.Glide;
import controller.CartManager;
import model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onQuantityChanged();
        void onItemDeleted(CartItem item);
    }

    public CartAdapter(List<CartItem> cartItemList, OnCartChangeListener listener) {
        this.cartItemList = cartItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);

        // Usamos los getters directos de tu modelo CartItem
        holder.tvTitle.setText(item.getTitle());
        holder.tvPrice.setText(String.format("$%.2f", item.getSubtotal()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        Glide.with(holder.itemView.getContext()).load(item.getImage()).into(holder.imgProduct);

        // Manejo de botones de incremento, decremento y eliminar
        holder.btnIncrease.setOnClickListener(v -> {
            CartManager.getInstance().updateQuantity(item.getProductId(), item.getQuantity() + 1, () -> {
                notifyDataSetChanged();
                if (listener != null) listener.onQuantityChanged();
            });
        });

        holder.btnDecrease.setOnClickListener(v -> {
            CartManager.getInstance().updateQuantity(item.getProductId(), item.getQuantity() - 1, () -> {
                notifyDataSetChanged();
                if (listener != null) listener.onQuantityChanged();
            });
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onItemDeleted(item);
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList != null ? cartItemList.size() : 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvTitle, tvPrice, tvQuantity;
        Button btnDecrease, btnIncrease;
        ImageButton btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvTitle = itemView.findViewById(R.id.tvProductTitle);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
