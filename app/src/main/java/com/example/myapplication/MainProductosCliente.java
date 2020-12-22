package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ConexionBD.DBConnection;
import com.example.myapplication.SQLite.conexionSQLiteHelper;
import com.example.myapplication.SQLite.ulilidades.utilidades;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainProductosCliente extends AppCompatActivity implements View.OnClickListener {
    /*variables de los componentes de la vista*/
private ImageButton IbuttonInicio,IbuttonAgregar,IbuttonSiguiente;
private TextView tvnombreproducto,textcontar,textinfo1,textinfo2,textinfo3,textinfo4,textinfo5,tvunidadmedida,tvcontadorproducto,tvimagenBD,tvIDproducto;
private Spinner precios,monedas;
private ImageView img;
private EditText editcantidad;


/* variables globales */
String NombreCliente;
String CodigoCliente;
String ZonaCliente;

 private String producto;

 private String contador;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_productos_cliente);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Productos");


/////////////////////////////////Metodo para permisos de las imagenes/////////////////////////////////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Verifica permisos para Android 6.0+
            checkExternalStoragePermission(); }

        ///////// Botones
        IbuttonInicio = findViewById(R.id.btn_Inicio);
        IbuttonAgregar = findViewById(R.id.btn_Agregar);
        IbuttonSiguiente = findViewById(R.id.btn_siguente);

        editcantidad=findViewById(R.id.editTextCantidad);
        ////////////imagen producto
        img=findViewById(R.id.imgProducto);
        /////////// campos de texto
        tvnombreproducto=findViewById(R.id.tvnombreP);
        textcontar=findViewById(R.id.text_contar);
        textinfo1=findViewById(R.id.text_info1);
        textinfo2=findViewById(R.id.text_info2);
        textinfo3=findViewById(R.id.text_info3);
        textinfo4=findViewById(R.id.text_info4);
        textinfo5=findViewById(R.id.text_info5);
        tvimagenBD=findViewById(R.id.imagenBD);
        tvunidadmedida=findViewById(R.id.text_unidadM);
        tvcontadorproducto=findViewById(R.id.contadorproducto);
        tvIDproducto=findViewById(R.id.IDProduto);
        ////////// Spinmer
        precios = findViewById(R.id.spinerPrecios);
        monedas = findViewById(R.id.spinner_tipo_moneda);

        IbuttonInicio.setOnClickListener(this);
        IbuttonAgregar.setOnClickListener(this);
        IbuttonSiguiente.setOnClickListener(this);

//////////////////////////////pasando datos por parametros entre activitys/////////////////////////////////

        String NombrePreducto;
        Bundle extra=getIntent().getExtras();

        if (extra !=null){
            NombreCliente = extra.getString("NombreCliente");
            System.out.println("Nombre Cliente Activity ProductosClientea----->"+NombreCliente);

            CodigoCliente = extra.getString("CodigoCliente");
            System.out.println("Codigo Cliente Activity ProductosClientea----->"+CodigoCliente);

            ZonaCliente = extra.getString("ZonaCliente");
            System.out.println("Zona Cliente Activity ProductosClientea----->"+ZonaCliente);
            NombrePreducto= extra.getString("NombreP");
            producto = extra.getString("NombreP");
            tvnombreproducto.setText(NombrePreducto);
            tvunidadmedida.setText(extra.getString("UnidadMed"));
            textinfo1.setText(extra.getString("info1"));
            textinfo2.setText(extra.getString("info2"));
            textinfo3.setText(extra.getString("info3"));
            textinfo4.setText(extra.getString("info4"));
            textinfo5.setText(extra.getString("info5"));
            textcontar.setText(extra.getString("stock"));
            tvimagenBD.setText(extra.getString("imagenproducto"));
            tvIDproducto.setText(extra.getString("idproducto"));
//////////////////////////////pasando datos por parametros entre activitys/////////////////////////////////

        }

        /*Spinner del tipo de moneda*/
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.tipo_moneda, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monedas.setAdapter(adapter);

        /*spinner de los precios*/

        monedas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(monedas.getSelectedItem().toString().equals("Cordobas"))
                {
                    precios.setAdapter(precioCordoba());
                }
                else
                {
                    precios.setAdapter(precioDolar());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

                        /*mandando a llamar las imagenes libreria */
                                        cargarImagen();
     }

    private void checkExternalStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para leer!");
        }
    }


    public void cargarImagen(){

        File file= new File("///storage/emulated/0/MARNOR/"+tvimagenBD.getText()+".jpg");
        Picasso.get().load(file)
                .placeholder(R.drawable.bucandoimg)
                .error(R.drawable.error)
                .into(img);
    }

    public ArrayAdapter precioDolar()
    {
        ArrayAdapter NoCoreAdapter=null;
        DBConnection sesion;
        sesion = DBConnection.getDbConnection();

        String query = "select PrecioDolar1, PrecioDolar2,PrecioDolar3,PrecioDolar4,PrecioDolar5 from Inventario where Nombre='" + producto + "'";
        try {
            Statement stm = sesion.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                data.add(rs.getString("PrecioDolar1"));
                data.add(rs.getString("PrecioDolar2"));
                data.add(rs.getString("PrecioDolar3"));
                data.add(rs.getString("PrecioDolar4"));
                data.add(rs.getString("PrecioDolar5"));
            }
            NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NoCoreAdapter;
    }

    public ArrayAdapter precioCordoba()
    {
        ArrayAdapter NoCoreAdapter=null;
        DBConnection sesion;
        sesion = DBConnection.getDbConnection();

        String query = "select Precio1, Precio2,Precio3,Precio4,Precio5 from Inventario where Nombre='" + producto + "'";
        try {
            Statement stm = sesion.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                data.add(rs.getString("Precio1"));
                data.add(rs.getString("Precio2"));
                data.add(rs.getString("Precio3"));
                data.add(rs.getString("Precio4"));
                data.add(rs.getString("Precio5"));
            }
            NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NoCoreAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_Inicio:
                Intent intent = new Intent(getApplicationContext(),MainMenu.class);
                startActivity(intent);
                break;
            case R.id.btn_siguente:
                MainListaproducto datos= new MainListaproducto();
                Intent intent1 = new Intent(getApplicationContext(), MainFacturaList.class);
                intent1.putExtra("NombreCliente",datos.nombrecliente);
                intent1.putExtra("CodigoCliente",datos.codigocliente);
                intent1.putExtra("ZonaCliente",datos.zonacliente);

                intent1.putExtra("nombreproducto",tvnombreproducto.getText());
                intent1.putExtra("cantidad",editcantidad.getText());
                startActivity(intent1);
                break;
            case R.id.btn_Agregar:
                // implementar agregar

                if (editcantidad.getText().toString().isEmpty()){

                    Toast.makeText(this,"debes ingresar una cantidad",Toast.LENGTH_SHORT).show();
                }else {

                    GuardarProductos();
                    Intent intent2 = new Intent(getApplicationContext(),MainListaproducto.class);
                    startActivity(intent2);

                }

                break;
        }
    }

    private void GuardarProductos() {

        /*mandando a llamar conexion a SQLite */
        conexionSQLiteHelper conn= new conexionSQLiteHelper(this,"bd_productos",null,1);
        /*abrir la conexion a SQLite*/
        SQLiteDatabase db= conn.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(utilidades.CAMPO_ID,tvIDproducto.getText().toString());
        values.put(utilidades.CAMPO_NOMBRE,tvnombreproducto.getText().toString());
        values.put(utilidades.CAMPO_CANTIDAD,editcantidad.getText().toString());
        values.put(utilidades.CAMPO_PRECIO,precios.getSelectedItem().toString());
        values.put(utilidades.CAMPO_IMAGEN,tvimagenBD.getText().toString());
        long idResultante= db.insert(utilidades.TABLA_PRODUCTO,utilidades.CAMPO_ID,values);

        Intent intent2 = new Intent(getApplicationContext(),MainListaproducto.class);
        startActivity(intent2);
        if (idResultante<=8){
            Toast.makeText(this,"ID PRODUCTO: " + idResultante,Toast.LENGTH_SHORT).show();


        }else {
            Toast.makeText(this,"solo puedes agregar 30 productos!!!",Toast.LENGTH_SHORT).show();
            IbuttonAgregar.setEnabled(false);

        }


    }

}