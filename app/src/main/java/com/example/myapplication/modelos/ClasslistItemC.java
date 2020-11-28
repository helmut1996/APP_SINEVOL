package com.example.myapplication.modelos;

public class ClasslistItemC {

    String nombre,zona;
     int codigo;
    public ClasslistItemC() {
    }

    public ClasslistItemC(String nombre, String zona, int codigo) {
        this.nombre = nombre;
        this.zona = zona;
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

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
