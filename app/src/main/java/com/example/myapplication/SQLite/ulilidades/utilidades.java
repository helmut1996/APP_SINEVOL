package com.example.myapplication.SQLite.ulilidades;

public class utilidades {
    //constantes tabla producto
    public static final String TABLA_PRODUCTO="producto";
    public static final String CAMPO_ID="id";
    public static final String CAMPO_NOMBRE="nombreproducto";
    public static final String CAMPO_CANTIDAD="cantidad";
    public static final String CAMPO_PRECIO="precio";
    public static final String CAMPO_IMAGEN="imagen";

    public static final  String CREAR_TABLA_PRODUCTO= "CREATE TABLE "+TABLA_PRODUCTO+"("+CAMPO_ID+" INTEGER , "+CAMPO_NOMBRE+" TEXT, "+CAMPO_CANTIDAD+" INTEGER, "+CAMPO_PRECIO+" INTEGER, "+CAMPO_IMAGEN+" TEXT)";
}
