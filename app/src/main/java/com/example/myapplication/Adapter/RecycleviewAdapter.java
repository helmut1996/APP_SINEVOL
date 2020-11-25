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

public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView codigocliente,nombrecliente,zona,direccion,credito;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            codigocliente=itemView.findViewById(R.id.CodigoCliente);
            nombrecliente=itemView.findViewById(R.id.nombreCliente);
            zona=itemView.findViewById(R.id.cliente_zona);
            direccion=itemView.findViewById(R.id.cliente_direccion);
            credito=itemView.findViewById(R.id.cliente_credito);

        }
    }

    public List<ClasslistItemC>clientelists;

    public RecycleviewAdapter(List<ClasslistItemC> clientelists) {
        this.clientelists = clientelists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcliente,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.codigocliente.setText(String.valueOf(clientelists.get(position).getCodigo()));
        holder.nombrecliente.setText(clientelists.get(position).getNombre());
        holder.zona.setText(clientelists.get(position).getZona());
        holder.direccion.setText(clientelists.get(position).getDireccion());
        holder.credito.setText(String.valueOf( clientelists.get(position).getCredito()));
    }

    @Override
    public int getItemCount() {
        return clientelists.size();
    }
}
