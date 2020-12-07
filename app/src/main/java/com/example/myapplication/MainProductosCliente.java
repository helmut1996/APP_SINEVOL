package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.ConexionBD.DBConnection;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainProductosCliente extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
ImageButton IbuttonInicio,IbuttonAgregar,IbuttonSiguiente;
TextView tvnombreproducto,textcontar,textinfo1,textinfo2,textinfo3,textinfo4,textinfo5,tvunidadmedida,tvcontadorproducto,tvimagenBD;
Spinner precios,monedas;
ImageView img;

/////////
String producto;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_productos_cliente);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Productos");
/////////////////////////////////Implementando shared preferences////////////////////////////
        SharedPreferences  preferences = getSharedPreferences("agregandoproducto",Context.MODE_PRIVATE);
        tvcontadorproducto.setText(preferences.getString("nombre",""));

//////////////////////////////Implementando shared preferences////////////////////////////


        ///////// Botones
        IbuttonInicio = findViewById(R.id.btn_Inicio);
        IbuttonAgregar = findViewById(R.id.btn_Agregar);
        IbuttonSiguiente = findViewById(R.id.btn_siguente);

        ////////////imagen producto
        img=findViewById(R.id.imageProducto);
        /////////// campos de texto
        tvnombreproducto=findViewById(R.id.tvnombreP);
        textcontar=findViewById(R.id.text_contar);
        textinfo1=findViewById(R.id.text_info1);
        textinfo2=findViewById(R.id.text_info2);
        textinfo3=findViewById(R.id.text_info3);
        textinfo4=findViewById(R.id.text_info4);
        textinfo5=findViewById(R.id.text_info5);
        tvimagenBD=findViewById(R.id.imagenBD);
        tvunidadmedida=findViewById(R.id.text_unidadM);
        tvcontadorproducto=findViewById(R.id.contadorproducto);
        ////////// Spinmer
        precios = findViewById(R.id.spinerPrecios);
        monedas = findViewById(R.id.spinner_tipo_moneda);

        IbuttonInicio.setOnClickListener(this);
        IbuttonAgregar.setOnClickListener(this);
        IbuttonSiguiente.setOnClickListener(this);
///////////////////////////////mandando a llamar las imagenes libreria ////////////////////////////////////////////////////
   //     Glide.with(this).load("Este equipo\\T1\\Almacenamiento interno\\DCIM\\Camera\\IMG_20201207_111450").into(img);
//////////////////////////////pasando datos por parametros entre anctivitys//////////////////////////////////////
        String NombrePreducto;
        Bundle extra=getIntent().getExtras();

        if (extra !=null){
            NombrePreducto= extra.getString("NombreP");
            producto = extra.getString("NombreP");
            tvnombreproducto.setText(NombrePreducto);
            tvunidadmedida.setText(extra.getString("UnidadMed"));
            textinfo1.setText(extra.getString("info1"));
            textinfo2.setText(extra.getString("info2"));
            textinfo3.setText(extra.getString("info3"));
            textinfo4.setText(extra.getString("info4"));
            textinfo5.setText(extra.getString("info5"));
            textcontar.setText(extra.getString("stock"));
            tvimagenBD.setText(extra.getString("imagenproducto"));

        }


//////////////////////////////pasando datos por parametros entre activitys//////////////////////////////////////////


        /////////Spinner del tipo de moneda
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.tipo_moneda, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monedas.setAdapter(adapter);

        ////////////spinner de los precios
        DBConnection sesion;
        sesion = DBConnection.getDbConnection();

        String query = "select Precio1, Precio2,Precio3,Precio4,Precio5 from Inventario where Nombre like '%"+producto+"%'";
        try {
            Statement stm = sesion.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                data.add(rs.getString("Precio1"));
                data.add(rs.getString("Precio2"));
                data.add(rs.getString("Precio3"));
                data.add(rs.getString("Precio4"));
                data.add(rs.getString("Precio5"));
            }
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
            precios.setAdapter(NoCoreAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }


       // cargarImageFile();
    }


    public void requestStoragePermission(){

    }
/*    public void cargarImageFile(){
        File file= new File("/Storage/Download/foto.jpg");
        Glide
                .with(this)
                .load(file)
                .into(img);
    }*/

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
            case R.id.btn_Agregar:
                // implementar agregar
                SharedPreferences preferences = getSharedPreferences("agregandoproducto",Context.MODE_PRIVATE);
                SharedPreferences.Editor Objeditor = preferences.edit();
                Objeditor.putString("nombre",tvnombreproducto.getText().toString());

                    Intent intent2 = new Intent(getApplicationContext(),MainListaproducto.class);
                    startActivity(intent2);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}