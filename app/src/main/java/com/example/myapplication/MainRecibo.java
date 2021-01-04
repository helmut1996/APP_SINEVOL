package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.ConexionBD.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainRecibo extends AppCompatActivity {
AutoCompleteTextView buscadorCliente,BuscadorFactura;
EditText abono,descuento;
TextView saldo;
ImageButton Guardar,imprimir;

public static int id;


String []clientes= new String[]{
  "cleinte1","Helmut","brian","jefrry"
};

    String []factura= new String[]{
            "Factura1","Factura2","factura3","factura4"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recibo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recibos");
        
        buscadorCliente=findViewById(R.id.editBuscarCliente);
        BuscadorFactura=findViewById(R.id.editBuscadorFactura);
        abono=findViewById(R.id.editAbono);
        descuento=findViewById(R.id.editDescuento);
        saldo= findViewById(R.id.tvr_saldo);
        Guardar=findViewById(R.id.btnGuardarRecibo);
        imprimir=findViewById(R.id.btnImprimirRecibo);


        id=getIntent().getIntExtra("Id",0);
        System.out.println("ID vendedor activity Recibo===========>"+ id);

        ArrayAdapter<String>adaptercliente=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,clientes);
        buscadorCliente.setAdapter(Clientes());

        ArrayAdapter<String>adapterfactura=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,factura);
        BuscadorFactura.setAdapter(Facturas());

        Clientes();
        Facturas();


    }

    public ArrayAdapter Clientes()
    {
        ArrayAdapter NoCoreAdapter=null;
        DBConnection sesion;
        sesion = DBConnection.getDbConnection();

        String query = "select  Nombre,idCliente from Clientes where idVendedor='" + id + "' AND Estado = 'Activo' order by Nombre asc";
        try {
            Statement stm = sesion.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                data.add(rs.getString("Nombre"));
                data.add(rs.getString("idCliente"));

            }
            NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NoCoreAdapter;
    }

    public ArrayAdapter Facturas()
    {
        ArrayAdapter NoCoreAdapter=null;
        DBConnection sesion;
        sesion = DBConnection.getDbConnection();

        String query = "select NombreFactura,TipoFactura from Facturas where idVendedor='"+id+"'";
        try {
            Statement stm = sesion.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                data.add(rs.getString("NombreFactura"));
                data.add(rs.getString("TipoFactura"));

            }
            NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NoCoreAdapter;
    }
}