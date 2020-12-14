package com.example.myapplication.modelos;

public class modelGuardarDatos {
   private String nombreproduc;
   private int cantidad;
   private int precios;

    public modelGuardarDatos() {
    }

    public modelGuardarDatos(String nombreproduc, int cantidad, int precios) {
        this.nombreproduc = nombreproduc;
        this.cantidad = cantidad;
        this.precios = precios;
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
}
