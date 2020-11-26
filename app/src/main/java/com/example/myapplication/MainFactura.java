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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.myapplication.Adapter.RecycleviewAdapter;
import com.example.myapplication.ConexionBD.DBConnection;
import com.example.myapplication.modelos.ClasslistItemC;

import java.util.ArrayList;
import java.util.List;

public class MainFactura extends AppCompatActivity  {


   // VARIABLE PARA RECYCLEVIEW
    RecyclerView recyclerViewCliente;
    RecycleviewAdapter adapterCliente;
    //SearchView textBuscar;

    List<ClasslistItemC> itemCList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_factura);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Clientes");

        initview();
        initValues();
    }


    public void initview(){
        recyclerViewCliente=findViewById(R.id.listaClientes);
       // textBuscar=findViewById(R.id.textbuscar);
    }

    public void initValues(){
        LinearLayoutManager manager= new LinearLayoutManager(this);
        recyclerViewCliente.setLayoutManager(manager);

        itemCList=getItemCList();
        adapterCliente= new RecycleviewAdapter(itemCList);
        recyclerViewCliente.setAdapter(adapterCliente);
    }

    private List<ClasslistItemC>getItemCList(){
        List<ClasslistItemC> itemslistcliente= new ArrayList<>();
        itemslistcliente.add(new ClasslistItemC("Maria Calero","Managua","multicentros las brisas 2C al sur ",40000,010101));

        itemslistcliente.add(new ClasslistItemC("Maria Calero","Managua","multicentros las brisas 2C al sur ",40000,010101));

        itemslistcliente.add(new ClasslistItemC("Maria Calero","Managua","multicentros las brisas 2C al sur ",40000,010101));

        itemslistcliente.add(new ClasslistItemC("Maria Calero","Managua","multicentros las brisas 2C al sur ",40000,010101));

        itemslistcliente.add(new ClasslistItemC("Maria Calero","Managua","multicentros las brisas 2C al sur ",40000,010101));

        itemslistcliente.add(new ClasslistItemC("Maria Calero","Managua","multicentros las brisas 2C al sur ",40000,010101));

        return itemslistcliente;
    }
    /*public void OpenDialog(){

        ClassDialog dialog = new ClassDialog();
        dialog.show(getSupportFragmentManager(),"Ventana Emergente");
    }

*/





    ///////// metodo de busqueda

}
