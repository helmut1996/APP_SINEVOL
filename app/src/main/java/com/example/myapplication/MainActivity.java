package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ConexionBD.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity{
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
                    //usuario.setText(rs.getString(2));
                    NombreVendedor= rs.getString(2);
                    int id=rs.getInt(1);
                    Intent i = new Intent(this,MainMenu.class);
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
