package com.example.myapplication.modelos;

public class ClasslistItemC {

    String nombre,zona,direccion;
    double credito;
    public ClasslistItemC() {
    }

    public ClasslistItemC(String nombre, String zona, String direccion, double credito) {
        this.nombre = nombre;
        this.zona = zona;
        this.direccion = direccion;
        this.credito = credito;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getCredito() {
        return credito;
    }

    public void setCredito(double credito) {
        this.credito = credito;
    }
}
