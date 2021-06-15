package com.example.sinevol.modelos;

public class ClasslistItemC {

    String nombre,zona;
     int codigo;
     int idCliente;
     int clienteEspecial;
    public ClasslistItemC() {
    }



    public ClasslistItemC(String nombre, String zona, int codigo, int idCliente, int clienteEspecial) {
        this.nombre = nombre;
        this.zona = zona;
        this.codigo = codigo;
        this.idCliente=idCliente;
        this.clienteEspecial=clienteEspecial;
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

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getClienteEspecial() {
        return clienteEspecial;
    }

    public void setClienteEspecial(int clienteEspecial) {
        this.clienteEspecial = clienteEspecial;
    }
}
