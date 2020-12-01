package com.example.myapplication.Adapter;

import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainListaproducto;
import com.example.myapplication.MainMenu;
import com.example.myapplication.R;
import com.example.myapplication.modelos.ClasslistItemC;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.RecyclerHolder> {

    List<ClasslistItemC>items;
    List<ClasslistItemC>originalItems;

    public RecycleviewAdapter(List<ClasslistItemC> items) {
        this.items = items;
        this.originalItems= new ArrayList<>();
        originalItems.addAll(items);


      }


    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcliente,parent,false);

        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {

        ClasslistItemC itemC= items.get(position);
        holder.codigo.setText(String.valueOf(itemC.getCodigo()));
        holder.nombre.setText(itemC.getNombre());
        holder.zona.setText(itemC.getZona());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(new Intent(holder.itemView.getContext(), MainListaproducto.class));
                holder.itemView.getContext().startActivity(intent );
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void filterList(ArrayList<ClasslistItemC> filteredlist) {
    items=filteredlist;
    notifyDataSetChanged();
    }


    public class RecyclerHolder extends RecyclerView.ViewHolder{
        TextView codigo,nombre,zona;

        public RecyclerHolder(@NonNull View itemView ){

            super(itemView);
            codigo=itemView.findViewById(R.id.CodigoCliente);
            nombre=itemView.findViewById(R.id.nombreCliente);
            zona=itemView.findViewById(R.id.cliente_zona);
        }


    }


}
