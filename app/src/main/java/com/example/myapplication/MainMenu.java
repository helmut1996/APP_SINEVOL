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

public class MainMenu extends AppCompatActivity implements View.OnClickListener{
    ImageButton btn_factura,btn_recibo,btn_cuentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().setTitle("Menu Principal");

        btn_factura = (ImageButton)findViewById(R.id.btnfactura);
        btn_cuentas = (ImageButton) findViewById(R.id.btnrecibo);
        btn_recibo = (ImageButton) findViewById(R.id.btncuenta);

        btn_factura.setOnClickListener(this);
        btn_recibo.setOnClickListener(this);
        btn_cuentas.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnfactura:
                startActivity(new Intent(getApplicationContext(),MainFactura.class));
                break;
            case R.id.btnrecibo:
                startActivity(new Intent(getApplicationContext(),MainRecibo.class));
                break;
            case R.id.btncuenta:
                startActivity(new Intent(getApplicationContext(),Maincuentas.class));
                break;
        }
    }
}