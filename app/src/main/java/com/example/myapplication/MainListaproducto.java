package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.myapplication.Adapter.RecycleviewAdapter;
import com.example.myapplication.Adapter.RecycleviewProductoAdapter;
import com.example.myapplication.modelos.ModelItemsProducto;

import java.util.ArrayList;

public class MainListaproducto extends AppCompatActivity {

    EditText editBuscar;
    RecyclerView recyclerlistproducto;

ArrayList<ModelItemsProducto> listaProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_listaproducto);
        getSupportActionBar().setTitle("Lista de Productos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listaProducto=new ArrayList<>();
        editBuscar= findViewById(R.id.edit_Buscar);
        recyclerlistproducto= findViewById(R.id.list_producto);
        recyclerlistproducto.setLayoutManager(new LinearLayoutManager(this));

        llenarProductos();

        RecycleviewProductoAdapter adapter=new RecycleviewProductoAdapter(listaProducto);
        recyclerlistproducto.setAdapter(adapter);
/*
        adadter =new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Productos));
        listproducto.setAdapter(adadter);
        editBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adadter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        listproducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                      Intent intent = new Intent(getApplicationContext(),MainProductosCliente.class);
                      startActivity(intent);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                }
            }
        });




 */


    }

    private void llenarProductos() {
            listaProducto.add(new ModelItemsProducto("Lapiz Azul","1/10",152.50));
            listaProducto.add(new ModelItemsProducto("Lapiz Azul","1/10",152.50));
            listaProducto.add(new ModelItemsProducto("Lapiz Azul","1/10",152.50));
            listaProducto.add(new ModelItemsProducto("Lapiz Azul","1/10",152.50));
            listaProducto.add(new ModelItemsProducto("Lapiz Azul","1/10",152.50));
            listaProducto.add(new ModelItemsProducto("Lapiz Azul","1/10",152.50));
    }


}