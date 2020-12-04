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
import android.widget.LinearLayout;
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
    EditText search2;

    RecycleviewProductoAdapter adaptadorProducto;
    List<ModelItemsProducto> listaProducto;

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
        init();
        initVlaues();
        search2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter2(s.toString());
            }
        });

    }

        private void filter2(String text) {
        ArrayList<ModelItemsProducto> filterlistP = new ArrayList<>();
        for (ModelItemsProducto item:listaProducto){
            if (item.getNombreP().toUpperCase().contains(search2.getText().toString().toUpperCase())){
                filterlistP.add(item);
            }
        }
            adaptadorProducto.filterListProducto(filterlistP);
        }


        private List<ModelItemsProducto> llenarProductosBD(){
        List<ModelItemsProducto> listProducto = new ArrayList<>();
        try {

            DBConnection dbConnection = new DBConnection();
            dbConnection.conectar();

            Statement st = dbConnection.getConnection().createStatement();
            ResultSet rs = st.executeQuery("\n" +
                    "select concat(i.Nombre, ' C$ ', i.Precio1,' ',um.Nombre) as Nombre,i.Nombre as Producto,um.Nombre as UM, i.Precio1 from Inventario i inner join Unidad_Medida um on i.idUndMedida=um.idUnidadMedida and Estado='Activo'");

            while (rs.next()){

                listProducto.add(new ModelItemsProducto(rs.getString("Nombre"),rs.getString("UM"),rs.getDouble("Precio1"), rs.getString("Producto") ));
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return listProducto;
    }

    private void init(){

        recyclerlistproducto= findViewById(R.id.list_producto);
        search2 = findViewById(R.id.search2);
    }
    private void initVlaues(){
       LinearLayoutManager manager= new LinearLayoutManager(this);
       recyclerlistproducto.setLayoutManager(manager);
       listaProducto=llenarProductosBD();
       adaptadorProducto= new RecycleviewProductoAdapter((ArrayList<ModelItemsProducto>) listaProducto);
       recyclerlistproducto.setAdapter(adaptadorProducto);
    }

}