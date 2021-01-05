package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ConexionBD.DBConnection;
import com.example.myapplication.SQLite.conexionSQLiteHelper;
import com.example.myapplication.SQLite.ulilidades.utilidadesFact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainRecibo extends AppCompatActivity {
AutoCompleteTextView buscadorCliente,BuscadorFactura;
EditText abono,descuento;
TextView saldo,numresf,fecha,zona,vendedor;
ImageButton Guardar,imprimir;
EditText onservacion;

public static int id;
public static String nombreVendedor;

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

        fecha=findViewById(R.id.tvr_fecha);
        zona=findViewById(R.id.tvr_zona);
        vendedor=findViewById(R.id.tv_NombreVendedor);
        buscadorCliente=findViewById(R.id.editBuscarCliente);
        BuscadorFactura=findViewById(R.id.editBuscadorFactura);
        abono=findViewById(R.id.editAbono);
        descuento=findViewById(R.id.editDescuento);
        saldo= findViewById(R.id.tvr_saldo);
        numresf=findViewById(R.id.tv_NReferencia);
        onservacion=findViewById(R.id.editObservacion);
        Guardar=findViewById(R.id.btnGuardarRecibo);
        imprimir=findViewById(R.id.btnImprimirRecibo);


        id=getIntent().getIntExtra("Id",0);

        System.out.println("ID vendedor activity Recibo===========>"+ id);

        Bundle extra=getIntent().getExtras();
        if (extra != null) {
            nombreVendedor = extra.getString("NombreVendedor");
            System.out.println("----> NombreVendedor: " + nombreVendedor);
            vendedor.setText(extra.getString("NombreVendedor"));
        }

            ArrayAdapter<String>adaptercliente=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,clientes);
        buscadorCliente.setAdapter(Clientes());

        ArrayAdapter<String>adapterfactura=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,factura);
        BuscadorFactura.setAdapter(Facturas());


        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (buscadorCliente.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"debes eligir el Cliente",Toast.LENGTH_SHORT).show();
                }else if(BuscadorFactura.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"debes eligir la Factura",Toast.LENGTH_SHORT).show();
                } else if (abono.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"El Abono no puede ser 0",Toast.LENGTH_LONG).show();
                }else if(descuento.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"El Descuento no puede ser 0",Toast.LENGTH_LONG).show();
                }else{
                    GuardarReciboSQLite();
                    limpiarcampos();
                    Toast.makeText(getApplicationContext()," Recibo Guardado....",Toast.LENGTH_LONG).show();
                }

            }
        });

        imprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"imprimiendo Recibo  sinevol ",Toast.LENGTH_SHORT).show();
            }
        });
        Clientes();
        Facturas();
    }

    public ArrayAdapter Clientes() {
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

    public ArrayAdapter Facturas() {
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

    public void limpiarcampos(){
        buscadorCliente.setText("");
        BuscadorFactura.setText("");
        abono.setText("");
        descuento.setText("");
        onservacion.setText("");
        saldo.setText("");

    }

    public void GuardarReciboSQLite() {
        /*mandando a llamar conexion a SQLite */
       conexionSQLiteHelper conn= new conexionSQLiteHelper(this,"bd_productos",null,1);
        /*abrir la conexion a SQLite*/
        SQLiteDatabase db= conn.getWritableDatabase();
            ContentValues values= new ContentValues();
            values.put(utilidadesFact.CAMPO_NOMBRE_CLIEBTE,buscadorCliente.getText().toString());
            values.put(utilidadesFact.CAMPO_FECHA,fecha.getText().toString());
            values.put(utilidadesFact.CAMPO_FACTURA,BuscadorFactura.getText().toString());
            values.put(utilidadesFact.CAMPO_ABONO,abono.getText().toString());
            values.put(utilidadesFact.CAMPO_DESCUENTO,descuento.getText().toString());
            values.put(utilidadesFact.CAMPO_SALDO_RES,saldo.getText().toString());
            values.put(utilidadesFact.CAMPO_NUMERO_RESF,numresf.getText().toString());
            long idResultante= db.insert(utilidadesFact.TABLA_RECIBO,utilidadesFact.CAMPO_NOMBRE_CLIEBTE,values);
            Toast.makeText(this,"agregado: " + idResultante,Toast.LENGTH_SHORT).show();



    }

}