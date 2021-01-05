package com.example.myapplication.SQLite.entidades;

public class FacturasAdd {
  private String NombreCliente;
  private String NombreVendedor;
  private String Fecha;
  private double Abono;
  private double Descuento;
  private int NumReferencia;
  private  double SaldoRes;

    public FacturasAdd() {
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

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
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

    public int getNumReferencia() {
        return NumReferencia;
    }

    public void setNumReferencia(int numReferencia) {
        NumReferencia = numReferencia;
    }

    public double getSaldoRes() {
        return SaldoRes;
    }

    public void setSaldoRes(double saldoRes) {
        SaldoRes = saldoRes;
    }
}