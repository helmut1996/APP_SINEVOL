package com.example.sinevollibreria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmedelsayed.sunmiprinterutill.PrintMe;
import com.example.sinevollibreria.ConexionBD.DBConnection;
import com.example.sinevol.R;
import com.example.sinevollibreria.SQLite.conexionSQLiteHelper;
import com.example.sinevollibreria.SQLite.entidades.FacturasAdd;
import com.example.sinevollibreria.SQLite.ulilidades.utilidadesFact;
import com.example.sinevollibreria.Utils.Coversion_Numero_Letra;
import com.google.android.material.snackbar.Snackbar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainRecibo extends AppCompatActivity {
    LinearLayout cuerpo;
    AutoCompleteTextView buscadorCliente;
    EditText abono, descuento;
    TextView saldo, numresf, fecha, zona, vendedor, tvIdclienyte, tvIdCuentasxCobrar, saldo2,NRecibo;
    ImageButton Guardar, imprimir;
    EditText observacion;
    Spinner BuscadorFactura;
    public static int id,
            idClientesRecibo;
    public static String nombreVendedor, idcliente;
    public static Double SaldoR;
    String letra;
    Double totalabono,totalSaldo;
    int idcuentasxcobrar;

    conexionSQLiteHelper conn;
    public static int IdTalonario, NumeracionInicial, numeracion, IdPagosCxC, IdPagosCxC2;
    public static String Estado;

    ArrayList<String> listainformacion;
    ArrayList<FacturasAdd> listarecibo;

    ///////////////////////Impresora//////////////////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recibo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recibos");


        ///llamando los componentos
        saldo2 = findViewById(R.id.tvr_saldo2);
        cuerpo = findViewById(R.id.linearLayout);
        tvIdclienyte = findViewById(R.id.tv_IdclienteRecibo);
        fecha = findViewById(R.id.tvr_fecha);
        zona = findViewById(R.id.tvr_zona);
        vendedor = findViewById(R.id.tv_NombreVendedor);
        buscadorCliente = findViewById(R.id.editBuscarCliente);
        BuscadorFactura = findViewById(R.id.buscadorFacturaCliente);
        abono = findViewById(R.id.editAbono);
        descuento = findViewById(R.id.editDescuento);
        saldo = findViewById(R.id.tvr_saldo);
        numresf = findViewById(R.id.tv_NReferencia);
        observacion = findViewById(R.id.editObservacion);
        Guardar = findViewById(R.id.btnGuardarRecibo);
        imprimir = findViewById(R.id.btnImprimirRecibo);
        tvIdCuentasxCobrar = findViewById(R.id.idcuentasxCobrar);
        NRecibo=findViewById(R.id.tvNumRecibo);
        /////////////////////////////////////////////////////////


        /*pasando id del  vendedor*/
        id = getIntent().getIntExtra("Id", 0);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            nombreVendedor = extra.getString("NombreVendedor");
            System.out.println("----> NombreVendedor: " + nombreVendedor);
            vendedor.setText(extra.getString("NombreVendedor"));
        }
        /*pasando id del  vendedor*/

        /*Capturando la fecha*/
        Date date = new Date();
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yy");
        System.out.println(dateFormat.format(date));
        fecha.setText(dateFormat.format(date));
        /*Capturando la fecha*/

        buscadorCliente.setAdapter(Clientes());

        buscadorCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvIdclienyte.setText(buscadorCliente.getText().toString());

                BuscadorFactura.setAdapter(Facturas());

                buscadorCliente.setAdapter(Clientes());
                Talonario();
                NReferencia();


            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.facturas_clientes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        BuscadorFactura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BuscadorFactura.getSelectedItem().toString();
                calcularsaldo();
                abono.setText("");
                descuento.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        abono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (abono.getText().toString().trim().equals("")) {
                    abono.requestFocus();
                    saldo.setText(SaldoR.toString());

                } else if (!descuento.getText().toString().trim().equals("")) {
                    Double tmpDescuento = Double.parseDouble(descuento.getText().toString());
                    Double tmpSaldo = SaldoR - Double.parseDouble(s.toString()) - tmpDescuento;
                    saldo.setText(tmpSaldo.toString());
                } else {
                    Double tmpSaldo = SaldoR - Double.parseDouble(s.toString());
                    saldo.setText(tmpSaldo.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        descuento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (descuento.getText().toString().trim().equals("")) {
                    descuento.requestFocus();

                } else if (!abono.getText().toString().trim().equals("")) {
                    Double tmpDescuento = Double.parseDouble(abono.getText().toString());
                    Double tmpSaldo = SaldoR - Double.parseDouble(s.toString()) - tmpDescuento;
                    saldo.setText(tmpSaldo.toString());
                } else {
                    Double tmpSaldo = SaldoR - Double.parseDouble(s.toString());
                    saldo.setText(tmpSaldo.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //IMPRESORA SUNMIV2 PRO
        PrintMe print =  new PrintMe(MainRecibo.this);

        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(MainRecibo.this);
                alerta.setMessage("Quieres Guardar")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (buscadorCliente.getText().toString().isEmpty()) {

                                    buscadorCliente.setError("Debe seleccionar un cliente");

                                } else if (abono.getText().toString().isEmpty()) {
                                    abono.setError("el campo  Abono esta vacio");

                                } else if(Double.parseDouble(abono.getText().toString())==0){
                                    abono.setError("la cantidad del abono no puede ser  0");

                                } else {
                                    if (abono.getText().toString().isEmpty() && descuento.getText().toString().isEmpty())
                                        Toast.makeText(getApplicationContext(), "Error campos vacios", Toast.LENGTH_LONG).show();
                                    else {
                                        if ((!abono.getText().toString().isEmpty()) && descuento.getText().toString().isEmpty()) {
                                            descuento.setText("0");
                                            ejecutarGuardado();
                                            calcularsaldo();
                                            CalcularTotalAbono();
                                            CalcularSaldoTotal();
                                            buscadorCliente.setEnabled(false);
                                            abono.setText("");
                                            observacion.setText("");
                                            saldo.setText(String.format("%,.2f",SaldoR));

                                        } else {
                                            if ((abono.getText().toString().isEmpty()) && !descuento.getText().toString().isEmpty()) {
                                                buscadorCliente.setEnabled(false);
                                                abono.setText("0");
                                                calcularsaldo();
                                                ejecutarGuardado();
                                                CalcularTotalAbono();
                                                CalcularSaldoTotal();
                                                abono.setText("");
                                                observacion.setText("");
                                                saldo.setText(SaldoR.toString());

                                            }

                                        }
                                    }

                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog= alerta.create();
                alertDialog.setTitle("Guardar Recibo");
                alertDialog.show();
            }

        });

        imprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conexionSQLiteHelper conn= new conexionSQLiteHelper(MainRecibo.this,"bd_productos",null,1);
                SQLiteDatabase db= conn.getWritableDatabase();

                androidx.appcompat.app.AlertDialog.Builder alerta = new androidx.appcompat.app.AlertDialog.Builder(MainRecibo.this);
                alerta.setMessage("Quieres Imprimir")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Cursor verificar_recibos=db.rawQuery("select count(*) as cantidad from recibo;", null);

                                if (verificar_recibos.moveToFirst()) {
                                    if (verificar_recibos.getInt(verificar_recibos.getColumnIndex("cantidad")) == 0) {
                                        Snackbar snackbar = Snackbar.make(cuerpo, "Debes de Guardar un recibo para Imprimir", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                        return;
                                    }
                                }

                                if (buscadorCliente.getText().toString().isEmpty() && abono.getText().toString().isEmpty()){
                                    Snackbar snackbar = Snackbar.make(cuerpo, "Debes de Guardar un recibo para Imprimir", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                                else{

                                    try {


                                            Consultarlista();
                                            Estado();
                                            AgregarReciboSQLSEVER();
                                            printText(print);
                                            borrardatosTabla();
                                            limpiarcampos();
                                    

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                    buscadorCliente.setEnabled(true);
                                    Snackbar snackbar = Snackbar.make(cuerpo, "Imprimiendo Recibo!!", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog= alerta.create();
                alertDialog.setTitle("Generar Recibo");
                alertDialog.show();

            }
        });


        descuento.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu4,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.Mbtn_MenuCheque:
                Intent intent = new Intent(getApplicationContext(),MainCheques.class);
                intent.putExtra("NombreVendedor",vendedor.getText().toString());
                intent.putExtra("id",id);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Funciones de la imprsora
     */

    void ejecutarGuardado() {

        GuardarReciboSQLite();
    }




    public ArrayAdapter Clientes() {
        ArrayAdapter NoCoreAdapter = null;
        DBConnection dbConnection = new DBConnection();
        dbConnection.conectar();

        String query = "select Nombre, idCliente from Clientes where idVendedor='" + id + "' AND Estado = 'Activo' order by Nombre desc";
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

    public ArrayAdapter Facturas() {
        ArrayAdapter NoCoreAdapter = null;
        DBConnection dbConnection = new DBConnection();
        dbConnection.conectar();

        String query = ("exec sp_BuscarClienteFacturaMovil '" + tvIdclienyte.getText().toString() + "'");
        try {
            Statement stm = dbConnection.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();

            while (rs.next()) {
                data.add(rs.getString("IdFactura"));
                zona.setText(rs.getString("Zona"));
                idClientesRecibo = (rs.getInt("idCliente"));
                System.out.println("ID CLIENTE RECIBO: " + idClientesRecibo);
            }
            NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NoCoreAdapter;
    }

    public void CalcularTotalAbono() {
        conexionSQLiteHelper conn = new conexionSQLiteHelper(this, "bd_productos", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        String total="select sum(abono ) as Total from recibo";
        Cursor query =  db.rawQuery(total,null);

        if (query.moveToFirst()){
             totalabono= query.getDouble(query.getColumnIndex("Total"));

            System.out.println("====> Prueba TotalAbono;"+totalabono);
            saldo2.setText(totalabono.toString());

            Coversion_Numero_Letra convertir = new Coversion_Numero_Letra();
            letra = convertir.Convertir(String.valueOf(totalabono), true);
            System.out.println("vonvercion de total abono====> "+letra);
        }

        db.close();
    }


    public void CalcularSaldoTotal(){
        conexionSQLiteHelper conn = new conexionSQLiteHelper(this, "bd_productos", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        String total2="select sum(saldorestante) as Total2 from recibo";
        Cursor query= db.rawQuery(total2,null);

        if (query.moveToFirst()){
            totalSaldo=query.getDouble(query.getColumnIndex("Total2"));
            System.out.println("======>PruebaSaldoTotal"+totalSaldo);


        }
        db.close();
    }

    public void  Estado(){

        DBConnection dbConnection = new DBConnection();
        dbConnection.conectar();

        int actualizacion= 0;
        try {

            PreparedStatement stmt= dbConnection.getConnection().prepareStatement("UPDATE Talonarios SET Estado = 'Entregado' WHERE idTalonario = ? ");

            stmt.setString(1, String.valueOf(IdTalonario));
            actualizacion = stmt.executeUpdate();

            System.out.println("Actualizacion de Estado===>"+actualizacion);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void Talonario(){
        DBConnection dbConnection=new DBConnection();
        dbConnection.conectar();
        try {
            Statement st2 = dbConnection.getConnection().createStatement();
            ResultSet rs2 = st2.executeQuery("\n" +
                    "select top 1 idTalonario,Estado from Talonarios where idVendedor=' " +id+"' and Estado = 'Pendiente'  order by idTalonario ");
            while (rs2.next()) {
                IdTalonario = rs2.getInt("idTalonario");

                System.out.println("==============> Ultimo Registro IdTalonario :" + IdTalonario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


public void NReferencia(){

    DBConnection dbConnection=new DBConnection();
    dbConnection.conectar();
    try {
        Statement st = dbConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery("select top 1 NumeracionInicial,Estado,idVendedor from Talonarios where idVendedor = ' "+id+" ' and Estado= 'Pendiente' order by idTalonario ");
        while (rs.next()) {
            NumeracionInicial = rs.getInt("NumeracionInicial");
            numeracion = NumeracionInicial;
            System.out.println("==============> Ultimo Registro NumeroInicial :" + numeracion);
        }

        if (numeracion==0){
            AlertDialog.Builder alerta = new AlertDialog.Builder(MainRecibo.this);
            alerta.setMessage("ADVERTENCIA ")
                    .setCancelable(false)
                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                finish();
                        }
                    });

            AlertDialog alertDialog= alerta.create();
            alertDialog.setMessage("Ya no tienes talonario Asignado debes solicitar para generar recibo ");
            alertDialog.show();

        }else {
            NRecibo.setText("No.Recibo:"+numeracion);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }


}

    public void calcularsaldo() {
        DBConnection dbConnection=new DBConnection();
        dbConnection.conectar();
        String query = "exec sp_SaldoFacturaMovil " + BuscadorFactura.getSelectedItem();

        try {
            Statement stm = dbConnection.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
               tvIdCuentasxCobrar.setText(rs.getString("idCuentasxCobrar"));
                idcuentasxcobrar= Integer.parseInt(rs.getString("idCuentasxCobrar"));

                System.out.println("IDCuentasxCobrar Capturando:====>" +idcuentasxcobrar);
                SaldoR = rs.getDouble("SaldoRestante");
                saldo.setText(SaldoR.toString());


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void limpiarcampos() {
        buscadorCliente.setText("");
        abono.setText("");
        descuento.setText("");
        observacion.setText("");
        saldo.setText("");
        NRecibo.setText("No.Recibo:");
        BuscadorFactura.setAdapter(null);
    }


    public void GuardarReciboSQLite() {
        String CapturaFactura= BuscadorFactura.getSelectedItem().toString();
        /*mandando a llamar conexion a SQLite */
        conexionSQLiteHelper conn = new conexionSQLiteHelper(this, "bd_productos", null, 1);
        /*abrir la conexion a SQLite*/
        SQLiteDatabase db = conn.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT factura FROM recibo WHERE factura='"+CapturaFactura+"'", null);
        if(c.moveToNext()) {
            Snackbar snackbar = Snackbar.make(cuerpo, "Ya le Aplicaste abono a esta Factura:"+CapturaFactura, Snackbar.LENGTH_LONG);
            snackbar.show();
        }else {
            ContentValues values = new ContentValues();
            values.put(utilidadesFact.CAMPO_NOMBRE_CLIEBTE, buscadorCliente.getText().toString());
            values.put(utilidadesFact.CAMPO_FECHA, fecha.getText().toString());
            values.put(utilidadesFact.CAMPO_FACTURA, BuscadorFactura.getSelectedItem().toString());
            values.put(utilidadesFact.CAMPO_ABONO, abono.getText().toString());
            values.put(utilidadesFact.CAMPO_DESCUENTO, descuento.getText().toString());
            values.put(utilidadesFact.CAMPO_SALDO_RES, saldo.getText().toString());
            values.put(utilidadesFact.CAMPO_NUMERO_RESF, IdPagosCxC2);
            values.put(utilidadesFact.CAMPO_CUENTASXCOBRAR, idcuentasxcobrar);
            values.put(utilidadesFact.CAMPO_OBSERVACIONES, observacion.getText().toString());
            long idResultante = db.insert(utilidadesFact.TABLA_RECIBO, utilidadesFact.CAMPO_NOMBRE_CLIEBTE, values);
             //Toast.makeText(this, "Cantidad Recibo: " + idResultante, Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(cuerpo, "Recibo Guardado", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        db.close();
    }

    public void Consultarlista() {

        /*mandando a llamar conexion a SQLite */
        conexionSQLiteHelper conn = new conexionSQLiteHelper(this, "bd_productos", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        FacturasAdd facturasAdd;
        listarecibo = new ArrayList<FacturasAdd>();
        //query SQLite
        Cursor cursor = db.rawQuery("select * from " + utilidadesFact.TABLA_RECIBO, null);
        while (cursor.moveToNext()) {
            facturasAdd = new FacturasAdd();
            facturasAdd.setNombreCliente(cursor.getString(0));
            facturasAdd.setFactura(cursor.getString(1));
            facturasAdd.setFecha(cursor.getString(2));
            facturasAdd.setAbono(cursor.getDouble(3));
            facturasAdd.setDescuento(cursor.getDouble(4));
            facturasAdd.setSaldoRes(cursor.getDouble(5));
            facturasAdd.setNumReferencia(cursor.getInt(6));
            facturasAdd.setIdCuentasxCobrar(cursor.getInt(7));
            facturasAdd.setObservaciones(cursor.getString(8));


            listarecibo.add(facturasAdd);
            db.close();
        }

    }


    public void printText(PrintMe print) {
        listainformacion = new ArrayList<String>();

        for (int p=0; p<2;p++){
            print.sendTextToPrinter("RECIBO No."+numeracion+ "\n", 40, true, false, 1);
            print.sendTextToPrinter("NREF:" +IdPagosCxC+ "\t\t"+fecha.getText().toString()+ "\n", 30, true, false, 1);
            print.sendTextToPrinter(vendedor.getText().toString()+ " \n", 25, true, false, 0);
            print.sendTextToPrinter("*****************************"+ " \n", 25, true, false, 0);
            print.sendTextToPrinter("Recibo de:" + " " + tvIdclienyte.getText().toString()+ " \n", 30, true, false, 1);
            print.sendTextToPrinter("suma de:" + " " + letra+ " \n", 25, true, false, 0);
            print.sendTextToPrinter("Total Abonado: C$" + String.format("%,.2f",totalabono)+ " \n", 25, true, false, 1);
            print.sendTextToPrinter("___________________________________ \n", 20, true, false, 0);
            print.sendTextToPrinter("N.Fact  |"+"\t"+"|Abono |"+"\t"+"|Saldo |", 25, true, false, 0);
            print.sendTextToPrinter("__________________________________________\n", 20, true, false, 0);
            for (int i = 0; i < listarecibo.size(); i++) {
                print.sendTextToPrinter("|"+listarecibo.get(i).getFactura()+"| \t"+String.format("%,.2f", listarecibo.get(i).getAbono())+"| \t"+String.format("%,.2f",listarecibo.get(i).getSaldoRes())+ "|", 25   , true, false, 0);
                print.sendTextToPrinter("________________________________________\n", 20, true, false, 0);
            }
            print.sendTextToPrinter("Saldo Total: C$ "+ String.format("%,.2f",totalSaldo)+ " \n", 25, true, false, 0);
            print.sendTextToPrinter("Observaciones"+ " \n", 25, true, false, 1);
            for (int j = 0; j < listarecibo.size(); j++) {
                print.sendTextToPrinter(listarecibo.get(j).getObservaciones() , 25, true, false, 0);
            }
            print.sendTextToPrinter("__________________________"+ " \n", 25, true, false, 1);
            print.sendTextToPrinter("Recibe Conforme" + " " + "\n\n_______________________\n\n", 25, true, false, 0);
            print.sendTextToPrinter("Entragado Conforme" + " " + "\n\n______________________"+ " \n", 25, true, false, 1);
            print.sendTextToPrinter("**********END***********"+ " \n", 25, true, false, 2);

        }


    }




    public void borrardatosTabla() {
      conexionSQLiteHelper conn = new conexionSQLiteHelper(this, "bd_productos", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
            db.execSQL("delete from recibo");
        db.close();
    }

    public void AgregarReciboSQLSEVER() throws SQLException {

        DBConnection dbConnection = new DBConnection();
        dbConnection.conectar();

        try {
            dbConnection.getConnection().setAutoCommit(false);

            PreparedStatement pst2 = dbConnection.getConnection().prepareStatement("  exec sp_insertPagoCxC ?,?,?,?,?");
            pst2.setInt(1, Integer.parseInt(String.valueOf(IdTalonario)));
            pst2.setDouble(2,Double.parseDouble(saldo2.getText().toString()));
            pst2.setInt(3, Integer.parseInt(String.valueOf(idClientesRecibo)));
            pst2.setString(4, observacion.getText().toString());
            pst2.setString(5, vendedor.getText().toString());
            pst2.executeUpdate();


            Statement st3 = dbConnection.getConnection().createStatement();
            ResultSet rs3 = st3.executeQuery("select top 1 idPagoCxC from Pagos_CxC order by idPagoCxC desc");
            while (rs3.next()) {
                IdPagosCxC = rs3.getInt("idPagoCxC");
                System.out.println("============> Ultimo Registro IdPagosCxC :" + IdPagosCxC);
            }


            for (int i =0; i<listarecibo.size(); i++){

                PreparedStatement pst3 = dbConnection.getConnection().prepareStatement(" exec sp_insertDetallePagoCxC ?,?,?,?");
                pst3.setInt(1, Integer.parseInt(String.valueOf(IdPagosCxC)));
                pst3.setInt(2, listarecibo.get(i).getIdCuentasxCobrar());
                pst3.setDouble(3, listarecibo.get(i).getAbono());
                pst3.setDouble(4,listarecibo.get(i).getDescuento());
                pst3.executeUpdate();

            }

            if(SaldoR<0)  {
                PreparedStatement pst4 = dbConnection.getConnection().prepareStatement("exec sp_insertNotaDebito ?,?,?");
                pst4.setInt(1, Integer.parseInt(String.valueOf(IdPagosCxC)));
                pst4.setInt(2, Integer.parseInt(String.valueOf(idClientesRecibo)));
                pst4.setDouble(3, Double.parseDouble(SaldoR.toString()) * -1);
                pst4.executeUpdate();
            }



        } catch (SQLException e) {
            dbConnection.getConnection().rollback();
            System.out.println("ERROR: ======> " + e);
            Snackbar snackbar = Snackbar.make(cuerpo, "No Registrado SQLServer", Snackbar.LENGTH_LONG);
            snackbar.show();
        } finally {

            dbConnection.getConnection().setAutoCommit(true);
        }

    }

}