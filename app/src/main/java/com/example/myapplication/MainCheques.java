package com.example.myapplication;

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
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.Layout;
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

import com.example.myapplication.ConexionBD.DBConnection;
import com.example.myapplication.Utils.HandlerUtils;
import com.iposprinter.iposprinterservice.IPosPrinterCallback;
import com.iposprinter.iposprinterservice.IPosPrinterService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainCheques extends AppCompatActivity  {
TextView zona,vendedor,fecha,NombreCliente,idBanco,IdCliente,NRC;
EditText NCheque,MCheque,Beneficiario,ObservacionesC;
Spinner Bancos;
AutoCompleteTextView BuscadorClienteC;
ImageButton imprimirC;

String nombreVendedor;
int idVendedor,IdTalonario,NumeracionInicialC,numeracionC,IdCheque ;




///////////////////////Impresora//////////////////////////////////

    private static final String TAG = "MainCheques";
    /* Demo 版本号*/
    private static final String VERSION = "V1.1.0";

    private IPosPrinterService mIPosPrinterService;
    private IPosPrinterCallback callback = null;
    private Random random = new Random();
    private HandlerUtils.MyHandler handler;


    /*Definir el estado de la impresora*/
    private final int PRINTER_NORMAL = 0;
    private final int PRINTER_PAPERLESS = 1;
    private final int PRINTER_THP_HIGH_TEMPERATURE = 2;
    private final int PRINTER_MOTOR_HIGH_TEMPERATURE = 3;
    private final int PRINTER_IS_BUSY = 4;
    private final int PRINTER_ERROR_UNKNOWN = 5;
    /*El estado actual de la impresora*/
    private int printerStatus = 0;

    private final String PRINTER_NORMAL_ACTION = "com.iposprinter.iposprinterservice.NORMAL_ACTION";
    private final String PRINTER_PAPERLESS_ACTION = "com.iposprinter.iposprinterservice.PAPERLESS_ACTION";
    private final String PRINTER_PAPEREXISTS_ACTION = "com.iposprinter.iposprinterservice.PAPEREXISTS_ACTION";
    private final String PRINTER_THP_HIGHTEMP_ACTION = "com.iposprinter.iposprinterservice.THP_HIGHTEMP_ACTION";
    private final String PRINTER_THP_NORMALTEMP_ACTION = "com.iposprinter.iposprinterservice.THP_NORMALTEMP_ACTION";
    private final String PRINTER_MOTOR_HIGHTEMP_ACTION = "com.iposprinter.iposprinterservice.MOTOR_HIGHTEMP_ACTION";
    private final String PRINTER_BUSY_ACTION = "com.iposprinter.iposprinterservice.BUSY_ACTION";
    private final String PRINTER_CURRENT_TASK_PRINT_COMPLETE_ACTION = "com.iposprinter.iposprinterservice.CURRENT_TASK_PRINT_COMPLETE_ACTION";

    /*Mensaje*/
    private final int MSG_TEST = 1;
    private final int MSG_IS_NORMAL = 2;
    private final int MSG_IS_BUSY = 3;
    private final int MSG_PAPER_LESS = 4;
    private final int MSG_PAPER_EXISTS = 5;
    private final int MSG_THP_HIGH_TEMP = 6;
    private final int MSG_THP_TEMP_NORMAL = 7;
    private final int MSG_MOTOR_HIGH_TEMP = 8;
    private final int MSG_MOTOR_HIGH_TEMP_INIT_PRINTER = 9;
    private final int MSG_CURRENT_TASK_PRINT_COMPLETE = 10;

    /*El tipo de imprecion circular*/
    private final int MULTI_THREAD_LOOP_PRINT = 1;
    private final int INPUT_CONTENT_LOOP_PRINT = 2;
    private final int DEMO_LOOP_PRINT = 3;
    private final int PRINT_DRIVER_ERROR_TEST = 4;
    private final int DEFAULT_LOOP_PRINT = 0;

    // Ciclo a través de la broca de la bandera
    private int loopPrintFlag = DEFAULT_LOOP_PRINT;
    private byte loopContent = 0x00;
    private int printDriverTestCount = 0;


    private final HandlerUtils.IHandlerIntent iHandlerIntent = new HandlerUtils.IHandlerIntent() {
        @Override
        public void handlerIntent(Message msg) {
            switch (msg.what) {
                case MSG_TEST:
                    break;
                case MSG_IS_NORMAL:
                    if (getPrinterStatus() == PRINTER_NORMAL) {
                        loopPrint(loopPrintFlag);
                    }
                    break;
                case MSG_IS_BUSY:
                    Toast.makeText(MainCheques.this, R.string.printer_is_working, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_LESS:
                    loopPrintFlag = DEFAULT_LOOP_PRINT;
                    Toast.makeText(MainCheques.this, R.string.out_of_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_EXISTS:
                    Toast.makeText(MainCheques.this, R.string.exists_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_THP_HIGH_TEMP:
                    Toast.makeText(MainCheques.this, R.string.printer_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_MOTOR_HIGH_TEMP:
                    loopPrintFlag = DEFAULT_LOOP_PRINT;
                    Toast.makeText(MainCheques.this, R.string.motor_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(MSG_MOTOR_HIGH_TEMP_INIT_PRINTER, 180000);  //马达高温报警，等待3分钟后复位打印机
                    break;
                case MSG_MOTOR_HIGH_TEMP_INIT_PRINTER:
                    printerInit();
                    break;
                case MSG_CURRENT_TASK_PRINT_COMPLETE:
                    Toast.makeText(MainCheques.this, R.string.printer_current_task_print_complete, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    private void setButtonEnable(boolean flag) {
        imprimirC.setEnabled(flag);
    }


    private BroadcastReceiver IPosPrinterStatusListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                Log.d(TAG, "IPosPrinterStatusListener onReceive action = null");
                return;
            }
            Log.d(TAG, "IPosPrinterStatusListener action = " + action);
           /* if(action.equals(PRINTER_NORMAL_ACTION))
            {
                handler.sendEmptyMessageDelayed(MSG_IS_NORMAL,0);
            }
            else if (action.equals(PRINTER_PAPERLESS_ACTION))
            {
                handler.sendEmptyMessageDelayed(MSG_PAPER_LESS,0);
            }
            else if (action.equals(PRINTER_BUSY_ACTION))
            {
                handler.sendEmptyMessageDelayed(MSG_IS_BUSY,0);
            }
            else if (action.equals(PRINTER_PAPEREXISTS_ACTION))
            {
                handler.sendEmptyMessageDelayed(MSG_PAPER_EXISTS,0);
            }
            else if (action.equals(PRINTER_THP_HIGHTEMP_ACTION))
            {
                handler.sendEmptyMessageDelayed(MSG_THP_HIGH_TEMP,0);
            }
            else if (action.equals(PRINTER_THP_NORMALTEMP_ACTION))
            {
                handler.sendEmptyMessageDelayed(MSG_THP_TEMP_NORMAL,0);
            }
            else if (action.equals(PRINTER_MOTOR_HIGHTEMP_ACTION))  //此时当前任务会继续打印，完成当前任务后，请等待2分钟以上时间，继续下一个打印任务
            {
                handler.sendEmptyMessageDelayed(MSG_MOTOR_HIGH_TEMP,0);
            }
            else if(action.equals(PRINTER_CURRENT_TASK_PRINT_COMPLETE_ACTION))
            {
                handler.sendEmptyMessageDelayed(MSG_CURRENT_TASK_PRINT_COMPLETE,0);
            }
            else
            {
                handler.sendEmptyMessageDelayed(MSG_TEST,0);
            }*/
        }
    };

    private ServiceConnection connectService = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIPosPrinterService = IPosPrinterService.Stub.asInterface(service);
            setButtonEnable(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIPosPrinterService = null;
        }
    };
    ///////////////////////Impresora//////////////////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cheques);

        getSupportActionBar().setTitle("Cheques");

        callback = new IPosPrinterCallback.Stub() {

            @Override
            public void onRunResult(final boolean isSuccess) throws RemoteException {
                Log.i(TAG, "result:" + isSuccess + "\n");
            }

            @Override
            public void onReturnString(final String value) throws RemoteException {
                Log.i(TAG, "result:" + value + "\n");
            }
        };

        Intent intent = new Intent();
        intent.setPackage("com.iposprinter.iposprinterservice");
        intent.setAction("com.iposprinter.iposprinterservice.IPosPrintService");
        bindService(intent, connectService, Context.BIND_AUTO_CREATE);


        IntentFilter printerStatusFilter = new IntentFilter();
        printerStatusFilter.addAction(PRINTER_NORMAL_ACTION);
        printerStatusFilter.addAction(PRINTER_PAPERLESS_ACTION);
        printerStatusFilter.addAction(PRINTER_PAPEREXISTS_ACTION);
        printerStatusFilter.addAction(PRINTER_THP_HIGHTEMP_ACTION);
        printerStatusFilter.addAction(PRINTER_THP_NORMALTEMP_ACTION);
        printerStatusFilter.addAction(PRINTER_MOTOR_HIGHTEMP_ACTION);
        printerStatusFilter.addAction(PRINTER_BUSY_ACTION);

        registerReceiver(IPosPrinterStatusListener, printerStatusFilter);



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
                    IdCheque();
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
                    if (getPrinterStatus() == PRINTER_NORMAL) {
                        try {
                            AgregarChequesSQLSEVER();
                            printText();
                            LimpiarCampos();
                            Toast.makeText(getApplicationContext(),"Cheque Guardado",Toast.LENGTH_LONG).show();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }



                    }


                }

            }

        });
    }

    /*
     *Funciones de la imprsora
     * */

    public int getPrinterStatus() {

        Log.i(TAG, "***** printerStatus" + printerStatus);
        try {
            printerStatus = mIPosPrinterService.getPrinterStatus();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "#### printerStatus" + printerStatus);
        return printerStatus;
    }

    /**
     * La impresora se inicializa
     */

    public void printerInit() {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    mIPosPrinterService.printerInit(callback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loopPrint(int flag) {
        switch (flag) {

        }
    }

    public void printText() {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {

                try {
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
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });
    }


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
                    "select top 1 idCheque,Estado from Cheque where Estado = 'Pendiente'  order by idCheque");
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