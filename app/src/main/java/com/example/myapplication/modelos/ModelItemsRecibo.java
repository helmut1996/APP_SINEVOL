package com.example.myapplication.modelos;

public class ModelItemsRecibo {
   private String NombreCliente;
   private String NombreVendedor;
   private  String Fecharegistro;
   private String Usuario;
   private int idVendedor;
   private double Abono;
   private double Descuento;

    public ModelItemsRecibo() {
    }

    public ModelItemsRecibo(String nombreCliente, String nombreVendedor, String fecharegistro, String usuario, int idVendedor, double abono, double descuento) {
        NombreCliente = nombreCliente;
        NombreVendedor = nombreVendedor;
        Fecharegistro = fecharegistro;
        Usuario = usuario;
        this.idVendedor = idVendedor;
        Abono = abono;
        Descuento = descuento;
    }

    public String getNombreCliente() {
        return NombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        NombreCliente = nombreCliente;
    }

    public String getNombreVendedor() {
        return NombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        NombreVendedor = nombreVendedor;
    }

    public String getFecharegistro() {
        return Fecharegistro;
    }

    public void setFecharegistro(String fecharegistro) {
        Fecharegistro = fecharegistro;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public double getAbono() {
        return Abono;
    }

    public void setAbono(double abono) {
        Abono = abono;
    }

    public double getDescuento() {
        return Descuento;
    }

    public void setDescuento(double descuento) {
        Descuento = descuento;
    }
}
