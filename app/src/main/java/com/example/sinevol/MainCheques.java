package com.example.sinevol;

import androidx.appcompat.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sinevol.ConexionBD.DBConnection;
import com.example.sinevol.Utils.BluetoothUtil;
import com.example.sinevol.Utils.BytesUtil;
import com.example.sinevol.Utils.ESCUtil;
import com.example.sinevol.Utils.SunmiPrintHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.io.UnsupportedEncodingException;
public class MainCheques extends AppCompatActivity  {
TextView zona,vendedor,fecha,NombreCliente,idBanco,IdCliente,NRC;
EditText NCheque,MCheque,Beneficiario,ObservacionesC;
Spinner Bancos;
AutoCompleteTextView BuscadorClienteC;
ImageButton imprimirC;

String nombreVendedor;
int idVendedor,IdTalonario,NumeracionInicialC,numeracionC,IdCheque ;

    byte[] rv = null;
///////////////////////Impresora//////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cheques);

        getSupportActionBar().setTitle("Cheques");

        //vinculando el diseño
        NRC=findViewById(R.id.tvc_NReferencia);
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

        //Buscador de clientes
        BuscadorClienteC.setAdapter(Clientes());
        BuscadorClienteC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NombreCliente.setText(BuscadorClienteC.getText().toString());
                try {
                    busqueda();
                    Talonario();
                    NReferencia();
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


        /// mandando a llamar la clase  cuadro  dialogo de  carga
        ClassDialogLoading loading=new ClassDialogLoading(MainCheques.this);

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

                    Toast.makeText(getApplicationContext(),"Precionado",Toast.LENGTH_LONG).show();
                   rv= BytesUtil.getBaiduTestBytes();
                    //if (getPrinterStatus() == PRINTER_NORMAL) {
                    /*
                    try {
                            AgregarChequesSQLSEVER();
                            IdCheque();
                            BytesUtil.getBaiduTestBytes();
                           // impresora();
                          //  printText();
                            loading.iniciarCarga();
                            Handler handler= new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loading.cerrarCarga();
                                    LimpiarCampos();
                                }
                            }, 8000);
                            Toast.makeText(getApplicationContext(),"Cheque Guardado",Toast.LENGTH_LONG).show();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }



                     */

                   // }


                }
                if(BluetoothUtil.isBlueToothPrinter){
                    BluetoothUtil.sendData(rv);
                }else{
                    SunmiPrintHelper.getInstance().sendRawData(rv);
                }

            }

        });
    }

    /// prueba
    public void impresora(){
        byte[] rv = BytesUtil.customData();
        Bitmap head = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
        //打印光栅位图(print raster bitmap ——normal)
        rv = BytesUtil.byteMerger(rv, ESCUtil.printBitmap(head, 0));
        //打印光栅位图  倍宽  (print raster bitmap ——double width)
        rv = BytesUtil.byteMerger(rv, ESCUtil.printBitmap(head, 1));
        //打印光栅位图  倍高    (print raster bitmap ——double height)
        rv = BytesUtil.byteMerger(rv,ESCUtil.printBitmap(head, 2));
        //打印光栅位图  倍宽倍高   (print raster bitmap ——double width and height)
        rv = BytesUtil.byteMerger(rv,ESCUtil.printBitmap(head, 3));
        //选择位图指令  8点单密度 （print Bitmap mode)
        rv = BytesUtil.byteMerger(rv,ESCUtil.selectBitmap(head, 0));
        //选择位图指令  8点双密度
        rv = BytesUtil.byteMerger(rv,ESCUtil.selectBitmap(head, 1));
        //选择位图指令  24点单密度
        rv = BytesUtil.byteMerger(rv,ESCUtil.selectBitmap(head, 32));
        //选择位图指令  24点双密度
        rv = BytesUtil.byteMerger(rv,ESCUtil.selectBitmap(head, 33));
        //之后将输出可显示的ascii码及制表符
        rv = BytesUtil.byteMerger(rv,BytesUtil.wordData());
        byte[] ascii = new byte[96];
        for(int i = 0; i < 95; i++){
            ascii[i] = (byte) (0x20+i);
        }
        ascii[95] = 0x0A;
        rv = BytesUtil.byteMerger(rv, ascii);
        char[] tabs = new char[116];
        for(int i = 0; i < 116; i++){
            tabs[i] = (char) (0x2500 + i);
        }
        String test = new String(tabs);
        try {
            rv = BytesUtil.byteMerger(rv, test.getBytes("gb18030"));
            rv = BytesUtil.byteMerger(rv, new byte[]{0x0A});
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(BluetoothUtil.isBlueToothPrinter){
            BluetoothUtil.sendData(rv);
        }else{
            SunmiPrintHelper.getInstance().sendRawData(rv);
        }
    }

   /* public void printText() {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {

                try {
                    for (int p=0; p<2;p++){
                        mIPosPrinterService.printSpecifiedTypeText("RECIBO "+ NumeracionInicialC+"\n", "ST", 48, callback);
                        mIPosPrinterService.printSpecifiedTypeText("NRefCK:"+IdCheque+" \n", "ST", 32, callback);
                        mIPosPrinterService.printBlankLines(1, 8, callback);
                        mIPosPrinterService.printSpecifiedTypeText("Fecha:"+fecha.getText().toString()+" \n", "ST", 32, callback);
                        mIPosPrinterService.printBlankLines(1, 8, callback);
                        mIPosPrinterService.printSpecifiedTypeText("Vendedor:"+vendedor.getText().toString()+"\n", "ST", 32, callback);
                        mIPosPrinterService.printBlankLines(1, 8, callback);
                        mIPosPrinterService.printSpecifiedTypeText("Cliente: "+BuscadorClienteC.getText().toString()+" \n", "ST", 32, callback);
                        mIPosPrinterService.printBlankLines(1, 8, callback);
                        mIPosPrinterService.printSpecifiedTypeText("Zona: "+zona.getText().toString()+" \n", "ST", 32, callback);
                        mIPosPrinterService.printBlankLines(1, 8, callback);
                        mIPosPrinterService.printSpecifiedTypeText("_____________________________________________\n", "ST", 16, callback);
                        mIPosPrinterService.printBlankLines(1, 16, callback);
                        mIPosPrinterService.printBlankLines(1, 16, callback);
                        mIPosPrinterService.printSpecifiedTypeText("No.Cheque:"+NCheque.getText().toString()+"\n", "ST", 32, callback);
                        mIPosPrinterService.PrintSpecFormatText("Beneficiario"+"\n", "ST", 32, 1, callback);
                        mIPosPrinterService.PrintSpecFormatText(Beneficiario.getText().toString()+"\n", "ST", 32, 1, callback);
                        mIPosPrinterService.printBlankLines(1, 16, callback);
                        mIPosPrinterService.PrintSpecFormatText("Banco "+Bancos.getSelectedItem().toString()+"\n", "ST", 32, 1, callback);
                        mIPosPrinterService.printSpecifiedTypeText("Monto Cheque: C$"+MCheque.getText().toString()+"\n", "ST", 32, callback);
                        mIPosPrinterService.printSpecifiedTypeText("********************************", "ST", 24, callback);
                        mIPosPrinterService.setPrinterPrintAlignment(0,callback);
                        mIPosPrinterService.printBlankLines(1, 16, callback);
                        mIPosPrinterService.printBlankLines(1, 16, callback);
                        mIPosPrinterService.printSpecifiedTypeText("Nota", "ST", 24, callback);
                        mIPosPrinterService.printSpecifiedTypeText(ObservacionesC.getText().toString(), "ST", 24, callback);

                        mIPosPrinterService.printSpecifiedTypeText("____________________________\n\n", "ST", 24, callback);
                        mIPosPrinterService.printSpecifiedTypeText("Recibe Conforme" + " " + "\n\n\n_______________________\n\n\n", "ST", 24, callback);
                        mIPosPrinterService.printSpecifiedTypeText("Entragado Conforme" + " " + "\n\n\n______________________", "ST", 24, callback);

                        mIPosPrinterService.printerPerformPrint(32, callback);
                        mIPosPrinterService.setPrinterPrintAlignment(0, callback);
                        mIPosPrinterService.printBlankLines(1, 16, callback);
                        mIPosPrinterService.printSpecifiedTypeText("**********END***********\n\n", "ST", 32, callback);
                        mIPosPrinterService.printerPerformPrint(160,  callback);
                    }

                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });
    }


   */
    /**
     * Funciones de la imprsora
     */



    // Funciones del modulo de Cheques
    public void  LimpiarCampos(){
        BuscadorClienteC.setText("");
        NCheque.setText("");
        MCheque.setText("");
        Beneficiario.setText("");
        ObservacionesC.setText("");
        NRC.setText("NReferencia:");
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

    public void NReferencia(){

        DBConnection dbConnection=new DBConnection();
        dbConnection.conectar();
        try {
            Statement st = dbConnection.getConnection().createStatement();
            ResultSet rs = st.executeQuery("select top 1 NumeracionInicial,Estado,idVendedor from Talonarios where idVendedor = ' "+idVendedor+" ' and Estado= 'Pendiente' order by idTalonario ");
            while (rs.next()) {
                NumeracionInicialC = rs.getInt("NumeracionInicial");
                numeracionC = NumeracionInicialC;
                System.out.println("==============> Ultimo Registro NumeroInicial :" + numeracionC);
                NRC.setText("No.Recibo:"+numeracionC);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void IdCheque(){
        DBConnection dbConnection=new DBConnection();
        dbConnection.conectar();
        try {
            Statement st2 = dbConnection.getConnection().createStatement();
            ResultSet rs2 = st2.executeQuery("\n" +
                    "select top 1 idCheque from Cheque  order by idCheque desc");
            while (rs2.next()) {
                 IdCheque = rs2.getInt("idCheque");

                System.out.println("==============> Ultimo Registro IdCheque :" + IdCheque);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public  void AgregarChequesSQLSEVER() throws SQLException {
        DBConnection dbConnection = new DBConnection();
        dbConnection.conectar();

        try {
            dbConnection.getConnection().setAutoCommit(false);
            PreparedStatement pst= dbConnection.getConnection().prepareStatement("exec sp_insertCheque ?,?,?,?,?,?,?,?");
            pst.setInt(1, Integer.parseInt(idBanco.getText().toString()));
            pst.setString(2,NCheque.getText().toString());
            pst.setString(3, Beneficiario.getText().toString());
            pst.setFloat(4, Float.parseFloat(MCheque.getText().toString()));
            pst.setInt(5, IdTalonario);
            pst.setInt(6, Integer.parseInt(IdCliente.getText().toString()));
            pst.setString(7,vendedor.getText().toString());
            pst.setString(8,ObservacionesC.getText().toString());
            pst.executeUpdate();

        }catch (SQLException e){
            dbConnection.getConnection().rollback();
            System.out.println("ERROR: ======> "+e);
            Toast.makeText(this," No Registrado en SQLServer",Toast.LENGTH_LONG).show();
        }finally {

            dbConnection.getConnection().setAutoCommit(true);
        }

    }
}