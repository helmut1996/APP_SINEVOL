package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;


import com.example.myapplication.Adapter.RecyclerviewAdapterCuentas;
import com.example.myapplication.ConexionBD.DBConnection;
import com.example.myapplication.modelos.ClasslistItemC;
import com.example.myapplication.modelos.ModelItemCuentas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Maincuentas extends AppCompatActivity {

    RecyclerView listaCuentas;
    TableLayout tabla;
    EditText cliente,telefono,direccion,search;
    RecyclerviewAdapterCuentas adaptadorCuentas;

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
        search=findViewById(R.id.editbuscarcuentas);

        desabilitarText();

        setRecycler();


    }

    private void setRecycler() {
        listaCuentas.setHasFixedSize(true);
        listaCuentas.setLayoutManager(new LinearLayoutManager(this));
        adaptadorCuentas= new RecyclerviewAdapterCuentas(this,getlist());
        listaCuentas.setAdapter(adaptadorCuentas);
    }


    public List<ModelItemCuentas> getlist(){
        List<ModelItemCuentas> listCEstado= new ArrayList<>();
       /* listCEstado.add(new ModelItemCuentas("helmut","credito",22403355,"direccion","15/05/20","descipcion prueba",50000,3000,8000));
        listCEstado.add(new ModelItemCuentas("helmut","credito",22403355,"direccion","15/05/20","descipcion prueba",50000,3000,8000));
        listCEstado.add(new ModelItemCuentas("helmut","credito",22403355,"direccion","15/05/20","descipcion prueba",50000,3000,8000));
        listCEstado.add(new ModelItemCuentas("helmut","credito",22403355,"direccion","15/05/20","descipcion prueba",50000,3000,8000));
        listCEstado.add(new ModelItemCuentas("helmut","credito",22403355,"direccion","15/05/20","descipcion prueba",50000,3000,8000));
        listCEstado.add(new ModelItemCuentas("helmut","credito",22403355,"direccion","15/05/20","descipcion prueba",50000,3000,8000));
        listCEstado.add(new ModelItemCuentas("helmut","credito",22403355,"direccion","15/05/20","descipcion prueba",50000,3000,8000));
        listCEstado.add(new ModelItemCuentas("helmut","credito",22403355,"direccion","15/05/20","descipcion prueba",50000,3000,8000));
        listCEstado.add(new ModelItemCuentas("helmut","credito",22403355,"direccion","15/05/20","descipcion prueba",50000,3000,8000));
        listCEstado.add(new ModelItemCuentas("helmut","credito",22403355,"direccion","15/05/20","descipcion prueba",50000,3000,8000));
        */

        try {
            DBConnection dbConnection = new DBConnection();
            dbConnection.conectar();

            Statement st=dbConnection.getConnection().createStatement();

            ResultSet rs=st.executeQuery("exec sp_EstadoCuenta 106");
            while(rs.next()){
                listCEstado.add(new ModelItemCuentas("helmut",rs.getString("TipoCompra"),22403355,"direccion",rs.getString("Fecha"),rs.getString("Descripcion"), rs.getInt("Entrada1"), rs.getInt("Salida1"), rs.getInt("Total")));
            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return listCEstado;

    }
    private void desabilitarText() {
        cliente.setEnabled(false);
        telefono.setEnabled(false);
        direccion.setEnabled(false);
    }

}