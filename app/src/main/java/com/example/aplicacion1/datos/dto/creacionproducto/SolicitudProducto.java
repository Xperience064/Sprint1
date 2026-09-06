package com.example.aplicacion1.datos.dto.creacionproducto;

import com.google.gson.annotations.SerializedName;

public class SolicitudProducto {

    @SerializedName("title")
    private String titulo;

    @SerializedName("price")
    private double precio;

    @SerializedName("description")
    private String descripcion;

    @SerializedName("image")
    private String imagen;

    @SerializedName("category")
    private String categoria;

    public SolicitudProducto(
            String titulo,
            double precio,
            String descripcion,
            String imagen,
            String categoria
    ) {
        this.titulo = titulo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public double getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public String getCategoria() {
        return categoria;
    }
}