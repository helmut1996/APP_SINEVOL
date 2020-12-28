package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.Adapter.AdapterEstadoCuentas;
import com.example.myapplication.modelos.ModelItemCuentas;

import java.util.ArrayList;
import java.util.List;

public class Maincuentas extends AppCompatActivity {
RecyclerView listaCuentas;
List<ModelItemCuentas> lista_estado_cuentas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincuentas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Estado Cuentas");

        listaCuentas= findViewById(R.id.RecyclerCuentas);

        initData();
        setRecyclerCuentas();
    }

    private void setRecyclerCuentas() {
        AdapterEstadoCuentas estadoCuentasAdapter = new AdapterEstadoCuentas(lista_estado_cuentas);
        listaCuentas.setAdapter(estadoCuentasAdapter);
        listaCuentas.setHasFixedSize(true);
    }

    private void initData() {
        lista_estado_cuentas = new ArrayList<>();

        lista_estado_cuentas.add(new ModelItemCuentas("nombreCliente",  "credito", 888555, "ireccionCliente",  "fechaCliente",  "descripcionCliente", 0, 350000, 350000, 50));
    }
}