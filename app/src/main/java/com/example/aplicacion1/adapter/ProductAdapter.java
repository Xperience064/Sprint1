package com.example.aplicacion1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aplicacion1.R;
import com.example.aplicacion1.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter
        extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product> productList = new ArrayList<>();
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter() {
    }

    public ProductAdapter(OnProductClickListener listener) {
        this.listener = listener;
    }

    public void setProducts(List<Product> products) {
        productList.clear();

        if (products != null) {
            productList.addAll(products);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ProductViewHolder holder,
            int position
    ) {

        Product product = productList.get(position);

        holder.tvTitle.setText(product.getTitle());

        holder.tvPrice.setText(
                String.format(
                        Locale.getDefault(),
                        "$%.2f",
                        product.getPrice()
                )
        );

        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .into(holder.imgProduct);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder
            extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvPrice;
        private final ImageView imgProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}
