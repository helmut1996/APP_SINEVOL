package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ClassDialogFactura extends DialogFragment {
    TextView producto,cantidad,precio_C,precio_D;
    ImageButton btn_delete;
    ImageView imgProductoDetalle;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.detalle_factura,null);

        builder.setView(view)
                .setTitle("Detalle Factura")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        MainFacruraList variable = new MainFacruraList();
        producto=view.findViewById(R.id.id_producto);
        producto.setText(variable.nombre);
        cantidad=view.findViewById(R.id.editTextCantidad2);
        cantidad.setText(String.valueOf(variable.cantidadProducto));
        precio_C=view.findViewById(R.id.id_precio_cordobas);
        precio_C.setText(String.valueOf(variable.precioProducto));
        precio_D=view.findViewById(R.id.id_precio_dolares);
        precio_D.setText(String.valueOf(variable.idProd));
        btn_delete=view.findViewById(R.id.imageButton_Eliminar);
        imgProductoDetalle=view.findViewById(R.id.imageProducto);

        return builder.create();

    }
}
