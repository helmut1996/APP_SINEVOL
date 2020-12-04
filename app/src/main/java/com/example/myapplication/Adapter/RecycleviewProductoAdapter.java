package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainFacruraList;
import com.example.myapplication.MainProductosCliente;
import com.example.myapplication.R;
import com.example.myapplication.modelos.ModelItemsProducto;

import java.util.ArrayList;

public class RecycleviewProductoAdapter extends RecyclerView.Adapter<RecycleviewProductoAdapter.ViewHolderProducto> {

    ArrayList<ModelItemsProducto>listaProducto;

    public RecycleviewProductoAdapter(ArrayList<ModelItemsProducto> listaProducto) {
        this.listaProducto = listaProducto;
    }

    @NonNull
    @Override
    public ViewHolderProducto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemproductos,null,false);
        return new ViewHolderProducto(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProducto holder, int position) {

        holder.tvnombreP.setText(listaProducto.get(position).getNombreP());
        holder.tvunidadM.setText(listaProducto.get(position).getUnidadmedidaP());
        holder.tvprecio.setText(String.valueOf(listaProducto.get(position).getPrecioP()));
        holder.tvproducto.setText(listaProducto.get(position).getProducto());

    }

    @Override
    public int getItemCount() {
        return listaProducto.size();
    }


    public void filterListProducto(ArrayList<ModelItemsProducto> filterlistp) {
      listaProducto=filterlistp;
      notifyDataSetChanged();
    }
    public class ViewHolderProducto extends RecyclerView.ViewHolder {
        TextView tvnombreP,tvunidadM,tvprecio,tvproducto;

        public ViewHolderProducto(@NonNull View itemView) {
            super(itemView);

            Context context = itemView.getContext();
            tvnombreP= itemView.findViewById(R.id.Nombreproducto);
            tvunidadM= itemView.findViewById(R.id.unidadmedidaP);
            tvprecio= itemView.findViewById(R.id.Preciopeucto);
            tvproducto = itemView.findViewById(R.id.txtProducto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent intent=new Intent(context,MainProductosCliente.class);
                    intent.putExtra("NombreP",tvproducto.getText());
                    intent.putExtra("UnidadMed",tvunidadM.getText());
                    context.startActivity(intent);
                }
            });

        }
    }
}
