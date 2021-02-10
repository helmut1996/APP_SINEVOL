package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.myapplication.SQLite.conexionSQLiteHelper;
import com.example.myapplication.SQLite.entidades.VendedorAdd;
import com.example.myapplication.SQLite.ulilidades.utilidadesVendedor;
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
    EditText search;
    List<ClasslistItemC> itemCList;
    public static int id;

    int id2;
    String NombreVendedor;
    ArrayList<VendedorAdd>listavendedor;
    ArrayList<String> listainformacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_factura);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" Lista Clientes");


        id = getIntent().getIntExtra("Id", 0);
        System.out.println("ID vendedor activity lista cliente===========>" + id);

        ConsultarVendedorSQLite();
        listainformacion = new ArrayList<String>();
        for (int i=0; i<listavendedor.size();i++){

            id2=listavendedor.get(i).getIdVendedor();
            NombreVendedor=listavendedor.get(i).getNombreVendedor();
            System.out.println("llamando el nombre del vendedor SQLite Lista Cliente====> "+NombreVendedor);
            System.out.println("llamando el ID del vendedor SQLite lista Clientes====> "+id2);

        }


        initview();
        initValues();


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });

    }

    public void onStart(){
        super.onStart();
        obtenerclientesBD();
    }

    private void filter(String text) {
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

                ResultSet rs = st.executeQuery("select CONCAT (Codigo, '-',Nombre) as Nombre,Direccion,Codigo,idCliente from Clientes where idVendedor='" + id2 + "' AND Estado = 'Activo' order by Nombre asc");
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



    public void ConsultarVendedorSQLite(){
        conexionSQLiteHelper conn= new conexionSQLiteHelper(this,"bd_productos",null,1);
        SQLiteDatabase db=conn.getReadableDatabase();
        VendedorAdd vendedorAdd = null;
        listavendedor=new ArrayList<VendedorAdd>();
        //query SQLite
        Cursor cursor=db.rawQuery("select * from "+ utilidadesVendedor.TABLA_VENDEDORES,null);
        while (cursor.moveToNext()){
            vendedorAdd=new VendedorAdd();
            vendedorAdd.setIdVendedor(cursor.getInt(0));
            vendedorAdd.setNombreVendedor(cursor.getString(1));

            listavendedor.add(vendedorAdd);
        }
    }
}
