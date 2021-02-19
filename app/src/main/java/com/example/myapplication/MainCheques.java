package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ConexionBD.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainCheques extends AppCompatActivity  {
TextView zona,vendedor,fecha,NombreCliente,idBanco,IdCliente;
EditText NCheque,MCheque,Beneficiario,ObservacionesC;
Spinner Bancos;
AutoCompleteTextView BuscadorClienteC;
ImageButton imprimirC;

String nombreVendedor;
int idVendedor,IdTalonario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cheques);

        getSupportActionBar().setTitle("Cheques");
        IdCliente=findViewById(R.id.tvc_IdclienteCheque);
        NombreCliente=findViewById(R.id.tvc_NombreCliente);
        zona=findViewById(R.id.tvc_zona);
        vendedor=findViewById(R.id.tvc_vendedor);
        fecha=findViewById(R.id.tvc_fecha);
        BuscadorClienteC=findViewById(R.id.editBuscarClienteC);
        NCheque=findViewById(R.id.editNumeroCheque);
        MCheque=findViewById(R.id.editMontocheque);
        Beneficiario=findViewById(R.id.editBeneficiario);
        ObservacionesC=findViewById(R.id.editObservacionC);
        Bancos=findViewById(R.id.BuscadorBanco);
        idBanco=findViewById(R.id.tvc_idBaanco);
        imprimirC=findViewById(R.id.btnImprimirCheque);



        /*pasando id del  vendedor*/
        idVendedor = getIntent().getIntExtra("id", 0);
        System.out.println("----> IdVendedorCheque: " + idVendedor);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            nombreVendedor = extra.getString("NombreVendedor");
            vendedor.setText(extra.getString("NombreVendedor"));
        }

        /*Capturando la fecha*/
        Date date = new Date();
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yy");
        System.out.println(dateFormat.format(date));
        fecha.setText(dateFormat.format(date));
        /*Capturando la fecha*/

        BuscadorClienteC.setAdapter(Clientes());
        BuscadorClienteC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NombreCliente.setText(BuscadorClienteC.getText().toString());
                try {
                    busqueda();
                    Talonario();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });

        //spinner Bancos
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.bancos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Bancos.setAdapter(adapter);

        Bancos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==1){
                    idBanco.setText("1");
                }else if(position==2){
                    idBanco.setText("2");
                }else if (position==3){
                    idBanco.setText("3");
                }else if (position==4){
                    idBanco.setText("4");
                }else if (position==5){
                    idBanco.setText("5");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        imprimirC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (BuscadorClienteC.getText().toString().isEmpty()) {
                    BuscadorClienteC.setError("Debe seleccionar un cliente");
                }else if (Bancos.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Selecciona un Banco",Toast.LENGTH_LONG).show();
                } else if (NCheque.getText().toString().isEmpty()){
                    NCheque.setError("Ingrese No.Cheque");
                }else if (MCheque.getText().toString().isEmpty()){
                    MCheque.setError("Ingrese el monto");
                }else if (Beneficiario.getText().toString().isEmpty()){
                    Beneficiario.setError("Ingrese el Beneficiario");
                }else if (Integer.parseInt(NCheque.getText().toString())==0){
                    NCheque.setError("El numero de Cheque no puede ser 0");
                }else if (Integer.parseInt(MCheque.getText().toString())==0){
                    MCheque.setError("El monto no puede ser 0");
                } else {
                    Toast.makeText(getApplicationContext(),"Cheque Guardado",Toast.LENGTH_LONG).show();

                }



            }

        });
    }

    public ArrayAdapter Clientes() {
        ArrayAdapter NoCoreAdapter=null;
        DBConnection dbConnection = new DBConnection();
        dbConnection.conectar();

        String query = "select  Nombre,idCliente,Telefono1,Direccion from Clientes where idVendedor='" + idVendedor + "' AND Estado = 'Activo' order by Nombre asc";
        try {
            Statement stm = dbConnection.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                data.add(rs.getString("Nombre"));

            }
            NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NoCoreAdapter;
    }

    public void busqueda() throws SQLException {
        DBConnection dbConnection = new DBConnection();
        dbConnection.conectar();
        Statement st = dbConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery("select idCliente,Telefono1,Direccion from Clientes where Nombre = '" + NombreCliente.getText().toString() + "' ");
        while (rs.next()) {
            IdCliente.setText( rs.getString("idCliente"));
            zona.setText(rs.getString("Direccion"));

        }
    }

    public void Talonario(){
        DBConnection dbConnection=new DBConnection();
        dbConnection.conectar();
        try {
            Statement st2 = dbConnection.getConnection().createStatement();
            ResultSet rs2 = st2.executeQuery("\n" +
                    "select top 1 idTalonario,Estado from Talonarios where idVendedor=' " +idVendedor+"' and Estado = 'Pendiente'  order by idTalonario ");
            while (rs2.next()) {
                IdTalonario = rs2.getInt("idTalonario");

                System.out.println("==============> Ultimo Registro IdTalonario :" + IdTalonario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}