package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ConexionBD.DBConnection;
import com.example.myapplication.modelos.ModelItemsProducto;
import com.example.myapplication.modelos.modelGuardarDatos;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainProductosCliente extends AppCompatActivity implements View.OnClickListener {
    /*variables de los componentes de la vista*/
ImageButton IbuttonInicio,IbuttonAgregar,IbuttonSiguiente;
TextView tvnombreproducto,textcontar,textinfo1,textinfo2,textinfo3,textinfo4,textinfo5,tvunidadmedida,tvcontadorproducto,tvimagenBD;
Spinner precios,monedas;
ImageView img;
EditText editcantidad;
private ArrayList<modelGuardarDatos> datos;


/* variables globales */

 private String producto;
 private String compraProductos [] []=new String[3][3];
 private String llenarArregloProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_productos_cliente);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Productos");


/////////////////////////////////Metodo para permisos de las imagenes/////////////////////////////////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Verifica permisos para Android 6.0+
            checkExternalStoragePermission(); }

        ///////// Botones
        IbuttonInicio = findViewById(R.id.btn_Inicio);
        IbuttonAgregar = findViewById(R.id.btn_Agregar);
        IbuttonSiguiente = findViewById(R.id.btn_siguente);

        editcantidad=findViewById(R.id.editTextCantidad);
        ////////////imagen producto
        img=findViewById(R.id.imgProducto);
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
//////////////////////////////pasando datos por parametros entre activitys/////////////////////////////////

        /////////Spinner del tipo de moneda
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.tipo_moneda, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monedas.setAdapter(adapter);

        ////////////spinner de los precios

        monedas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(monedas.getSelectedItem().toString().equals("Cordobas"))
                {
                    precios.setAdapter(precioCordoba());
                }
                else
                {
                    precios.setAdapter(precioDolar());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

///////////////////////////////mandando a llamar las imagenes libreria ////////////////////////////////////////////////////
                                        cargarImagen();
//////////////////////////////pasando datos por parametros entre anctivitys////////////////////////////////////////////////

/////////////////////////////Metodo de guardado usando sharedpreferences////////////////////////////////////////////////////////////
SharedPreferences preferences= getSharedPreferences("datos",Context.MODE_PRIVATE);
editcantidad.setText(preferences.getString("NombreProducto",""));
    }

    private void checkExternalStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para leer!");
        }
    }


    public void cargarImagen(){

        File file= new File("///storage/emulated/0/MARNOR/"+tvimagenBD.getText()+".jpg");
        Picasso.get().load(file)
                .placeholder(R.drawable.bucandoimg)
                .error(R.drawable.error)
                .into(img);
    }

    public ArrayAdapter precioDolar()
    {
        ArrayAdapter NoCoreAdapter=null;
        DBConnection sesion;
        sesion = DBConnection.getDbConnection();

        String query = "select PrecioDolar1, PrecioDolar2,PrecioDolar3,PrecioDolar4,PrecioDolar5 from Inventario where Nombre='" + producto + "'";
        try {
            Statement stm = sesion.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                data.add(rs.getString("PrecioDolar1"));
                data.add(rs.getString("PrecioDolar2"));
                data.add(rs.getString("PrecioDolar3"));
                data.add(rs.getString("PrecioDolar4"));
                data.add(rs.getString("PrecioDolar5"));
            }
            NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NoCoreAdapter;
    }

    public ArrayAdapter precioCordoba()
    {
        ArrayAdapter NoCoreAdapter=null;
        DBConnection sesion;
        sesion = DBConnection.getDbConnection();

        String query = "select Precio1, Precio2,Precio3,Precio4,Precio5 from Inventario where Nombre='" + producto + "'";
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
            NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NoCoreAdapter;
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
            case R.id.btn_Agregar:
                // implementar agregar

                if (editcantidad.getText().toString().isEmpty()){

                    Toast.makeText(this,"debes ingresar una cantidad",Toast.LENGTH_SHORT).show();
                }else {


                   /*
                   metodo 1 utilizando Matrizes Bidimencional para guardar
                   for (int indice1=0;indice1<3;indice1++){
                        for (int indice2=0;indice2<3;indice2++){
                           // llenarArregloProducto=tvnombreproducto.getText().toString()+(compraProductos [indice1] [indice2]+ " " + editcantidad.getText().toString()+(compraProductos [indice1] [indice2]+ " "));
                            //llenarArregloProducto= precios.getSelectedView().toString()+(compraProductos [indice1] [indice2]+ " ");
                            //llenarArregloProducto= tvnombreproducto.getText().toString()+(compraProductos [indice1] [indice2]+ " ");
                            System.out.println(llenarArregloProducto);

                            tvcontadorproducto.setText(llenarArregloProducto);
                        }
                    }*/

                  /*
                  metodo 2 utilizando un arreglo para guardar
                  String [] arrayone = {editcantidad.getText().toString(),tvnombreproducto.getText().toString()};
                  tvcontadorproducto.setText(arrayone.length);
                    System.out.println("Cantidad Guardada: ----- >"+arrayone[0]);
                    System.out.println("Cantidad Guardada: ----- >"+arrayone[1]);
                    String [] arraytwo = arrayone;
                    System.out.println("arreglo Guardada: ----- >"+arraytwo[0]);
                    */

                  /*
                    metodo 3 utilizando arraylist para guardar
                    datos = new ArrayList<modelGuardarDatos>();
                    datos.add(new modelGuardarDatos(tvnombreproducto.getText().toString(),Integer.parseInt(editcantidad.getText().toString())));
                    System.out.println("--*-*-*-***-*-*-*-*-*-"+datos.size());*/
                    //tvcontadorproducto.setText(datos.size());


                /*
                 Metodo 4 utilizando SharedPreferences para guardar datos
                 SharedPreferences preferencias = getSharedPreferences("datos",Context.MODE_PRIVATE);
                    SharedPreferences.Editor obj_editor=preferencias.edit();
                    obj_editor.putString("NombreProducto",tvcontadorproducto.getText().toString());
                    obj_editor.commit();
                    System.out.println("-------> capturando "+ tvcontadorproducto);
                    Toast.makeText(this,"Producto Guardado",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(),MainListaproducto.class);
                    startActivity(intent2);*/
                }

                break;
        }
    }

}