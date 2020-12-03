package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.ConexionBD.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainProductosCliente extends AppCompatActivity implements View.OnClickListener {
ImageButton IbuttomBuscar,IbuttonInicio,IbuttonAgregar,IbuttonSiguiente;
TextView textcontar,textinfo1,textinfo2,textinfo3,textinfo4,textinfo5;
Spinner precios,monedas;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_productos_cliente);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Productos");

        ///////// Botones
        IbuttomBuscar = findViewById(R.id.btn_buscar);
        IbuttonInicio = findViewById(R.id.btn_Inicio);
        IbuttonAgregar = findViewById(R.id.btn_Agregar);
        IbuttonSiguiente = findViewById(R.id.btn_siguente);

        /////////// campos de texto
        textcontar=findViewById(R.id.text_contar);
        textinfo1=findViewById(R.id.text_info1);
        textinfo2=findViewById(R.id.text_info2);
        textinfo3=findViewById(R.id.text_info3);
        textinfo4=findViewById(R.id.text_info4);
        textinfo5=findViewById(R.id.text_info5);

        ////////// Spinmer
        precios = findViewById(R.id.spinerPrecios);
        monedas = findViewById(R.id.spinner_tipo_moneda);

        IbuttomBuscar.setOnClickListener(this);
        IbuttonInicio.setOnClickListener(this);
        IbuttonAgregar.setOnClickListener(this);
        IbuttonSiguiente.setOnClickListener(this);


        /////////Spinner del tipo de moneda
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.tipo_moneda, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monedas.setAdapter(adapter);

        ////////////spinner de los precios
        DBConnection sesion;
        sesion = DBConnection.getDbConnection();
        String query = "select Precio1 from Inventario";
        try {
            Statement stm = sesion.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString(1);// value of database
                data.add(id);
            }
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
            precios.setAdapter(NoCoreAdapter);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        /*ArrayAdapter<CharSequence> adapter1 =ArrayAdapter.createFromResource(this, R.array.precios, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        precios.setAdapter(adapter1);*/
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
}