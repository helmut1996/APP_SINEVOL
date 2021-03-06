package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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
import com.google.android.material.snackbar.Snackbar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class
MainFacturaList extends AppCompatActivity {

    TextView textV_Codigo,textV_Cliente,textV_zona,textV_credito_disponible, textV_total,textIdcliente,textIdvendedor,tvtotalproducto;
    Spinner T_factura,T_ventas;
    ListView lista_factura;
    LinearLayout cuerpo;
    ArrayList<String>listainformacion;
    ArrayList<ProductosAdd>listaproducto;
    ///////////////////////////////////////
    public static String nombre ="HOLA MUNDO";
   public static int cantidadProducto, idProd ;
   public static double precioProducto;
   public static String nombreImagen, TotalFact, valor,idInventario;
///////////////////variables Dialog detalle producto/////////////////////////////////////
    conexionSQLiteHelper conn;
    private static final String TAG ="MainFacturaList";
    double TotalComision= 0;
/////////////////////////////Variables por parametros////////////////////////////////////////
public static String NombreCliente;
public static String CodigoCliente;
public static String ZonaCliente;
public static String IDCliente;
public static int IDVendedor;

    MainFactura id = new MainFactura();

    MainListaproducto RecargarDatos = new MainListaproducto();
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
        textIdcliente= findViewById(R.id.textV_idcliente);
        textIdvendedor= findViewById(R.id.textV_idvendedor);
        cuerpo=findViewById(R.id.cuerpo);

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
            NombreCliente=(extra.getString("NombreCliente"));
            CodigoCliente=(extra.getString("CodigoCliente"));
            ZonaCliente=(extra.getString("ZonaCliente"));
          IDCliente=(extra.getString("IdCliente"));
          IDVendedor=(extra.getInt("IdVendedor"));


            textV_Cliente.setText(NombreCliente);
            textV_Codigo.setText(CodigoCliente);
            textV_zona.setText(ZonaCliente);
            textIdcliente.setText(IDCliente);
            textIdvendedor.setText(String.valueOf(id.id));

            System.out.println("----> NombreCliente activity preFactura: "+NombreCliente);
            System.out.println("----> IDCliente activity preFactura: "+IDCliente);
            System.out.println("----> IDVemdedor activity preFactura: "+IDVendedor);
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
                        conexionSQLiteHelper conn= new conexionSQLiteHelper(MainFacturaList.this,"bd_productos",null,1);
                        SQLiteDatabase db= conn.getWritableDatabase();
                        AlertDialog.Builder alerta = new AlertDialog.Builder(MainFacturaList.this);
                        alerta.setMessage("Quieres Guardar")
                                .setCancelable(false)
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Cursor verificar_prefactura=db.rawQuery("select count(*) as cantidad from producto;", null);

                                        if (verificar_prefactura.moveToFirst()) {
                                            if (verificar_prefactura.getInt(verificar_prefactura.getColumnIndex("cantidad")) == 0) {
                                                Snackbar snackbar = Snackbar.make(cuerpo, "No puedes Guardar Prefactura Vacia", Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                                return;
                                            }
                                        }else{


                                            try {

                                                AgregarDatosSQLSEVER();
                                            }
                                            catch (SQLException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }


                                             borrardatosTabla();
                                          Toast.makeText(getApplicationContext(),"Guardar",Toast.LENGTH_SHORT).show();
                                            Intent refresh = new Intent(getApplicationContext(), MainMenu.class);
                                            refresh.putExtra("IdVendedor",IDVendedor);
                                            startActivity(refresh);

                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alertDialog= alerta.create();
                        alertDialog.setTitle("Guardar Prefactura");
                        alertDialog.show();

                        break;

                    case R.id.Mbtn_addProducto:
                        Intent intent2 = new Intent(getApplicationContext(),MainListaproducto.class);
                        startActivity(intent2);
                        break;
                    case R.id.Mbtn_Home:


                        AlertDialog.Builder alerta2 = new AlertDialog.Builder(MainFacturaList.this);
                        alerta2.setMessage("Regresar al menu principal")
                                .setCancelable(false)
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent3 = new Intent(getApplicationContext(),MainMenu.class);
                                        intent3.putExtra("IdVendedor",IDVendedor);
                                        startActivity(intent3);
                                        borrardatosTabla();
                                        // finish();

                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog titulo= alerta2.create();
                        titulo.setTitle("Advertencia");
                        titulo.show();

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

            listainformacion.add(listaproducto.get(i).getNombreproduc()+"\n \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t|\t\t\t\t\t\t\t "+listaproducto.get(i).getCantidad()+"\n C$"+listaproducto.get(i).getPrecios());
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

            textV_total.setText(TotalFact );
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

    public  void AgregarDatosSQLSEVER() throws SQLException {
        DBConnection dbConnection = new DBConnection();
        dbConnection.conectar();

        try {
            dbConnection.getConnection().setAutoCommit(false);
            PreparedStatement pst= dbConnection.getConnection().prepareStatement("exec sp_insertPrefacturas ?,?,?,?,?,?,?");
            pst.setInt(1, Integer.parseInt(textIdcliente.getText().toString()));
            pst.setInt(2, Integer.parseInt(textIdvendedor.getText().toString()));
            pst.setString(3, T_factura.getSelectedItem().toString()
            );
            pst.setString(4,T_ventas.getSelectedItem().toString());
            pst.setFloat(5, Float.parseFloat(textV_total.getText().toString()));
            pst.setDouble(6,TotalComision);
            pst.setString(7,textV_Cliente.getText().toString());
            pst.executeUpdate();

            Statement st= dbConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("select top 1 idPrefactura from Prefacturas order by idPrefactura desc");
             while (rs.next()){
                 valor  = rs.getString("idPrefactura");
                 System.out.println("==============> Ultimo Registro:"+valor);

             }

            for (int i=0; i<listaproducto.size();i++){
                PreparedStatement pst2 = dbConnection.getConnection().prepareStatement( "exec sp_insertDetallePrefacturas   ?,?,?,?,?,?,?");
                pst2.setInt(1, Integer.parseInt(valor));
                pst2.setInt(2, listaproducto.get(i).getId_producto());//idInventario
                pst2.setDouble(3, listaproducto.get(i).getPrecios());//precio cordobas
                pst2.setDouble(4,0.0);//precio Dolar
                pst2.setFloat(5,listaproducto.get(i).getCantidad());// cantidad
                pst2.setDouble(6,3.00);//PorcComision
                pst2.setString(7, listaproducto.get(i).getTipoprecio());//tipoPrecio
                pst2.executeUpdate();
            }


        }catch (SQLException e){
            dbConnection.getConnection().rollback();
            System.out.println("ERROR: ======> "+e);
            Toast.makeText(this," No Registrado en SQLServer",Toast.LENGTH_LONG).show();
        }finally {

            dbConnection.getConnection().setAutoCommit(true);
        }

    }
}