package com.example.myapplication.SQLite.entidades;

public class ProductosAdd {
    private int id_producto;
    private String nombreproduc;
    private int cantidad;
    private int precios;
    private String imagenProducto;

    public ProductosAdd() {
    }



    public ProductosAdd(int id_producto, String nombreproduc, int cantidad, int precios, String imagenProducto) {
        this.id_producto = id_producto;
        this.nombreproduc = nombreproduc;
        this.cantidad = cantidad;
        this.precios = precios;
        this.imagenProducto= imagenProducto;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombreproduc() {
        return nombreproduc;
    }

    public void setNombreproduc(String nombreproduc) {
        this.nombreproduc = nombreproduc;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrecios() {
        return precios;
    }

    public void setPrecios(int precios) {
        this.precios = precios;
    }

    public String getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(String imagenProducto) {
        this.imagenProducto = imagenProducto;
    }
}