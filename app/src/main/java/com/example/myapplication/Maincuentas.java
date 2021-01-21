package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
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
    TextView telefono,direccion,cliente,IDCLIENTE;
    AutoCompleteTextView search;
    RecyclerviewAdapterCuentas adaptadorCuentas;
    String []clientes= new String[]{
            "cleinte1","Helmut","brian","jefrry"
    };
    int id,idCliente;
    String Direccion,tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincuentas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Estado Cuentas");

        listaCuentas= findViewById(R.id.RecyclerCuentas);
        cliente=findViewById(R.id.tvC_cliente);
        telefono=findViewById(R.id.edittelefono);
        direccion=findViewById(R.id.editdireccion);
        search=findViewById(R.id.buscadorClienteCuentas);
        IDCLIENTE=findViewById(R.id.tvC_Idcliente);
        id=getIntent().getIntExtra("Id",0);

        System.out.println("ID vendedor activity cuentas===========>"+ id);


        setRecycler();

     //   ArrayAdapter<String> adaptercliente=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,clientes);
        search.setAdapter(Clientes());

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cliente.setText(search.getText().toString());
                try {
                    Busqueda();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void setRecycler() {
        listaCuentas.setHasFixedSize(true);
        listaCuentas.setLayoutManager(new LinearLayoutManager(this));
        adaptadorCuentas= new RecyclerviewAdapterCuentas(this,getlist());
        listaCuentas.setAdapter(adaptadorCuentas);
    }

    public List<ModelItemCuentas> getlist(){
        List<ModelItemCuentas> listCEstado= new ArrayList<>();

        try {
            DBConnection dbConnection = new DBConnection();
            dbConnection.conectar();

            Statement st=dbConnection.getConnection().createStatement();

            ResultSet rs=st.executeQuery("exec sp_EstadoCuenta "+ idCliente);
            while(rs.next()){
                listCEstado.add(new ModelItemCuentas("helmut",
                        rs.getString("TipoCompra"),
                        22403355,
                        "direccion",
                        rs.getString("Fecha"),
                        rs.getString("Descripcion"),
                         rs.getString("Entrada"),
                        rs.getString("Salida"),
                        rs.getString("SaldoRestante")));
            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return listCEstado;

    }

    public ArrayAdapter Clientes() {
        ArrayAdapter NoCoreAdapter=null;
        DBConnection sesion;
        sesion = DBConnection.getDbConnection();

        String query = "select  Nombre,idCliente,Telefono1,Direccion from Clientes where idVendedor='" + id + "' AND Estado = 'Activo' order by Nombre asc";
        try {
            Statement stm = sesion.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                data.add(rs.getString("Nombre"));
               String Prueba= rs.getString("IdCliente");
            }
            NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NoCoreAdapter;
    }


    public void Busqueda() throws SQLException {
        DBConnection dbConnection = new DBConnection();
        dbConnection.conectar();


        Statement st=dbConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery("\n" +
                "select idCliente,Direccion,Telefono1 from Clientes where Nombre= '" +cliente.getText().toString()+"' ");
        while (rs.next()){

            idCliente=rs.getInt("idCliente");
            System.out.println("==============> ID CLIENTE :"+idCliente);
            telefono.setText(rs.getString("Telefono1"));
            direccion.setText(rs.getString("Direccion" ));

            tel=rs.getString("Telefono1");
            System.out.println("==============> TELEFONO CLIENTE :"+tel);
            Direccion=rs.getString("Direccion" );
            System.out.println("==============> DIRECCION CLIENTE :"+Direccion);
        }
    }
}