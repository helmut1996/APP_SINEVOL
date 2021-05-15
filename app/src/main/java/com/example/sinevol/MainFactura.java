package com.example.sinevol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sinevol.Adapter.RecycleviewAdapter;
import com.example.sinevol.ConexionBD.DBConnection;
import com.example.sinevol.modelos.ClasslistItemC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainFactura extends AppCompatActivity {


    // VARIABLE PARA RECYCLEVIEW
    RecyclerView recyclerViewCliente;
    RecycleviewAdapter adapterCliente;
    EditText search;
    Button btn_buscador_cliente;
    List<ClasslistItemC> itemCList;
    public static int id;
    String CapturandoClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_factura);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getSupportActionBar().setTitle(" Lista Clientes");


        id = getIntent().getIntExtra("Id", 0);
        System.out.println("ID vendedor activity lista cliente===========>" + id);

        initview();


        btn_buscador_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (search.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(),"Ingrese un cliente",Toast.LENGTH_SHORT).show();
                    } else {
                         CapturandoClientes= search.getText().toString();

                        filter(CapturandoClientes);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    }

            }
        });

    }

    private void filter(String text) {
        initValues();
        ArrayList<ClasslistItemC> filteredlist = new ArrayList<>();
        for (ClasslistItemC item : itemCList) {
            if (item.getNombre().toUpperCase().contains(search.getText().toString().toUpperCase())) {
                filteredlist.add(item);
            }
        }
        adapterCliente.filterList(filteredlist);

    }


    public void initview() {
        recyclerViewCliente = findViewById(R.id.listaClientes);
        search = findViewById(R.id.search);
        btn_buscador_cliente=findViewById(R.id.btnBuscadorCliente);
    }

    public void initValues() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerViewCliente.setLayoutManager(manager);
        recyclerViewCliente.setHasFixedSize(true);
        itemCList = obtenerclientesBD();
        adapterCliente = new RecycleviewAdapter(itemCList);
        recyclerViewCliente.setAdapter(adapterCliente);
    }


    private List<ClasslistItemC> obtenerclientesBD() {
        List<ClasslistItemC> listCliiente = new ArrayList<>();
        try {
            DBConnection dbConnection = new DBConnection();
            dbConnection.conectar();


            if (id == 2) {
                try {

                    Statement st = dbConnection.getConnection().createStatement();

                    ResultSet rs = st.executeQuery("select CONCAT (Codigo, '-',Nombre) as Nombre,Direccion,Codigo,idCliente from Clientes where Estado = 'Activo' order by Nombre asc");
                    while (rs.next()) {
                        listCliiente.add(new ClasslistItemC(rs.getString("Nombre"),
                                rs.getString("Direccion"),
                                rs.getInt("Codigo"),
                                rs.getInt("idCliente")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            } else {
                Statement st = dbConnection.getConnection().createStatement();

                ResultSet rs = st.executeQuery("select CONCAT (Codigo, '-',Nombre) as Nombre,Direccion,Codigo,idCliente from Clientes where idVendedor='" + id + "' AND Estado = 'Activo' order by Nombre asc");
                while (rs.next()) {
                    listCliiente.add(new ClasslistItemC(rs.getString("Nombre"),
                            rs.getString("Direccion"),
                            rs.getInt("Codigo"),
                            rs.getInt("idCliente")));

                }


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listCliiente;
    }

}
