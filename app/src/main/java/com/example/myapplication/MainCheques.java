package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainCheques extends AppCompatActivity implements View.OnClickListener {
TextView zona,vendedor,fecha;
EditText BuscadorClienteC,NCheque,MCheque,Beneficiario,ObservacionesC;
Spinner Bancos;
ImageButton imprimirC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cheques);

        getSupportActionBar().setTitle("Cheques");

        zona=findViewById(R.id.tvc_zona);
        vendedor=findViewById(R.id.tvc_vendedor);
        fecha=findViewById(R.id.tvc_fecha);
        BuscadorClienteC=findViewById(R.id.editBuscarCliente);
        NCheque=findViewById(R.id.editNumeroCheque);
        MCheque=findViewById(R.id.editMontocheque);
        Beneficiario=findViewById(R.id.editBeneficiario);
        ObservacionesC=findViewById(R.id.editObservacionC);
        Bancos=findViewById(R.id.BuscadorBanco);
        imprimirC=findViewById(R.id.btnImprimirCheque);


        /*Capturando la fecha*/
        Date date = new Date();
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yy");
        System.out.println(dateFormat.format(date));
        fecha.setText(dateFormat.format(date));
        /*Capturando la fecha*/
    }

    @Override
    public void onClick(View v) {

    }
}