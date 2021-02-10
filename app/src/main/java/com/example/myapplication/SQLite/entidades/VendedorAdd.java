package com.example.myapplication.SQLite.entidades;

public class VendedorAdd {

private int idVendedor;
private String NombreVendedor;

    public VendedorAdd() {
    }

    public VendedorAdd(int idVendedor, String nombreVendedor) {
        this.idVendedor = idVendedor;
        NombreVendedor = nombreVendedor;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getNombreVendedor() {
        return NombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        NombreVendedor = nombreVendedor;
    }
}
