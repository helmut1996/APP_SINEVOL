    package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.Adapter.RecycleviewAdapter;
import com.example.myapplication.Adapter.RecycleviewProductoAdapter;
import com.example.myapplication.ConexionBD.DBConnection;
import com.example.myapplication.modelos.ModelItemsProducto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

    public class MainListaproducto extends AppCompatActivity {


    RecyclerView recyclerlistproducto;

ArrayList<ModelItemsProducto> listaProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /// permisoos de conexion para uso de sql server ////
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        /// permisoos de conexion para uso de sql server ////

        setContentView(R.layout.activity_main_listaproducto);
        getSupportActionBar().setTitle("Lista de Productos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listaProducto=new ArrayList<>();
        recyclerlistproducto= findViewById(R.id.list_producto);

        initVlaues();

    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu_producto,menu);

        return true;
        }

        private List<ModelItemsProducto> llenarProductosBD(){
        List<ModelItemsProducto> listProducto = new ArrayList<>();
        try {

            DBConnection dbConnection = new DBConnection();
            dbConnection.conectar();

            Statement st = dbConnection.getConnection().createStatement();
            ResultSet rs = st.executeQuery("\n" +
                    "select concat(i.Nombre, ' C$ ', i.Precio1,' ',um.Nombre) as Nombre,um.Nombre as UM, i.Precio1 from Inventario i inner join Unidad_Medida um on i.idUndMedida=um.idUnidadMedida");

            while (rs.next()){

                listProducto.add(new ModelItemsProducto(rs.getString("Nombre"),rs.getString("UM"),rs.getDouble("Precio1") ));
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return listProducto;
    }

    private void initVlaues(){
        recyclerlistproducto.setLayoutManager(new LinearLayoutManager(this));
        RecycleviewProductoAdapter adapter=new RecycleviewProductoAdapter(listaProducto);
      listaProducto= (ArrayList<ModelItemsProducto>) llenarProductosBD();
      adapter= new RecycleviewProductoAdapter(listaProducto);
        recyclerlistproducto.setAdapter(adapter);

    }

}