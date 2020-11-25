package com.example.myapplication.modelos;

public class ClasslistItemC {

    String nombre,zona,direccion;
    int credito;
     int codigo;
    public ClasslistItemC() {
    }

    public ClasslistItemC(String nombre, String zona, String direccion, int credito, int codigo) {
        this.nombre = nombre;
        this.zona = zona;
        this.direccion = direccion;
        this.credito = credito;
        this.codigo = codigo;
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

    public int getCredito() {
        return credito;
    }

    public void setCredito(int credito) {
        this.credito = credito;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
