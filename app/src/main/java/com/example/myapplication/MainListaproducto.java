    package com.example.myapplication;

import androidx.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    Button btnBuscar;
    RecycleviewProductoAdapter adaptadorProducto;
    List<ModelItemsProducto> listaProducto;
    public static String nombrecliente;
    public static String codigocliente;
    public static String zonacliente;
    public static String idcliente;
    public static int idvendedor;
    public static int stock;
    public static int IdInventario;



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


        Bundle extra=getIntent().getExtras();
        if (extra != null){
            nombrecliente= extra.getString("Nombrecliente");
            System.out.println("----> NombreCliente: "+nombrecliente);

            codigocliente= extra.getString("Codigocliente");
            System.out.println("----> CodigoCliente: "+codigocliente);

            zonacliente= extra.getString("Zonacliente");
            System.out.println("----> Nombre Zona Cliente: "+zonacliente);

            idcliente= extra.getString("Idcliente");
            System.out.println("----> ID Cliente: "+idcliente);

            idvendedor= extra.getInt("Idvendedor");
            System.out.println("----> ID Vendedor lista Producto: "+idvendedor);

        }

        listaProducto=new ArrayList<>();
        init();
        initVlaues(search2.getText().toString().toUpperCase());


        search2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                btnBuscar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerlistproducto.setAdapter(adaptadorProducto);
                        filter2(s.toString());
                    }
                });

            }
        });

    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu2,menu);
            return true;

        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.Mbtn_prefactura:

                    Intent intent2 = new Intent(getApplicationContext(),MainFacturaList.class);
                    intent2.putExtra("NombreCliente",nombrecliente);
                    intent2.putExtra("CodigoCliente",codigocliente);
                    intent2.putExtra("ZonaCliente",zonacliente);
                    intent2.putExtra("IdCliente",idcliente);
                    intent2.putExtra("IdVendedor",idvendedor);
                    startActivity(intent2);
                    break;

            }

            return super.onOptionsItemSelected(item);
        }
        private void filter2(String text) {
        text = search2.getText().toString().toUpperCase();
      initVlaues(text);
        ArrayList<ModelItemsProducto> filterlistP = new ArrayList<>();
        for (ModelItemsProducto item:listaProducto){
            if (item.getNombreP().toUpperCase().contains(text)){
                filterlistP.add(item);
            }
        }
            adaptadorProducto.filterListProducto(filterlistP);
        }




        public List<ModelItemsProducto> llenarProductosBD(String Buscar){
        List<ModelItemsProducto> listProducto = new ArrayList<>();
        try {

            DBConnection dbConnection = new DBConnection();
            dbConnection.conectar();

            Statement st = dbConnection.getConnection().createStatement();
            ResultSet rs = st.executeQuery("\n" +
                        "select top 25 concat(i.Nombre, ' C$ ', i.Precio1,' ',um.Nombre) as Nombre,i.Nombre as Producto,um.Nombre as UM,i.idInventario, i.ImagenApk, i.Precio1,ad.info1,ad.info2,ad.info3,ad.info4,ad.info5,i.Stock from Inventario i inner join Unidad_Medida um on i.idUndMedida=um.idUnidadMedida inner join InventarioInfoAdic ad on i.idInventario= ad.idInventario where i.Estado = 'Activo' and i.Nombre like '%"+Buscar+"%' and Stock >0");

            while (rs.next()){

                listProducto.add(new ModelItemsProducto(rs.getString("Nombre"),
                        rs.getString("UM"),
                        rs.getDouble("Precio1"),
                        rs.getString("Producto"),
                        rs.getString("info1"),
                        rs.getString("info2"),
                        rs.getString("info3"),
                        rs.getString("info4"),
                        rs.getString("info5"),
                        rs.getString("ImagenApk"),
                       stock= rs.getInt("Stock"),
                       IdInventario= rs.getInt("idInventario")));


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
        btnBuscar=findViewById(R.id.btn_Buscar);


    }
    public void initVlaues(String Buscar){
       LinearLayoutManager manager= new LinearLayoutManager(this);
       recyclerlistproducto.setLayoutManager(manager);
       listaProducto=llenarProductosBD(Buscar);
       adaptadorProducto= new RecycleviewProductoAdapter((ArrayList<ModelItemsProducto>) listaProducto);

    }
}