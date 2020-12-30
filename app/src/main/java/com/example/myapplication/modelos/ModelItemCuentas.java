package com.example.myapplication.modelos;

public class ModelItemCuentas {
    private String nombreCliente;
    private int telefonoCliente;
    private String direccionCliente;
    private String fechaCliente;
    private String descripcionCliente;
    private int entradaCliente;
    private int salidaCliente;
    private int totalCliente;

    public String getCredito(String credito) {
        return this.credito;
    }

    private String credito;



    public ModelItemCuentas(String helmut, String credito, int i, String direccion, String s, String descipcion_prueba, int i1, double v) {
    }



    public ModelItemCuentas(String nombreCliente, String credito, int telefonoCliente, String direccionCliente, String fechaCliente, String descripcionCliente, int entradaCliente, int salidaCliente, int totalCliente) {
        this.nombreCliente = nombreCliente;
        this.credito=credito;
        this.telefonoCliente = telefonoCliente;
        this.direccionCliente = direccionCliente;
        this.fechaCliente = fechaCliente;
        this.descripcionCliente = descripcionCliente;
        this.entradaCliente = entradaCliente;
        this.salidaCliente = salidaCliente;
        this.totalCliente = totalCliente;


    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(int i) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String waspan_sur) {
        this.direccionCliente = direccionCliente;
    }

    public String getFechaCliente() {
        return fechaCliente;
    }

    public void setFechaCliente(String s) {
        this.fechaCliente = fechaCliente;
    }

    public String getDescripcionCliente() {
        return descripcionCliente;
    }

    public void setDescripcionCliente(String descripcionCliente) {
        this.descripcionCliente = descripcionCliente;
    }

    public int getEntradaCliente() {
        return entradaCliente;
    }

    public void setEntradaCliente(int entradaCliente) {
        this.entradaCliente = entradaCliente;
    }

    public int getSalidaCliente() {
        return salidaCliente;
    }

    public void setSalidaCliente(int salidaCliente) {
        this.salidaCliente = salidaCliente;
    }

    public int getTotalCliente() {
        return totalCliente;
    }

    public void setTotalCliente(int totalCliente) {
        this.totalCliente = totalCliente;
    }
    public String getCredito() {
        return credito;
    }

    public void setCredito(String credito) {
        this.credito = credito;
    }


}

