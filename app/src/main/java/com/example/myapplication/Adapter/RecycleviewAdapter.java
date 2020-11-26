package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.modelos.ClasslistItemC;

import java.util.List;

public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.RecyclerHolder> {

    List<ClasslistItemC>items;

    public RecycleviewAdapter(List<ClasslistItemC> items) {
        this.items = items;
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
        holder.direccion.setText(itemC.getDireccion());
        holder.credito.setText(String.valueOf(itemC.getCredito()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder{
        TextView codigo,nombre,zona,direccion,credito;

        public RecyclerHolder(@NonNull View itemView ){

            super(itemView);
            codigo=itemView.findViewById(R.id.CodigoCliente);
            nombre=itemView.findViewById(R.id.nombreCliente);
            zona=itemView.findViewById(R.id.cliente_zona);
            direccion=itemView.findViewById(R.id.cliente_direccion);
            credito=itemView.findViewById(R.id.cliente_credito);
        }
    }
}
