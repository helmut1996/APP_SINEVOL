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
    TableLayout tabla;
    TextView telefono,direccion,cliente;
    AutoCompleteTextView search;
    RecyclerviewAdapterCuentas adaptadorCuentas;
    String []clientes= new String[]{
            "cleinte1","Helmut","brian","jefrry"
    };
    public static int id,idCliente;
   // String tel,direccion;

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

        id=getIntent().getIntExtra("Id",0);

        System.out.println("ID vendedor activity cuentas===========>"+ id);



     //   ArrayAdapter<String> adaptercliente=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,clientes);
        search.setAdapter(Clientes());

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cliente.setText(search.getText().toString());
                try {
                    busqueda();
                  //  setRecycler();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                setRecycler();

            }
        });

    }
/// ejecucion del recycler view
    private void setRecycler() {
        listaCuentas.setHasFixedSize(true);
        listaCuentas.setLayoutManager(new LinearLayoutManager(this));
        adaptadorCuentas= new RecyclerviewAdapterCuentas(this,getlist());
        listaCuentas.setAdapter(adaptadorCuentas);
    }

    ///mostrando los datos del estado Cuentas
    public List<ModelItemCuentas> getlist(){
        List<ModelItemCuentas> listCEstado= new ArrayList<>();

        try {
            DBConnection dbConnection = new DBConnection();
            dbConnection.conectar();

            Statement st=dbConnection.getConnection().createStatement();
            int idPrueba = 106;
            ResultSet rs=st.executeQuery("exec sp_EstadoCuenta "+idCliente);
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

    // metodo de busqueda de los clientes
    public ArrayAdapter Clientes() {
        ArrayAdapter NoCoreAdapter=null;
        DBConnection dbConnection = new DBConnection();
        dbConnection.conectar();

        String query = "select  Nombre,idCliente,Telefono1,Direccion from Clientes where idVendedor='" + id + "' AND Estado = 'Activo' order by Nombre asc";
        try {
            Statement stm = dbConnection.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                data.add(rs.getString("Nombre"));

            }
            NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NoCoreAdapter;
    }

    // metodo para la busqueda espesifica de cada estado de cuentas por cada cliente
    public void busqueda() throws SQLException {
        DBConnection dbConnection = new DBConnection();
        dbConnection.conectar();
        Statement st = dbConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery("select idCliente,Telefono1,Direccion from Clientes where Nombre = '" + cliente.getText().toString() + "' ");
        while (rs.next()) {
            idCliente = rs.getInt("idCliente");
            telefono.setText(rs.getString("Telefono1"));
            direccion.setText(rs.getString("Direccion"));

        }
    }
}