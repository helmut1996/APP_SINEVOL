package com.example.myapplication.modelos;
public class ModelItemsProducto {
    private String nombreP;
    private String unidadmedidaP;
    private double precioP;

    public ModelItemsProducto() {
    }

    public ModelItemsProducto(String nombreP, String unidadmedidaP, double precioP) {
        this.nombreP = nombreP;
        this.unidadmedidaP = unidadmedidaP;
        this.precioP = precioP;
    }

    public String getNombreP() {
        return nombreP;
    }

    public void setNombreP(String nombreP) {
        this.nombreP = nombreP;
    }

    public String getUnidadmedidaP() {
        return unidadmedidaP;
    }

    public void setUnidadmedidaP(String unidadmedidaP) {
        this.unidadmedidaP = unidadmedidaP;
    }

    public double getPrecioP() {
        return precioP;
    }

    public void setPrecioP(double precioP) {
        this.precioP = precioP;
    }
}
