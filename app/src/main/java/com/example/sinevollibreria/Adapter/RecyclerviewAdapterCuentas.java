package com.example.sinevollibreria.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sinevollibreria.R;
import com.example.sinevollibreria.modelos.ModelItemCuentas;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewAdapterCuentas extends RecyclerView.Adapter<RecyclerviewAdapterCuentas.ViewHolder> {

    Context context;
    List<ModelItemCuentas> listCuentas;

    public RecyclerviewAdapterCuentas(Context context,List<ModelItemCuentas>listCuentas) {
        this.context = context;
        this.listCuentas= listCuentas;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerviewAdapterCuentas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.estado_cuentas_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewAdapterCuentas.ViewHolder holder, int position) {

        if (listCuentas != null && listCuentas.size()>0){
            ModelItemCuentas cuentas= listCuentas.get(position);
            holder.cliente.setText(cuentas.getNombreCliente());
            holder.telefono.setText(String.valueOf(cuentas.getTelefonoCliente()));
            holder.direccion.setText(cuentas.getDireccionCliente());
            holder.fecha.setText(cuentas.getFechaCliente());
            holder.descripcion.setText(cuentas.getDescripcionCliente());
            holder.salida.setText(String.valueOf(cuentas.getSalidaCliente()));
            holder.entrada.setText(String.valueOf(cuentas.getEntradaCliente()));
            holder.total.setText(String.valueOf(cuentas.getTotalCliente()));
            holder.TC.setText(cuentas.getCredito());

        }else{
            return;
        }
    }

    @Override
    public int getItemCount() {
        return listCuentas.size();
    }


    public void filterList(ArrayList<ModelItemCuentas> filteredlist) {
        listCuentas=filteredlist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cliente,telefono,direccion;
        TextView fecha,descripcion,salida,entrada,total,TC;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cliente=itemView.findViewById(R.id.tv_nombrecliente);
            telefono=itemView.findViewById(R.id.tv_telefono);
            direccion=itemView.findViewById(R.id.tv_descripcion);
            fecha=itemView.findViewById(R.id.tv_fecha);
            descripcion=itemView.findViewById(R.id.tv_descripcion);
            salida=itemView.findViewById(R.id.tv_salida);
            entrada=itemView.findViewById(R.id.tv_entrada);
            total=itemView.findViewById(R.id.tv_total);
            TC= itemView.findViewById(R.id.tv_compra);
        }
    }

}
