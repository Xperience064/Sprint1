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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList = new ArrayList<>();

    // Método compatible con ambos nombres que usaban tus compañeros
    public void setProductList(List<Product> products) {
        this.productList.clear();
        if (products != null) {
            this.productList.addAll(products);
        }
        notifyDataSetChanged();
    }

    public void setProducts(List<Product> products) {
        setProductList(products);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usamos tu layout personalizado de producto con imagen
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvTitle.setText(product.getTitle());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));

        // Carga segura de la imagen con Glide (manejando ambos getters de imagen por si acaso)
        String imageUrl = product.getImageUrl() != null ? product.getImageUrl() : product.getImageUrl();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.imgProduct);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvPrice;
        ImageView imgProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}