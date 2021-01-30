package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLException;
import java.util.Objects;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{
    Button btn_factura,btn_recibo,btn_cuentas,btn_cerrar;
    DBConnection sesion;
    int id;
    String nombreVendedor;
    Intent i;
    TextView titulo;
    LinearLayout menu;
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
        titulo.setText(nombreVendedor);
    }


    @Override
    protected void onStart() {
        super.onStart();
        isconnected();
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
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

   public void isconnected(){
     ConnectivityManager connectivity=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo info_wifi= connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
       NetworkInfo info_datos= connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
       if (String.valueOf(info_wifi.getState()).equals("CONNECTED")){
           Toast.makeText(this,"Conectado con wifi",Toast.LENGTH_LONG).show();
       }else{
           if (String.valueOf(info_datos.getState()).equals("CONNECTED")){
               Toast.makeText(this,"Conectado con datos",Toast.LENGTH_LONG).show();
           }else{
                Toast.makeText(this,"Sin internet",Toast.LENGTH_LONG).show();
           }
       }
   }
}