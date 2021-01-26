package com.example.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainFactura;
import com.example.myapplication.MainListaproducto;
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
        holder.tvinfo1.setText(listaProducto.get(position).getInfo1());
        holder.tvinfo2.setText(listaProducto.get(position).getInfo2());
        holder.tvinfo3.setText(listaProducto.get(position).getInfo3());
        holder.tvinfo4.setText(listaProducto.get(position).getInfo4());
        holder.tvinfo5.setText(listaProducto.get(position).getInfo5());
        holder.tvimagen.setText(listaProducto.get(position).getImg());
        holder.tvcantidad.setText(String.valueOf(listaProducto.get(position).getStock()));
        holder.tvidproducto.setText(String.valueOf(listaProducto.get(position).getIdproducto()));
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
        TextView tvnombreP,tvunidadM,tvprecio,tvproducto,tvinfo1,tvinfo2,tvinfo3,tvinfo4,tvinfo5,tvcantidad,tvimagen,tvidproducto;

        public ViewHolderProducto(@NonNull View itemView) {
            super(itemView);

            Context context = itemView.getContext();
            tvnombreP= itemView.findViewById(R.id.Nombreproducto);
            tvunidadM= itemView.findViewById(R.id.unidadmedidaP);
            tvprecio= itemView.findViewById(R.id.Preciopeucto);
            tvproducto = itemView.findViewById(R.id.txtProducto);
            tvinfo1=itemView.findViewById(R.id.info1);
            tvinfo2=itemView.findViewById(R.id.info2);
            tvinfo3=itemView.findViewById(R.id.info3);
            tvinfo4=itemView.findViewById(R.id.info4);
            tvinfo5=itemView.findViewById(R.id.info5);
            tvimagen=itemView.findViewById(R.id.imagenProducto);
            tvcantidad=itemView.findViewById(R.id.StockCantidad);
            tvidproducto=itemView.findViewById(R.id.tvidproducto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainListaproducto datos = new MainListaproducto();
                    MainFactura datos2= new MainFactura();
                   Intent intent=new Intent(context,MainProductosCliente.class);
                   intent.putExtra("NombreCliente",datos.nombrecliente);
                    intent.putExtra("CodigoCliente",datos.codigocliente);
                    intent.putExtra("ZonaCliente",datos.zonacliente);
                    intent.putExtra("IdCliente",datos.idcliente);
                    intent.putExtra("IdVendedor",datos2.id);


                    intent.putExtra("NombreP",tvproducto.getText());
                    intent.putExtra("UnidadMed",tvunidadM.getText());
                    intent.putExtra("info1",tvinfo1.getText());
                    intent.putExtra("info2",tvinfo2.getText());
                    intent.putExtra("info3",tvinfo3.getText());
                    intent.putExtra("info4",tvinfo4.getText());
                    intent.putExtra("info5",tvinfo5.getText());
                    intent.putExtra("imagenproducto",tvimagen.getText());
                    intent.putExtra("stock",tvcantidad.getText());
                    intent.putExtra("idproducto",tvidproducto.getText());

                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });

        }
    }
}
