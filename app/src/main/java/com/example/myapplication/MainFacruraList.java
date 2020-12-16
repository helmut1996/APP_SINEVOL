package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.myapplication.SQLite.conexionSQLiteHelper;
import com.example.myapplication.SQLite.entidades.ProductosAdd;
import com.example.myapplication.SQLite.ulilidades.utilidades;

import java.util.ArrayList;

public class MainFacruraList extends AppCompatActivity {

    TextView textV_Codigo,textV_Cliente,textV_zona,textV_credito_disponible,textV_total;
    Spinner T_factura,T_ventas;
    ListView lista_factura;
    ArrayList<String>listainformacion;
    ArrayList<ProductosAdd>listaproducto;

    conexionSQLiteHelper conn;
    private static final String TAG ="MainFacturaList";

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

        ////////////////////////////// ListView///////////////////////////////
                //conexion de SQLite
        conn=new conexionSQLiteHelper(getApplicationContext(),"bd_productos",null,1);

              ConsultarlistaProducto();

              ArrayAdapter adaptador= new ArrayAdapter(this, android.R.layout.simple_list_item_1,listainformacion);
              lista_factura.setAdapter(adaptador);
        /*adapter =new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Clientes));
        lista_factura.setAdapter(adapter);


        lista_factura.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                switch (position){
                    case 0:
                        Dialog_detalle_factura();
                        break;
                    case 1:

                        Dialog_detalle_factura();
                        break;
                    case 2:
                       Dialog_detalle_factura();
                        break;
                    case 3:
                      Dialog_detalle_factura();
                        break;
                }

            }
        });*/
            }

    private void ConsultarlistaProducto() {
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
            productosAdd.setPrecios(cursor.getInt(3));

            listaproducto.add(productosAdd);
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listainformacion=new ArrayList<String>();
        for (int i=0; i<listaproducto.size();i++){
            listainformacion.add(listaproducto.get(i).getNombreproduc()+" - "+listaproducto.get(i).getCantidad()+" -   "+listaproducto.get(i).getPrecios());
        }
    }



           /* public void Dialog_detalle_factura(){


                ClassDialogFactura dialogFactura = new ClassDialogFactura();
                dialogFactura.show(getSupportFragmentManager(),"ventana emergente");
            }*/
    }
