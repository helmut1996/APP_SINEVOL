package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ConexionBD.DBConnection;
import com.example.myapplication.SQLite.conexionSQLiteHelper;
import com.example.myapplication.SQLite.ulilidades.utilidadesFact;
import com.example.myapplication.Utils.ButtonDelayUtils;
import com.example.myapplication.Utils.BytesUtil;
import com.example.myapplication.Utils.HandlerUtils;
import com.iposprinter.iposprinterservice.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import static com.example.myapplication.MemInfo.bitmapRecycle;
import static com.example.myapplication.Utils.PrintContentsExamples.Baidu;
import static com.example.myapplication.Utils.PrintContentsExamples.Elemo;
import static com.example.myapplication.Utils.PrintContentsExamples.Text;
import static com.example.myapplication.Utils.PrintContentsExamples.customCHR;
import static com.example.myapplication.Utils.PrintContentsExamples.customCHZ1;

public class MainRecibo extends AppCompatActivity {
    AutoCompleteTextView buscadorCliente;
    EditText abono, descuento;
    TextView saldo, numresf, fecha, zona, vendedor, tvIdclienyte;
    ImageButton Guardar, imprimir;
    EditText onservacion;
    Spinner BuscadorFactura;
    public static int id;
    public static String nombreVendedor, idcliente;

    String[] clientes = new String[]{
            "cleinte1", "Helmut", "brian", "jefrry"
    };

    ///////////////////////Impresora//////////////////////////////////

    private static final String TAG                 = "MainRecibo";
    /* Demo 版本号*/
    private static final String VERSION        = "V1.1.0";


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

    private final String  PRINTER_NORMAL_ACTION = "com.iposprinter.iposprinterservice.NORMAL_ACTION";
    private final String  PRINTER_PAPERLESS_ACTION = "com.iposprinter.iposprinterservice.PAPERLESS_ACTION";
    private final String  PRINTER_PAPEREXISTS_ACTION = "com.iposprinter.iposprinterservice.PAPEREXISTS_ACTION";
    private final String  PRINTER_THP_HIGHTEMP_ACTION = "com.iposprinter.iposprinterservice.THP_HIGHTEMP_ACTION";
    private final String  PRINTER_THP_NORMALTEMP_ACTION = "com.iposprinter.iposprinterservice.THP_NORMALTEMP_ACTION";
    private final String  PRINTER_MOTOR_HIGHTEMP_ACTION = "com.iposprinter.iposprinterservice.MOTOR_HIGHTEMP_ACTION";
    private final String  PRINTER_BUSY_ACTION = "com.iposprinter.iposprinterservice.BUSY_ACTION";
    private final String  PRINTER_CURRENT_TASK_PRINT_COMPLETE_ACTION = "com.iposprinter.iposprinterservice.CURRENT_TASK_PRINT_COMPLETE_ACTION";

    /*定义消息*/
    private final int MSG_TEST                               = 1;
    private final int MSG_IS_NORMAL                          = 2;
    private final int MSG_IS_BUSY                            = 3;
    private final int MSG_PAPER_LESS                         = 4;
    private final int MSG_PAPER_EXISTS                       = 5;
    private final int MSG_THP_HIGH_TEMP                      = 6;
    private final int MSG_THP_TEMP_NORMAL                    = 7;
    private final int MSG_MOTOR_HIGH_TEMP                    = 8;
    private final int MSG_MOTOR_HIGH_TEMP_INIT_PRINTER       = 9;
    private final int MSG_CURRENT_TASK_PRINT_COMPLETE     = 10;

    /*循环打印类型*/
    private final int  MULTI_THREAD_LOOP_PRINT  = 1;
    private final int  INPUT_CONTENT_LOOP_PRINT = 2;
    private final int  DEMO_LOOP_PRINT          = 3;
    private final int  PRINT_DRIVER_ERROR_TEST  = 4;
    private final int  DEFAULT_LOOP_PRINT       = 0;

    //循环打印标志位
    private       int  loopPrintFlag            = DEFAULT_LOOP_PRINT;
    private       byte loopContent              = 0x00;
    private       int  printDriverTestCount     = 0;


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
                    Toast.makeText(MainRecibo.this, R.string.printer_is_working, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_LESS:
                    loopPrintFlag = DEFAULT_LOOP_PRINT;
                    Toast.makeText(MainRecibo.this, R.string.out_of_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_EXISTS:
                    Toast.makeText(MainRecibo.this, R.string.exists_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_THP_HIGH_TEMP:
                    Toast.makeText(MainRecibo.this, R.string.printer_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_MOTOR_HIGH_TEMP:
                    loopPrintFlag = DEFAULT_LOOP_PRINT;
                    Toast.makeText(MainRecibo.this, R.string.motor_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(MSG_MOTOR_HIGH_TEMP_INIT_PRINTER, 180000);  //马达高温报警，等待3分钟后复位打印机
                    break;
                case MSG_MOTOR_HIGH_TEMP_INIT_PRINTER:
                    printerInit();
                    break;
                case MSG_CURRENT_TASK_PRINT_COMPLETE:
                    Toast.makeText(MainRecibo.this, R.string.printer_current_task_print_complete, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    private void setButtonEnable(boolean flag){
        imprimir.setEnabled(flag);
    }


    private  BroadcastReceiver IPosPrinterStatusListener = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            if(action == null)
            {
                Log.d(TAG,"IPosPrinterStatusListener onReceive action = null");
                return;
            }
            Log.d(TAG,"IPosPrinterStatusListener action = "+action);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recibo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recibos");



        callback = new IPosPrinterCallback.Stub() {

            @Override
            public void onRunResult(final boolean isSuccess) throws RemoteException {
                Log.i(TAG,"result:" + isSuccess + "\n");
            }

            @Override
            public void onReturnString(final String value) throws RemoteException {
                Log.i(TAG,"result:" + value + "\n");
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

        registerReceiver(IPosPrinterStatusListener,printerStatusFilter);
        
        
        tvIdclienyte=findViewById(R.id.tv_IdclienteRecibo);
        fecha = findViewById(R.id.tvr_fecha);
        zona = findViewById(R.id.tvr_zona);
        vendedor = findViewById(R.id.tv_NombreVendedor);
        buscadorCliente = findViewById(R.id.editBuscarCliente);
        BuscadorFactura = findViewById(R.id.buscadorFacturaCliente);
        abono = findViewById(R.id.editAbono);
        descuento = findViewById(R.id.editDescuento);
        saldo = findViewById(R.id.tvr_saldo);
        numresf = findViewById(R.id.tv_NReferencia);
        onservacion = findViewById(R.id.editObservacion);
        Guardar = findViewById(R.id.btnGuardarRecibo);
        imprimir = findViewById(R.id.btnImprimirRecibo);


        id = getIntent().getIntExtra("Id", 0);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            nombreVendedor = extra.getString("NombreVendedor");
            System.out.println("----> NombreVendedor: " + nombreVendedor);
            vendedor.setText(extra.getString("NombreVendedor"));
        }


        //ArrayAdapter<String> adaptercliente = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clientes);
        buscadorCliente.setAdapter(Clientes());

        System.out.println("*******Cliente :"+buscadorCliente);

        buscadorCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvIdclienyte.setText(buscadorCliente.getText().toString());
            }
        });

        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.facturas_clientes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BuscadorFactura.setAdapter(Facturas());

        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (buscadorCliente.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "debes eligir el Cliente", Toast.LENGTH_SHORT).show();
                } else if (abono.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "El Abono no puede ser 0", Toast.LENGTH_LONG).show();
                } else if (descuento.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "El Descuento no puede ser 0", Toast.LENGTH_LONG).show();
                } else {
                    GuardarReciboSQLite();
                    limpiarcampos();
                    Toast.makeText(getApplicationContext(), " Recibo Guardado....", Toast.LENGTH_LONG).show();
                }

            }
        });

        imprimir.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if (getPrinterStatus() == PRINTER_NORMAL)
                 printText();
         }
     });

    }

    public int getPrinterStatus(){

        Log.i(TAG,"***** printerStatus"+printerStatus);
        try{
            printerStatus = mIPosPrinterService.getPrinterStatus();
        }catch (RemoteException e){
            e.printStackTrace();
        }
        Log.i(TAG,"#### printerStatus"+printerStatus);
        return  printerStatus;
    }

    /**
     * 打印机初始化
     */
    public void printerInit(){
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try{
                    mIPosPrinterService.printerInit(callback);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void loopPrint(int flag) {
        switch (flag)
        {

        }
    }

    public void printText() {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    mIPosPrinterService.printSpecifiedTypeText("    Hola Mundo\n", "ST", 48, callback);
                    mIPosPrinterService.printSpecifiedTypeText("   Imprimiendo prueba Recibo\n", "ST", 32, callback);
                    mIPosPrinterService.printBlankLines(1, 8, callback);
                    mIPosPrinterService.printSpecifiedTypeText("********************************", "ST", 24, callback);
                    mIPosPrinterService.setPrinterPrintAlignment(0,callback);
                    mIPosPrinterService.printBlankLines(1, 16, callback);
                    mIPosPrinterService.printSpecifiedTypeText("**********END***********\n\n", "ST", 32, callback);
                    mIPosPrinterService.printerPerformPrint(160,  callback);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public ArrayAdapter Clientes() {
        ArrayAdapter NoCoreAdapter = null;
        DBConnection sesion;
        sesion = DBConnection.getDbConnection();

        String query = "select Nombre, idCliente from Clientes where idVendedor='" + id + "' AND Estado = 'Activo' order by Nombre desc";
        try {
            Statement stm = sesion.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                data.add(rs.getString("Nombre"));
                data.add(rs.getString("idCliente"));
                //idcliente=rs.getString("idCliente");
                //idcliente = rs.getString("Nombre");
                //tvIdclienyte.setText(idcliente);

            }

            System.out.println("IDCLIENTE No.:"+idcliente);

            NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NoCoreAdapter;
    }

    public ArrayAdapter Facturas() {
        ArrayAdapter NoCoreAdapter = null;
        DBConnection sesion;
        sesion = DBConnection.getDbConnection();

        String query = "exec sp_listarCxC 1158";
        try {
            Statement stm = sesion.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(query);

            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                data.add(rs.getString("IdFactura"));

            }
            NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NoCoreAdapter;
    }

    public void limpiarcampos() {
        buscadorCliente.setText("");
        abono.setText("");
        descuento.setText("");
        onservacion.setText("");
        saldo.setText("");

    }

    public void GuardarReciboSQLite() {
        /*mandando a llamar conexion a SQLite */
        conexionSQLiteHelper conn = new conexionSQLiteHelper(this, "bd_productos", null, 1);
        /*abrir la conexion a SQLite*/
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(utilidadesFact.CAMPO_NOMBRE_CLIEBTE, buscadorCliente.getText().toString());
        values.put(utilidadesFact.CAMPO_FECHA, fecha.getText().toString());
        values.put(utilidadesFact.CAMPO_FACTURA, BuscadorFactura.getSelectedItem().toString());
        values.put(utilidadesFact.CAMPO_ABONO, abono.getText().toString());
        values.put(utilidadesFact.CAMPO_DESCUENTO, descuento.getText().toString());
        values.put(utilidadesFact.CAMPO_SALDO_RES, saldo.getText().toString());
        values.put(utilidadesFact.CAMPO_NUMERO_RESF, numresf.getText().toString());
        long idResultante = db.insert(utilidadesFact.TABLA_RECIBO, utilidadesFact.CAMPO_NOMBRE_CLIEBTE, values);
        Toast.makeText(this, "agregado: " + idResultante, Toast.LENGTH_SHORT).show();


    }


}

