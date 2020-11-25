package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myapplication.Adapter.RecycleviewAdapter;
import com.example.myapplication.ConexionBD.DBConnection;
import com.example.myapplication.modelos.ClasslistItemC;

import java.util.ArrayList;
import java.util.List;

public class MainFactura extends AppCompatActivity {

    /*EditText textBuscar;
    ListView listClientes;
    ArrayAdapter<String> adadter;
// String vendedores [] ={"Helmut Colindres","Josue Brenes","Maria Calero","Maribel Brenes","Hellen Gutierrez","Katherine Robleto1"};
*/
   // VARIABLE PARA RECYCLEVIEW
    RecyclerView recyclerViewCliente;
    RecycleviewAdapter adapterCliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_factura);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Clientes");


   // INTEGRACION DE RECICLEVIW PERO NO MUESTRA
   recyclerViewCliente= findViewById(R.id.listaClientes);
    recyclerViewCliente.setLayoutManager(new LinearLayoutManager(this));

    adapterCliente= new RecycleviewAdapter(obtenerClientes());
    recyclerViewCliente.setAdapter(adapterCliente);



     /*-  textBuscar= (EditText) findViewById(R.id.editTextBuscar);
        listClientes = (ListView)findViewById(R.id.listaClientes);

        adadter =new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Clientes));
        listClientes.setAdapter(adadter);
        textBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adadter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        listClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        OpenDialog();
                        break;
                    case 1:
                        OpenDialog();
                        break;
                    case 2:
                        OpenDialog();
                        break;
                    case 3:
                        OpenDialog();
                        break;
                    case 4:
                        OpenDialog();
                        break;
                    case 5:
                        OpenDialog();
                        break;
                }

            }
        });*/

    }

    /*public void OpenDialog(){

        ClassDialog dialog = new ClassDialog();
        dialog.show(getSupportFragmentManager(),"Ventana Emergente");
    }

*/





    public List<ClasslistItemC> obtenerClientes(){
            List<ClasslistItemC> clientes=new ArrayList<>();
            clientes.add(new ClasslistItemC("Maria Jose Grarcia","Managua","Waspan sur 2C al sur",40000,102503));
            clientes.add(new ClasslistItemC("Maria Jose Grarcia","Managua","Waspan sur 2C al sur",40000,251586));
            clientes.add(new ClasslistItemC("Maria Jose Grarcia","Managua","Waspan sur 2C al sur",40000,247812));
            clientes.add(new ClasslistItemC("Maria Jose Grarcia","Managua","Waspan sur 2C al sur",40000,12335));
            clientes.add(new ClasslistItemC("Maria Jose Grarcia","Managua","Waspan sur 2C al sur",40000,12355));

        return clientes;
    }
}
