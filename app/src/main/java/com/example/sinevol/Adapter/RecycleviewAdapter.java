package com.example.sinevol.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sinevol.MainFactura;
import com.example.sinevol.MainListaproducto;
import com.example.sinevol.R;
import com.example.sinevol.modelos.ClasslistItemC;

import java.util.ArrayList;
import java.util.List;

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
        holder.idcliente.setText(String.valueOf(itemC.getIdCliente()));

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
        TextView codigo,nombre,zona,idcliente;
        public RecyclerHolder(@NonNull View itemView ){

            super(itemView);
            Context context = itemView.getContext();
            codigo=itemView.findViewById(R.id.CodigoCliente);
            nombre=itemView.findViewById(R.id.nombreCliente);
            zona=itemView.findViewById(R.id.cliente_zona);
            idcliente=itemView.findViewById(R.id.id_cliente);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainFactura datos = new MainFactura();

                    Intent intent=new Intent(context, MainListaproducto.class);
                    intent.putExtra("Idvendedor",datos.id);
                    intent.putExtra("Nombrecliente",nombre.getText());
                    intent.putExtra("Codigocliente",codigo.getText());
                    intent.putExtra("Zonacliente",zona.getText());
                    intent.putExtra("Idcliente",idcliente.getText());
                    context.startActivity(intent);
                    //((Activity)context).finish();
                   


                }
            });

        }


    }


}
