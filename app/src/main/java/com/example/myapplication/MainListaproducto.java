package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainListaproducto extends AppCompatActivity implements View.OnClickListener {

    EditText editBuscar;
    ImageButton btn_atras;
    ListView listproducto;

    ArrayAdapter<String> adadter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_listaproducto);
        getSupportActionBar().setTitle("Lista de Productos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editBuscar= findViewById(R.id.edit_Buscar);
        btn_atras = findViewById(R.id.btn_Atras);
        listproducto= findViewById(R.id.list_producto);

        btn_atras.setOnClickListener(this);



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
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(),MainProductosCliente.class);
        startActivity(intent);
    }
}