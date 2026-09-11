package com.example.aplicacion1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.Holder> {
    private final List<String> rows = new ArrayList<>();
    public void submit(List<String> values) {
        rows.clear();
        if (values != null) rows.addAll(values);
        notifyDataSetChanged();
    }
    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        view.setPadding(24, 20, 24, 20);
        return new Holder(view);
    }
    @Override public void onBindViewHolder(@NonNull Holder holder, int position) { holder.text.setText(rows.get(position)); }
    @Override public int getItemCount() { return rows.size(); }
    static class Holder extends RecyclerView.ViewHolder {
        final TextView text;
        Holder(View view) { super(view); text = view.findViewById(android.R.id.text1); }
    }
}
