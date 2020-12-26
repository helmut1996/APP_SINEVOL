package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication.Adapter.RecycleviewAdapter;
import com.example.myapplication.ConexionBD.DBConnection;
import com.example.myapplication.SQLite.conexionSQLiteHelper;
import com.example.myapplication.SQLite.entidades.ProductosAdd;
import com.example.myapplication.SQLite.ulilidades.utilidades;
import com.example.myapplication.modelos.ClasslistItemC;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class MainFacturaList extends AppCompatActivity {

    TextView textV_Codigo,textV_Cliente,textV_zona,textV_credito_disponible, textV_total;
    Spinner T_factura,T_ventas;
    ListView lista_factura;
    ArrayList<String>listainformacion;
    ArrayList<ProductosAdd>listaproducto;
    String CodigoCliente;
   public static String nombre ="HOLA MUNDO";
   public static int cantidadProducto, idProd ;
   public static double precioProducto;
   public static String nombreImagen, TotalFact;

    conexionSQLiteHelper conn;

    private static final String TAG ="MainFacturaList";


    ////////////
    String Estado= "Habilitado";
    String FechaExp="25/12/2020";
    double TotalComision= 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_facrura_list);

        getSupportActionBar().setTitle("Facturacion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textV_Codigo = findViewById(R.id.textViewCodigo);
        textV_Cliente = findViewById(R.id.textViewcliente);
        textV_zona = findViewById(R.id.textView_Zona);
        textV_credito_disponible = findViewById(R.id.textView_C_Disponible);
        textV_total = findViewById(R.id.textV_total);
        T_ventas = findViewById(R.id.spinner_tventas);
        T_factura = findViewById(R.id.spinner_facura);
        lista_factura=findViewById(R.id.lista_Factura);
        /////////Spinner del tipo de ventas
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.tipo_ventas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        T_ventas.setAdapter(adapter);



        ////////////spinner tipo factura
        ArrayAdapter<CharSequence> adapter1 =ArrayAdapter.createFromResource(this, R.array.tipo_moneda, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        T_factura.setAdapter(adapter1);

        textV_Cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ////////////////////////////// ListView///////////////////////////////
                //conexion de SQLite
        conn=new conexionSQLiteHelper(getApplicationContext(),"bd_productos",null,1);

              ConsultarlistaProducto();
                CalcularTotalFactura();
              ArrayAdapter adaptador= new ArrayAdapter(this, android.R.layout.simple_list_item_1,listainformacion);
              lista_factura.setAdapter(adaptador);
              adaptador.notifyDataSetChanged();
              lista_factura.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      System.out.println("Tratando de descubrir la informacion: ------> " +lista_factura.getAdapter().getItem(position));
                      Dialog_detalle_factura(position);
                      nombre = listaproducto.get(position).getNombreproduc();
                      cantidadProducto=listaproducto.get(position).getCantidad();
                      precioProducto=listaproducto.get(position).getPrecios();
                      idProd=listaproducto.get(position).getId_producto();
                      nombreImagen=listaproducto.get(position).getImagenProducto();
                  }
              });
              /////pasando los datos del cliente
        Bundle extra=getIntent().getExtras();
        if (extra != null){
            textV_Cliente.setText(extra.getString("NombreCliente"));
            textV_Codigo.setText(extra.getString("CodigoCliente"));
            textV_zona.setText(extra.getString("ZonaCliente"));
            System.out.println("----> NombreCliente activity preFactura: "+textV_Cliente);
        }
        /////pasando los datos del cliente

    CalcularTotalFactura();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
getMenuInflater().inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Mbtn_guardar:
                        AgregarDatosSQLSEVER();
                        Toast.makeText(this,"Guardar",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.Mbtn_addProducto:
                        Intent intent2 = new Intent(getApplicationContext(),MainListaproducto.class);
                        startActivity(intent2);
                        break;
                    case R.id.Mbtn_Home:
                        borrardatosTabla();
                        Intent intent3 = new Intent(getApplicationContext(),MainFactura.class);
                        startActivity(intent3);
                }

        return super.onOptionsItemSelected(item);
      }

    public void ConsultarlistaProducto() {
        SQLiteDatabase db=conn.getReadableDatabase();
        ProductosAdd productosAdd = null;
        listaproducto=new ArrayList<ProductosAdd>();
        //query SQLite
        Cursor cursor=db.rawQuery("select * from "+ utilidades.TABLA_PRODUCTO,null);
        while (cursor.moveToNext()){
            productosAdd=new ProductosAdd();
            productosAdd.setId_producto(cursor.getInt(0));
            productosAdd.setNombreproduc(cursor.getString(1));
            productosAdd.setCantidad(cursor.getInt(2));
            productosAdd.setPrecios(cursor.getDouble(3));
            productosAdd.setImagenProducto(cursor.getString(4));

            listaproducto.add(productosAdd);
        }
        obtenerLista();
    }

    public void obtenerLista() {
        listainformacion=new ArrayList<String>();
        for (int i=0; i<listaproducto.size();i++){

            listainformacion.add(listaproducto.get(i).getNombreproduc()+" -"+listaproducto.get(i).getCantidad()+listaproducto.get(i).getPrecios());
            System.out.println("MOSTRANDO LA CANTIDAD GUARDADA "+listaproducto.get(i).getCantidad());
        }

    }

    public void CalcularTotalFactura(){
        SQLiteDatabase db= conn.getReadableDatabase();
        String total="select sum(precio * cantidad ) as Total from producto";
        Cursor query =  db.rawQuery(total,null);
        if (query.moveToFirst()){
            TotalFact= query.getString(query.getColumnIndex("Total"));
            System.out.println("TOTAL DE LA FACTURA ACTUALMENTE ES : ----> "+TotalFact);

            textV_total.setText(TotalFact);
        }
        db.close();
    }

    public void borrardatosTabla(){
        SQLiteDatabase db= conn.getReadableDatabase();
        db.execSQL("delete from producto");
        db.close();
    }

   public void Dialog_detalle_factura(int position){
        ClassDialogFactura dialogFactura = new ClassDialogFactura();
        dialogFactura.show(getSupportFragmentManager(),"ventana emergente");
    }

    public  void AgregarDatosSQLSEVER(){
        String numeroFactura = "0";
        Date hoy= new Date();
        java.sql.Date dataStartSql = new java.sql.Date(hoy.getTime());
        DBConnection dbConnection = new DBConnection();
        dbConnection.conectar();
        try {
            PreparedStatement pst= dbConnection.getConnection().prepareStatement("exec sp_insertPrefact ?,?,?,?,?,?,?");
            pst.setInt(1, 4);
            pst.setInt(2,1);
            pst.setString(3,T_ventas.getSelectedItem().toString());
            pst.setString(4,T_factura.getSelectedItem().toString());
            pst.setFloat(5, Float.parseFloat(textV_total.getText().toString()));
            pst.setDouble(6,TotalComision);
            pst.setString(7,textV_Cliente.getText().toString());
            pst.executeUpdate();
          Toast.makeText(this,"Registrado SQLServer",Toast.LENGTH_LONG).show();

        }catch (SQLException e){
            System.out.println("ERROR: ======> "+e);
            Toast.makeText(this," No Registrado SQLServer",Toast.LENGTH_LONG).show();
        }
    }
}
