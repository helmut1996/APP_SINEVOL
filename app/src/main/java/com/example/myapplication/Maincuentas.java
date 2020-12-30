package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TableLayout;


import com.example.myapplication.modelos.ModelItemCuentas;

import java.util.ArrayList;
import java.util.List;


public class Maincuentas extends AppCompatActivity {

    RecyclerView listaCuentas;
    TableLayout tabla;
    EditText cliente,telefono,direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincuentas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Estado Cuentas");

        listaCuentas= findViewById(R.id.RecyclerCuentas);
        cliente=findViewById(R.id.editCliente);
        telefono=findViewById(R.id.edittelefono);
        direccion=findViewById(R.id.editdireccion);


    }


    private void desabilitarText() {
        cliente.setEnabled(false);
        telefono.setEnabled(false);
        direccion.setEnabled(false);
    }

}