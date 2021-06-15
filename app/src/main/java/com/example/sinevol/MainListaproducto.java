    package com.example.sinevol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sinevol.Adapter.RecycleviewProductoAdapter;
import com.example.sinevol.ConexionBD.DBConnection;
import com.example.sinevol.modelos.ModelItemsProducto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

    public class MainListaproducto extends AppCompatActivity {

    EditText search2;
    Button btnBuscar;
    ProgressBar loader;
    RecycleviewProductoAdapter adaptadorProducto;
   public static List<ModelItemsProducto> listaProducto;
    public static String nombrecliente;
    public static String codigocliente;
    public static String zonacliente;
    public static String idcliente;
    public static int idvendedor;
    public static String ClienteE;
    public static int stock;
    public static int IdInventario;
    public String CapturaBuscador="";
    public  RecyclerView recyclerlistproducto;
    public static int numItems=0;
    boolean isScrollView=false;

        @Override
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
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

            ClienteE=extra.getString("ClienteEspecial");
            System.out.println("-----> pasando cliente especial: "+ClienteE);
        }

        listaProducto=new ArrayList<>();
        init();

            btnBuscar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(search2.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(),"Debes ingresar un nombre",Toast.LENGTH_SHORT).show();
                    }else{
                     CapturaBuscador=  search2.getText().toString();
                        //clear();
                        numItems=0;
                        filter2(CapturaBuscador);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(search2.getWindowToken(), 0);

                        System.out.println("Mostrando lo capturado=====>"+CapturaBuscador);

                    }

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
                    finish();
                    break;

            }

            return super.onOptionsItemSelected(item);
        }
        private void filter2(String text) {
        text = search2.getText().toString().toUpperCase();
        if(!text.isEmpty()){
            initVlaues(text);

            ArrayList<ModelItemsProducto> filterlistP = new ArrayList<>();
            for (ModelItemsProducto item:listaProducto){
                if (item.getNombreP().toUpperCase().contains(text)){
                    filterlistP.add(item);
                }
            }
                adaptadorProducto.filterListProducto(filterlistP);
            }else{
                    Toast.makeText(getApplicationContext(),"Producto no encontrado",Toast.LENGTH_LONG).show();
                }
        }

        public List<ModelItemsProducto> llenarProductosBD(String Buscar){

        try {

            DBConnection dbConnection = new DBConnection();
            dbConnection.conectar();

            Statement st = dbConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("\n" +
                        "select  concat(i.Nombre, ' C$ ', i.Precio1,' ',um.Nombre) as Nombre,i.Nombre as Producto,um.Nombre as UM,i.idInventario, i.ImagenApk, i.Precio1,ad.info1,ad.info2,ad.info3,ad.info4,ad.info5,i.Stock from Inventario i inner join Unidad_Medida um on i.idUndMedida=um.idUnidadMedida inner join InventarioInfoAdic ad on i.idInventario= ad.idInventario where i.Estado = 'Activo' and i.Nombre like '%"+Buscar+"%' and Stock >0 ");


            System.out.println("capturando texto Busqueda===>"+Buscar);
            while (rs.next()){

                listaProducto.add(new ModelItemsProducto(rs.getString("Nombre"),
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
        return listaProducto;

    }

    private void init(){

        recyclerlistproducto= findViewById(R.id.list_producto);
        search2 = findViewById(R.id.search2);
        btnBuscar=findViewById(R.id.btn_Buscar);
        loader=findViewById(R.id.loading_indicator);

    } 
    public void initVlaues(String Buscar){
       LinearLayoutManager manager= new LinearLayoutManager(this);
       recyclerlistproducto.setLayoutManager(manager);
       listaProducto=llenarProductosBD(Buscar);
       adaptadorProducto= new RecycleviewProductoAdapter((ArrayList<ModelItemsProducto>) listaProducto);
        recyclerlistproducto.setAdapter(adaptadorProducto);

        /*
        recyclerlistproducto.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrollView=true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int CurrentImten=manager.getChildCount();
                int totalItems=manager.getItemCount();
                int ScrollOut=manager.findFirstVisibleItemPosition();

                if(isScrollView && (CurrentImten + ScrollOut == totalItems))
                {
                    isScrollView = false;
                    getData();
                }
            }

            private void getData() {
                loader.setVisibility(View.VISIBLE);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        isLastVisible();
                        adaptadorProducto.notifyDataSetChanged();
                        loader.setVisibility(View.GONE);
                    }
                }, 5000);
            }
        });

         */

    }


   /*
    boolean isLastVisible()   {

        LinearLayoutManager layoutManager =((LinearLayoutManager) recyclerlistproducto.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        numItems =  adaptadorProducto.getItemCount();
        filter2(CapturaBuscador);
       // Toast.makeText(getApplicationContext(),"catidad  de productos "+numItems+" ",Toast.LENGTH_LONG).show();
        return (pos >= numItems - 1);
    }

        public void clear() {
            if (listaProducto.size()>0){
                listaProducto.clear();
                adaptadorProducto.notifyDataSetChanged();
            }

        }


    */

    }