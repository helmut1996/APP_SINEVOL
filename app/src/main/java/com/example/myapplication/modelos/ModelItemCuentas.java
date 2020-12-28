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
    private int TcompraCliente;
    private String credito;

    private boolean expandle;

    public ModelItemCuentas() {
    }



    public ModelItemCuentas(String nombreCliente, String credito, int telefonoCliente, String direccionCliente, String fechaCliente, String descripcionCliente, int entradaCliente, int salidaCliente, int totalCliente, int tcompraCliente) {
        this.nombreCliente = nombreCliente;
        this.credito=credito;
        this.telefonoCliente = telefonoCliente;
        this.direccionCliente = direccionCliente;
        this.fechaCliente = fechaCliente;
        this.descripcionCliente = descripcionCliente;
        this.entradaCliente = entradaCliente;
        this.salidaCliente = salidaCliente;
        this.totalCliente = totalCliente;
        TcompraCliente = tcompraCliente;
        this.expandle=false;
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

    public void setTelefonoCliente(int telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getFechaCliente() {
        return fechaCliente;
    }

    public void setFechaCliente(String fechaCliente) {
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

    public int getTcompraCliente() {
        return TcompraCliente;
    }

    public void setTcompraCliente(int tcompraCliente) {
        TcompraCliente = tcompraCliente;
    }
    public String getCredito() {
        return credito;
    }

    public void setCredito(String credito) {
        this.credito = credito;
    }
    public boolean isExpandle() {
        return expandle;
    }

    public void setExpandle(boolean expandle) {
        this.expandle = expandle;
    }
    @Override
    public String toString() {
        return "ModelItemCuentas{" +
                "nombreCliente='" + nombreCliente + '\'' +
                ", telefonoCliente=" + telefonoCliente +
                ", direccionCliente='" + direccionCliente + '\'' +
                ", fechaCliente='" + fechaCliente + '\'' +
                ", descripcionCliente='" + descripcionCliente + '\'' +
                ", entradaCliente=" + entradaCliente +
                ", salidaCliente=" + salidaCliente +
                ", totalCliente=" + totalCliente +
                ", TcompraCliente=" + TcompraCliente +
                ", credito='" + credito + '\'' +
                '}';
    }
}

