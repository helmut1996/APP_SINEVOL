package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainProductosCliente extends AppCompatActivity implements View.OnClickListener {
ImageButton IbuttomBuscar,IbuttonInicio,IbuttonAgregar,IbuttonSiguiente;
TextView textcontar,textprecio;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_productos_cliente);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Productos");

        IbuttomBuscar = findViewById(R.id.btn_buscar);
        IbuttonInicio = findViewById(R.id.btn_Inicio);
        IbuttonAgregar = findViewById(R.id.btn_Agregar);
        IbuttonSiguiente = findViewById(R.id.btn_siguente);

        IbuttomBuscar.setOnClickListener(this);
        IbuttonInicio.setOnClickListener(this);
        IbuttonAgregar.setOnClickListener(this);
        IbuttonSiguiente.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_Inicio:
                Intent intent = new Intent(getApplicationContext(),MainMenu.class);
                startActivity(intent);
                break;
            case R.id.btn_siguente:
                Intent intent1 = new Intent(getApplicationContext(),MainFacruraList.class);
                startActivity(intent1);
                break;

            case R.id.btn_buscar:
                //implementar buscador
                Intent intent2 = new Intent(getApplicationContext(),MainListaproducto.class);
                startActivity(intent2);
                break;
            case R.id.btn_Agregar:
                // implementar agregar
                break;
        }
    }

   public void OpenDialog(){

   }
}