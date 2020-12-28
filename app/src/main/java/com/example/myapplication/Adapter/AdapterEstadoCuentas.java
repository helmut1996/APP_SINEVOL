package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.modelos.ModelItemCuentas;

import java.util.List;

public class AdapterEstadoCuentas extends RecyclerView.Adapter <AdapterEstadoCuentas.EstadoVH>{

    List<ModelItemCuentas>estadocuentalista;

    public AdapterEstadoCuentas(List<ModelItemCuentas> estadocuentalista) {
        this.estadocuentalista = estadocuentalista;
    }

    @NonNull
    @Override
    public EstadoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estado_cuentas_item,parent,false);
        return new EstadoVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstadoVH holder, int position) {
    ModelItemCuentas cuentas = estadocuentalista.get(position);
    holder.tvnombrecliente.setText(cuentas.getNombreCliente());
    holder.tvdfecha.setText(cuentas.getFechaCliente());
    holder.tvtelefono.setText(String.valueOf(cuentas.getTelefonoCliente()));
    holder.tvdescripcion.setText(cuentas.getDescripcionCliente());
    holder.tvtotal.setText(String.valueOf(cuentas.getTotalCliente()));
    holder.tventrada.setText(String.valueOf(cuentas.getEntradaCliente()));
    holder.tvsalida.setText(String.valueOf(cuentas.getSalidaCliente()));
    holder.tvdireccion.setText(cuentas.getDireccionCliente());
    holder.tvcredido.setText(String.valueOf(cuentas.getCredito()));

    boolean isExpandadle= estadocuentalista.get(position).isExpandle();
    holder.relativeLayout.setVisibility(isExpandadle ? View.VISIBLE: View.GONE);
    }

    @Override
    public int getItemCount() {
        return estadocuentalista.size();
    }
    public class EstadoVH extends RecyclerView.ViewHolder{

        TextView tvnombrecliente,tvdireccion,tvtelefono,tvcredido,tvtotal,tventrada,tvsalida,tvdescripcion,tvdfecha;
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;

        public EstadoVH(@NonNull View itemView) {
            super(itemView);

            tvnombrecliente= itemView.findViewById(R.id.CuentaCliente);
            tvtelefono= itemView.findViewById(R.id.telefonoCliente);
            tvdescripcion= itemView.findViewById(R.id.DescripcionCliente);
            tvdireccion= itemView.findViewById(R.id.DireecionCliente);
            tvdfecha= itemView.findViewById(R.id.FechaCliente);
            tvtotal= itemView.findViewById(R.id.TotalCliente);
            tvcredido= itemView.findViewById(R.id.TipoCompraCliente);
            tvsalida= itemView.findViewById(R.id.SalidaCliente);
            tventrada= itemView.findViewById(R.id.EntradaCliente);
            linearLayout= itemView.findViewById(R.id.linearLayout1);
            relativeLayout = itemView.findViewById(R.id.Expanndable_layout);


            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelItemCuentas cuentas = estadocuentalista.get(getAdapterPosition());
                    cuentas.setExpandle(cuentas.isExpandle());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
