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
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.Adapter.RecycleviewAdapter;
import com.example.myapplication.ConexionBD.DBConnection;
import com.example.myapplication.modelos.ClasslistItemC;
import com.google.android.material.button.MaterialButton;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainFactura extends AppCompatActivity {


   // VARIABLE PARA RECYCLEVIEW
    RecyclerView recyclerViewCliente;
    RecycleviewAdapter adapterCliente;
    //EditText search;
    List<ClasslistItemC> itemCList;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_factura);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Clientes");

        id=getIntent().getIntExtra("Id",0);
        initview();
        initValues();



    }

    public void initview(){
        recyclerViewCliente=findViewById(R.id.listaClientes);
     //   search=findViewById(R.id.edit_search);
    }
/////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        MenuItem menuItem=menu.findItem(R.id.action_search);

  //      SearchView searchView= (SearchView)menuItem.getActionView();
    //    searchView.setOnQueryTextListener(this);
        return true;

    }
///////////////////////////////////////////////////////////////////////////////
    public void initValues(){
        LinearLayoutManager manager= new LinearLayoutManager(this);
        recyclerViewCliente.setLayoutManager(manager);

        itemCList=obtenerclientesBD();
        adapterCliente= new RecycleviewAdapter(itemCList);
        recyclerViewCliente.setAdapter(adapterCliente);
    }


private List<ClasslistItemC>obtenerclientesBD(){
        List<ClasslistItemC>listCliiente= new ArrayList<>();
        try {
            DBConnection dbConnection = new DBConnection();
            dbConnection.conectar();

            Statement st=dbConnection.getConnection().createStatement();
            ResultSet rs=st.executeQuery("select Nombre,Codigo,Direccion from Clientes where IdVendedor='" + id + "'");
            while(rs.next()){
                listCliiente.add(new ClasslistItemC(rs.getString("Nombre"),rs.getString("Direccion"),rs.getInt("Codigo")));

            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return listCliiente;
}

/*
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override

    public boolean onQueryTextChange(String newText) {
    /////////////////////////////////////////////////////////
        String clientInput = newText.toLowerCase();
        List<ClasslistItemC> newList = new ArrayList<>();
        for(ClasslistItemC name:itemCList){

            if (name.getNombre().toLowerCase().contains(clientInput)){
                newList.add(name);
            }
            adapterCliente.updatelist(newList);
        }
        return true;
        ///////////////////////////////////////////////////////////////
    }*/
}
