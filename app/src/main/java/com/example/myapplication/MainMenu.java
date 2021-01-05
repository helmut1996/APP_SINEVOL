package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.ConexionBD.DBConnection;

import java.sql.SQLException;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{
    ImageButton btn_factura,btn_recibo,btn_cuentas,btn_cerrar;
    DBConnection sesion;
    int id;
    String nombreVendedor;
    Intent i;

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

        btn_factura = (ImageButton)findViewById(R.id.btnfactura);
        btn_cuentas = (ImageButton) findViewById(R.id.btnrecibo);
        btn_recibo = (ImageButton) findViewById(R.id.btncuenta);
        btn_cerrar= findViewById(R.id.CerrarSesion);
        btn_factura.setOnClickListener(this);
        btn_recibo.setOnClickListener(this);
        btn_cuentas.setOnClickListener(this);
        btn_cerrar.setOnClickListener(this);
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
                startActivity(new Intent(getApplicationContext(),Maincuentas.class));
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
}