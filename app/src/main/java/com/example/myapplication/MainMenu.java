package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ConexionBD.DBConnection;
import com.example.myapplication.SQLite.conexionSQLiteHelper;
import com.example.myapplication.SQLite.entidades.ProductosAdd;
import com.example.myapplication.SQLite.entidades.VendedorAdd;
import com.example.myapplication.SQLite.ulilidades.utilidades;
import com.example.myapplication.SQLite.ulilidades.utilidadesFact;
import com.example.myapplication.SQLite.ulilidades.utilidadesVendedor;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{
    Button btn_factura,btn_recibo,btn_cuentas,btn_cerrar;
    DBConnection sesion;
    int id,id2;
   int idResultante;
    String nombreVendedor,nombreVendedor2;
    Intent i;
    TextView titulo;
    LinearLayout menu;
    ArrayList<VendedorAdd>listavendedor;
    ArrayList<String> listainformacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().setTitle("Menu Principal");



        id=getIntent().getIntExtra("IdVendedor",0);
        Bundle extra=getIntent().getExtras();
        if (extra != null) {

            nombreVendedor = extra.getString("NombreVendedor");
            System.out.println("----> NombreVendedor: " + nombreVendedor);


        }

        titulo=findViewById(R.id.tv_titulo);
        btn_factura = findViewById(R.id.btnfactura);
        btn_cuentas =  findViewById(R.id.btnrecibo);
        btn_recibo =  findViewById(R.id.btncuenta);
        btn_cerrar= findViewById(R.id.CerrarSesion);
        menu=findViewById(R.id.Layout_menu);
        btn_factura.setOnClickListener(this);
        btn_recibo.setOnClickListener(this);
        btn_cuentas.setOnClickListener(this);
        btn_cerrar.setOnClickListener(this);
     //   titulo.setText(nombreVendedor);

        ConsultarVendedorSQLite();
        listainformacion = new ArrayList<String>();
        for (int i=0; i<listavendedor.size();i++){

            id2=listavendedor.get(i).getIdVendedor();
           nombreVendedor2=listavendedor.get(i).getNombreVendedor();
            System.out.println("llamando el nombre del vendedor SQLite====> "+nombreVendedor2);
            System.out.println("llamando el ID del vendedor SQLite====> "+id2);



        }



        MainFacturaList datos = new MainFacturaList();
        System.out.println("Id Vemdedpr de FacturaList"+ datos.IDVendedor);

    }


    @Override
    protected void onStart() {
        super.onStart();
        isconnected();
        GuardarVendedorSQLite();
        titulo.setText(nombreVendedor2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnfactura:
                i = new Intent(this,MainFactura.class);
                i.putExtra("Id",id);
                startActivity(i);
                break;
            case R.id.btnrecibo:
                Intent intent1 = new Intent(getApplicationContext(), MainRecibo.class);
                intent1.putExtra("Id",id);
                intent1.putExtra("NombreVendedor",nombreVendedor);
                startActivity(intent1);
                break;
            case R.id.btncuenta:
                Intent intent2 = new Intent(getApplicationContext(), Maincuentas.class);
                intent2.putExtra("Id",id);
                intent2.putExtra("NombreVendedor",nombreVendedor);
                startActivity(intent2);
                break;
            case R.id.CerrarSesion:
                try {
                    i = new Intent(this,MainActivity.class);
                    startActivity(i);
                    finish();
                    sesion = DBConnection.getDbConnection();
                    sesion.getConnection().close();
                    BorrarTablaVendedor();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void GuardarVendedorSQLite(){
        /*mandando a llamar conexion a SQLite */
        conexionSQLiteHelper conn= new conexionSQLiteHelper(this,"bd_productos",null,1);
        /*abrir la conexion a SQLite*/
        SQLiteDatabase db= conn.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(utilidadesVendedor.CAMPO_ID_VENDEDOR,id);
        values.put(utilidadesVendedor.CAMPO_NOMBRE_VENDEDOR,nombreVendedor);

        idResultante= (int) db.insert(utilidadesVendedor.TABLA_VENDEDORES,utilidadesVendedor.CAMPO_ID_VENDEDOR,values);
        db.close();
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

    public void BorrarTablaVendedor(){
        conexionSQLiteHelper conn= new conexionSQLiteHelper(this,"bd_productos",null,1);
        SQLiteDatabase db= conn.getReadableDatabase();
        db.execSQL("delete from producto");
        db.close();
    }
   public void isconnected(){
     ConnectivityManager connectivity=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo info_wifi= connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
       NetworkInfo info_datos= connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
       if (String.valueOf(info_wifi.getState()).equals("CONNECTED")){
         //  Toast.makeText(this,"Conectado con wifi",Toast.LENGTH_LONG).show();
       }else{
           if (String.valueOf(info_datos.getState()).equals("CONNECTED")){
             //  Toast.makeText(this,"Conectado con datos",Toast.LENGTH_LONG).show();
           }else{
                Toast.makeText(this,"Sin internet",Toast.LENGTH_LONG).show();
           }
       }
   }
}