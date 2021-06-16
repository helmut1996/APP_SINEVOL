package com.example.sinevollibreria;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sinevollibreria.ConexionBD.DBConnection;
import com.example.sinevol.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends Activity {
    EditText editPint;
    Button btn_entar;

    String NombreVendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        editPint = (EditText) findViewById(R.id.edit_Pin);
        btn_entar = (Button) findViewById(R.id.btn_entrar);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        }

    protected void onStart() {
        super.onStart();
        isconnected();
    }


    @Override
    public void onBackPressed() {

            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

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
                startActivity(new Intent(MainActivity.this, MainError_Internet.class));
                Toast.makeText(this,"Sin internet",Toast.LENGTH_LONG).show();
            }
        }
    }


    public void conectar (View view)
    {

        if (editPint.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "debe ingresar su pin ", Toast.LENGTH_LONG).show();
            editPint.requestFocus();
        } else {
            try {
                DBConnection dbConnection = new DBConnection();
                dbConnection.conectar();
                Statement stm = dbConnection.getConnection().createStatement();
                ResultSet rs = stm.executeQuery("Select IdVendedor, Nombre From Vendedores where Pin='" + editPint.getText().toString() + "'");
                if (rs.next()) {
                    NombreVendedor= rs.getString(2);
                    int id=rs.getInt(1);
                    Intent i = new Intent(this,MainFullScreen.class);
                    i.putExtra("IdVendedor",id);
                    i.putExtra("NombreVendedor",NombreVendedor);
                    startActivity(i);
                    finish();
                } else {

                    Toast.makeText(getApplicationContext(), "Pin incorrecto", Toast.LENGTH_LONG).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}